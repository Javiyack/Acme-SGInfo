package services;

import domain.*;
import forms.BillForm;
import forms.LaborForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.BillingRepository;
import repositories.MonthlyDueRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class BillingService {
    // Repositories
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private MonthlyDueRepository monthlyDueRepository;
    // Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    ConfigurationService configurationService;
    @Autowired
    private LaborService laborService;
    @Autowired
    private IncidenceService incidenceService;
    @Autowired
    private ServantService servantService;
    @Autowired
    private RequestService requestService;
    @Autowired
    private Validator validator;

    // Constructor
    public BillingService() {
        super();
    }

    // Simple CRUD methods ----------------------------------------------------

    // Create

    public Bill create() {
        Actor actor = actorService.findByPrincipal();
        Assert.isTrue(actor instanceof Manager, "msg.not.owned.block");
        final Bill result = new Bill();
        result.setMoment(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        result.setYear(calendar.get(Calendar.YEAR));
        result.setMonth(calendar.get(Calendar.MONTH) + 1);
        result.setAmount(new Money());
        return result;
    }

    // Save
    public Bill save(final Bill bill) {
        Assert.notNull(bill);
        Actor actor = actorService.findByPrincipal();
        Assert.isTrue(actor instanceof Manager, "msg.not.owned.block");

        Bill result;
        result = this.billingRepository.save(bill);
        return result;
    }

    public Collection<Bill> findAll() {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor);
        Assert.isTrue(actor instanceof Manager, "msg.not.owned.block");
        return billingRepository.findAll();
    }

    public Bill recontruct(BillForm form, BindingResult binding) {
        Bill result = null;
        if (form.getId() == 0) {
        } else {
        }

        validator.validate(result, binding);
        return result;

    }

    public Page<Bill> findAllPaginated(int page, int size) {
        return billingRepository.findAll(new PageRequest(page, size));
    }

    public Bill findOne(int id) {
        return billingRepository.findOne(id);
    }

    public boolean delete(LaborForm labor) {
        Assert.notNull(labor);
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Technician || actor instanceof Manager, "msg.not.owned.block");
        if (actor instanceof Technician)
            Assert.isTrue(actor.getId() == labor.getIncidence().getTechnician().getId(), "msg.not.owned.block");

        this.billingRepository.delete(labor.getId());

        return billingRepository.findOne(labor.getId()) == null;
    }

    public Collection<Bill> findByIncidence(int incidenceId) {
        Collection<Bill> result = billingRepository.findByIncidenceId(incidenceId);
        return result;
    }

    public boolean delete(Labor labor) {
        Assert.notNull(labor);
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Technician || actor instanceof Manager, "msg.not.owned.block");
        if (actor instanceof Technician)
            Assert.isTrue(actor.getId() == labor.getIncidence().getTechnician().getId(), "msg.not.owned.block");
        this.billingRepository.delete(labor.getId());
        return billingRepository.findOne(labor.getId()) == null;
    }

    public Collection<Incidence> findAllByActor() {
        // TODO Auto-generated method stub
        return null;
    }

    public void generateIncidenceBills() {
        // Recuperamos la incidencias facturables
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Manager, "msg.not.owned.block");
        Collection<Incidence> incidences = this.incidenceService.findFacturables();
        if (!incidences.isEmpty()) {
            for (Incidence incidence : incidences) {
                // Buscamos las tareas de esa incidencia
                Collection<Labor> labors = this.laborService.findByIncidence(incidence.getId());
                if (!labors.isEmpty()) {
                    // creamos una factura con esas labores
                    Bill bill = this.create();
                    Money money = new Money();
                    Double precioHora = configurationService.findHourPrice();
                    Double iva = configurationService.findIVA();
                    bill.setCurrentIVA(iva);
                    bill.setCurrentHourPrice(precioHora);
                    Calendar calendar = Calendar.getInstance();
                    bill = this.billingRepository.save(bill);
                    for (Labor labor : labors) {
                        labor.setBill(bill);
                        calendar.setTime(labor.getTime());
                        money.setAmount((precioHora * calendar.get(Calendar.HOUR_OF_DAY)
                                + calendar.get(Calendar.MINUTE) * precioHora / 60));
                        bill.getAmount().add(money);
                        laborService.save(labor);
                    }
                }
            }
        }
        /*
         * // recuperamos los cutomers Collection<Customer> customers =
         * this.customerService.findAll(); // por cada uno recuperamos las labores sin
         * facturar hasta el mes pasado for (Customer customer : customers) {
         * Collection<Labor> labors =
         * this.laborService.findFacturableByCustomer(customer); if (!labors.isEmpty())
         * { // creamos una factura con esas labores Bill bill = this.create(); Money
         * money = new Money(); money.setCurrency(Constant.CURRENCY_EURO);
         * money.setAmount(0.0); Double precioHora = 28.0; Calendar calendar =
         * Calendar.getInstance(); bill = this.billingRepository.save(bill); for (Labor
         * labor : labors) { labor.setBill(bill); calendar.setTime(labor.getTime());
         * money.setAmount(precioHora * calendar.get(Calendar.HOUR_OF_DAY) +
         * calendar.get(Calendar.MINUTE) * precioHora / 60);
         * bill.getAmount().add(money); laborService.save(labor); } } }
         */
    }

    public void generateServiceBills() {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Manager, "msg.not.owned.block");
        // Recuperamos los servicios facturables
        Collection<Customer> customers = customerService.findAll();
        for (Customer customer : customers) {
            Collection<MonthlyDue> newDues = new ArrayList<MonthlyDue>();
            Collection<Request> requests = this.servantService.findFacturablesByCustomerId(customer.getId());
            int minAnioInicial = 2018;
            int minMesInicial = 12;
            if (!requests.isEmpty()) {
                // Buscamos las deudas de los servicio

                for (Request request : requests) {

                    // Buscamos las deudas de ese servicio
                    Collection<MonthlyDue> monthlyDues = this.servantService.findAllMonthlyDues(request.getId());

                    //comprobamos que estan todas la deudas desde su comienzo
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(request.getStartingDay());
                    int mesInicial = calendar.get(Calendar.MONTH) + 1;
                    int anioInicial = calendar.get(Calendar.YEAR);
                    minAnioInicial = Math.min(minAnioInicial, anioInicial);
                    minMesInicial = Math.min(minMesInicial, mesInicial);
                    calendar = Calendar.getInstance();
                    int mesFinal = calendar.get(Calendar.MONTH) + 1;
                    int anioFinal = calendar.get(Calendar.YEAR);
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 0, 0, 0);
                    Date limit = calendar.getTime();
                    for (int anio = anioInicial; anio <= anioFinal; anio++) {
                        for (int mes = mesInicial; mes <= 12; mes++) {
                            boolean exists = false;
                            for (MonthlyDue monthlyDue : monthlyDues) {

                                if (monthlyDue.getMonth() == mes && monthlyDue.getYear() == anio) {
                                    exists = true;
                                    break;
                                }
                            }
                            if (!exists) {
                                if(!(anio==anioFinal && mes>=mesFinal)){
                                    MonthlyDue due = new MonthlyDue();
                                    due.setYear(anio);
                                    due.setMonth(mes);
                                    due.setRequest(request);
                                    newDues.add(due);
                                }
                            }
                        }
                    }
                }
            }
            // Creamos una factura por cada cliente y  mes contodos los servicios activos en ese momento
            Calendar calendar = Calendar.getInstance();
            int mesFinal = calendar.get(Calendar.MONTH) + 1;
            int anioFinal = calendar.get(Calendar.YEAR);
            for (int year = minAnioInicial; year <= anioFinal; year++) {
                for (int month = 1; month <= 12; month++) {
                    Bill bill = this.create();
                    Money money = new Money();
                    Double precioHora = configurationService.findHourPrice();
                    Double iva = configurationService.findIVA();
                    bill.setCurrentIVA(iva);
                    bill.setCurrentHourPrice(precioHora);
                    bill.setAmount(money);
                    bill.setYear(year);
                    bill.setMonth(month);
                    bill.setMoment(new Date());
                    for (MonthlyDue due : newDues) {
                        if (due.getYear() == year && due.getMonth() == month && due.getBill() == null) {
                            money.setAmount(this.calculaImporte(due, year, month));
                            bill.getAmount().add(money);
                            bill = this.billingRepository.save(bill);
                            due.setBill(bill);
                            this.saveDue(due);

                        }
                    }
                }
            }
            /*
             * // recuperamos los cutomers Collection<Customer> customers =
             * this.customerService.findAll(); // por cada uno recuperamos las labores sin
             * facturar hasta el mes pasado for (Customer customer : customers) {
             * Collection<Labor> labors =
             * this.laborService.findFacturableByCustomer(customer); if (!labors.isEmpty())
             * { // creamos una factura con esas labores Bill bill = this.create(); Money
             * money = new Money(); money.setCurrency(Constant.CURRENCY_EURO);
             * money.setAmount(0.0); Double precioHora = 28.0; Calendar calendar =
             * Calendar.getInstance(); bill = this.billingRepository.save(bill); for (Labor
             * labor : labors) { labor.setBill(bill); calendar.setTime(labor.getTime());
             * money.setAmount(precioHora * calendar.get(Calendar.HOUR_OF_DAY) +
             * calendar.get(Calendar.MINUTE) * precioHora / 60);
             * bill.getAmount().add(money); laborService.save(labor); } } }
             */
        }
    }

    public Double calculaImporte(MonthlyDue due, int year, int month){
        Double parte = 1.0;

        Calendar periodStartingDay = Calendar.getInstance();
        Calendar periodEndingDay = Calendar.getInstance();
        Calendar requestStartingDay = Calendar.getInstance();
        Calendar requestEndingDay = Calendar.getInstance();

        periodStartingDay.set(year, month-1, 1);
        periodEndingDay.set(year, month-1, 1);
        periodEndingDay.add(Calendar.MONTH, 1);

        if (due.getRequest().getStartingDay() != null)
            requestStartingDay.setTime(due.getRequest().getStartingDay());
        else
            requestStartingDay.setTime(new Date());

        if (due.getRequest().getEndingDay() != null)
            requestEndingDay.setTime(due.getRequest().getEndingDay());
        else
            requestEndingDay.setTime(new Date());

        if (requestStartingDay.getTime().after(periodStartingDay.getTime())) {
            if (requestStartingDay.getTime().before(periodEndingDay.getTime())) {
                parte = parte - (requestStartingDay.getTime().getTime() - periodStartingDay.getTime().getTime())*1.0 / (periodEndingDay.getTime().getTime() - periodStartingDay.getTime().getTime());
            }
        }
        if (requestEndingDay.getTime().before(periodEndingDay.getTime())) {
            if (requestEndingDay.getTime().after(periodStartingDay.getTime())) {
                parte = parte -(periodEndingDay.getTime().getTime() - requestEndingDay.getTime().getTime())*1.0 /(periodEndingDay.getTime().getTime() - periodStartingDay.getTime().getTime());
            }

        }
        return due.getRequest().getServant().getPrice() * parte;
    }

    public MonthlyDue saveDue(MonthlyDue due) {
        return monthlyDueRepository.save(due);
    }

    public Collection<Object> findAllPropperLaborBills() {
        // TODO Auto-generated method stub
        return billingRepository.findAllPropperLaborBills();
    }
    public Collection<Object> findAllPropperServiceBills() {
        // TODO Auto-generated method stub
        return billingRepository.findAllPropperServiceBills();
    }

    public Collection<Object> findPropperByCustomer() {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Responsible || actor instanceof Manager, "msg.not.owned.block");
        return billingRepository.findPropperByCustomerId(((Responsible) actor).getCustomer().getId());
    }

    public Collection<Object> findByCustomerId(Integer customerId) {
        return billingRepository.findByCustomerId(customerId);
    }


    public void checkOwns(Customer customer) {
        final Actor logedActor = this.actorService.findByPrincipal();
        Assert.notNull(logedActor, "msg.not.logged.block");
        Assert.isTrue(logedActor.getCustomer().equals(customer), "msg.not.owned.block");

    }

    public Collection<MonthlyDue> findDuesByBill(Bill bill) {
        return billingRepository.findDuesByBill(bill);
    }
}
