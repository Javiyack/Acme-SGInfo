package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Servant;
import forms.ServantEditForm;
import services.ServantService;

@Controller
@RequestMapping("/servant")
public class ServantController extends AbstractController {

	// Services
	@Autowired
	private ServantService servantService;

	
	// List All servants
	// ---------------------------------------------------------------
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Servant> servants = this.servantService.findAll();

		result = new ModelAndView("servant/list");
		result.addObject("servants", servants);
		return result;
	}
	
	
}
