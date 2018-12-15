
package controllers.manager;

import controllers.AbstractController;
import domain.Actor;
import domain.Customer;
import domain.Technician;
import forms.ActorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import security.Authority;
import services.ActorService;
import services.CustomerService;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping("/actor/manager")
public class ManagerActorController extends AbstractController {

    // Supporting services -----------------------------------------------------

    @Autowired
    private ActorService actorService;

    @Autowired
    private CustomerService customerService;

    // Constructors -----------------------------------------------------------

    public ManagerActorController() {
        super();
    }

    // List ------------------------------------------------------------------
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(final Integer pageSize) {
        ModelAndView result;

        final Collection<Actor> actors = this.actorService.findAllTecnicians();
        actors.addAll(this.actorService.findAllResponsibles());
        actors.addAll(this.actorService.findAllManagers());
        actors.addAll(this.actorService.findAllUsers());

        result = new ModelAndView("actor/list");
        result.addObject("actors", actors);
        result.addObject("requestUri", "actor/manager/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 5);
        return result;
    }

    // Activate deactivate members ------------------------------------------------------------------
    @RequestMapping(value = "/activation", method = RequestMethod.GET)
    public ModelAndView activation(final Integer id, Integer pageSize) {
        ModelAndView result;
        pageSize = (pageSize != null) ? pageSize : 5;
        final Actor actor = this.actorService.findOne(id);
        try {
            actorService.toggleActivation(actor);
            result = new ModelAndView("redirect:/actor/manager/list.do?pageSize=" + pageSize);
        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                result = createMessageModelAndView(oops.getLocalizedMessage(), "/actor/manager/list.do?pageSize=" + pageSize);
            } else
                result = this.createMessageModelAndView("msg.commit.error", "/actor/manager/list.do?pageSize=" + pageSize);
        }
        return result;
    }

    // activate deactivate All members ------------------------------------------------------------------
    @RequestMapping(value = "/activateAll", method = RequestMethod.GET)
    public ModelAndView activateAll(Integer pageSize) {
        ModelAndView result;
        pageSize = (pageSize != null) ? pageSize : 5;

        try {
            Assert.isTrue(this.actorService.setAllResponsiblesActivationTo(true));
            Assert.isTrue(this.actorService.setAllTechniciansActivationTo(true));
            result = new ModelAndView("redirect:/actor/manager/list.do?pageSize=" + pageSize);
        } catch (final Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                result = createMessageModelAndView(oops.getLocalizedMessage(), "/actor/manager/list.do?pageSize=" + pageSize);
            } else
                result = this.createMessageModelAndView("msg.commit.error", "/actor/manager/list.do?pageSize=" + pageSize);
        }
        return result;
    }

    // activate deactivate All members ------------------------------------------------------------------
    @RequestMapping(value = "/deactivateAll", method = RequestMethod.GET)
    public ModelAndView deactivateAllResponsibles(Integer pageSize) {
        ModelAndView result;
        pageSize = (pageSize != null) ? pageSize : 5;

        try {
            Assert.isTrue(this.actorService.setAllResponsiblesActivationTo(false));
            Assert.isTrue(this.actorService.setAllTechniciansActivationTo(false));
            result = new ModelAndView("redirect:/actor/manager/list.do?pageSize=" + pageSize);
        } catch (final Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                result = createMessageModelAndView(oops.getLocalizedMessage(), "/actor/manager/list.do?pageSize=" + pageSize);
            } else
                result = this.createMessageModelAndView("msg.commit.error", "/actor/manager/list.do?pageSize=" + pageSize);
        }
        return result;
    }


    // Create ---------------------------------------------------------------

    @RequestMapping("/create")
    public ModelAndView create() {
        ModelAndView result;
        final ActorForm registerForm = new ActorForm();
        result = this.createEditModelAndView(registerForm, null);
        return result;
    }


    // Save mediante Post ---------------------------------------------------

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(final ActorForm actorForm, final BindingResult binding) {
        ModelAndView result;
        Actor actor;

        try {
            actor = this.actorService.reconstruct(actorForm, binding);
            if (binding.hasErrors())
                result = this.createEditModelAndView(actorForm);
            else
                try {
                    Assert.isTrue(actor instanceof Technician, "msg.not.owned.block");
                    actor = this.actorService.save(actor);
                    actor.getUserAccount().setActive(true);
                    this.actorService.save(actor);
                    result = new ModelAndView("redirect:/actor/manager/list.do");
                } catch (final Throwable oops) {
                    if (oops.getMessage().startsWith("msg.")) {
                        return createMessageModelAndView(oops.getLocalizedMessage(), "/");
                    } else if (oops.getCause().getCause() != null
                            && oops.getCause().getCause().getMessage().startsWith("Duplicate"))
                        result = this.createEditModelAndView(actorForm, "msg.duplicate.username");
                    else
                        result = this.createEditModelAndView(actorForm, "msg.commit.error");
                }

        } catch (final Throwable oops) {
            if (oops.getLocalizedMessage().startsWith("msg."))
                result = this.createEditModelAndView(actorForm, oops.getLocalizedMessage());
            else if (oops.getCause().getCause() != null
                    && oops.getCause().getCause().getMessage().startsWith("Duplicate"))
                result = this.createEditModelAndView(actorForm, "msg.duplicate.username");
            else
                result = this.createEditModelAndView(actorForm, "actor.reconstruct.error");
        }

        return result;
    }

    // Auxiliary methods -----------------------------------------------------
    protected ModelAndView createEditModelAndView(final ActorForm model) {
        final ModelAndView result;
        result = this.createEditModelAndView(model, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final ActorForm model, final String message) {
        final ModelAndView result;
        final Collection<Authority> permisos = new ArrayList<>();
        final Collection<Customer> customers = new ArrayList<>();
        final Authority authority = new Authority();
        authority.setAuthority(Authority.TECHNICIAN);
        permisos.add(authority);
        customers.add(actorService.findByPrincipal().getCustomer());
        result = new ModelAndView("actor/create");
        result.addObject("actorForm", model);
        result.addObject("requestUri", "actor/manager/create.do");
        result.addObject("permisos", permisos);
        result.addObject("customers", customers);
        result.addObject("edition", true);
        result.addObject("creation", model.getId() == 0);
        result.addObject("message", message);

        return result;

    }

}
