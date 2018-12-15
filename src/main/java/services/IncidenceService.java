package services;

import domain.*;
import forms.IncidenceForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.IncidenceRepository;
import security.Authority;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class IncidenceService {
    // Repositories
    @Autowired
    private IncidenceRepository incidenceRepository;
    // Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private LaborService laborService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private Validator validator;

    // Constructor
    public IncidenceService() {
        super();
    }

    // Simple CRUD methods ----------------------------------------------------

    // Create

    public Incidence create() {

        final Incidence result = new Incidence();
        Actor actor = actorService.findByPrincipal();
        // Hack
        Assert.notNull(actor);
        switch (actor.getUserAccount().getAuthorities().iterator().next().getAuthority()) {
            case Authority.USER:
                Assert.isTrue(actor instanceof User);
                result.setUser((User) actor);
                result.setTechnician(technicianService.findLessOcuped());
                break;
            case Authority.RESPONSIBLE:
                Assert.isTrue(actor instanceof Responsible);
                result.setTechnician(technicianService.findLessOcuped());
                break;

            case Authority.TECHNICIAN:
                Assert.isTrue(actor instanceof Technician);
                result.setTechnician((Technician) actor);
                break;

            case Authority.MANAGER:
                Assert.isTrue(actor instanceof Manager);
                result.setTechnician(technicianService.findLessOcuped());
                break;

            default:
                break;
        }
        result.setTicker(generateTicker());
        result.setPublicationDate(new Date());
        result.setCancelled(false);
        return result;
    }

    // Save
    public Incidence save(final Incidence incidence) {
        Incidence result;
        Assert.notNull(incidence);
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(
                actor instanceof Manager || actor instanceof Technician
                        || incidence.getUser().getId() == (actor.getId())
                        || (actor instanceof Responsible
                        && incidence.getUser().getCustomer().equals(actor.getCustomer())),
                "msg.not.owned.block");
        if (incidence.getId() == 0) {
            incidence.setPublicationDate(new Date(System.currentTimeMillis() - 999));
        } else {
            result = this.incidenceRepository.findOne(incidence.getId());
            Assert.isTrue(incidence.getUser().getCustomer().equals(result.getUser().getCustomer()),
                    "msg.not.owned.block");
            if (actor instanceof Technician) {
                Assert.isTrue(incidence.getTechnician().getId() == (result.getTechnician().getId()),
                        "msg.not.owned.block");
                Message savedMessage = new Message();
                Message message = messageService.create();
                message.setBody("La incidencia " + incidence.getTitle() + " cambió de estado");
                message.setRecipient(incidence.getUser());
                message.setSubject("Aviso Incidencia");
                message.setPriority("NEUTRAL");
                savedMessage = messageService.save(message);
                messageService.saveOnSender(savedMessage);
                messageService.saveOnRecipient(savedMessage);
            }
            if (incidence.getCancelled()) {
                Assert.notNull(incidence.getCancellationReason(), "msg.missing.cancel.reason.block");
                Assert.isTrue(!incidence.getCancellationReason().isEmpty(), "msg.missing.cancel.reason.block");
            }

        }

        checkMoments(incidence);
        result = this.incidenceRepository.save(incidence);
        return result;
    }

    public Collection<Incidence> findAll() {
        return incidenceRepository.findAll();
    }

    public Incidence recontruct(IncidenceForm form, BindingResult binding) {

        Actor actor = actorService.findByPrincipal();
        Incidence result;
        if (form.getId() == 0) {
            result = this.create();
            result.setId(form.getId());
            result.setVersion(form.getVersion());
            result.setTitle(form.getTitle());
            result.setDescription(form.getDescription());
            result.setTicker(form.getTicker());
            result.setUser(form.getUser());
            result.setTechnician(form.getTechnician());
            result.setPublicationDate(form.getPublicationDate());
            result.setStartingDate(form.getStartingDate());
            result.setEndingDate(form.getEndingDate());
            result.setServant(form.getServant());
            result.setCancelled(false);
            result.setCancellationReason(form.getCancellationReason());
        } else {
            result = this.incidenceRepository.findOne(form.getId());
            result.setTitle(form.getTitle());
            result.setDescription(form.getDescription());
            result.setUser(form.getUser());
            result.setTechnician(form.getTechnician());
            result.setStartingDate(form.getStartingDate());
            result.setEndingDate(form.getEndingDate());
            result.setServant(form.getServant());
            if (!result.getCancelled().equals(form.getCancelled())) {
                Assert.notNull(actor);
                Assert.isTrue(actor instanceof Manager || actor.equals(result.getTechnician()), "msg.not.owned.block");
                if (form.getCancelled()) {
                    Assert.notNull(form.getCancellationReason(), "msg.missing.cancel.reason.block");
                    Assert.isTrue(!form.getCancellationReason().isEmpty(), "msg.missing.cancel.reason.block");
                } else
                    form.setCancellationReason("");
                result.setCancelled(form.getCancelled());
            }
            result.setCancellationReason(form.getCancellationReason());
            checkMoments(form);
        }

        validator.validate(result, binding);
        if (binding.hasErrors() && result.getId() != 0) {
            result = null;
            result = this.incidenceRepository.findOne(form.getId());
            validator.validate(result, binding);

        }
        return result;

    }

    public Page<Incidence> findAllPaginated(int page, int size) {
        return incidenceRepository.findAll(new PageRequest(page, size));
    }

    public Incidence findOne(int id) {
        return incidenceRepository.findOne(id);
    }

    private String generateTicker() {
        String result = "INC-";
        Calendar calendar = Calendar.getInstance();
        result += (("" + calendar.get(Calendar.DAY_OF_MONTH)).length() == 2) ? calendar.get(Calendar.DAY_OF_MONTH)
                : "0" + calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH) + 1;
        result += (("" + mes).length() == 2) ? mes : "0" + mes;
        result += (("" + calendar.get(Calendar.YEAR) % 100).length() == 2) ? calendar.get(Calendar.YEAR) % 100
                : "0" + calendar.get(Calendar.YEAR) % 100;
        result += "-";
        String momentOfDay = "" + System.currentTimeMillis() % 86400000;
        result += momentOfDay;
        return result;
    }

    public Incidence delete(IncidenceForm incidence) {
        Assert.notNull(incidence);
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor.getId() == incidence.getTechnician().getId() || actor.getId() == incidence.getUser().getId()
                || actor instanceof Manager, "msg.not.owned.block");
        Incidence incidencia = this.incidenceRepository.findOne(incidence.getId());
        this.checkHasNotBegun(incidencia);
        ;
        this.checkHasNoLabors(incidencia);
        this.incidenceRepository.delete(incidencia);

        return null;
    }

    private void checkHasNotBegun(Incidence incidence) {
        this.checkMoments(incidence);
        Assert.isTrue(incidence.getStartingDate() == null, "msg.has.begun.delete.block");

    }

    private void checkHasNoLabors(Incidence incidence) {
        Collection<Labor> labors = this.laborService.findByIncidence(incidence.getId());
        Assert.isTrue(labors.isEmpty(), "msg.has.labors.delete.block");
    }

    public Collection<Incidence> findAllByActor() {
        final Collection<Incidence> result;
        Actor actor = actorService.findByPrincipal();
        // Hack
        Assert.notNull(actor);
        switch (actor.getUserAccount().getAuthorities().iterator().next().getAuthority()) {
            case Authority.USER:
                Assert.isTrue(actor instanceof User);
                result = this.incidenceRepository.findByUserAccountId(actor.getCustomer().getId());
                break;
            case Authority.RESPONSIBLE:
                Assert.isTrue(actor instanceof Responsible);
                result = this.incidenceRepository.findByUserAccountId(actor.getCustomer().getId());
                break;
            case Authority.TECHNICIAN:
                Assert.isTrue(actor instanceof Technician);
                result = this.incidenceRepository.findAll();
                break;
            case Authority.MANAGER:
                Assert.isTrue(actor instanceof Manager);
                result = this.incidenceRepository.findAll();
                break;

            default:
                result = null;
                break;
        }
        return result;
    }

    private void checkMoments(Incidence incidence) {
        Assert.notNull(incidence.getPublicationDate(), "msg.incidence.bad.moments");
        Assert.isTrue(incidence.getPublicationDate().before(new Date()));
        if (incidence.getStartingDate() != null) {
            Assert.isTrue(incidence.getStartingDate().before(new Date()), "msg.incidence.bad.moments");
            Assert.isTrue(incidence.getPublicationDate().before(incidence.getStartingDate()),
                    "msg.incidence.bad.moments");

        }
        if (incidence.getEndingDate() != null) {
            Assert.notNull(incidence.getStartingDate(), "msg.incidence.bad.moments");
            Assert.isTrue(incidence.getStartingDate().before(incidence.getEndingDate()), "msg.incidence.bad.moments");
            Assert.isTrue(incidence.getEndingDate().before(new Date()), "msg.incidence.bad.moments");
        }

    }

    private void checkMoments(IncidenceForm incidence) {
        Assert.notNull(incidence.getPublicationDate(), "msg.incidence.bad.moments");
        Assert.isTrue(incidence.getPublicationDate().before(new Date()));
        if (incidence.getStartingDate() != null) {
            Assert.isTrue(incidence.getStartingDate().before(new Date()), "msg.incidence.bad.moments");
            Assert.isTrue(incidence.getPublicationDate().before(incidence.getStartingDate()),
                    "msg.incidence.bad.moments");
            Incidence dbObject = findOne(incidence.getId());
            Assert.isTrue(incidence.getStartingDate().equals(dbObject.getStartingDate()), "msg.incidence.starting.changing.block");

        }
        if (incidence.getEndingDate() != null) {
            Assert.notNull(incidence.getStartingDate(), "msg.incidence.bad.moments");
            Assert.isTrue(incidence.getStartingDate().before(incidence.getEndingDate()), "msg.incidence.bad.moments");
            Assert.isTrue(incidence.getEndingDate().before(new Date()), "msg.incidence.bad.moments");
            Incidence dbObject = findOne(incidence.getId());
            Assert.isTrue(incidence.getEndingDate().equals(dbObject.getEndingDate()), "msg.incidence.ending.changing.block");

        }

    }

    public Collection<Incidence> findFacturables() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        Date limit = calendar.getTime();
        return this.incidenceRepository.findFacturables(limit);
    }

    public Incidence start(int id) {
        Incidence result = findOne(id);
        Assert.isTrue(result.getStartingDate() == null, "msg.incidence.starting.changing.block");
        result.setStartingDate(new Date(System.currentTimeMillis() - 100));
        return this.incidenceRepository.save(result);
    }

    public Incidence close(int id) {
        Incidence result = findOne(id);
        Assert.isTrue(result.getEndingDate() == null, "msg.incidence.ending.changing.block");
        result.setEndingDate(new Date(System.currentTimeMillis() - 100));
        return this.incidenceRepository.save(result);
    }
}
