
package controllers.administrator;

import controllers.AbstractController;
import domain.Folder;
import domain.Message;
import domain.PostBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.MessageService;

@Controller
@RequestMapping("/message/administrator")
public class MessageAdminController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private MessageService	messageService;


	//Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Message m;
		m = this.messageService.create();
		result = this.createEditModelAndView(m);
		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("modelMessage") Message m, final BindingResult binding) {

		ModelAndView result;

		m = this.messageService.reconstruct(m, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(m);
		else
			try {
				this.messageService.broadcastMessage(m);
				result = new ModelAndView("redirect:/");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(m, "ms.commit.error");
			}
		return result;
	}


	// Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int messageId) {
		ModelAndView result;
		Message message;
		message = messageService.findOne(messageId);
		try {
			this.messageService.delete(message);
			result = new ModelAndView("redirect:/tabooWord/administrator/seek.do");
		} catch (final Throwable oops) {
			if (oops.getMessage().startsWith("msg."))
				result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
			else
				result = this.createMessageModelAndView("msg.commit.error", "/");
		}

		return result;

	}


	//Ancillary Methods

	protected ModelAndView createEditModelAndView(final Message m) {
		ModelAndView result;

		result = this.createEditModelAndView(m, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message m, final String messageCode) {
		ModelAndView result;
		result = new ModelAndView("broadcast/create");
		result.addObject("modelMessage", m);
		result.addObject("message", messageCode);
		result.addObject("requestUri", "message/administrator/edit.do");

		return result;
	}

}
