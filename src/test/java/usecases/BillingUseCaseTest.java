package usecases;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.BillingService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@Transactional
public class BillingUseCaseTest extends AbstractTest{
	
    @Autowired
    private BillingService billingService;

    private Map <String, Object> testingDataMap;

    /* 
     * Generate incidence and service bills
     */
    @Test
    public void generateBillsTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateCustomerTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("expected", userData[i][1]);
            this.templateGenerateBillsTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                	"manager1",
                   null
                }, {//Negative: wrong username
                	"man",
                    IllegalArgumentException.class
                }, {// Negative: username without privileges
                	"user1",
                	IllegalArgumentException.class
                },{// Negative: without username
                	"",
                    IllegalArgumentException.class
                }
        
        };
        return testingData;
    }

	
    protected void templateGenerateBillsTest() {
        Class <?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            
            billingService.generateIncidenceBills();
            billingService.generateServiceBills();
            
           
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

}
