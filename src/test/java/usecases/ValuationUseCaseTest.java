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

import domain.Valuation;
import forms.ValuationForm;

import services.IncidenceService;
import services.ValuationService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/junit.xml" })
@Transactional
public class ValuationUseCaseTest extends AbstractTest{
	
	@Autowired
    private ValuationService valuationService;
	
	@Autowired
	private IncidenceService incidenceService;

    private Map <String, Object> testingDataMap;

    /* 
     * Create valuation.
     */
    @Test
    public void createValuationTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateCustomerTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("incidence", userData[i][1]);
            testingDataMap.put("value", userData[i][2]);
            testingDataMap.put("comments", userData[i][3]);
            testingDataMap.put("expected", userData[i][4]);
            this.templateCreateValuationTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                   "user2",
                   "incidence6", 4,
                   "buen",
                   null
                }, {// Negative: wrong incidence
                	"user2",
                    "incidence5", 4,
                    "buen",
                    NullPointerException.class
                }, {//Negative: wrong user
                	"user1",
                    "incidence6", 4,
                    "buen",
                    IllegalArgumentException.class
                }, {// Negative: wrong value
                	"user2",
                    "incidence6", 6,
                    "buen",
                    IllegalArgumentException.class
                }
        
        };
        return testingData;
    }

    protected void templateCreateValuationTest() {
        Class <?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            
            Valuation valuation;
            ValuationForm valuationForm = new ValuationForm(valuationService.create(incidenceService.findOne(super.getEntityId((String) testingDataMap.get("incidence")))));
           
            valuationForm.setValue((Integer) testingDataMap.get("value"));
            valuationForm.setComments((String) testingDataMap.get("comments"));
            
            DataBinder dataBinder = new DataBinder(valuationForm);
            BindingResult binding = dataBinder.getBindingResult();
            valuation = valuationService.recontruct(valuationForm, binding);
            Assert.isTrue(!binding.hasErrors());
            valuation = this.valuationService.save(valuation);
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

}
