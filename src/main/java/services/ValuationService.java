package services;

import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ValuationRepository;
import domain.Actor;
import domain.Incidence;
import domain.User;
import domain.Valuation;
import forms.ValuationForm;

@Service
@Transactional
public class ValuationService {
	// Repositories
    @Autowired
    private ValuationRepository valuationRepository;
    // Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private Validator validator;

    // Constructor
    public ValuationService() {
        super();
    }

    // Simple CRUD methods ----------------------------------------------------

    // Create

    public Valuation create(Incidence incidence) {
    	Assert.isTrue(incidence.getEndingDate().before(Calendar.getInstance().getTime()),"msg.valuation.date");
        Assert.notNull(incidence, "msg.save.first");
        Actor actor = actorService.findByPrincipal();
        Assert.isTrue(actor instanceof User, "msg.not.owned.block");
        Assert.isTrue(actor.getId() == incidence.getUser().getId(), "msg.not.owned.block");
        final Valuation result = new Valuation();
        result.setIncidence(incidence);
        return result;
    }

    // Save
    public Valuation save(final Valuation valuation) {
    	Assert.isTrue(valuation.getIncidence().getEndingDate().before(Calendar.getInstance().getTime()),"msg.valuation.date");
        Assert.notNull(valuation);
        Actor actor = actorService.findByPrincipal();
        Assert.isTrue(actor instanceof User, "msg.not.owned.block");
        if (actor instanceof User)
            Assert.isTrue(actor.getId() == valuation.getIncidence().getUser().getId(), "msg.not.owned.block");
        Valuation result;
        result = this.valuationRepository.save(valuation);
        return result;
    }

    public Collection<Valuation> findAll() {
        return valuationRepository.findAll();
    }

    public Valuation recontruct(ValuationForm form, BindingResult binding) {
    	Valuation result;
        if (form.getId() == 0) {
            result = this.create(form.getIncidence());
            result.setId(0);
            result.setVersion(0);
            result.setValue(form.getValue());
            result.setComments(form.getComments());
            result.setIncidence(form.getIncidence());
        } else {
            result = this.valuationRepository.findOne(form.getId());
            result.setValue(form.getValue());
            result.setComments(form.getComments());
            result.setIncidence(form.getIncidence());
        }

        validator.validate(result, binding);
        return result;

    }

    public Page<Valuation> findAllPaginated(int page, int size) {
        return valuationRepository.findAll(new PageRequest(page, size));
    }

    public Valuation findOne(int id) {
        return valuationRepository.findOne(id);
    }
    
    public Integer numValorationByUserInIncidence(Integer incidenceId){
    	return valuationRepository.numValuationByUserInIncidence(incidenceId);
    	
    }
    
    public Valuation valuationByUserInIncidence(Integer incidenceId){
    	return valuationRepository.valuationByUserInIncidence(incidenceId);
    }

}
