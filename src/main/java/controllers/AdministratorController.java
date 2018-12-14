/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private AdministratorService administratorService;

	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	// DASHBOARD
	// ----------------------------------------------------------------------------------

	@RequestMapping("/dashboard")
	public ModelAndView dashboard() {
		ModelAndView result;

		result = new ModelAndView("dashboard/display");

		// Level C -------------------------------------------------------------------------------------
		
		List<Object[]> rankingTop3Users;
		if(administratorService.usersWithMoreIncidences()!=null){
			rankingTop3Users = administratorService.usersWithMoreIncidences();
		}else{
			rankingTop3Users = new ArrayList<Object[]>(0);
		}
		
		List<Object[]> rankingTop3Technicians;
		if(administratorService.techniciansWithLessIncidences()!=null){
			rankingTop3Technicians = administratorService.techniciansWithLessIncidences();
		}else{
			rankingTop3Technicians = new ArrayList<Object[]>(0);
		}
		
		List<Object[]> percMessagesSenderByActor;
		if(administratorService.percMessagesSenderByActor()!=null){
			percMessagesSenderByActor = administratorService.percMessagesSenderByActor();
		}else{
			percMessagesSenderByActor = new ArrayList<Object[]>(0);
		}
		
		

		// Ranking 3 users with more incidences
		result.addObject("rankingTop3Users", rankingTop3Users);
		
		// Ranking 3 technicians with less incidences
		result.addObject("rankingTop3Technicians", rankingTop3Technicians);

		// Percentage messages sender by actor
		result.addObject("percMessagesSenderByActor", percMessagesSenderByActor);
		
		//Level B ------------------------------------------------------------------------------------

		// Avg, min, max,desv number of request by responsible
		result.addObject("avgRequestByResponsable", administratorService.avgRequestByResponsible());
		result.addObject("minRequestByResponsable", administratorService.minRequestByResponsible());
		result.addObject("maxRequestByResponsable", administratorService.maxRequestByResponsible());
		result.addObject("stddevRequestByResponsable", administratorService.stddevRequestByResponsible());

		

		return result;
	}

}
