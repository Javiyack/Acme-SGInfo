package services;

import domain.*;
import forms.ServantForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.ServantRepository;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Service
@Transactional
public class ServantService {
    //Repositories
    @Autowired
    private ServantRepository servantRepository;
    //Services
    @Autowired
    private RequestService requestService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private Validator validator;

    //Constructor
    public ServantService() {
        super();
    }

    // Simple CRUD methods ----------------------------------------------------

    //Create
    public Servant create() {

        final Servant result = new Servant();

        Actor actor = actorService.findByPrincipal();
        // Hack
        Assert.notNull(actor);
        Assert.isTrue(actor instanceof Manager);
        result.setDraft(true);
        return result;
    }

    //Save
    public Servant save(final Servant servant) {
        Assert.notNull(servant);
        Actor actor = actorService.findByPrincipal();
        // Hack
        Assert.notNull(actor);
        Assert.isTrue(actor instanceof Manager);
        Servant result = servantRepository.findOne(servant.getId());

        if (servant.getId() == 0) {
            result = this.servantRepository.save(servant);
        } else if (!servant.isDraft()) {
            /* Si no esta en modo borrador solo se podra cambiar el precio y activar/desactivar y
             * solo en caso de que no tenga solicitudes activas (endingDay==null)*/
            Collection<Request> requests = requestService.findActiveRequessToServant(servant);
            Assert.isTrue(requests.isEmpty(), "msg.requested.servant.edition.block");
            servant.setName(result.getName());
            servant.setDescription(result.getDescription());
            servant.setDraft(false);
            servant.setPicture(result.getPicture());
            result = this.servantRepository.saveAndFlush(servant);
        } else {
            // Si e
            result = this.servantRepository.saveAndFlush(servant);
        }


        return result;
    }


    public Collection<Servant> findAll() {
        Collection<Servant> result = servantRepository.findAll();
        return result;
    }

    public Servant recontruct(ServantForm form, BindingResult binding) {
        Servant result;
        if (form.getId() == 0) {
            result = this.create();
        } else {
            result = this.servantRepository.findOne(form.getId());
        }
        result.setName(form.getName());
        result.setDescription(form.getDescription());
        result.setPrice(form.getPrice());
        result.setPicture(form.getPicture());
        result.setDraft(form.isDraft());
        result.setCancelled(form.isCancelled());
        validator.validate(result, binding);
        return result;
    }

    public Servant findOne(int id) {
        return servantRepository.findOne(id);
    }

    public Collection<Request> findFacturables() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        Date limit = calendar.getTime();
        return this.requestService.findFacturables(limit);
    }

    public void cancel(Servant servant) {
        servant.setCancelled(true);
        servantRepository.save(servant);
    }

    public Collection<Servant> findRequestableServants() {
        return servantRepository.findRequestableServants();
    }

    public Set<MonthlyDue> findAllMonthlyDues(int requestId) {
        return requestService.findAllMonthlyDues(requestId);
    }

    public Collection<Request> findFacturablesByCustomerId(int id) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
        Date limit = calendar.getTime();
        return this.requestService.findFacturablesByCustomerId(limit, id);
    }
}
