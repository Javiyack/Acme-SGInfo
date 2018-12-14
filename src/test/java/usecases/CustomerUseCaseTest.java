package usecases;

import domain.Customer;
import forms.CustomerForm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import services.CustomerService;
import utilities.AbstractTest;

import java.text.ParseException;
import java.util.Date;
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
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateCustomerTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("name", userData[i][0]);
            testingDataMap.put("description", userData[i][1]);
            testingDataMap.put("address", userData[i][2]);
            testingDataMap.put("billingAddress", userData[i][3]);
            testingDataMap.put("nif", userData[i][4]);
            testingDataMap.put("fechaAlta", userData[i][5]);
            testingDataMap.put("email", userData[i][6]);
            testingDataMap.put("logo", userData[i][7]);
            testingDataMap.put("passKey", userData[i][8]);
            testingDataMap.put("webSite", userData[i][9]);
            testingDataMap.put("expected", userData[i][10]);
            this.templateCreateCustomerTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                   "name1", "description1",
                   "address1", "billingAddress1", "12345678H",
                   new Date(), "email@email.com", "http://logo.com",
                   "CUS-020100-00000001","http://webSite.com",
                   IllegalArgumentException.class
                }, {//Negative: wrong url
                	"name1", "description1",
                    "address1", "billingAddress1", "12345678H",
                    new Date(), "email@email.com", "http://logo.com",
                    "CUS-020100-00000001","hp//webSite.com",
                    IllegalArgumentException.class
                }, {// Negative: wrong email
                	"name1", "description1",
                    "address1", "billingAddress1", "12345678H",
                    new Date(), "emailemail.com", "http://logo.com",
                    "CUS-020100-00000001","hp//webSite.com",
                    IllegalArgumentException.class
                }, {//Negative: without name
                	"", "description1",
                    "address1", "billingAddress1", "12345678H",
                    new Date(), "emailemail.com", "http://logo.com",
                    "CUS-020100-00000001","hp//webSite.com",
                    IllegalArgumentException.class
                }, {// Negative: wrong nif
                	"name1", "description1",
                    "address1", "billingAddress1", "12345678",
                    new Date(), "emailemail.com", "http://logo.com",
                    "CUS-020100-00000001","hp//webSite.com",
                    IllegalArgumentException.class
                },{// Negative: no date
                	"name1", "description1",
                    "address1", "billingAddress1", "12345678H",
                    null, "emailemail.com", "http://logo.com",
                    "CUS-020100-00000001","hp//webSite.com",
                    IllegalArgumentException.class
                }, {//Negative: blank form
                	"", "",
                    "", "", "",
                    "", "", "",
                    "","",
                    IllegalArgumentException.class
                }, {// Negative: Duplicate customer name
                	"Ka Sevilla", "description1",
                    "address1", "billingAddress1", "12345678H",
                    null, "emailemail.com", "http://logo.com",
                    "CUS-020100-00000001","hp//webSite.com",
                    IllegalArgumentException.class
                }, {// Negative: wrong Pass Key
                	"Ka Sevilla", "description1",
                    "address1", "billingAddress1", "12345678H",
                    null, "emailemail.com", "http://logo.com",
                    "CUS-020100-0000000","hp//webSite.com",
                    IllegalArgumentException.class
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
            Customer customer;
            CustomerForm customerForm = new CustomerForm(customerService.create());
           
            customerForm.setName((String) testingDataMap.get("name"));
            customerForm.setDescription((String) testingDataMap.get("description"));
            customerForm.setAddress((String) testingDataMap.get("address"));
            customerForm.setBillingAddress((String) testingDataMap.get("billingAddress"));
            customerForm.setNif((String) testingDataMap.get("nif"));
            customerForm.setFechaAlta((Date) testingDataMap.get("fechaAlta"));
            customerForm.setLogo((String) testingDataMap.get("logo"));
            customerForm.setPassKey((String) testingDataMap.get("passKey"));
            customerForm.setWebSite((String) testingDataMap.get("webSite"));
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
