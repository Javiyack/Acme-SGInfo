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
import domain.Servant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.RequestService;
import services.ServantService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/servant")
public class ServantController extends AbstractController {

    // Supporting services -----------------------------------------------------

    @Autowired
    private ServantService servantService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private RequestService requestService;
    // Constructor

    public ServantController() {
        super();
    }

    // List requestables servants
    // ---------------------------------------------------------------
    @RequestMapping("/list")
    public ModelAndView list(final Integer pageSize) {
        ModelAndView result;
        final Collection <Servant> servants = this.servantService.findRequestableServants();

        result = new ModelAndView("servant/list");
        result.addObject("servants", servants);
        result.addObject("requestableOnly", true);
        result.addObject("requestUri", "servant/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        return result;
    }


    // Display servant
    // ---------------------------------------------------------------
    @RequestMapping("/display")
    public ModelAndView display(final int id) {
        ModelAndView result;
        final Servant servant = this.servantService.findOne(id);
        result = new ModelAndView("servant/display");
        result.addObject("servantForm", servant);
        result.addObject("display", true);
        return result;
    }


}
