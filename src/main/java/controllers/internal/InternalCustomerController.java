/*
 * CustomerController.java
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.internal;

import controllers.AbstractController;
import domain.Actor;
import domain.Customer;
import domain.Manager;
import domain.Responsible;
import forms.CustomerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.CustomerService;

import java.util.Collection;

@Controller
@RequestMapping("/customer/internal")
public class InternalCustomerController extends AbstractController {

    // Supporting services -----------------------------------------------------

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ActorService actorService;

    // Constructor

    public InternalCustomerController() {
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
        result.addObject("logedCustomerId", -1);
        result.addObject("requestUri", "customer/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        return result;
    }

}
