package usecases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import domain.Labor;
import forms.LaborForm;

import services.IncidenceService;
import services.LaborService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class LaborUseCaseTest extends AbstractTest{
	
    @Autowired
    private LaborService laborService;
    
    @Autowired
    private IncidenceService incidenceService;

    private Map <String, Object> testingDataMap;

    /* 
     * Create,edit and delete labor.
     */
    @Test
    public void createLaborTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateCustomerTest'
         */
    	
        

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
        	SimpleDateFormat timesdf = new SimpleDateFormat("HH:mm");
            Date time= timesdf.parse((String) userData[i][3]);
            
            SimpleDateFormat timesdfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date moment= timesdfm.parse((String) userData[i][4]);
            
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("incidence", userData[i][1]);
            testingDataMap.put("labor", userData[i][2]);
            testingDataMap.put("time", time);
            testingDataMap.put("moment", moment);
            testingDataMap.put("description", userData[i][5]);
            
            testingDataMap.put("expected", userData[i][6]);
            this.templateCreateCustomerTest();
        }
    }
    
    

    protected Object getEditionTestingData() {
    	
        final Object testingData[][] = {
                {// Positive
                   "technician2","incidence5",
                   "name1", "15:00",
                   "2018-12-16T15:00",
                   "description1",
                   null
                }, /*{// Negative: wrong email
                	"admin",
                    "name1", "address1",
                    "http://webSite.com", "emailemail.com",
                    "description1", "87632154H",
                    "billingAddress1","http://logo.com",
                    IllegalArgumentException.class
                }, {//Negative: without name
                	"admin",
                    "", "address1",
                    "http://webSite.com", "email@email.com",
                    "description1", "87632154H",
                    "billingAddress1","http://logo.com",
                    IllegalArgumentException.class
                }, {// Negative: wrong nif
                	"admin",
                    "name1", "address1",
                    "http://webSite.com", "email@email.com",
                    "description1", "afdadsf",
                    "billingAddress1","http://logo.com",
                    IllegalArgumentException.class
                },{// Negative: repit nif
                	"admin",
                    "name1", "address1",
                    "http://webSite.com", "email@email.com",
                    "description1", "28614080X",
                    "billingAddress1","http://logo.com",
                    DataIntegrityViolationException.class
                }, {//Negative: blank form
                	"",
                    "", "",
                    "", "",
                    "", "",
                    "","",
                    IllegalArgumentException.class
                }, {// Negative: Duplicate customer name
                	"admin",
                    "Ka Sevilla", "address1",
                    "http://webSite.com", "email@email.com",
                    "description1", "28614080X",
                    "billingAddress1","http://logo.com",
                    DataIntegrityViolationException.class
                }, {// Negative: wrong logo url
                	"admin",
                    "name1", "address1",
                    "http://webSite.com", "email@email.com",
                    "description1", "28614080X",
                    "billingAddress1","httplogo.com",
                    IllegalArgumentException.class
                }*/
        
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
            
            Labor labor;
            LaborForm laborForm = new LaborForm(laborService.create(incidenceService.findOne(super.getEntityId((String) testingDataMap.get("incidence")))));
           
            laborForm.setTitle((String) testingDataMap.get("name"));
            
            laborForm.setTime((Date) testingDataMap.get("time"));
            laborForm.setMoment((Date) testingDataMap.get("moment"));
            laborForm.setDescription((String) testingDataMap.get("description"));;
            
            DataBinder dataBinder = new DataBinder(laborForm);
            BindingResult binding = dataBinder.getBindingResult();
            labor = laborService.recontruct(laborForm, binding);
            Assert.isTrue(!binding.hasErrors());
            labor = this.laborService.save(labor);
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

}
