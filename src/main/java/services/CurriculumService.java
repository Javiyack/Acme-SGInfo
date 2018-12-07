
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Curriculum;
import repositories.CurriculumRepository;

@Service
@Transactional
public class CurriculumService {

	/*
	 * TODO
	 * restricción1 --> LISTAR: puede listar currículums a partir del id de una subsección cualquier comercial o administrativo que haya creado una subsección en la oferta a la que pertenece
	 * la subsección que me pasan y el comercial que haya creado la oferta a la que pertenece la subsección que me pasan.
	 * 
	 * restricción2 --> MOSTRAR: puede mostrar un currículum cualquier comercial o administrativo que haya creado una subsección en la oferta a la que pertenece
	 * la subsección a la que pertenece el currículum y el comercial que haya creado la oferta a la que pertenece la subsección a la que pertenece el currículum.
	 * 
	 * restricción3 --> CREAR: puede crear un currículum el comercial que haya creado la subsección a la que pertenece el currículum.
	 * 
	 * restricción4 --> EDITAR: puede editar un currículum el comercial que lo haya creado.
	 * 
	 * restricción5 --> BORRAR: puede borrar un currículum el comercial que haya creado la subsección a la que pertenece el currículum.
	 */

	// Managed repository ------------------------------------------------
	@Autowired
	private CurriculumRepository	curriculumRepository;

	//Supporting services
	@Autowired
	private ActorService			actorService;



	//CRUD methods

	public Curriculum create() {

		Curriculum result;

		result = new Curriculum();

		return result;

	}

	public Curriculum findOne(final int curriculumId) {
		Curriculum result;

		result = this.curriculumRepository.findOne(curriculumId);
		Assert.notNull(result);

		return result;
	}

	public Curriculum findOneToEdit(final int curriculumId) {
		Curriculum result;

		result = this.curriculumRepository.findOne(curriculumId);
		Assert.notNull(result);
		this.checkPrincipal(result);

		return result;
	}

	public Collection<Curriculum> findAll() {

		Collection<Curriculum> result;

		result = this.curriculumRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Curriculum save(final Curriculum curriculum) {

		Assert.notNull(curriculum);
	//	Assert.isTrue(!curriculum.getSubSection().getSection().equals(Constant.SECTION_ADMINISTRATIVE_ACREDITATION));

		if (curriculum.getId() != 0)
			this.checkPrincipal(curriculum);

		final Curriculum saved = this.curriculumRepository.save(curriculum);

		return saved;
	}

	public void delete(final Curriculum curriculum) {
		Assert.notNull(curriculum);
		this.checkPrincipal(curriculum);
		this.curriculumRepository.delete(curriculum);
	}

	

	public void flush() {
		this.curriculumRepository.flush();

	}

	//Other methods


	

	public Collection<Curriculum> getCurriculumsFromTechnicianId(final int technicianId) {
		return this.curriculumRepository.getCurriculumsFromTechnicianId(technicianId);
	}

	public void checkListAndDisplay(final int subSectionId) {

	
		/*
		 * Actor principal;
		 * principal = this.actorService.findByPrincipal();
		 * Assert.isTrue(principal instanceof Commercial || principal instanceof Administrative);
		 * 
		 * SubSection subSection;
		 * subSection = this.subSectionService.findOne(subSectionId);
		 * Assert.isTrue(!subSection.getSection().equals(Constant.SECTION_ADMINISTRATIVE_ACREDITATION));
		 * 
		 * Collection<Actor> subSectionCreators = new ArrayList<Actor>();
		 * Collection<Commercial> subSectionCommercials = this.commercialService.getSubSectionCommercialsFromOfferId(subSection.getOffer().getId());
		 * Collection<Commercial> subSectionAdministratives = this.administrativeService.getSubSectionAdministrativesFromOfferId(subSection.getOffer().getId());
		 * subSectionCreators.addAll(subSectionCommercials);
		 * subSectionCreators.addAll(subSectionAdministratives);
		 * Assert.isTrue(subSection.getOffer().getCommercial().equals(principal) || subSectionCreators.contains(principal));
		 */
	}

	public void checkPrincipal(final Curriculum curriculum) {

		
		/*
		 * final Actor principal = this.actorService.findByPrincipal();
		 * Assert.isTrue(principal instanceof Commercial);
		 * Assert.isTrue(curriculumRepository.getCurriculumsFromCommercialId(principal.getId()).contains(curriculum));
		 */
	}

	public boolean checkLegalAge(final Date dateOfBirth) {

		boolean result = false;

		final Date d = new Date();
		final Calendar currentDate = new GregorianCalendar();
		currentDate.setTime(d);

		final Calendar birthDate = new GregorianCalendar();
		birthDate.setTime(dateOfBirth);

		final int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
		final int currentMonth = currentDate.get(Calendar.MONTH) + 1;
		final int currentYear = currentDate.get(Calendar.YEAR);

		final int birthDay = birthDate.get(Calendar.DAY_OF_MONTH);
		final int birthMonth = birthDate.get(Calendar.MONTH) + 1;
		final int birthYear = birthDate.get(Calendar.YEAR);

		if (currentYear - birthYear > 18)
			result = true;
		else if (currentYear - birthYear == 18)
			if (currentMonth > birthMonth)
				result = true;
			else if (currentMonth == birthMonth)
				if (currentDay >= birthDay)
					result = true;

		return result;
	}

}
