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

import domain.Folder;
import domain.Message;
import domain.PostBox;

import services.ActorService;
import services.FolderService;
import services.MessageService;
import services.PostBoxService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class MessageUseCaseTest extends AbstractTest{
	
	@Autowired
    private MessageService messageService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FolderService folderService;
	
	@Autowired
	private PostBoxService postBoxService;

    private Map <String, Object> testingDataMap;

    /* 
     * Create message and move folder.
     */
    @Test
    public void createMessageTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateMessageTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("subject", userData[i][1]);
            testingDataMap.put("body", userData[i][2]);
            testingDataMap.put("priority", userData[i][3]);
            testingDataMap.put("recipient", userData[i][4]);
            testingDataMap.put("folder", userData[i][5]);
            testingDataMap.put("expected", userData[i][6]);
            this.templateCreateMessageTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                   "user1", "subject1",
                   "body1", "NEUTRAL", "technician1",
                   "custombox1",
                   null
                }, {//Negative: wrong sender actor
                	"user", "subject1",
                    "body1", "NEUTRAL", "user2",
                    "notificationbox1",
                    IllegalArgumentException.class
                }, {// Negative: wrong recipient actor
                	"user1", "subject1",
                    "body1", "NEUTRAL", "user",
                    "notificationbox1",
                    AssertionError.class
                }, {//Negative: without subject
                	"user1", "",
                    "body1", "NEUTRAL", "user2",
                    "notificationbox1",
                    ConstraintViolationException.class
                }, {// Negative: without body
                	"user1", "subject1",
                    "", "NEUTRAL", "user2",
                    "notificationbox1",
                    ConstraintViolationException.class
                }, {// Negative: wrong priority
                	"user1", "subject1",
                    "body1", "wrong", "user2",
                    "notificationbox1",
                    ConstraintViolationException.class
                }, {// Negative: without priority
                	"user1", "subject1",
                    "body1", "", "user2",
                    "notificationbox1",
                    ConstraintViolationException.class
                }, {//Negative: blank form
                	"", "",
                    "", "", "","",
                    IllegalArgumentException.class
                },{//Negative: wrong folder
                	"user1", "subject1",
                    "body1", "NEUTRAL", "technician1",
                    "notificationbox",
                    AssertionError.class
                }
        
        };
        return testingData;
    }

    protected void templateCreateMessageTest() {
        Class <?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            Message savedMessage = new Message();
            Message message = messageService.create();
            message.setSubject((String) testingDataMap.get("subject"));
            message.setBody((String) testingDataMap.get("body"));
            message.setPriority((String) testingDataMap.get("priority"));
            message.setRecipient(actorService.findOne(super.getEntityId((String)testingDataMap.get("recipient"))));
            
            
            savedMessage = messageService.save(message);
            messageService.saveOnSender(savedMessage);
            messageService.saveOnRecipient(savedMessage);
            
            messageService.saveToMove(savedMessage, folderService.findOne(super.getEntityId((String)testingDataMap.get("folder"))));
      
            
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

}
