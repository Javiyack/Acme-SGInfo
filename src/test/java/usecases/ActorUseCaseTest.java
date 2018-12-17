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
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import domain.Actor;
import forms.ActorForm;

import services.ActorService;
import utilities.AbstractTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/junit.xml"
	})
@Transactional
public class ActorUseCaseTest extends AbstractTest {
	
    @Autowired
    private ActorService actorService;

    private Map <String, Object> testingDataMap;

    /* 
     * Edit actor
     */
    @Test
    public void editActorTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateCustomerTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("name", userData[i][1]);
            testingDataMap.put("surname", userData[i][2]);
            testingDataMap.put("email", userData[i][3]);
            testingDataMap.put("phone", userData[i][4]);
            testingDataMap.put("address", userData[i][5]);
            testingDataMap.put("editusername", userData[i][6]);
            testingDataMap.put("password", userData[i][7]);
            testingDataMap.put("newpassword", userData[i][8]);
            testingDataMap.put("confirmpassword", userData[i][9]);
            testingDataMap.put("expected", userData[i][10]);
            this.templateEditActorTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                	"admin",
                   "name1", "surname1",
                   "email@email.com","634543123",
                   "address1", "admin1",
                   "admin","password","password",
                   null
                }, {//Negative: without name
                	"admin",
                    "", "surname1",
                    "email@email.com","634543123",
                    "address1", "admin1",
                    "admin","password","password",
                    IllegalArgumentException.class
                }, {// Negative: without surname
                	"admin",
                    "name1", "",
                    "email@email.com","634543123",
                    "address1", "admin1",
                    "admin","password","password",
                    IllegalArgumentException.class
                },{// Negative: wrong email
                	"admin",
                    "name1", "surname1",
                    "emailemail.com","634543123",
                    "address1", "admin1",
                    "admin","password","password",
                    IllegalArgumentException.class
                }, {//Negative: without email
                	"admin",
                    "name1", "surname1",
                    "","634543123",
                    "address1", "admin1",
                    "admin","password","password",
                    IllegalArgumentException.class
                }, {// Negative: wrong phone
                	"admin",
                    "name1", "surname1",
                    "email@email.com","6345431232",
                    "address1", "admin1",
                    "admin","password","password",
                    IllegalArgumentException.class
                }, {// Negative: without phone
                	"admin",
                    "name1", "surname1",
                    "email@email.com","",
                    "address1", "admin1",
                    "admin","password","password",
                    IllegalArgumentException.class
                }, {// Negative: without address
                	"admin",
                    "name1", "surname1",
                    "email@email.com","",
                    "", "admin1",
                    "admin","password","password",
                    IllegalArgumentException.class
                }, {// Negative: without username
                	"admin",
                    "name1", "surname1",
                    "email@email.com","",
                    "address1", "",
                    "admin","password","password",
                    IllegalArgumentException.class
                }, {// Negative: without current password
                	"admin",
                    "name1", "surname1",
                    "email@email.com","",
                    "address1", "admin1",
                    "","password","password",
                    IllegalArgumentException.class
                }, {// Negative: differents new passwords
                	"admin",
                    "name1", "surname1",
                    "email@email.com","",
                    "address1", "admin1",
                    "admin","password","password1",
                    IllegalArgumentException.class
                }, {// Negative: without new password
                	"admin",
                    "name1", "surname1",
                    "email@email.com","",
                    "address1", "admin1",
                    "admin","","password",
                    IllegalArgumentException.class
                }, {// Negative: without confirm password
                	"admin",
                    "name1", "surname1",
                    "email@email.com","",
                    "address1", "admin1",
                    "admin","password","",
                    IllegalArgumentException.class
                }
        
        };
        return testingData;
    }

    protected void templateEditActorTest() {
        Class <?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            
            Actor actor = actorService.findByPrincipal();
            ActorForm actorForm = new ActorForm(actor);
           
            actorForm.setName((String) testingDataMap.get("name"));
            actorForm.setSurname((String) testingDataMap.get("surname"));
            actorForm.setEmail((String) testingDataMap.get("email"));
            actorForm.setPhone((String) testingDataMap.get("phone"));
            actorForm.setAddress((String) testingDataMap.get("address"));
            actorForm.setUsername((String) testingDataMap.get("editusername"));
            actorForm.setPassword((String) testingDataMap.get("password"));
            actorForm.setNewPassword((String) testingDataMap.get("newpassword"));
            actorForm.setConfirmPassword((String) testingDataMap.get("confirmpassword"));
            
            DataBinder dataBinder = new DataBinder(actorForm);
            BindingResult binding = dataBinder.getBindingResult();
            actor = actorService.reconstruct(actorForm, binding);
            Assert.isTrue(!binding.hasErrors());
            actor = this.actorService.save(actor);
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }


	
}