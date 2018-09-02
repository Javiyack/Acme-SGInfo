package controllers.internal;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Actor;
import domain.Incidence;
import domain.Labor;
import domain.Technician;
import domain.User;
import forms.IncidenceForm;
import services.ActorService;
import services.IncidenceService;
import services.LaborService;
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
	@Autowired
	private ActorService actorService;
	@Autowired
	private LaborService laborService;

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
		try {
			final Actor logedActor = this.actorService.findByPrincipal();
			Assert.notNull(logedActor, "msg.not.loged.block");
		} catch (Throwable oops) {
			if (oops.getMessage().startsWith("msg.")) {
				return createMessageModelAndView(oops.getLocalizedMessage(), "/");
			} else {
				return this.createMessageModelAndView("panic.message.text", "/");
			}
		}

		result = new ModelAndView("incidence/list");
		result.addObject("incidences", inidencias);
		result.addObject("requestUri", "incidence/internal/list.do");
		result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
		return result;
	}

	// Create incidence
	// ---------------------------------------------------------------
	@RequestMapping("/create")
	public ModelAndView create(@Valid final Integer customerId) {
		ModelAndView result;
		if (customerId != null) {
			final Incidence incidencia = this.incidenceService.create();
			final IncidenceForm incidence = new IncidenceForm(incidencia);
			result = this.createEditModelAndView(incidence, customerId);
		} else {
			result = new ModelAndView("redirect:/customer/list.do");
		}
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
		if (binding.hasErrors())
			result = this.createEditModelAndView(incidence, incidence.getCustomerId());
		else {
			Incidence incidencia = incidenceService.recontruct(incidence, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(incidence, incidence.getCustomerId());
			else
				try {
					incidencia = this.incidenceService.save(incidencia);
					incidence.setId(incidencia.getId());
					result = this.createEditModelAndView(incidence,
							incidence.getUser().getCustomer().getId());
					result.addObject("info", "msg.commit.ok");
				} catch (final Throwable oops) {
					if (oops.getMessage().startsWith("msg."))
						result = this.createEditModelAndView(incidence, oops.getLocalizedMessage(),
								incidence.getUser().getCustomer().getId());
					else
						result = this.createEditModelAndView(incidence, "msg.commit.error",
								incidence.getUser().getCustomer().getId());
				}
		}
		return result;
	}

	// Delete incidence
	// ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final IncidenceForm incidence, final BindingResult binding) {
		ModelAndView result;

		try {
			this.incidenceService.delete(incidence);
			result = new ModelAndView("redirect:/incidence/internal/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().startsWith("msg."))
				result = this.createEditModelAndView(incidence, oops.getLocalizedMessage(),
						incidence.getUser().getCustomer().getId());
			else
				result = this.createEditModelAndView(incidence, "msg.commit.error",
						incidence.getUser().getCustomer().getId());
		}
		return result;
	}

	// Close incidence. Set ending moment to current and save
	// ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "close")
	public ModelAndView close(@Valid final IncidenceForm incidence, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(incidence, incidence.getUser().getCustomer().getId());
		else {
			Incidence incidencia = incidenceService.recontruct(incidence, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(incidence, incidencia.getUser().getCustomer().getId());
			else
				try {
					incidencia.setEndingDate(new Date(System.currentTimeMillis() - 1000));
					this.incidenceService.save(incidencia);
					result = new ModelAndView("redirect:/incidence/internal/list.do");
				} catch (final Throwable oops) {
					if (oops.getMessage().startsWith("msg."))
						result = this.createEditModelAndView(incidence, oops.getLocalizedMessage(),
								incidence.getUser().getCustomer().getId());
					else
						result = this.createEditModelAndView(incidence, "msg.commit.error",
								incidence.getUser().getCustomer().getId());
				}
		}
		return result;
	}

	// Reopen incidence. Set ending moment to null and save
	// ---------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "reopen")
	public ModelAndView reopen(@Valid final IncidenceForm incidence, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(incidence, incidence.getUser().getCustomer().getId());
		else {
			Incidence incidencia = incidenceService.recontruct(incidence, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(incidence, incidencia.getUser().getCustomer().getId());
			else
				try {
					incidencia.setEndingDate(null);
					incidence.setEndingDate(null);
					this.incidenceService.save(incidencia);
					this.laborService.setBillToNull(incidencia);
					result = this.createEditModelAndView(incidence, "msg.commit.ok",
							incidence.getUser().getCustomer().getId());
				} catch (final Throwable oops) {
					if (oops.getMessage().startsWith("msg."))
						result = this.createEditModelAndView(incidence, oops.getLocalizedMessage(),
								incidence.getUser().getCustomer().getId());
					else
						result = this.createEditModelAndView(incidence, "msg.commit.error",
								incidence.getUser().getCustomer().getId());
				}
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
		Collection<Labor> labors = laborService.findByIncidence(incidence.getId());
		incidence.setCustomerId(customerId);
		result = new ModelAndView("incidence/edit");
		result.addObject("incidenceForm", incidence);
		result.addObject("technicians", techs);
		result.addObject("users", users);
		result.addObject("message", message);
		result.addObject("closed",
				(incidence.getEndingDate() != null) ? (new Date()).after(incidence.getEndingDate()) : false);
		result.addObject("requestUri", "incidence/internal/edit.do");
		result.addObject("labors", labors);

		return result;

	}
}
