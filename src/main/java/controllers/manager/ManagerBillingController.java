package controllers.manager;

import controllers.AbstractController;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import services.*;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.util.*;

@Controller
@RequestMapping("/billing/manager")
public class ManagerBillingController extends AbstractController {

	// Services
	@Autowired
	private BillingService billingService;
	@Autowired
	private LaborService laborService;
	@Autowired
	ConfigurationService configurationService;
	@Autowired
	private ActorService actorService;
	@Autowired
	private CustomerService customerService;
	// Constructor

	public ManagerBillingController() {
		super();
	}


	// List all incidences Bill
	// ---------------------------------------------------------------
	@RequestMapping("/labor/list")
	public ModelAndView listIncidenceBills(Integer pageSize) {
		ModelAndView result;
		try {
			Actor actor = actorService.findByPrincipal();
			Assert.notNull(actor, "msg.not.logged.block");

			final Collection<Object> bills = this.billingService.findOwnedLaborBills();
			Map<Customer, List<Bill>> billsByCustomer = new HashMap<Customer, List<Bill>>();
			if (!bills.isEmpty()) {
				for (Object object : bills) {
					final Object[] entryCustomerBill = (Object[]) object;
					Bill bill = (Bill) entryCustomerBill[0];
					Customer customer = (Customer) entryCustomerBill[1];
					List<Bill> customerBills = ((billsByCustomer.get(customer) != null) ? billsByCustomer.get(customer)
							: new ArrayList<Bill>());
					customerBills.add(bill);
					billsByCustomer.put(customer, customerBills);
				}
			}
			result = new ModelAndView("billing/list");
			result.addObject("facturas", billsByCustomer);
			result.addObject("requestUri", "billing/manager/service/list.do");
			result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
            result.addObject("backUrl", "/billing/responsible/manager/list.do");
		} catch (final Throwable oops) {
			if (oops.getMessage().startsWith("msg."))
				result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
			else
				result = this.createMessageModelAndView("msg.commit.error", "/");
		}
		return result;
	}
	// List All incidences
	// ---------------------------------------------------------------
	@RequestMapping("/service/list")
	public ModelAndView listServantBills(Integer pageSize ) {
		ModelAndView result;
		try {
			Actor actor = actorService.findByPrincipal();
			Assert.notNull(actor, "msg.not.logged.block");
			final Collection<Object> bills = this.billingService.findOwnedServiceBills();
			Map<Customer, List<Bill>> billsByCustomer = new HashMap<Customer, List<Bill>>();
			if (!bills.isEmpty()) {
				for (Object object : bills) {
					final Object[] entryCustomerBill = (Object[]) object;
					Bill bill = (Bill) entryCustomerBill[0];
					Customer customer = (Customer) entryCustomerBill[1];
					List<Bill> customerBills = ((billsByCustomer.get(customer) != null) ? billsByCustomer.get(customer)
							: new ArrayList<Bill>());
					customerBills.add(bill);
					billsByCustomer.put(customer, customerBills);
				}
			}
			result = new ModelAndView("billing/list");
			result.addObject("facturas", billsByCustomer);
			result.addObject("requestUri", "billing/manager/service/list.do");
			result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
            result.addObject("backUrl", "/billing/responsible/manager/list.do");
		} catch (final Throwable oops) {
			if (oops.getMessage().startsWith("msg."))
				result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
			else
				result = this.createMessageModelAndView("msg.commit.error", "/");
		}
		return result;
	}
	// List incidences by customer
	// ---------------------------------------------------------------
	@RequestMapping("/customer/list")
	public ModelAndView listCuntomerBills(int customerId, Integer pageSize ) {
		ModelAndView result;
		try {
			Actor actor = actorService.findByPrincipal();
			Assert.notNull(actor, "msg.not.logged.block");
			final Set<Bill> bills = this.billingService.findBillsByCustomerId( customerId);

			result = new ModelAndView("billing/list");
			result.addObject("facturas", bills);
			result.addObject("oneCustomerOnly", true);
			result.addObject("requestUri", "billing/manager/customer/list.do");
			result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
			result.addObject("customerId", customerId);
            result.addObject("backUrl", "/billing/responsible/manager/list.do?customerId=" + customerId);
		} catch (final Throwable oops) {
			if (oops.getMessage().startsWith("msg."))
				result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
			else
				result = this.createMessageModelAndView("msg.commit.error", "/");
		}
		return result;
	}

