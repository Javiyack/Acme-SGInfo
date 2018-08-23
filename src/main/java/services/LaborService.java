package services;

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
import domain.Labor;
import domain.Manager;
import domain.Technician;
import forms.LaborForm;
import repositories.LaborRepository;

@Service
@Transactional
public class LaborService {
	// Repositories
	@Autowired
	private LaborRepository laborRepository;
	// Services
	@Autowired
	private ActorService actorService;
	@Autowired
	private TechnicianService technicianService;
	@Autowired
	private Validator validator;

	// Constructor
	public LaborService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	// Create

	public Labor create(Incidence incidence) {
		Assert.notNull(incidence, "msg.save.incidence.first");
		Actor actor = actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Technician, "msg.not.owned.block");		
		Assert.isTrue(actor.getId()==incidence.getTechnician().getId(), "msg.not.owned.block");		
		final Labor result = new Labor();
		result.setMoment(new Date());
		result.setIncidence(incidence);
		return result;
	}

	// Save
	public Labor save(final Labor labor) {
		Assert.notNull(labor);
		Actor actor = actorService.findByPrincipal();
		Assert.isTrue(actor instanceof Technician || actor instanceof Manager, "msg.not.owned.block");
		if (actor instanceof Technician)
			Assert.isTrue(actor.getId() == labor.getIncidence().getTechnician().getId(), "msg.not.owned.block");
		Labor result;
		result = this.laborRepository.save(labor);
		return result;
	}

	public Collection<Labor> findAll() {
		return laborRepository.findAll();
	}


	public Collection<Labor> findByTechnician(int techId) {
		return laborRepository.findByTechnicianId(techId);
	}


	public Collection<Labor> findByUser(int userId) {
		return laborRepository.findByUserId(userId);
	}

	public Labor recontruct(LaborForm form, BindingResult binding) {
		Labor result;
		if (form.getId() == 0) {
			result = this.create(form.getIncidence());
			result.setId(0);
			result.setVersion(0);
			result.setTitle(form.getTitle());
			result.setDescription(form.getDescription());
			result.setIncidence(form.getIncidence());
			result.setMoment(form.getMoment());
			result.setTime(form.getTime());
			result.setIncidenceBill(form.getIncidenceBill());
		} else {
			result = this.laborRepository.findOne(form.getId());
			result.setTitle(form.getTitle());
			result.setDescription(form.getDescription());
			result.setIncidence(form.getIncidence());
			result.setMoment(form.getMoment());
			result.setTime(form.getTime());
			result.setIncidenceBill(form.getIncidenceBill());
		}

		validator.validate(result, binding);
		return result;

	}

	public Page<Labor> findAllPaginated(int page, int size) {
		return laborRepository.findAll(new PageRequest(page, size));
	}

	public Labor findOne(int id) {
		return laborRepository.findOne(id);
	}

	
	public boolean delete(LaborForm labor) {
		Assert.notNull(labor);
		Actor actor = actorService.findByPrincipal();
		Assert.notNull(actor, "msg.not.loged.block");
		Assert.isTrue(actor instanceof Technician || actor instanceof Manager, "msg.not.owned.block");
		if (actor instanceof Technician)
			Assert.isTrue(actor.getId() == labor.getIncidence().getTechnician().getId(), "msg.not.owned.block");

		this.laborRepository.delete(labor.getId());

		return laborRepository.findOne(labor.getId())==null;
	}

	public Collection<Labor> findByIncidence(int incidenceId) {
		 Collection<Labor> result = laborRepository.findByIncidenceId(incidenceId);
			return result;
	}

	public boolean delete(Labor labor) {
		Assert.notNull(labor);
		Actor actor = actorService.findByPrincipal();
		Assert.notNull(actor, "msg.not.loged.block");
		Assert.isTrue(actor instanceof Technician || actor instanceof Manager, "msg.not.owned.block");
		if (actor instanceof Technician)
			Assert.isTrue(actor.getId() == labor.getIncidence().getTechnician().getId(), "msg.not.owned.block");
		this.laborRepository.delete(labor.getId());
		return laborRepository.findOne(labor.getId())==null;
	}
}
