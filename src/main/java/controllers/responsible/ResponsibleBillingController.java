package controllers.responsible;

import controllers.AbstractController;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.BillingService;
import services.ConfigurationService;
import services.LaborService;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/billing/responsible")
public class ResponsibleBillingController extends AbstractController {

    // Services
    @Autowired
    private BillingService billingService;
    @Autowired
    private LaborService laborService;
    @Autowired
    ConfigurationService configurationService;
    @Autowired
    private ActorService actorService;

    // Constructor

    public ResponsibleBillingController() {
        super();
    }

    // List All bills
    // ---------------------------------------------------------------
    @RequestMapping("/list")
    public ModelAndView list() {
        ModelAndView result;
        final Collection<Object> bills = this.billingService.findPropperByCustomer();
        Map<Customer, List<Bill>> billsByCustomer = new HashMap<Customer, List<Bill>>();
        if (!bills.isEmpty()) {
            for (Object object : bills) {
                final Object[] entryCustomerBill = (Object[]) object;
                Bill bill = (Bill) entryCustomerBill[0];
                Customer customer = (Customer) entryCustomerBill[1];
                List<Bill> customerBills = ((billsByCustomer.get(customer) != null) ? billsByCustomer.get(customer) : new ArrayList<Bill>());
                customerBills.add(bill);
                billsByCustomer.put(customer, customerBills);
            }
        }
        result = new ModelAndView("billing/list");
        result.addObject("facturas", billsByCustomer);
        try {
            Actor actor = actorService.findByPrincipal();
            Assert.notNull(actor, "msg.not.logged.block");
            Assert.isTrue(actor instanceof Responsible, "msg.not.owned.block");
        } catch (final Throwable oops) {
            if (oops.getMessage().startsWith("msg."))
                result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
            else
                result = this.createMessageModelAndView("msg.commit.error", "/");
        }
        return result;
    }


    // Edit billing
    // ---------------------------------------------------------------
    @RequestMapping("/display")
    public ModelAndView edit(@Valid final int id) {
        ModelAndView result;

        Bill bill = billingService.findOne(id);

        Collection<Labor> labors = this.laborService.findByBill(bill);

//		Customer customer = this.customerService.findByBill(bill);
		Labor anyLabor = labors.iterator().next();
		Customer customer = anyLabor.getIncidence().getUser().getCustomer();
		try {
			billingService.checkOwns(customer);
		} catch (Throwable oops) {
			if (oops.getMessage().startsWith("msg.")) {
				return createMessageModelAndView(oops.getLocalizedMessage(), "/");
			} else {
				return this.createMessageModelAndView("panic.message.text", "/");
			}
		}
	Customer biller = anyLabor.getIncidence().getTechnician().getCustomer();
		Double iva = configurationService.findIVA();
		Double precioHora = configurationService.findHourPrice();
		result = new ModelAndView("billing/display");
		result.addObject("bill", bill);
		result.addObject("labors", labors);
		result.addObject("customer", customer);
		result.addObject("biller", biller);
		result.addObject("precioHora", precioHora);
		result.addObject("iva", iva);
		result.addObject("backUrl", "/billing/responsible/list.do");
		return result;
	}


    // Auxiliary methods
    // ---------------------------------------------------------------
    protected ModelAndView createEditModelAndView(final Bill bill) {
        final ModelAndView result;
        result = this.createEditModelAndView(bill, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final Bill bill, final String message) {
        final ModelAndView result;

        result = new ModelAndView("billing/display");
        result.addObject("billing", bill);
        result.addObject("message", message);

        return result;

    }

}
