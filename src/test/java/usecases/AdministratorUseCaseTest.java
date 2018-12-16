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
import services.AdministratorService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@Transactional
public class AdministratorUseCaseTest extends AbstractTest{
	
    @Autowired
    private AdministratorService administratorService;

    private Map <String, Object> testingDataMap;

    /* 
     * Display dashboard
     */
    @Test
    public void displayDashboardTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateCustomerTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("expected", userData[i][1]);
            this.templateDisplayDashboardTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                	"admin",
                   null
                }, {//Negative: wrong username
                	"admin1",
                    IllegalArgumentException.class
                }, {// Negative: username without privileges
                	"user1",
                    ClassCastException.class
                },{// Negative: without username
                	"",
                    IllegalArgumentException.class
                }
        
        };
        return testingData;
    }

	
    protected void templateDisplayDashboardTest() {
        Class <?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            
            administratorService.usersWithMoreIncidences();
            administratorService.avgRequestByResponsible();
            administratorService.bestRatedIncidences();
            administratorService.maxRequestByResponsible();
            administratorService.minRequestByResponsible();
            administratorService.percMessagesSenderByActor();
            administratorService.stddevRequestByResponsible();
            administratorService.techniciansWithLessIncidences();
            administratorService.usersWithMoreIncidences();
            administratorService.worstRatedTechnicianOfIncidences();
            
           
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

}
