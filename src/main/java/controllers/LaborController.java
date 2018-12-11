/*
 * CustomerController.java
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import domain.*;
import forms.LaborForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.IncidenceService;
import services.LaborService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/labor")
public class LaborController extends AbstractController {

    // Supporting services -----------------------------------------------------

    @Autowired
    private LaborService laborService;

    @Autowired
    private ActorService actorService;
    @Autowired
    private IncidenceService incidenceService;

    // Constructor

    public LaborController() {
        super();
    }

    // List All labors
    // ---------------------------------------------------------------
    @RequestMapping("/list")
    public ModelAndView list(final Integer pageSize) {
        ModelAndView result;
        final Collection<Labor> labors = this.laborService.findAll();
        result = new ModelAndView("labor/list");
        result.addObject("labors", labors);
        try {
            Actor actor;
            actor = this.actorService.findByPrincipal();
            if (actor != null && actor instanceof Responsible) {
                result.addObject("logedCustomerId", ((Responsible)actor).getCustomer().getId());
            } else {
                result.addObject("logedCustomerId", -1);
            }

        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                return createMessageModelAndView(oops.getLocalizedMessage(), "folder/list.do");
            } else {
                return this.createMessageModelAndView("msg.commit.error", "folder/list.do");
            }
        }

        result.addObject("requestUri", "labor/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        return result;
    }

    // Create labor
    // ---------------------------------------------------------------
    @RequestMapping("/create")
    public ModelAndView create(int incidenceId) {
        ModelAndView result;
        Incidence incidence = incidenceService.findOne(incidenceId);
        LaborForm laborForm;
        try {
            final Labor labor = this.laborService.create(incidence);
            laborForm = new LaborForm(labor);
            result = this.createEditModelAndView(laborForm);
            result.addObject("incidenceId", incidenceId);
        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                result = this.createMessageModelAndView(oops.getLocalizedMessage(),
                        "incidence/internal/edit.do?id=" + incidenceId);
            } else {
                result = this.createMessageModelAndView("msg.commit.error",
                        "incidence/internal/edit.do?id=" + incidenceId);
            }
        }

        return result;
    }

    // Edit labor
    // ---------------------------------------------------------------
    @RequestMapping("/edit")
    public ModelAndView edit(@Valid final int id) {
        ModelAndView result;
        final Labor incidencia = this.laborService.findOne(id);

        final LaborForm labor = new LaborForm(incidencia);
        result = this.createEditModelAndView(labor);

        return result;
    }

    // Edit labor
    // ---------------------------------------------------------------
    @RequestMapping("/display")
    public ModelAndView display(@Valid final int id) {
        ModelAndView result;
        final Labor incidencia = this.laborService.findOne(id);

        final LaborForm labor = new LaborForm(incidencia);
        result = this.createEditModelAndView(labor);
        result.addObject("display", true);

        return result;
    }

    // Save labor
    // ---------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@Valid final LaborForm labor, final BindingResult binding) {
        ModelAndView result;
        Labor incidencia;
        try {
            incidencia = laborService.recontruct(labor, binding);

            if (binding.hasErrors())
                result = this.createEditModelAndView(labor);
            else
                try {
                    this.laborService.save(incidencia);
                    result = new ModelAndView(
                            "redirect:/incidence/internal/edit.do?id=" + labor.getIncidence().getId());
                } catch (final Throwable oops) {
                    result = this.createEditModelAndView(labor, "msg.commit.error");
                }
        } catch (final Throwable oops) {
            result = this.createEditModelAndView(labor, "msg.reconstruct.error");
        }

        return result;
    }

    // Delete labor
    // ---------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(@Valid final LaborForm labor, final BindingResult binding) {
        ModelAndView result;

        try {
            this.laborService.delete(labor);
            result = new ModelAndView("redirect:/incidence/internal/edit.do?id=" + labor.getIncidence().getId());

        } catch (final Throwable ooops) {
            if (ooops.getMessage().equals("msg.not.logged.block"))
                result = this.createEditModelAndView(labor, "msg.not.logged.block");
            else if (ooops.getMessage().equals("msg.not.owned.block"))
                result = this.createEditModelAndView(labor, "msg.not.owned.block");
            else
                result = this.createEditModelAndView(labor, "msg.commit.error");
        }
        return result;
    }

    // Delete ---------------------------------------------------------------
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam(required = false) final Integer id, int incidenceId) {
        ModelAndView result;

        Assert.notNull(id);
        final Labor labor = this.laborService.findOne(id);
        result = new ModelAndView("redirect:/incidence/internal/edit.do?id=" + incidenceId);

        try {
            this.laborService.delete(labor);
        } catch (final Throwable ooops) {
            if (ooops.getMessage().startsWith("msg."))
                result = this.createMessageModelAndView(ooops.getLocalizedMessage(), "/incidence/internal/edit.do?id=" + incidenceId);
            else
                result = this.createMessageModelAndView("msg.commit.error", "/incidence/internal/edit.do?id=" + incidenceId);
        }
        return result;
    }

    protected ModelAndView createEditModelAndView(final LaborForm inidence) {
        final ModelAndView result;
        result = this.createEditModelAndView(inidence, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final LaborForm labor, final String message) {
        final ModelAndView result;
        Actor actor = this.actorService.findByPrincipal();

        result = new ModelAndView("labor/edit");
        result.addObject("laborForm", labor);
        result.addObject("message", message);
        result.addObject("requestUri", "labor/edit.do");
        result.addObject("actor", actor);
        result.addObject("owns", false);
        if (actor != null && actor instanceof Technician) {
            if (labor.getIncidence().getTechnician().getId() == actor.getId()) {
                result.addObject("actor", (Technician) actor);
                result.addObject("owns", true);
            }
        } else {
            result.addObject("actor", actor);
            result.addObject("owns", false);
        }
        return result;

    }
}
