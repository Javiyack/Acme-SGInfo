package usecases;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Configuration;

import services.ConfigurationService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class ConfigurationUseCaseTest extends AbstractTest {

	@Autowired
	private ConfigurationService configurationService;

	private Map<String, Object> testingDataMap;

	/*
	 * Edit configuration
	 */
	@Test
	public void editConfigurationTest() throws ParseException {

		Object userData[][] = (Object[][]) getEditionTestingData();
		for (int i = 0; i < userData.length; i++) {
			testingDataMap = new HashMap<String, Object>();
			testingDataMap.put("username", userData[i][0]);
			testingDataMap.put("company", userData[i][1]);
			testingDataMap.put("passkey", userData[i][2]);
			testingDataMap.put("logo", userData[i][3]);
			testingDataMap.put("welcomees", userData[i][4]);
			testingDataMap.put("welcomeen", userData[i][5]);
			testingDataMap.put("folders", userData[i][6]);
			testingDataMap.put("price", userData[i][7]);
			testingDataMap.put("VAT", userData[i][8]);
			testingDataMap.put("currency", userData[i][9]);
			testingDataMap.put("expected", userData[i][10]);
			this.templateEditConfigurationTest();
		}
	}
	

	protected Object getEditionTestingData() {
		final Object testingData[][] = { 
				{
					// Positive
					"admin","company1",
					"CUS-010100-00000022",
					"http://logo.com",
					"welcomees","welcomeen",
					Arrays.asList("folders"),
					2.0,10.0,"USD",
					null 
				}/*,{
					// Negative: wrong username
					"man", IllegalArgumentException.class 
				}, {
					
				}*/

		};
		return testingData;
	}

	protected void templateEditConfigurationTest() {
		Class<?> caught;
		/*
		 * Simulamos la edición de usuario con los datos cargados en
		 * 'testingDataMap' y luego comprobamos el error esperado
		 */
		caught = null;
		try {
			super.startTransaction();
			super.authenticate((String) testingDataMap.get("username"));
			
			Configuration configuration = configurationService.findOne(super.getEntityId("configuration"));
			configuration.setCompanyName((String)testingDataMap.get("companyName"));
			configuration.setPassKey((String)testingDataMap.get("passkey"));
			configuration.setLogo((String)testingDataMap.get("logo"));
			configuration.setWelcomeMessageEs((String)testingDataMap.get("welcomees"));
			configuration.setWelcomeMessageEn((String)testingDataMap.get("welcomeen"));
			configuration.setFolderNames((List<String>)testingDataMap.get("folders"));
			configuration.setHourPrice((Double)testingDataMap.get("price"));
			configuration.setIva((Double)testingDataMap.get("VAT"));
			configuration.setDefaultCurrency((String)testingDataMap.get("currency"));
			
			configurationService.save(configuration);

			super.flushTransaction();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions((Class<?>) testingDataMap.get("expected"), caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
