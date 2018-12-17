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

import java.util.*;

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
        // Por cada customer ...
        Collection<Customer> customers = customerService.findAll();
        for (Customer customer : customers) {
            int minAnioInicial = Calendar.getInstance().get(Calendar.YEAR);
            int minMesInicial = Calendar.getInstance().get(Calendar.MONTH) + 1;

            // Aqui vamos almacenando las nuevas deudas
            Set<MonthlyDue> newDues = new HashSet<MonthlyDue>();

            // Requests. Recuperamos las solicitudes facturables
            Collection<Request> requests = this.servantService.findFacturablesByCustomerId(customer.getId());

            // REQUEST. Por cada request facturable
            for (Request request : requests) {
                // Buscamos las deudas de ese servicio
                Set<MonthlyDue> monthlyDues = this.servantService.findAllMonthlyDues(request.getId());

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
                for (int anio = anioInicial; anio <= anioFinal; anio++) {
                    for (int mes = mesInicial; mes <= 12; mes++) {
                        // Comprobamos que sea facturable por las fechas
                        if (checkFacturable(request, mes, anio)) {

                            // Creamos una deuda por mes
                            MonthlyDue newDue = new MonthlyDue();
                            newDue.setYear(anio);
                            newDue.setMonth(mes);
                            newDue.setRequest(request);
                            // Conprobamos que no exista ya la deuda (ahora no hace falta esto porque usamos un set.
                            // TODO Comprobar que sigue funcionando si quitamos este check pues monthly Due es un Set
                            if (!monthlyDues.contains(newDue)) {
                                // añadimos la nueva deuda
                                newDues.add(newDue);
                            }
                        }

                    }
                }

                // En este punto tenemos todas la nuevas deudas para ese request

            }
            // En este punto tenemos todas la nuevas deudas para todos los requests facturables de ese customer

            // Creamos una factura por cada cliente y  mes con todos las deudas de ese mes
            Calendar calendar = Calendar.getInstance();
            int mesFinal = calendar.get(Calendar.MONTH) + 1;
            int anioFinal = calendar.get(Calendar.YEAR);
            Set<MonthlyDue> billedDues = new HashSet<>();
            // Recorremos los meses
            for (int year = minAnioInicial; year <= anioFinal; year++) {
                for (int month = 1; month <= 12; month++) {
                    // Creamos una factura
                    Bill bill = this.create();
                    Money money = new Money();
                    Double precioHora = configurationService.findHourPrice();
                    Double iva = configurationService.findIVA();
                    bill.setCurrentIVA(iva);
                    bill.setCurrentHourPrice(precioHora);
                    bill.setAmount(money);
                    bill.setYear(year);
                    bill.setMonth(month);
                    bill.setMoment(new Date(System.currentTimeMillis() - 100));
                    Double amount = 0.0;
                    for (MonthlyDue due : newDues) {
                        //Si la deuda es del mes y año entonces la añadimos a la factura del mes y año
                        if (due.getYear() == year && due.getMonth() == month && due.getBill() == null) {
                            amount += this.calculaImporte(due, month, year);
                            bill.getAmount().setAmount(amount);
                            bill = this.billingRepository.saveAndFlush(bill);
                            due.setBill(bill);
                            this.saveDue(due);
                            this.monthlyDueRepository.flush();
                            billedDues.add(due);
                        }
                    }
                    // Quitamos las deudas que ya tengan factura
                    newDues.removeAll(billedDues);
                }
            }
        }
    }

    private boolean checkFacturable(Request request, int month, int year) {
        boolean isFacturable = false;
        Calendar periodStartingDay = Calendar.getInstance();
        Calendar periodEndingDay = Calendar.getInstance();
        Calendar requestStartingDay = Calendar.getInstance();
        Calendar requestEndingDay = Calendar.getInstance();

        periodStartingDay.set(year, month - 1, 1);
        periodEndingDay.set(year, month - 1, 1);
        periodEndingDay.add(Calendar.MONTH, 1);
        if (request.getStartingDay() != null)
            requestStartingDay.setTime(request.getStartingDay());
        else
            requestStartingDay.setTime(new Date());

        if (request.getEndingDay() != null)
            requestEndingDay.setTime(request.getEndingDay());
        else
            requestEndingDay.setTime(new Date());
        if (requestStartingDay.before(requestEndingDay) && requestStartingDay.before(periodEndingDay) && requestEndingDay.after(periodStartingDay))
            isFacturable = true;
        return isFacturable;
    }

    public Double calculaImporte(MonthlyDue due, int month, int year) {
        Double importe = 0.0;
        Double fracion = 1.0;
        Calendar periodStartingDay = Calendar.getInstance();
        Calendar periodEndingDay = Calendar.getInstance();
        Calendar requestStartingDay = Calendar.getInstance();
        Calendar requestEndingDay = Calendar.getInstance();

        periodStartingDay.set(year, month - 1, 1);
        periodEndingDay.set(year, month - 1, 1);
        periodEndingDay.add(Calendar.MONTH, 1);

        if (due.getRequest().getStartingDay() != null)
            requestStartingDay.setTime(due.getRequest().getStartingDay());
        else
            requestStartingDay.setTime(new Date());

        if (due.getRequest().getEndingDay() != null)
            requestEndingDay.setTime(due.getRequest().getEndingDay());
        else
            requestEndingDay.setTime(new Date());

        if (requestStartingDay.before(requestEndingDay) && requestStartingDay.before(periodEndingDay) && requestEndingDay.after(periodStartingDay)){
            if (requestStartingDay.getTime().after(periodStartingDay.getTime())) {
                if (requestStartingDay.getTime().before(periodEndingDay.getTime())) {
                    fracion = fracion - (requestStartingDay.getTime().getTime() - periodStartingDay.getTime().getTime()) * 1.0 / (periodEndingDay.getTime().getTime() - periodStartingDay.getTime().getTime());
                }
            }
            if (requestEndingDay.getTime().before(periodEndingDay.getTime())) {
                if (requestEndingDay.getTime().after(periodStartingDay.getTime())) {
                    fracion = fracion - (periodEndingDay.getTime().getTime() - requestEndingDay.getTime().getTime()) * 1.0 / (periodEndingDay.getTime().getTime() - periodStartingDay.getTime().getTime());
                }
            }
        }
        importe = due.getRequest().getServant().getPrice() * fracion;
        return importe;
    }


    public MonthlyDue saveDue(MonthlyDue due) {
        MonthlyDue savedDue = monthlyDueRepository.save(due);
        monthlyDueRepository.flush();
        return savedDue;
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


    public Collection<Object> findOwnedBills() {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Responsible || actor instanceof Manager, "msg.not.owned.block");
        Collection<Object> result;
        if(actor instanceof Manager){
            result =  billingRepository.findAllPropperServiceBills();
            result.addAll(billingRepository.findAllPropperLaborBills());
        }else{
            result =  billingRepository.findPropperByCustomerId(((Responsible) actor).getCustomer().getId());
            result.addAll(billingRepository.findAllPropperServiceBillsByCustomerId(((Responsible) actor).getCustomer().getId()));
        }
        return result;
    }
    public Collection<Object> findOwnedServiceBills() {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Responsible || actor instanceof Manager, "msg.not.owned.block");
        Collection<Object> result;
        if(actor instanceof Manager){
            return  billingRepository.findAllPropperServiceBills();
        }else{
            return billingRepository.findAllPropperServiceBillsByCustomerId(((Responsible) actor).getCustomer().getId());
        }

    }
    public Collection<Object> findOwnedLaborBills() {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Responsible || actor instanceof Manager, "msg.not.owned.block");
        if(actor instanceof Manager){
            return billingRepository.findAllPropperLaborBills();
        }else{
            return billingRepository.findPropperByCustomerId(((Responsible) actor).getCustomer().getId());
        }

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
