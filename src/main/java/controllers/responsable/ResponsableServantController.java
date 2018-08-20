package controllers.responsable;

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
@RequestMapping("/servant/responsable")
public class ResponsableServantController extends AbstractController {

	// Services
	@Autowired
	private ServantService servantService;

	// Constructor

	public ResponsableServantController() {
		super();
	}

	// List All incidences
	// ---------------------------------------------------------------
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Servant> inidencias = this.servantService.findAll();

		result = new ModelAndView("service/user/list");
		result.addObject("services", inidencias);
		return result;
	}

	// Create service
		// ---------------------------------------------------------------
		@RequestMapping("/create")
		public ModelAndView create() {
			ModelAndView result;
			final Servant inidencia = this.servantService.create();

			result = this.createEditModelAndView(inidencia);

			return result;
		}

		
		// Edit service
		// ---------------------------------------------------------------
		@RequestMapping("/edit")
		public ModelAndView edit(@Valid final int id) {
			ModelAndView result;
			final Servant inidencia = this.servantService.findOne(id);

			result = this.createEditModelAndView(inidencia);

			return result;
		}

		
		// Save service
	// ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Servant inidencia, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(inidencia);
		else
			try {
				this.servantService.save(inidencia);
				result = new ModelAndView("redirect:/service/user/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(inidencia, "manager.commit.error");
			}
		return result;
	}
	
	
	// Create service con Forms
		// ---------------------------------------------------------------
		@RequestMapping("/register")
		public ModelAndView register() {
			ModelAndView result;
			final ServantEditForm service = new ServantEditForm();


			result = this.createEditModelAndView(service);
			return result;
		}
		// Save service con Forms
		// ---------------------------------------------------------------
		@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
		public ModelAndView save(@Valid final ServantEditForm inidenciaForm, final BindingResult binding) {
			ModelAndView result;
			Servant service = servantService.recontruct(inidenciaForm, binding);
			
			if (binding.hasErrors())
				result = this.createEditModelAndView(inidenciaForm);
			else
				try {
					this.servantService.save(service);
					result = new ModelAndView("redirect:/");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(inidenciaForm, "manager.commit.error");
				}
			return result;
		}
		
	// Auxiliary methods
	// ---------------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Servant inidencia) {
		final ModelAndView result;
		result = this.createEditModelAndView(inidencia, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Servant service, final String message) {
		final ModelAndView result;

		result = new ModelAndView("service/user/create");
		result.addObject("service", service);
		result.addObject("message", message);
		result.addObject("requestUri", "service/user/create.do");

		return result;

	}
	
	
	protected ModelAndView createEditModelAndView(final ServantEditForm inidencia) {
		final ModelAndView result;
		result = this.createEditModelAndView(inidencia, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ServantEditForm service, final String message) {
		final ModelAndView result;

		result = new ModelAndView("service/user/register");
		result.addObject("service", service);
		result.addObject("message", message);
		result.addObject("requestUri", "service/user/register.do");

		return result;

	}
}
