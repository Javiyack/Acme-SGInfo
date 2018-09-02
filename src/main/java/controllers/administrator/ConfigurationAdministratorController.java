
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.ConfigurationService;
import controllers.AbstractController;

@Controller
@RequestMapping("/configuration/administrator")
public class ConfigurationAdministratorController extends AbstractController {

	@Autowired
	ConfigurationService	configurationService;

	@Autowired
	AdministratorService	administratorService;


	// Constructors -----------------------------------------------------------

	public ConfigurationAdministratorController() {
		super();
	}

	// ---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		ModelAndView result;
		final Collection<domain.Configuration> configurations = this.configurationService.findAll();
		result = new ModelAndView("configuration/administrator/edit");
		result.addObject("configuration", configurations.toArray()[0]);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final domain.Configuration configuration, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("configuration/administrator/edit");
			result.addObject("configuration", configuration);

		} else
			try {
				this.configurationService.save(configuration);
				result = this.createMessageModelAndView("configuration.commit.ok", "/");
			} catch (final Throwable oops) {
				result = this.createMessageModelAndView("configuration.commit.ko", "/");
			}
		return result;
	}
}
