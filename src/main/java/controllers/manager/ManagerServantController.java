
package controllers.manager;

import controllers.AbstractController;
import domain.Servant;
import forms.ServantForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import services.ServantService;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/servant/manager")
public class ManagerServantController extends AbstractController {

    // Supporting services -----------------------------------------------------

    @Autowired
    private ServantService servantService;

    // Constructor

    public ManagerServantController() {
        super();
    }
    // Create servant
    // ---------------------------------------------------------------
    @RequestMapping("/create")
    public ModelAndView create() {
        ModelAndView result;
        final Servant servant = this.servantService.create();
        ServantForm servantForm = new ServantForm(servant);
        result = this.createEditModelAndView(servantForm);

        return result;
    }

    // Edit servant
    // ---------------------------------------------------------------
    @RequestMapping("/edit")
    public ModelAndView edit(@Valid final int id) {
        ModelAndView result;
        final Servant servant = this.servantService.findOne(id);
        ServantForm servantForm = new ServantForm(servant);
        result = this.createEditModelAndView(servantForm);

        return result;
    }
    // List requestables servants
    // ---------------------------------------------------------------
    @RequestMapping("/list")
    public ModelAndView list(final Integer pageSize) {
        ModelAndView result;
        final Collection<Servant> servants = this.servantService.findAll();

        result = new ModelAndView("servant/manager/list");
        result.addObject("servants", servants);
        result.addObject("requestUri", "servant/manager/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        return result;
    }



    // Save servant
    // ---------------------------------------------------------------
    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(final ServantForm servantForm, final BindingResult binding) {
        ModelAndView result;
        Servant servant = servantService.recontruct(servantForm, binding);

        if (binding.hasErrors())
            result = this.createEditModelAndView(servantForm);
        else
            try {
                servant = this.servantService.save(servant);
                result = new ModelAndView("redirect:/servant/list.do");
            } catch (final Throwable oops) {
                if (oops.getMessage().startsWith("msg."))
                    result = this.createEditModelAndView(servantForm, oops.getLocalizedMessage());
                else if (oops.getCause().getCause() != null
                        && oops.getCause().getCause().getMessage().startsWith("Duplicate"))
                    result = this.createEditModelAndView(servantForm, "msg.duplicate.name");
                else
                    result = this.createEditModelAndView(servantForm, "msg.commit.error");
            }
        return result;
    }


    protected ModelAndView createEditModelAndView(final ServantForm servant) {
        final ModelAndView result;
        result = this.createEditModelAndView(servant, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final ServantForm servant, final String message) {
        final ModelAndView result;
        result = new ModelAndView("servant/manager/edit");
        result.addObject("servantForm", servant);
        result.addObject("message", message);
        result.addObject("requestUri", "servant/manager/create.do");
        return result;

    }

}