	// List All incidences
	// ---------------------------------------------------------------
	@RequestMapping("/list")
	public ModelAndView list(Integer pageSize) {
		ModelAndView result;
		try {
			Actor actor = actorService.findByPrincipal();
			Assert.notNull(actor, "msg.not.logged.block");

			final Collection<Object> bills = this.billingService.findOwnedBills();
			Map<Customer, List<Bill>> billsByCustomer = new HashMap<Customer, List<Bill>>();
			if (!bills.isEmpty()) {
				for (Object object : bills) {
					final Object[] entryCustomerBill = (Object[]) object;
					Bill bill = (Bill) entryCustomerBill[0];
					Customer customer = (Customer) entryCustomerBill[1];
					List<Bill> customerBills = ((billsByCustomer.get(customer) != null) ? billsByCustomer.get(customer)
							: new ArrayList<Bill>());
					customerBills.add(bill);
					billsByCustomer.put(customer, customerBills);
				}
			}
			result = new ModelAndView("billing/list");
			result.addObject("facturas", billsByCustomer);
			result.addObject("requestUri", "billing/manager/service/list.do");
			result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
		} catch (final Throwable oops) {
			if (oops.getMessage().startsWith("msg."))
				result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
			else
				result = this.createMessageModelAndView("msg.commit.error", "/");
		}
		return result;
	}

	// Generate billing
	// ---------------------------------------------------------------
	@RequestMapping("/generate")
	public ModelAndView generate() {
		ModelAndView result;
		String message;
		try {
			billingService.generateIncidenceBills();
			billingService.generateServiceBills();
			message =  "msg.commit.ok";
		} catch (Throwable oops) {
			if (oops.getMessage().startsWith("msg.")) {
				message =  oops.getLocalizedMessage();
			} else {
				message = "msg.commit.error";
			}
		}

		result =  this.list(20);
		result.addObject("info", message);
		return result;
	}

	// Edit billing
	// ---------------------------------------------------------------
	@RequestMapping("/display")
	public ModelAndView edit(@Valid final int id, String backUrl) {
		ModelAndView result = new ModelAndView("billing/display");
		Bill bill = billingService.findOne(id);
		Collection<Labor> labors = this.laborService.findByBill(bill);
		Double iva = configurationService.findIVA();
		if(!labors.isEmpty()) {
//		Customer customer = this.customerService.findByBill(bill);
			Labor anyLabor = labors.iterator().next();
			Customer customer = anyLabor.getIncidence().getUser().getCustomer();
			Customer biller = anyLabor.getIncidence().getTechnician().getCustomer();
			result.addObject("bill", bill);
			result.addObject("labors", labors);
			result.addObject("customer", customer);
			result.addObject("biller", biller);
			Double precioHora = configurationService.findHourPrice();
			result.addObject("precioHora", precioHora);
			result.addObject("iva", iva);
			result.addObject("backUrl", (backUrl!=null)? backUrl:"/billing/manager/list.do");
			try {
				Actor actor = actorService.findByPrincipal();
				Assert.notNull(actor, "msg.not.logged.block");
				Assert.isTrue(actor instanceof Manager, "msg.not.owned.block");
			} catch (final Throwable oops) {
				if (oops.getMessage().startsWith("msg."))
					result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
				else
					result = this.createMessageModelAndView("msg.commit.error", "/");
			}
		}else{
			Collection<MonthlyDue> dues = this.billingService.findDuesByBill(bill);
			if(!dues.isEmpty()) {
				MonthlyDue anyDue = dues.iterator().next();
				Customer customer = anyDue.getRequest().getResponsible().getCustomer();
				Customer biller = customerService.findBiller();
				Map<MonthlyDue, Double> dueAmountMap = new HashMap<>();
				for(MonthlyDue due: dues){
					dueAmountMap.put(due, billingService.calculaImporte(due, bill.getMonth(), bill.getYear()));
				}
				result.addObject("bill", bill);
				result.addObject("dues", dues);
				result.addObject("dueAmount", dueAmountMap);
				result.addObject("customer", customer);
				result.addObject("biller", biller);
				result.addObject("iva", iva);
				result.addObject("backUrl", "/billing/manager/list.do");
				try {
					Actor actor = actorService.findByPrincipal();
					Assert.notNull(actor, "msg.not.logged.block");
					Assert.isTrue(actor instanceof Manager, "msg.not.owned.block");
				} catch (final Throwable oops) {
					if (oops.getMessage().startsWith("msg."))
						result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
					else
						result = this.createMessageModelAndView("msg.commit.error", "/");
				}
			}


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

		result = new ModelAndView("billing/display");
		result.addObject("billing", bill);
		result.addObject("message", message);

		return result;

	}
}
