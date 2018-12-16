package usecases;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.TabooWord;

import services.BillingService;
import services.TabooWordService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class TabooUseCaseTest extends AbstractTest{
	
	@Autowired
    private TabooWordService tabooWordService;

    private Map <String, Object> testingDataMap;

    /* 
     * Create and delete taboo word
     */
    @Test
    public void createTabooWordTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateCustomerTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("word", userData[i][1]);
            testingDataMap.put("expected", userData[i][2]);
            this.templateCreateTabooWordTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                	"admin","feo",
                   null
                }, {//Negative: wrong username
                	"admin1","feo",
                    IllegalArgumentException.class
                }, {// Negative: username without privileges
                	"user1","feo",
                	ClassCastException.class
                },{// Negative: without word
                	"admin","",
                    ConstraintViolationException.class
                }
        
        };
        return testingData;
    }

	
    protected void templateCreateTabooWordTest() {
        Class <?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            
            TabooWord deleteTaboo;
	        TabooWord taboo = new TabooWord();
	        taboo.setText((String) testingDataMap.get("word"));
	        deleteTaboo = tabooWordService.save(taboo);
	        tabooWordService.delete(deleteTaboo);
            
           
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

}
