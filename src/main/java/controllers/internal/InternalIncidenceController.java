package controllers.internal;

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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/incidence/internal")
public class InternalIncidenceController extends AbstractController {

    // Services
    @Autowired
    private IncidenceService incidenceService;
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private LaborService laborService;
    @Autowired
    private ValuationService valuationService;

    // Constructor

    public InternalIncidenceController() {
        super();
    }

    // List All incidences
    // ---------------------------------------------------------------
    @RequestMapping("/list")
    public ModelAndView list(final Integer pageSize) {
        ModelAndView result;
        final Collection<Incidence> inidencias = this.incidenceService.findAll();
        try {
            final Actor logedActor = this.actorService.findByPrincipal();
            Assert.notNull(logedActor, "msg.not.logged.block");
        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                return createMessageModelAndView(oops.getLocalizedMessage(), "/");
            } else {
                return this.createMessageModelAndView("panic.message.text", "/");
            }
        }
        
        List<Incidence> noVoted = new ArrayList<Incidence>();
        for(Incidence i: inidencias){
        	if(valuationService.numValorationByUserInIncidence(i.getId())==0){
        		noVoted.add(i);
        	}
        }

        result = new ModelAndView("incidence/list");
        result.addObject("incidences", inidencias);
        result.addObject("requestUri", "incidence/internal/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        result.addObject("noVoted",noVoted);
        return result;
    }

    // Create incidence
    // ---------------------------------------------------------------
    @RequestMapping("/create")
    public ModelAndView create(@Valid final Integer customerId) {
        ModelAndView result;
        if (customerId != null) {
            final Incidence incidencia = this.incidenceService.create();
            final IncidenceForm incidence = new IncidenceForm(incidencia);
            result = this.createEditModelAndView(incidence, customerId);
        } else {
            result = new ModelAndView("redirect:/customer/list.do");
        }
        return result;
    }

    // Edit incidence
    // ---------------------------------------------------------------
    @RequestMapping("/edit")
    public ModelAndView edit(@Valid final int id) {
        ModelAndView result;
        final Incidence incidencia = this.incidenceService.findOne(id);

        final IncidenceForm incidence = new IncidenceForm(incidencia);
        result = this.createEditModelAndView(incidence, incidencia.getUser().getCustomer().getId());

        return result;
    }

    // Edit incidence
    // ---------------------------------------------------------------
    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public ModelAndView display(@Valid final int id) {
        ModelAndView result;
        final Incidence incidencia = this.incidenceService.findOne(id);

        final IncidenceForm incidence = new IncidenceForm(incidencia);
        result = this.createEditModelAndView(incidence, incidencia.getUser().getCustomer().getId());
        result.addObject("display", true);

        return result;
    }


    // Save incidence
    // ---------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@Valid final IncidenceForm incidence, final BindingResult binding) {
        ModelAndView result;
        Incidence dbObject = this.incidenceService.findOne(incidence.getId());

