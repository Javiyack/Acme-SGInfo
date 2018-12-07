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

import domain.Actor;
import domain.Customer;
import domain.Manager;
import domain.Responsable;
import forms.CustomerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.CustomerService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

    // Supporting services -----------------------------------------------------

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ActorService actorService;

    // Constructor

    public CustomerController() {
        super();
    }

    // List All customers
    // ---------------------------------------------------------------
    @RequestMapping("/list")
    public ModelAndView list(final Integer pageSize) {
        ModelAndView result;
        final Collection <Customer> customers = this.customerService.findAll();
        Actor actor = this.actorService.findByPrincipal();

        result = new ModelAndView("customer/list");
        result.addObject("customers", customers);
        if (actor != null && (actor instanceof Responsable || actor instanceof Responsable)) {
            result.addObject("logedCustomerId", actor.getCustomer().getId());
        } else {
            result.addObject("logedCustomerId", -1);

        }
        result.addObject("requestUri", "customer/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        return result;
    }

    // Create customer
    // ---------------------------------------------------------------
    @RequestMapping("/create")
    public ModelAndView create() {
        ModelAndView result;
        final Customer customer = this.customerService.create();
        final CustomerForm customerForm = new CustomerForm(customer);

        result = this.createEditModelAndView(customerForm);

        return result;
    }

    // Edit customer
    // ---------------------------------------------------------------
    @RequestMapping("/edit")
    public ModelAndView edit(@Valid final int id) {
        ModelAndView result;
        final Customer incidencia = this.customerService.findOne(id);

        final CustomerForm customer = new CustomerForm(incidencia);
        result = this.createEditModelAndView(customer);

        return result;
    }

    // Display/Edit customer
    // ---------------------------------------------------------------
    @RequestMapping("/displayOwn")
    public ModelAndView display() {
        ModelAndView result;
        try {
            Actor actor = actorService.findByPrincipal();
            Assert.notNull(actor, "msg.not.loged.block");
            final CustomerForm customerForm = new CustomerForm(actor.getCustomer());
            result = this.createEditModelAndView(customerForm);
            result.addObject("display", !(actor instanceof Responsable || actor instanceof Manager));

        } catch (final Throwable oops) {
            if (oops.getMessage().startsWith("msg."))
                result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
            else
                result = this.createMessageModelAndView("msg.commit.error", "/");
        }

        return result;
    }

    // Display customer
    // ---------------------------------------------------------------
    @RequestMapping("/display")
    public ModelAndView display(@Valid final int id) {
        ModelAndView result;
        final Customer incidencia = this.customerService.findOne(id);

        final CustomerForm customer = new CustomerForm(incidencia);
        result = this.createEditModelAndView(customer);
        result.addObject("display", true);

        return result;
    }

    // Save customer
    // ---------------------------------------------------------------
    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(@Valid final CustomerForm customer, final BindingResult binding) {
        ModelAndView result;
        Customer customer2 = customerService.recontruct(customer, binding);

        if (binding.hasErrors())
            result = this.createEditModelAndView(customer);
        else
            try {
                this.customerService.save(customer2);
                result = new ModelAndView("redirect:/customer/list.do");
            } catch (final Throwable oops) {
                if (oops.getMessage().startsWith("msg."))
                    result = this.createEditModelAndView(customer, oops.getLocalizedMessage());
                else if (oops.getCause().getCause() != null
                        && oops.getCause().getCause().getMessage().startsWith("Duplicate"))
                    result = this.createEditModelAndView(customer, "msg.duplicate.username");
                else
                    result = this.createEditModelAndView(customer, "msg.commit.error");
            }
        return result;
    }

    // Save customer
    // ---------------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(@Valid final CustomerForm customer, final BindingResult binding) {
        ModelAndView result;

        try {
            this.customerService.delete(customer);
            result = new ModelAndView("redirect:/customer/list.do");

        } catch (final Throwable oops) {
            if (oops.getMessage().startsWith("msg."))
                result = this.createEditModelAndView(customer, oops.getLocalizedMessage());
            else
                result = this.createEditModelAndView(customer, "msg.commit.error");
        }
        return result;
    }

    protected ModelAndView createEditModelAndView(final CustomerForm inidence) {
        final ModelAndView result;
        result = this.createEditModelAndView(inidence, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final CustomerForm customer, final String message) {
        final ModelAndView result;
        Actor actor = this.actorService.findByPrincipal();

        result = new ModelAndView("customer/edit");
        result.addObject("customerForm", customer);
        result.addObject("message", message);
        result.addObject("requestUri", "customer/create.do");
        result.addObject("actor", actor);
        result.addObject("owns", false);
        if (actor != null && actor instanceof Responsable) {
            if (customer.getId() == actor.getCustomer().getId()) {
                result.addObject("actor", (Responsable) actor);
                result.addObject("owns", true);
            }
        } else if (actor != null && actor instanceof Manager) {
            result.addObject("actor", actor);
            result.addObject("owns", true);
        }
        return result;

    }
}
