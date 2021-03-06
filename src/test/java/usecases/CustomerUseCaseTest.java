package usecases;

import domain.Customer;
import forms.CustomerForm;

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
import services.CustomerService;
import utilities.AbstractTest;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class CustomerUseCaseTest extends AbstractTest {

    @Autowired
    private CustomerService customerService;

    private Map <String, Object> testingDataMap;

    /* 
     * Create customer.
     */
    @Test
    public void createCustomerTest() throws ParseException {
        /*
         * Aqu� cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateCustomerTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("name", userData[i][1]);
            testingDataMap.put("address", userData[i][2]);
            testingDataMap.put("webSite", userData[i][3]);
            testingDataMap.put("email", userData[i][4]);
            testingDataMap.put("description", userData[i][5]);
            testingDataMap.put("nif", userData[i][6]);
            testingDataMap.put("billingAddress", userData[i][7]);
            testingDataMap.put("logo", userData[i][8]);
            testingDataMap.put("expected", userData[i][9]);
            this.templateCreateCustomerTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                	"admin",
                   "name1", "address1",
                   "http://webSite.com", "email@email.com",
                   "description1", "87632154H",
                   "billingAddress1","http://logo.com",
                   null
                }, {// Negative: wrong email
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
                }
        
        };
        return testingData;
    }

    protected void templateCreateCustomerTest() {
        Class <?> caught;
        /*
            Simulamos la edici�n  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            
            Customer customer;
            CustomerForm customerForm = new CustomerForm(customerService.create());
           
            customerForm.setName((String) testingDataMap.get("name"));
            customerForm.setAddress((String) testingDataMap.get("address"));
            customerForm.setWebSite((String) testingDataMap.get("webSite"));
            customerForm.setEmail((String) testingDataMap.get("email"));
            customerForm.setDescription((String) testingDataMap.get("description"));
            customerForm.setNif((String) testingDataMap.get("nif"));
            customerForm.setBillingAddress((String) testingDataMap.get("billingAddress"));            
            customerForm.setLogo((String) testingDataMap.get("logo"));
            
            DataBinder dataBinder = new DataBinder(customerForm);
            BindingResult binding = dataBinder.getBindingResult();
            customer = customerService.recontruct(customerForm, binding);
            Assert.isTrue(!binding.hasErrors());
            customer = this.customerService.save(customer);
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }



}
