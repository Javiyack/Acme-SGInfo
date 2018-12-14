/*
 * CustomerController.java
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.manager;

import controllers.AbstractController;
import domain.*;
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
@RequestMapping("/customer/manager")
public class ManagerCustomerController extends AbstractController {

    // Supporting services -----------------------------------------------------

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ActorService actorService;

    // Constructor

    public ManagerCustomerController() {
        super();
    }



    // Toggle activation  ----------------------------------------------------------
    @RequestMapping(value = "/toggleActivation", method = RequestMethod.GET)
    public ModelAndView subscribe(@RequestParam final int customerId, String redirectUrl) {
        ModelAndView result;
        try{
            Customer customer = customerService.findOne(customerId);
            this.customerService.disableUsersWhenDeactivated(customer);
           // this.customerService.setAllMembersActivation(customer,!customer.isActive());
            customer.setActive(!customer.getActive());
            this.customerService.save(customer);
        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                return createMessageModelAndView(oops.getLocalizedMessage(), redirectUrl);
            } else {
                return this.createMessageModelAndView("msg.commit.error", "/");
            }
        }
        result = new ModelAndView("redirect:" + ((redirectUrl!=null)?redirectUrl:"/customer/edit.do?id=" + customerId));
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
        if (actor != null && actor instanceof Responsible) {
            if (customer.getId() == actor.getCustomer().getId()) {
                result.addObject("actor", (Responsible) actor);
                result.addObject("owns", true);
            }
        } else if (actor != null && actor instanceof Manager) {
            result.addObject("actor", actor);
            result.addObject("owns", true);
        }
        return result;

    }
}
