
package controllers.manager;

import controllers.AbstractController;
import domain.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.RequestService;

import java.util.Arrays;
import java.util.Collection;

@Controller
@RequestMapping("/request/manager")
public class ManagerRequestController extends AbstractController {
    /* 	Manage the requests for items in their showrooms, which includes listing them and
        updating them. The status of a request may be either pending, accepted, or reject-
        ed. A pending request can be accepted or rejected; no other status changes can be
        performed.
        4. Manage his or her own requests for items, which includes listing and creating them.
    */
    // Supporting services -----------------------------------------------------

    @Autowired
    private RequestService requestService;


    // Constructors -----------------------------------------------------------

    public ManagerRequestController() {
        super();
    }

    // List received requests -------------------------------------------------------
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView createdList(final Integer pageSize) {
        ModelAndView result;
        final Collection <Request> requests = this.requestService.findAll();
        result = new ModelAndView("request/manager/list");
        result.addObject("requests", requests);
        result.addObject("requestUri", "request/manager/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 5);
        return result;
    }


    // Save mediante Post ---------------------------------------------------

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(Request request, BindingResult binding) {
        ModelAndView result;
        request = requestService.reconstruct(request, binding);
        if (binding.hasErrors())
            result = this.createEditModelAndView(request);
        else
            try {
                request = this.requestService.save(request);
                result = new ModelAndView("redirect:/request/manager/list.do");
            } catch (Throwable oops) {
                if (oops.getMessage().startsWith("msg."))
                    result = this.createEditModelAndView(request, oops.getLocalizedMessage());
                else
                    result = this.createEditModelAndView(request, "msg.commit.error");
            }
        return result;
    }

    // Auxiliary methods -----------------------------------------------------
    protected ModelAndView createEditModelAndView(final Request model) {
        final ModelAndView result;
        result = this.createEditModelAndView(model, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final Request model, final String message) {
        final ModelAndView result;
        result = new ModelAndView("request/manager/create");
        result.addObject("request", model);
        result.addObject("requestUri", "request/manager/create.do");
        result.addObject("edition", true);
        result.addObject("creation", model.getId() == 0);
        String[] states = {Request.ACCEPTED, Request.PENDING, Request.REJECTED};
        Collection <String> estados = Arrays.asList(states);
        result.addObject("states", states);
        result.addObject("estados", estados);
        result.addObject("message", message);
        return result;
    }
}
