package controllers.internal;

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
import domain.Technician;
import domain.User;
import forms.IncidenceForm;
import services.IncidenceService;
import services.TechnicianService;
import services.UserService;

@Controller
@RequestMapping("/incidence/internal")
public class InternalIncidenceController extends AbstractController {

	// Services
	@Autowired
	private IncidenceService incidenceService;
	@Autowired
	private TechnicianService technicianService;
	@Autowired
	private UserService userService;

	// Constructor

	public InternalIncidenceController() {
		super();
	}

	// List All incidences
	// ---------------------------------------------------------------
	@RequestMapping("/list")
	public ModelAndView list(final Integer pageSize) {
		ModelAndView result;
		final Collection<Incidence> inidencias = this.incidenceService.findAll();

		result = new ModelAndView("incidence/list");
		result.addObject("incidences", inidencias);
		result.addObject("requestUri", "incidence/internal/list.do");
		result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
		return result;
	}

	// Create incidence
	// ---------------------------------------------------------------
	@RequestMapping("/create")
	public ModelAndView create(@Valid final int customerId) {
		ModelAndView result;
		final Incidence incidencia = this.incidenceService.create();
		final IncidenceForm incidence = new IncidenceForm(incidencia);

		result = this.createEditModelAndView(incidence, customerId);

		return result;
	}

	// Edit incidence
	// ---------------------------------------------------------------
	@RequestMapping("/edit")
	public ModelAndView edit(@Valid final int id) {
		ModelAndView result;
		final Incidence incidencia = this.incidenceService.findOne(id);

		final IncidenceForm incidence = new IncidenceForm(incidencia);
		result = this.createEditModelAndView(incidence, incidencia.getUser().getCustomer().getId());

		return result;
	}

	// Edit incidence
	// ---------------------------------------------------------------
	@RequestMapping("/display")
	public ModelAndView display(@Valid final int id) {
		ModelAndView result;
		final Incidence incidencia = this.incidenceService.findOne(id);

		final IncidenceForm incidence = new IncidenceForm(incidencia);
		result = this.createEditModelAndView(incidence, incidencia.getUser().getCustomer().getId());
		result.addObject("display", true);

		return result;
	}

	// Save incidence
	// ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final IncidenceForm incidence, final BindingResult binding) {
		ModelAndView result;
		Incidence incidencia = incidenceService.recontruct(incidence, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(incidence, incidencia.getUser().getCustomer().getId());
		else
			try {
				this.incidenceService.save(incidencia);
				result = new ModelAndView("redirect:/incidence/internal/list.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("msg.not.loged.block"))
					result = this.createEditModelAndView(incidence, "msg.not.loged.block",
							incidence.getUser().getCustomer().getId());
				else if (oops.getMessage().equals("msg.not.owned.block"))
					result = this.createEditModelAndView(incidence, "msg.not.owned.block",
							incidence.getUser().getCustomer().getId());
				else
					result = this.createEditModelAndView(incidence, "msg.commit.error",
							incidence.getUser().getCustomer().getId());
			}
		return result;
	}

	// Save incidence
	// ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final IncidenceForm incidence, final BindingResult binding) {
		ModelAndView result;

		try {
			this.incidenceService.delete(incidence);
			result = new ModelAndView("redirect:/incidence/internal/list.do");

		} catch (final Throwable ooops) {
			if (ooops.getMessage().equals("msg.not.loged.block"))
				result = this.createEditModelAndView(incidence, "msg.not.loged.block",
						incidence.getUser().getCustomer().getId());
			else if (ooops.getMessage().equals("msg.not.owned.block"))
				result = this.createEditModelAndView(incidence, "msg.not.owned.block",
						incidence.getUser().getCustomer().getId());
			else
				result = this.createEditModelAndView(incidence, "msg.commit.error",
						incidence.getUser().getCustomer().getId());
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final IncidenceForm inidence, int customerId) {
		final ModelAndView result;
		result = this.createEditModelAndView(inidence, null, customerId);
		return result;
	}

	protected ModelAndView createEditModelAndView(final IncidenceForm incidence, final String message, int customerId) {
		final ModelAndView result;

		Collection<Technician> techs = technicianService.findAll();
		Collection<User> users = userService.findAllByCustomerId(customerId);

		result = new ModelAndView("incidence/edit");
		result.addObject("incidenceForm", incidence);
		result.addObject("technicians", techs);
		result.addObject("users", users);
		result.addObject("message", message);
		result.addObject("requestUri", "incidence/internal/edit.do");

		return result;

	}
}
