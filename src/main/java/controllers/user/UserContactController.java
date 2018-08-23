package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Incidence;
import forms.IncidenceForm;
import services.IncidenceService;

@Controller
@RequestMapping("/contact/user")
public class UserContactController extends AbstractController {

	// Services
	@Autowired
	private IncidenceService incidenceService;

	// Constructor

	public UserContactController() {
		super();
	}

	// List All incidences
	// ---------------------------------------------------------------
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Incidence> inidencias = this.incidenceService.findAll();

		result = new ModelAndView("incidence/user/list");
		result.addObject("incidences", inidencias);
		return result;
	}

	// Create incidence
		// ---------------------------------------------------------------
		@RequestMapping("/create")
		public ModelAndView create() {
			ModelAndView result;
			final Incidence inidencia = this.incidenceService.create();

			result = this.createEditModelAndView(inidencia);

			return result;
		}

		
		// Edit incidence
		// ---------------------------------------------------------------
		@RequestMapping("/edit")
		public ModelAndView edit(@Valid final int id) {
			ModelAndView result;
			final Incidence inidencia = this.incidenceService.findOne(id);

			result = this.createEditModelAndView(inidencia);

			return result;
		}

		
		// Save incidence
	// ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Incidence inidencia, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(inidencia);
		else
			try {
				this.incidenceService.save(inidencia);
				result = new ModelAndView("redirect:/incidence/user/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(inidencia, "msg.commit.error");
			}
		return result;
	}
	
	
	// Create incidence con Forms
		// ---------------------------------------------------------------
		@RequestMapping("/register")
		public ModelAndView register() {
			ModelAndView result;
			final IncidenceForm incidence = new IncidenceForm();


			result = this.createEditModelAndView(incidence);
			return result;
		}
		// Save incidence con Forms
		// ---------------------------------------------------------------
		@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
		public ModelAndView save(@Valid final IncidenceForm inidenciaForm, final BindingResult binding) {
			ModelAndView result;
			Incidence incidence = incidenceService.recontruct(inidenciaForm, binding);
			
			if (binding.hasErrors())
				result = this.createEditModelAndView(inidenciaForm);
			else
				try {
					this.incidenceService.save(incidence);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(inidenciaForm, "msg.commit.error");
				}
			return result;
		}
		
	// Auxiliary methods
	// ---------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Incidence inidencia) {
		final ModelAndView result;
		result = this.createEditModelAndView(inidencia, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Incidence incidence, final String message) {
		final ModelAndView result;

		result = new ModelAndView("incidence/user/create");
		result.addObject("incidence", incidence);
		result.addObject("message", message);
		result.addObject("requestUri", "incidence/user/create.do");

		return result;

	}
	
	
	protected ModelAndView createEditModelAndView(final IncidenceForm inidencia) {
		final ModelAndView result;
		result = this.createEditModelAndView(inidencia, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final IncidenceForm incidence, final String message) {
		final ModelAndView result;

		result = new ModelAndView("incidence/user/register");
		result.addObject("incidence", incidence);
		result.addObject("message", message);
		result.addObject("requestUri", "incidence/user/register.do");

		return result;

	}
}
