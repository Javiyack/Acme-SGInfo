package controllers.external;

import controllers.AbstractController;
import domain.*;
import forms.IncidenceForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.*;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Date;

@Controller
@RequestMapping("/incidence/external")
public class ExternalIncidenceController extends AbstractController {

    // Services
    @Autowired
    private IncidenceService incidenceService;
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private LaborService laborService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private ValuationService valuationService;
    // Constructor

    public ExternalIncidenceController() {
        super();
    }

    // List All incidences
    // ---------------------------------------------------------------
    @RequestMapping("/list")
    public ModelAndView list(final Integer pageSize) {
        ModelAndView result;
        final Collection<Incidence> inidencias = this.incidenceService.findAllByActor();

        result = new ModelAndView("incidence/list");
        List<Incidence> noVoted = new ArrayList<Incidence>();
        for(Incidence i: inidencias){
        	if(valuationService.numValorationByUserInIncidence(i.getId())==0){
        		noVoted.add(i);
        	}
        }
        
        result.addObject("incidences", inidencias);
        result.addObject("requestUri", "incidence/external/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        result.addObject("noVoted",noVoted);
        return result;
    }

    // Create incidence
    // ---------------------------------------------------------------
    @RequestMapping("/create")
    public ModelAndView create() {
        ModelAndView result;
        final Incidence incidencia = this.incidenceService.create();
        final IncidenceForm incidence = new IncidenceForm(incidencia);
        try {
            Actor actor = actorService.findByPrincipal();
            Assert.notNull(actor, "msg.not.logged.block");
            incidence.setCustomerId(actor.getCustomer().getId());
        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg."))
                result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/customer/list.do");
            else
                result = this.createMessageModelAndView("msg.commit.error", "/");
        }

        result = this.createEditModelAndView(incidence);

        return result;
    }

    // Edit incidence
    // ---------------------------------------------------------------
    @RequestMapping("/edit")
    public ModelAndView edit(@Valid final int id) {
        ModelAndView result;
        final Incidence incidencia = this.incidenceService.findOne(id);

        final IncidenceForm incidence = new IncidenceForm(incidencia);
        result = this.createEditModelAndView(incidence);
        return result;
    }

    // Edit incidence
    // ---------------------------------------------------------------
    @RequestMapping("/display")
    public ModelAndView display(@Valid final int id) {
        ModelAndView result;
        final Incidence incidencia = this.incidenceService.findOne(id);

        final IncidenceForm incidence = new IncidenceForm(incidencia);
        result = this.createEditModelAndView(incidence);
        result.addObject("display", true);
        return result;
    }

    // Save incidence
    // ---------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@Valid final IncidenceForm incidence, final BindingResult binding) {
        ModelAndView result;
        if (binding.hasErrors())
            result = this.createEditModelAndView(incidence);
        else {
            try {
                Incidence incidencia = incidenceService.recontruct(incidence, binding);
                if (binding.hasErrors())
                    result = this.createEditModelAndView(incidence);
                else
                    try {
                        incidencia = this.incidenceService.save(incidencia);
                        incidence.setId(incidencia.getId());
                        result = this.createEditModelAndView(incidence);
                        result.addObject("info", "msg.commit.ok");
                    } catch (final Throwable oops) {
                        if (oops.getMessage().startsWith("msg."))
                            result = this.createEditModelAndView(incidence, oops.getLocalizedMessage());
                        else
                            result = this.createEditModelAndView(incidence, "msg.commit.error");
                    }
            } catch (final Throwable oops) {
                if (oops.getMessage().startsWith("msg."))
                    result = this.createEditModelAndView(incidence, oops.getLocalizedMessage());
                else
                    result = this.createEditModelAndView(incidence, "msg.reconstruct.error");
            }
        }
        return result;
    }

    // Save incidence
    // ---------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(@Valid final IncidenceForm incidence, final BindingResult binding) {
        ModelAndView result;

        try {
            this.incidenceService.delete(incidence);
            result = new ModelAndView("redirect:/incidence/external/list.do");

        } catch (final Throwable oops) {
            if (oops.getMessage().startsWith("msg."))
                result = this.createEditModelAndView(incidence, oops.getLocalizedMessage());
            else
                result = this.createEditModelAndView(incidence, "msg.commit.error");
        }
        return result;
    }

    // Métodos auxiliares
    // ----------------------------------------------------------

    protected ModelAndView createEditModelAndView(final IncidenceForm inidence) {
        final ModelAndView result;
        result = this.createEditModelAndView(inidence, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final IncidenceForm incidence, final String message) {
        final ModelAndView result;

        Collection<Technician> techs = technicianService.findAll();
        Collection<Actor> members = actorService.findWorkers();
        Collection<Labor> labors = laborService.findByIncidence(incidence.getId());

        result = new ModelAndView("incidence/edit");
        result.addObject("incidenceForm", incidence);
        result.addObject("technicians", techs);
        result.addObject("users", members);
        result.addObject("message", message);
        result.addObject("closed",
                (incidence.getEndingDate() != null) ? (new Date()).after(incidence.getEndingDate()) : false);
        result.addObject("requestUri", "incidence/external/edit.do");
        result.addObject("labors", labors);

        return result;

    }
}
