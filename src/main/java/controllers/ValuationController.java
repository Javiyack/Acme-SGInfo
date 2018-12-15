package controllers;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.IncidenceService;
import services.ValuationService;
import domain.Actor;
import domain.Incidence;
import domain.Valuation;
import forms.ValuationForm;

@Controller
@RequestMapping("/valuation")
public class ValuationController extends AbstractController {
	
	// Supporting services -----------------------------------------------------

    @Autowired
    private ValuationService valuationService;

    @Autowired
    private ActorService actorService;
    @Autowired
    private IncidenceService incidenceService;

    // Constructor

    public ValuationController() {
        super();
    }


    // Create valuation ---------------------------------------------------------------
    @RequestMapping("/create")
    public ModelAndView create(int id) {
        ModelAndView result;
        Incidence incidence = incidenceService.findOne(id);
        ValuationForm valuationForm;
        try {
            final Valuation valuation = this.valuationService.create(incidence);
            valuationForm = new ValuationForm(valuation);
            result = this.createEditModelAndView(valuationForm);
            result.addObject("incidence", id);
        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                result = this.createMessageModelAndView(oops.getLocalizedMessage(),
                        "valoration/create.do?id=" + id);
            } else {
                result = this.createMessageModelAndView("msg.commit.error",
                        "valoration/create.do?id=" + id);
            }
        }

        return result;
    }

    
    // Display valuation ---------------------------------------------------------------
    @RequestMapping("/display")
    public ModelAndView display(@Valid final int id) {
        ModelAndView result;
        final Valuation valuation = valuationService.valuationByUserInIncidence(id);
        final ValuationForm valuationForm = new ValuationForm(valuation);
        result = this.createEditModelAndView(valuationForm);
        result.addObject("display", true);

        return result;
    }

    // Save valuation ---------------------------------------------------------------
    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@Valid final ValuationForm valuationForm, final BindingResult binding) {
        ModelAndView result;
        Valuation valuation;
        try {
        	valuation = valuationService.recontruct(valuationForm, binding);

            if (binding.hasErrors())
                result = this.createEditModelAndView(valuationForm);
            else
                try {
                    this.valuationService.save(valuation);
                    result = new ModelAndView(
                            "redirect:/incidence/internal/edit.do?id=" + valuation.getIncidence().getId());
                } catch (final Throwable oops) {
                    result = this.createEditModelAndView(valuationForm, "msg.commit.error");
                }
        } catch (final Throwable oops) {
            result = this.createEditModelAndView(valuationForm, "msg.reconstruct.error");
        }

        return result;
    }

   
    protected ModelAndView createEditModelAndView(final ValuationForm valuation) {
        final ModelAndView result;
        result = this.createEditModelAndView(valuation, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final ValuationForm valuation, final String message) {
        final ModelAndView result;
        Actor actor = this.actorService.findByPrincipal();

        result = new ModelAndView("valuation/create");
        result.addObject("valuationForm", valuation);
        result.addObject("message", message);
        result.addObject("requestUri", "valuation/create.do");
        result.addObject("actor", actor);
        return result;

    }

}
