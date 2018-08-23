/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.technician;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import controllers.LaborController;

@Controller
@RequestMapping("/labor/manager")
public class TechnicianLaborController extends LaborController {
	
	// Constructor

	public TechnicianLaborController() {
		super();
	}

	

}
