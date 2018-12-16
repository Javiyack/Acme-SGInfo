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

import services.FolderService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class FolderUseCaseTest extends AbstractTest {
	
	@Autowired
	private FolderService folderService;

    private Map <String, Object> testingDataMap;

    /* 
     * Create edit and delete folder.
     */
    @Test
    public void createFolderTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateMessageTest'
         */

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("username", userData[i][0]);
            testingDataMap.put("name", userData[i][1]);
            testingDataMap.put("editname", userData[i][2]);
            testingDataMap.put("expected", userData[i][3]);
            this.templatecreateFolderTest();
        }
    }

    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                   "user1", "folder1","folder",
                   null
                }, {//Negative: wrong actor
                	"user", "folder1","folder",
                    IllegalArgumentException.class
                }, {// Negative: without name   to edit              	
                	"user1", "folder","",
                    ConstraintViolationException.class
                },{//Negative: blank form
                	"", "", "",
                    IllegalArgumentException.class
                }
        
        };
        return testingData;
    }

    protected void templatecreateFolderTest() {
        Class <?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            Folder savedFolder = new Folder();
            Folder savedEditFolder = new Folder();
            Folder folder = folderService.create();
            folder.setName((String) testingDataMap.get("name"));            
            
            savedFolder = folderService.save(folder);
            
            savedFolder.setName((String) testingDataMap.get("editname"));
            
            savedEditFolder = folderService.save(savedFolder);
            
            folderService.delete(savedEditFolder);
      
            
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

}
