package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Bill;
import domain.Customer;
import domain.Manager;
import domain.Responsable;
import forms.CustomerForm;
import repositories.CustomerRepository;

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
			customer.setFechaAlta(new Date());
		}else {
			Actor actor = actorService.findByPrincipal();
			Assert.notNull(actor, "msg.not.loged.block");
			Assert.isTrue(actor instanceof Responsable || actor instanceof Manager, "msg.not.owned.block");
			
		}
		result = this.customerRepository.save(customer);
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
		result.setActive(form.getActive());
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
		return result;
	}

	public Customer delete(CustomerForm customer) {
		Assert.notNull(customer);
		Actor actor = actorService.findByPrincipal();
		Assert.notNull(actor, "msg.not.loged.block");
		Assert.isTrue(actor instanceof Responsable || actor instanceof Manager, "msg.not.owned.block");
		if (actor instanceof Responsable)
			Assert.isTrue(((Responsable) actor).getCustomer().getId() == customer.getId(), "msg.not.owned.block");

		this.customerRepository.delete(customer.getId());

		return null;
	}

	public Customer findByBill(Bill bill) {		
		return this.customerRepository.findByBillId(bill.getId());
	}

	public Integer findByPrincipal() {
		Actor actor = actorService.findByPrincipal();
		Assert.notNull(actor, "msg.not.loged.block");
		return this.customerRepository.findByPrincipalId(actor.getId());
	}
}