        IncidenceForm bdObjectCopy = null;
        if (dbObject != null)
            bdObjectCopy = new IncidenceForm(dbObject);
        if (binding.hasErrors())
            result = this.createEditModelAndView(incidence, incidence.getCustomerId());
        else {
            try{
                Incidence incidencia = incidenceService.recontruct(incidence, binding);
                if (binding.hasErrors())
                    result = this.createEditModelAndView(incidence, incidence.getCustomerId());
                else
                    try {
                        incidencia = this.incidenceService.save(incidencia,bdObjectCopy);
                        incidence.setId(incidencia.getId());
                        result = this.createEditModelAndView(incidence,
                                incidence.getUser().getCustomer().getId());
                        result.addObject("info", "msg.commit.ok");
                    } catch (final Throwable oops) {
                        result = redirectOnError(incidence, oops);
                    }
            }catch (final Throwable oops) {
                result = redirectOnError(incidence, oops);
            }

        }
        return result;
    }

    // Delete incidence
    // ---------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(@Valid final IncidenceForm incidence, final BindingResult binding) {
        ModelAndView result;

        try {
            this.incidenceService.delete(incidence);
            result = new ModelAndView("redirect:/incidence/internal/list.do");

        } catch (final Throwable oops) {
            result = redirectOnError(incidence, oops);
        }
        return result;
    }

    // Close incidence. Set ending moment to current and save
    // ---------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "close")
    public ModelAndView close(@Valid final IncidenceForm incidence, final BindingResult binding) {
        ModelAndView result;
        Incidence dbObject = this.incidenceService.findOne(incidence.getId());

        IncidenceForm bdObjectCopy = null;
        if (dbObject != null)
            bdObjectCopy = new IncidenceForm(dbObject);
        if (binding.hasErrors())
            result = this.createEditModelAndView(incidence, incidence.getUser().getCustomer().getId());
        else {
            Incidence incidencia = incidenceService.recontruct(incidence, binding);

            if (binding.hasErrors())
                result = this.createEditModelAndView(incidence, incidencia.getUser().getCustomer().getId());
            else
                try {
                    incidencia.setEndingDate(new Date(System.currentTimeMillis() - 1000));
                    this.incidenceService.save(incidencia,bdObjectCopy);
                    result = new ModelAndView("redirect:/incidence/internal/list.do");
                } catch (final Throwable oops) {
                    result = redirectOnError(incidence, oops);
                }
        }
        return result;
    }

    // Reopen incidence. Set ending moment to null and save
    // ---------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "reopen")
    public ModelAndView reopen(@Valid final IncidenceForm incidence, final BindingResult binding) {
        ModelAndView result;
        Incidence dbObject = this.incidenceService.findOne(incidence.getId());

        IncidenceForm bdObjectCopy = null;
        if (dbObject != null)
            bdObjectCopy = new IncidenceForm(dbObject);;
        if (binding.hasErrors())
            result = this.createEditModelAndView(incidence, incidence.getUser().getCustomer().getId());
        else {
            Incidence incidencia = incidenceService.recontruct(incidence, binding);

            if (binding.hasErrors())
                result = this.createEditModelAndView(incidence, incidencia.getUser().getCustomer().getId());
            else
                try {
                    incidencia.setEndingDate(null);
                    incidence.setEndingDate(null);
                    this.incidenceService.save(incidencia,bdObjectCopy);
                    this.laborService.setBillToNull(incidencia);
                    result = this.createEditModelAndView(incidence, incidence.getUser().getCustomer().getId());
                    result.addObject("info", "msg.commit.ok");
                } catch (final Throwable oops) {
                    result = redirectOnError(incidence, oops);
                }
        }
        return result;
    }

    protected ModelAndView redirectOnError(IncidenceForm incidence, Throwable oops) {
        ModelAndView result;
        if (oops.getMessage().startsWith("msg."))
            result = this.createEditModelAndView(incidence, oops.getLocalizedMessage(),
                    incidence.getUser().getCustomer().getId());
        else
            result = this.createEditModelAndView(incidence, "msg.commit.error",
                    incidence.getUser().getCustomer().getId());
        return result;
    }

    protected ModelAndView createEditModelAndView(final IncidenceForm inidence, int customerId) {
        final ModelAndView result;
        result = this.createEditModelAndView(inidence, null, customerId);
        return result;
    }

    protected ModelAndView createEditModelAndView(final IncidenceForm incidence, final String message, int customerId) {
        final ModelAndView result;

        Collection<Technician> techs = technicianService.findAll();
        Collection<User> users = userService.findAllByCustomerId(customerId);
        Collection<Labor> labors = laborService.findByIncidence(incidence.getId());
        incidence.setCustomerId(customerId);
        result = new ModelAndView("incidence/edit");
        result.addObject("incidenceForm", incidence);
        result.addObject("technicians", techs);
        result.addObject("users", users);
        result.addObject("message", message);
        result.addObject("closed",
                (incidence.getEndingDate() != null) ? (new Date()).after(incidence.getEndingDate()) : false);
        result.addObject("requestUri", "incidence/internal/edit.do");
        result.addObject("labors", labors);

        return result;

    }
}
