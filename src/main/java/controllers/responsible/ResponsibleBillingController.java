package controllers.responsible;

import controllers.AbstractController;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import services.*;

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
    @Autowired
    private CustomerService customerService;

    // Constructor

    public ResponsibleBillingController() {
        super();
    }


    // List all incidences Bill
    // ---------------------------------------------------------------
    @RequestMapping("/labor/list")
    public ModelAndView listIncidenceBills( Integer pageSize) {
        ModelAndView result;
        pageSize = ((pageSize != null) ? pageSize : 20);
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
            result.addObject("requestUri", "billing/responsible/labor/list.do");
            result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
            result.addObject("backUrl", "/billing/responsible/labor/list.do");
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
            result.addObject("requestUri", "billing/responsible/service/list.do");
            result.addObject("backUrl", "/billing/responsible/service/list.do");
            result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        } catch (final Throwable oops) {
            if (oops.getMessage().startsWith("msg."))
                result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
            else
                result = this.createMessageModelAndView("msg.commit.error", "/");
        }
        return result;
    }
    // List All bills
    // ---------------------------------------------------------------
    @RequestMapping("/list")
    public ModelAndView list(Integer pageSize) {
        ModelAndView result;
        final Collection<Object> bills = this.billingService.findPropperByCustomer();
        bills.addAll(this.billingService.findAllPropperServiceBills());

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
        result.addObject("requestUri", "billing/responsible/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
        result.addObject("backUrl", "/billing/responsible/list.do");
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
            result.addObject("requestUri", "billing/responsible/customer/list.do");
            result.addObject("pageSize", (pageSize != null) ? pageSize : 20);
            result.addObject("customerId", customerId);
            result.addObject("backUrl", "/billing/responsible/customer/list.do?customerId=" + customerId);
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
        Double iva = configurationService.findIVA();
        Double precioHora = configurationService.findHourPrice();
        Customer biller = customerService.findBiller();
        result = new ModelAndView("billing/display");
        result.addObject("bill", bill);
        result.addObject("iva", iva);
        result.addObject("backUrl", "/billing/responsible/customer/list.do");
        Collection<Labor> labors = this.laborService.findByBill(bill);
        Customer customer;
        if (!labors.isEmpty()) {

            //		Customer customer = this.customerService.findByBill(bill);
            Labor anyLabor = labors.iterator().next();
            customer = anyLabor.getIncidence().getUser().getCustomer();
            result.addObject("labors", labors);
            result.addObject("biller", biller);
            result.addObject("precioHora", precioHora);
            result = checkPermisos(customer, result);
        } else {
            Collection<MonthlyDue> dues = this.billingService.findDuesByBill(bill);
            if (!dues.isEmpty()) {
                MonthlyDue anyDue = dues.iterator().next();
                customer = anyDue.getRequest().getResponsible().getCustomer();
                Map<MonthlyDue, Double> dueAmountMap = new HashMap<>();
                for (MonthlyDue due : dues) {
                    dueAmountMap.put(due, billingService.calculaImporte(due, bill.getMonth(), bill.getYear()));
                }
                result.addObject("dues", dues);
                result.addObject("dueAmount", dueAmountMap);
                result.addObject("biller", biller);
                result = checkPermisos(customer, result);
            }
        }
        return result;
    }

    private ModelAndView checkPermisos(Customer customer, ModelAndView result) {
        try {
            Actor actor = actorService.findByPrincipal();
            Assert.notNull(actor, "msg.not.logged.block");
            Assert.isTrue(actor instanceof Responsible, "msg.not.owned.block");
            Assert.isTrue(actor.getCustomer().equals(customer), "msg.not.owned.block");
        } catch (final Throwable oops) {
            if (oops.getMessage().startsWith("msg."))
                result = this.createMessageModelAndView(oops.getLocalizedMessage(), "/");
            else
                result = this.createMessageModelAndView("msg.commit.error", "/");
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
