package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Incidence;
import domain.Manager;
import domain.Responsable;
import domain.Technician;
import domain.User;
import forms.IncidenceForm;
import repositories.IncidenceRepository;
import security.Authority;
import security.LoginService;

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
		case Authority.RESPONSABLE:
			Assert.isTrue(actor instanceof Responsable);
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
		Assert.notNull(actor,"msg.not.loged.block");
		Assert.isTrue(actor instanceof Manager 
				|| actor instanceof Technician
				|| incidence.getUser().getId()==(actor.getId())
				|| (actor instanceof Responsable
						&& incidence.getUser().getCustomer().equals(actor.getCustomer())),"msg.not.owned.block");
		if (incidence.getId() == 0) {
			incidence.setPublicationDate(new Date());			
		}else {
			result = this.incidenceRepository.findOne(incidence.getId());
			Assert.isTrue(incidence.getUser().getCustomer().equals(result.getUser().getCustomer()),"msg.not.owned.block");
			if(actor instanceof Technician) {
				Assert.isTrue(incidence.getTechnician().getId()==(result.getTechnician().getId()),"msg.not.owned.block");
			}
			
		}
		result = this.incidenceRepository.save(incidence);
		return result;
	}

	public Collection<Incidence> findAll() {
		return incidenceRepository.findAll();
	}

	public Incidence recontruct(IncidenceForm form, BindingResult binding) {
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
			result.setCancelled(form.isCancelled());
			result.setCancelationReason(form.getCancelationReason());
		}else {
			result = this.incidenceRepository.findOne(form.getId());
			result.setTitle(form.getTitle());
			result.setDescription(form.getDescription());
			result.setUser(form.getUser());
			result.setTechnician(form.getTechnician());
			result.setStartingDate(form.getStartingDate());
			result.setEndingDate(form.getEndingDate());
			result.setServant(form.getServant());
			result.setCancelled(form.isCancelled());
			result.setCancelationReason(form.getCancelationReason());
		}
		
		validator.validate(result, binding);
		return result;

	}
	public Page<Incidence> findAllPaginated(int page, int size) {		
		return incidenceRepository.findAll(	new PageRequest(page, size));
	}

	public Incidence findOne(int id) {
		return incidenceRepository.findOne(id);
	}

	private String generateTicker() {
		String result = "INC-";
		Calendar calendar = Calendar.getInstance();
		result += (("" + calendar.get(Calendar.DAY_OF_MONTH)).length() == 2) ? calendar.get(Calendar.DAY_OF_MONTH)
				: "0" + calendar.get(Calendar.DAY_OF_MONTH);
		int mes = calendar.get(Calendar.MONTH)+1;
		result += (("" + mes).length() == 2) ? mes : "0" + mes;
		result += (("" + calendar.get(Calendar.YEAR) % 100).length() == 2) ? calendar.get(Calendar.YEAR) % 100
				: "0" + calendar.get(Calendar.YEAR) % 100;
		result += "-";		
		String momentOfDay = "" +  System.currentTimeMillis() % 86400000;
		result +=momentOfDay;		
		return result;
	}

	public Incidence delete(IncidenceForm incidence) {
		Assert.notNull(incidence);
		Actor actor = actorService.findByPrincipal();
		Assert.notNull(actor,"msg.not.loged.block");
		Assert.isTrue(actor.getId() == incidence.getTechnician().getId() 
				|| actor.getId() == incidence.getUser().getId()
				|| actor instanceof Manager,"msg.not.owned.block");
		this.incidenceRepository.delete(incidence.getId());
		
		return null;
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
		case Authority.RESPONSABLE:
			Assert.isTrue(actor instanceof Responsable);
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
}
