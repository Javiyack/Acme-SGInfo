package usecases;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import domain.Customer;
import domain.Request;
import forms.CustomerForm;

import services.CustomerService;
import services.RequestService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class RequestUseCaseTest extends AbstractTest {
	
	@Autowired
    private RequestService requestService;

    private Map <String, Object> testingDataMap;

    /* 
     * Create customer.
     */
    @Test
    public void createCustomerTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateCustomerTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("servant", userData[i][1]);
            testingDataMap.put("comments", userData[i][2]);
            testingDataMap.put("expected", userData[i][3]);
            this.templateCreateCustomerTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                	"responsable1",
                   "servant1", "comments1",
                   null
                }, {// Negative: wrong username
                	"responsable11",
                    "servant1", "comments1",
                    IllegalArgumentException.class
                }, {//Negative: without username
                	"",
                    "servant1", "comments1",
                }, {// Negative: without service
                	"responsable1",
                    "", "comments1",
                }
        
        };
        return testingData;
    }

    protected void templateCreateCustomerTest() {
        Class <?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            
            Request request = requestService.create(super.getEntityId("servant"));
            
            request.setComments((String) testingDataMap.get("comments"));
           
            request = requestService.save(request);
            
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

}
