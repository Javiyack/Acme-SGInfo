/*
 * CustomerController.java
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import controllers.AbstractController;
import domain.Actor;
import domain.Customer;
import domain.Manager;
import domain.Responsible;
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
@RequestMapping("/customer/administrator")
public class AdministratorCustomerController extends AbstractController {

    // Supporting services -----------------------------------------------------

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ActorService actorService;

    // Constructor

    public AdministratorCustomerController() {
        super();
    }

    // List All customers
    // ---------------------------------------------------------------
    @RequestMapping("/list")
    public ModelAndView list(Integer pageSize) {
        ModelAndView result;
        pageSize = (pageSize != null) ? pageSize : 5;
        final Collection<Customer> customers = this.customerService.findAll();
        Actor actor = this.actorService.findByPrincipal();

        result = new ModelAndView("customer/list");
        result.addObject("customers", customers);
        result.addObject("logedCustomerId", -1);
        result.addObject("requestUri", "customer/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        return result;
    }


    // Activating customer
    // ---------------------------------------------------------------
    @RequestMapping("/activation")
    public ModelAndView toggleActivation(@Valid final int id, Integer pageSize) {
        ModelAndView result;
        pageSize = (pageSize != null) ? pageSize : 5;
        final Customer customer = this.customerService.findOne(id);
        customer.setActive(customer.getActive());
        try {
            customerService.save(customer);
            result = this.list(pageSize);
        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                result = createMessageModelAndView(oops.getLocalizedMessage(), "/customer/administrator/list.do?pageSize=" + pageSize);
            } else
                result = this.createMessageModelAndView("msg.commit.error", "/customer/administrator/list.do?pageSize=" + pageSize);
        }


        return result;
    }
}
