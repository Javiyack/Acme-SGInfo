package usecases;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

import domain.Incidence;
import forms.IncidenceForm;

import services.IncidenceService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class IncidenceUseCaseTest extends AbstractTest{
	
	@Autowired
	private IncidenceService incidenceService;

	private Map<String, Object> testingDataMap;

	/*
	 * Create,edit and delete incidence
	 */
	@Test
	public void createIncidenceTest() throws ParseException {

		Object userData[][] = (Object[][]) getEditionTestingData();
		for (int i = 0; i < userData.length; i++) {
			testingDataMap = new HashMap<String, Object>();
			testingDataMap.put("username", userData[i][0]);
			testingDataMap.put("name", userData[i][1]);
			testingDataMap.put("description", userData[i][2]);
			testingDataMap.put("editname", userData[i][3]);
			testingDataMap.put("editdescription", userData[i][4]);
			testingDataMap.put("expected", userData[i][5]);
			this.templateCreateIncidenceTest();
		}
	}
	
	List<String> folders = Arrays.asList("folder1", "folder2", "folder3", "folder4", "folder5");

	protected Object getEditionTestingData() {
		final Object testingData[][] = { 
				{
					// Positive
					"user1","name1",
					"description1",
					"name2",
					"description2",
					null 
				},{
					// Negative: wrong username
					"user13","name1",
					"description1",
					"name2",
					"description2",
					IllegalArgumentException.class 
				}, {
					// Negative: without edit title
					"user1","name1",
					"description1",
					"",
					"description2",
					IllegalArgumentException.class 					
				}, {
					// Negative: without create title
					"user1","",
					"description1",
					"name2",
					"description2",
					IllegalArgumentException.class 
				}

		};
		return testingData;
	}

	protected void templateCreateIncidenceTest() {
		Class <?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("username"));
            
            Incidence incidence;
            Incidence incidenceEdit;
            IncidenceForm incidenceForm = new IncidenceForm(incidenceService.create());
           
            incidenceForm.setTitle((String) testingDataMap.get("name"));
            incidenceForm.setDescription((String) testingDataMap.get("description"));
            
            DataBinder dataBinder = new DataBinder(incidenceForm);
            BindingResult binding = dataBinder.getBindingResult();
            incidence = incidenceService.recontruct(incidenceForm, binding);
            Assert.isTrue(!binding.hasErrors());
            incidence = this.incidenceService.save(incidence,incidenceForm);
            
            IncidenceForm incidenceEditForm = new IncidenceForm(incidence);
            incidenceEditForm.setTitle((String) testingDataMap.get("editname"));
            incidenceEditForm.setDescription((String) testingDataMap.get("editdescription"));
            
            DataBinder dataBinderEdit = new DataBinder(incidenceEditForm);
            BindingResult bindingEdit = dataBinderEdit.getBindingResult();
            incidenceEdit = incidenceService.recontruct(incidenceEditForm, bindingEdit);
            Assert.isTrue(!bindingEdit.hasErrors());
            incidenceEdit = this.incidenceService.save(incidenceEdit,incidenceEditForm);
            
            incidenceService.delete(incidenceEditForm);
            
            super.flushTransaction();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
	}

}
