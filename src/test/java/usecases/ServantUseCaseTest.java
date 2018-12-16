package usecases;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import domain.Servant;
import forms.ServantForm;

import services.ServantService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class ServantUseCaseTest extends AbstractTest {

	@Autowired
	private ServantService servantService;

	private Map<String, Object> testingDataMap;

	/*
	 * Create and cancel servant
	 */
	@Test
	public void createCancelServantTest() throws ParseException {
		/*
		 * Aquí cargamos los datos a probar y el error esperado en
		 * 'testingDataMap' y llamamos a la plantilla
		 * 'templateCreateCustomerTest'
		 */

		Object userData[][] = (Object[][]) getEditionTestingData();
		for (int i = 0; i < userData.length; i++) {
			testingDataMap = new HashMap<String, Object>();
			testingDataMap.put("username", userData[i][0]);
			testingDataMap.put("name", userData[i][1]);
			testingDataMap.put("description", userData[i][2]);
			testingDataMap.put("picture", userData[i][3]);
			testingDataMap.put("price", userData[i][4]);
			testingDataMap.put("draft", userData[i][5]);
			testingDataMap.put("expected", userData[i][6]);
			this.templateCreateCancelServantTest();
		}
	}

	protected Object getEditionTestingData() {
		final Object testingData[][] = { 
				{
					// Positive
					"manager1", "servant1", "description1", "http://picture.com",
					5.0, false, null 
				}, {
					// Negative: User no privileges
					"user1", "servant1", "description1", "http://picture.com",
					5.0, false, IllegalArgumentException.class
				},{
					// Negative: without title
					"manager1", "", "description1", "http://picture.com",
					5.0, false, IllegalArgumentException.class
				},{
					// Negative: price 0.0
					"manager1", "", "description1", "http://picture.com",
					0.0, false, IllegalArgumentException.class
				},

		};
		return testingData;
	}

	protected void templateCreateCancelServantTest() {
		Class<?> caught;
		/*
		 * Simulamos la edición de usuario con los datos cargados en
		 * 'testingDataMap' y luego comprobamos el error esperado
		 */
		caught = null;
		try {
			super.startTransaction();
			super.authenticate((String) testingDataMap.get("username"));

			Servant servant;
			ServantForm servantForm = new ServantForm(servantService.create());

			servantForm.setName((String) testingDataMap.get("name"));
			servantForm.setDescription((String) testingDataMap.get("description"));
			servantForm.setPicture((String) testingDataMap.get("picture"));
			servantForm.setPrice((Double) testingDataMap.get("price"));
			servantForm.setDraft((Boolean) testingDataMap.get("draft"));

			DataBinder dataBinder = new DataBinder(servantForm);
			BindingResult binding = dataBinder.getBindingResult();
			servant = servantService.recontruct(servantForm, binding);
			Assert.isTrue(!binding.hasErrors());
			servant = this.servantService.save(servant);
			super.flushTransaction();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions((Class<?>) testingDataMap.get("expected"), caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
