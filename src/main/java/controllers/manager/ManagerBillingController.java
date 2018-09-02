package controllers.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;
import domain.Bill;
import domain.Customer;
import domain.Labor;
import forms.BillForm;
import services.BillingService;
import services.CustomerService;
import services.LaborService;

@Controller
@RequestMapping("/billing/manager")
public class ManagerBillingController extends AbstractController {

	// Services
	@Autowired
	private BillingService billingService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private LaborService laborService;

	// Constructor

	public ManagerBillingController() {
		super();
	}

	// List All incidences
	// ---------------------------------------------------------------
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Object> bills = this.billingService.findAllPropper();
		Map<Customer, List<Bill>> billsByCustomer = new HashMap<Customer, List<Bill>>();
		if (!bills.isEmpty()) {
			for (Object object : bills) {
				final Object[] entryCustomerBill = (Object[]) object;
			Bill bill = (Bill) entryCustomerBill[0];
			Customer customer = (Customer) entryCustomerBill[1];
			List<Bill> customerBills = ((billsByCustomer.get(customer)!=null) ?billsByCustomer.get(customer):new ArrayList<Bill>());
			customerBills.add(bill);
			billsByCustomer.put(customer, customerBills);			
			}
		}
		result = new ModelAndView("billing/list");
		result.addObject("facturas", billsByCustomer);
		return result;
	}

	// Generate billing
	// ---------------------------------------------------------------
	@RequestMapping("/generate")
	public ModelAndView generate() {
		ModelAndView result;
		try {
			billingService.generate();
			result = new ModelAndView("redirect:/billing/manager/list.do");
		} catch (Throwable oops) {
			if (oops.getMessage().startsWith("msg.")) {
				return createMessageModelAndView(oops.getLocalizedMessage(), "billing/manager/list.do");
			} else {
				return this.createMessageModelAndView("msg.commit.error", "billing/manager/list.do");
			}

		}
		return result;
	}

	// Create billing
	// ---------------------------------------------------------------
	@RequestMapping("/create")
	public ModelAndView create() {
		ModelAndView result;
		result = new ModelAndView("billing/create");
		result.addObject("incidences", "");
		return result;
	}

	// Edit billing
	// ---------------------------------------------------------------
	@RequestMapping("/edit")
	public ModelAndView edit(@Valid final int id) {
		ModelAndView result;
		Bill bill = billingService.findOne(id);
		Collection<Labor> labors = this.laborService.findByBill(bill);
//		Customer customer = this.customerService.findByBill(bill);
		Labor anyLabor = labors.iterator().next();
		Customer customer = anyLabor.getIncidence().getUser().getCustomer();
		Customer biller = anyLabor.getIncidence().getTechnician().getCustomer();
		result = new ModelAndView("billing/edit");
		result.addObject("bill", bill);
		result.addObject("labors", labors);
		result.addObject("customer", customer);
		result.addObject("biller", biller);
		result.addObject("precioHora", 28.0);
		result.addObject("iva", 0.21);
		result.addObject("backUrl", "/billing/manager/list.do");
		return result;
	}

	// Save billing
	// ---------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Bill bill, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(bill);
		else
			try {
				this.billingService.save(bill);
				result = new ModelAndView("redirect:/billing/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(bill, "msg.commit.error");
			}
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

		result = new ModelAndView("billing/create");
		result.addObject("billing", bill);
		result.addObject("message", message);
		result.addObject("requestUri", "billing/create.do");

		return result;

	}

	protected ModelAndView createEditModelAndView(final BillForm bill) {
		final ModelAndView result;
		result = this.createEditModelAndView(bill, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final BillForm bill, final String message) {
		final ModelAndView result;

		result = new ModelAndView("billing/edit");
		result.addObject("billing", bill);
		result.addObject("message", message);
		result.addObject("requestUri", "billing/edit.do");

		return result;

	}
}
