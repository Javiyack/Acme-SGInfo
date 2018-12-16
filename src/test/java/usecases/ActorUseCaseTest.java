package usecases;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import domain.Actor;
import forms.ActorForm;

import security.UserAccount;
import services.ActorService;
import services.CustomerService;
import utilities.AbstractTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@Transactional
public class ActorUseCaseTest extends AbstractTest {
	
	// System under test ------------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private CustomerService customerService;
	
	//Create user	
	protected void templateReconstructAndSave(final String name, final String surname, final String email, final String phone, final String address, final String username, final String password, 
			final String confirmPassword, final String customer, final String passKey, final String authority, final boolean agree, final Class<?> expected) {

			Class<?> caught;

			caught = null;

			try {
				ActorForm actorform = new ActorForm();
				Actor actor;
				UserAccount useraccount = new UserAccount();
				useraccount.setPassword(password);
				actorform.setName(name);
				actorform.setSurname(surname);
				actorform.setEmail(email);
				actorform.setPhone(phone);
				actorform.setAddress(address);
				actorform.getAccount().setUsername(username);
				actorform.getAccount().setPassword(password);
				actorform.getAccount().setNewPassword(password);
				actorform.getAccount().setConfirmPassword(confirmPassword);
				actorform.getAccount().setAuthority(authority);
				actorform.setCustomer(customerService.findOne(super.getEntityId(customer)));
				actorform.setPassKey(passKey);
				actorform.setAgree(agree);
				
				 DataBinder dataBinder = new DataBinder(actorform);
		         BindingResult binding = dataBinder.getBindingResult();
		         actor = actorService.reconstruct(actorform, binding);
		         Assert.isTrue(!binding.hasErrors());
		         actor = this.actorService.save(actor);
		         this.actorService.flush();

			} catch (Throwable oops) {
				caught = oops.getClass();
			}

			this.checkExceptions(expected, caught);
		}
	
	

	/**
	 * En este caso test vamos a probar a guardar un usuario con distintos errores, como por ejemplo, no aceptar los terminos y condiciones o
	 * guardar dos entidades con la misma id y recogeremos la excepcion que esperamos.
	 */
	@Test
	public void TestReconstructAndSaveNegative() {

		Object testingReconstructSaveNegative[][] = {
			{
				"NAME", "SURNAME", "actor@hotmail.com", "QWEQWERQWRQ", "ADDRESS", "USERNAME", "PASSWORD", "PASSWORD", "TECHNICIAN", "customer1", "CUS-010100-00000001", true, 
				ConstraintViolationException.class
			}, {
				"NAME", "SURNAME", "actor@hotmail.com", "666666666", "ADDRESS", "USERNAME", "PASSWORD", "PASSWORD", "TECHNICIAN", "customer2", "CUS-010100-00000001", true,  
				IllegalArgumentException.class
			}, {
				"NAME", "SURNAME", "actor@hotmail.com", "666666666", "ADDRESS", "USERNAME", "PASSWORD", "DIFFERENTPASSWORD", "TECHNICIAN", "customer1", "CUS-010100-00000001", false,  
				ConstraintViolationException.class
			}, {
				"NAME", "SURNAME", "actor@hotmail.com", "666666666", "ADDRESS", "USERNAME", "PASSWORD", "PASSWORD", "USER", "customer1", "CUS-010100-00000001", true,
				DataIntegrityViolationException.class
			}, {
				"NAME", "SURNAME", "actor", "666666666", "ADDRESS", "USERNAME", "PASSWORD", "PASSWORD", "TECHNICIAN", "customer1", "CUS-010100-00000001", true,
				ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingReconstructSaveNegative.length; i++) {
			this.templateReconstructAndSave((String) testingReconstructSaveNegative[i][0], (String) testingReconstructSaveNegative[i][1], (String) testingReconstructSaveNegative[i][2], (String) testingReconstructSaveNegative[i][3],
				(String) testingReconstructSaveNegative[i][4], (String) testingReconstructSaveNegative[i][5], (String) testingReconstructSaveNegative[i][6], (String) testingReconstructSaveNegative[i][7], (String) testingReconstructSaveNegative[i][8],
				(String) testingReconstructSaveNegative[i][9], (String) testingReconstructSaveNegative[i][10], (Boolean) testingReconstructSaveNegative[i][11], (Class<?>) testingReconstructSaveNegative[i][12]);
		}

	}

	/**
	 * En este caso test probaremos a guardar un usuario sin violar ninguna de las restricciones, por lo que el usuario se crea correctamente
	 */
	@Test
	public void TestReconstructAndSavePositive() {

		Object testingReconstructSavePositive[][] = {
			{
				"NAME", "SURNAME", "actor@hotmail.com", "666666666", "ADDRESS", "USERNAME", "PASSWORD", "PASSWORD", "customer1", "CUS-010100-00000001", "Authority.MANAGER", true, null
			}
		};

		for (int i = 0; i < testingReconstructSavePositive.length; i++) {
			this.templateReconstructAndSave((String) testingReconstructSavePositive[i][0], (String) testingReconstructSavePositive[i][1], (String) testingReconstructSavePositive[i][2], (String) testingReconstructSavePositive[i][3],
				(String) testingReconstructSavePositive[i][4], (String) testingReconstructSavePositive[i][5], (String) testingReconstructSavePositive[i][6], (String) testingReconstructSavePositive[i][7], (String) testingReconstructSavePositive[i][8],
				(String) testingReconstructSavePositive[i][9], (String) testingReconstructSavePositive[i][10], (Boolean) testingReconstructSavePositive[i][11], (Class<?>) testingReconstructSavePositive[i][12]);
		}

	}

	
}