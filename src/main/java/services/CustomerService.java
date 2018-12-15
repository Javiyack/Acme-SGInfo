package services;

import domain.*;
import forms.CustomerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.CustomerRepository;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class CustomerService {
    // Repositories
    @Autowired
    private CustomerRepository customerRepository;
    // Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private Validator validator;

    // Constructor
    public CustomerService() {
        super();
    }

    // Simple CRUD methods ----------------------------------------------------

    // Create

    public Customer create() {

        final Customer result = new Customer();

        result.setFechaAlta(new Date());
        result.setPassKey(generatePassKey());
        result.setActive(false);
        return result;
    }

    // Save
    public Customer save(final Customer customer) {
        Assert.notNull(customer);
        Customer result;
        if (customer.getId() == 0) {
            customer.setFechaAlta(new Date(System.currentTimeMillis() - 100));
            customer.setActive(false);
        } else {
            Actor actor = actorService.findByPrincipal();
            Assert.notNull(actor, "msg.not.logged.block");
            Assert.isTrue((actor instanceof Responsible && actor.getCustomer().equals(customer)) || actor instanceof Manager, "msg.not.owned.block");
        }
        result = this.customerRepository.saveAndFlush(customer);
        return result;
    }

    public boolean disableUsersWhenDeactivated(Customer customer) {
        boolean result = true;
        try {
            Collection<Actor> members = this.actorService.findWorkers(customer);
            for (Actor member : members) {
                if (member.getUserAccount().isEnabled()) {
                    this.actorService.deactivateUserAccountByActorId(member.getId());
                }
            }
        } catch (Throwable oops) {
            result = false;
        }
        return result;
    }

    public boolean setAllMembersActivation(Customer customer, boolean activation) {
        boolean result = true;
        try {
            Collection<Actor> members = this.actorService.findWorkers(customer);
            for (Actor member : members) {
                    this.actorService.setActivationStatus(member, activation);
            }

        } catch (Throwable oops) {
            result = false;
        }
        return result;
    }
    public boolean activateAllMembers() {
        boolean result = true;
        try {
            Actor actor = actorService.findByPrincipal();
            Assert.notNull(actor, "msg.not.logged.block");
            Customer customer  = actor.getCustomer();
            this.setAllMembersActivation(customer, true);
        } catch (Throwable oops) {
            result = false;
        }
        return result;
    }

    public boolean deactivateAllMembers() {
        boolean result = true;
        try {
            Actor actor = actorService.findByPrincipal();
            Assert.notNull(actor, "msg.not.logged.block");
            Customer customer  = actor.getCustomer();
            this.setAllMembersActivation(customer, false);
        } catch (Throwable oops) {
            result = false;
        }
        return result;
    }



    public Collection<Customer> findAll() {
        return customerRepository.findAll();
    }



    public Customer recontruct(CustomerForm form, BindingResult binding) {
        Customer result;
        if (form.getId() == 0) {
            result = this.create();
            result.setId(0);
            result.setVersion(0);
        } else {
            result = this.customerRepository.findOne(form.getId());
        }
        result.setName(form.getName());
        result.setDescription(form.getDescription());
        result.setAddress(form.getAddress());
        result.setBillingAddress(form.getBillingAddress());
        result.setNif(form.getNif());
        result.setFechaAlta(form.getFechaAlta());
        // result.setActive(form.getActive()); // Evitado intencionadamente. Solo el manager puede desactivarlo. Tiene una funcion especifica para ello
        result.setEmail(form.getEmail());
        result.setLogo(form.getLogo());
        result.setPassKey(form.getPassKey());
        result.setWebSite(form.getWebSite());

        validator.validate(result, binding);
        return result;

    }

    public Page<Customer> findAllPaginated(int page, int size) {
        return customerRepository.findAll(new PageRequest(page, size));
    }

    public Customer findOne(int id) {
        return customerRepository.findOne(id);
    }

    private String generatePassKey() {
        String result = "CUS-";
        Calendar calendar = Calendar.getInstance();
        result += (("" + calendar.get(Calendar.DAY_OF_MONTH)).length() == 2) ? calendar.get(Calendar.DAY_OF_MONTH)
                : "0" + calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH) + 1;
        result += (("" + mes).length() == 2) ? mes : "0" + mes;
        result += (("" + calendar.get(Calendar.YEAR) % 100).length() == 2) ? calendar.get(Calendar.YEAR) % 100
                : "0" + calendar.get(Calendar.YEAR) % 100;
        result += "-";
        String momentOfDay = "" + System.currentTimeMillis() % 86400000;
        result += momentOfDay;
        int check = result.length();
        for (int n = check; n < 19; n++) {
            result += "0";
        }

        return result;
    }

    public Customer delete(CustomerForm customer) {
        Assert.notNull(customer);
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Responsible || actor instanceof Manager, "msg.not.owned.block");
        if (actor instanceof Responsible)
            Assert.isTrue(((Responsible) actor).getCustomer().getId() == customer.getId(), "msg.not.owned.block");

        this.customerRepository.delete(customer.getId());

        return null;
    }

    public Customer findByBill(Bill bill) {
        return this.customerRepository.findByBillId(bill.getId());
    }

    public Integer findByPrincipal() {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        return this.customerRepository.findIdByPrincipalId(actor.getId());
    }

    public Collection<Customer> findAllActive() {
        return customerRepository.findAllActive();
    }
}
