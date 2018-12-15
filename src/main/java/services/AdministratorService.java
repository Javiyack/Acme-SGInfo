
package services;

import domain.Administrator;
import domain.TabooWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class AdministratorService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private AdministratorRepository	administratorRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService			actorService;
	@Autowired
	private TabooWordService		tabooWordService;


	// Constructor ----------------------------------------------------------
	public AdministratorService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------

	public Administrator findOne(final int administratorId) {
		Administrator result;

		result = this.administratorRepository.findOne(administratorId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Administrator> findAll() {

		Collection<Administrator> result;

		result = this.administratorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Administrator findByPrincipal() {
		Administrator result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = (Administrator) this.actorService.findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Administrator create() {
		return new Administrator();

	}

	public void flush() {
		this.administratorRepository.flush();

	}

	public boolean checkIsSpam(final String subject, final String body) {

		Boolean isSpam;

		if (subject.isEmpty() && body.isEmpty())
			isSpam = false;
		else {

			Collection<TabooWord> tabooWords;
			tabooWords = this.tabooWordService.getTabooWordFromMessageSubjectAndBody(subject, body);

			if (tabooWords.isEmpty())
				isSpam = false;
			else
				isSpam = true;

		}

		return isSpam;
	}
	
	public List<Object[]> usersWithMoreIncidences(){
		return administratorRepository.usersWithMoreIncidences();
	}
	
	public List<Object[]> techniciansWithLessIncidences(){
		return administratorRepository.techniciansWithLessIncidences();		
	}
	
	public List<Object[]> percMessagesSenderByActor(){
		return administratorRepository.percMessagesSenderByActor();
	}
	
	public Double avgRequestByResponsible(){
		return administratorRepository.avgRequestByResponsible();
	}
	
	public Double minRequestByResponsible(){
		return administratorRepository.minRequestByResponsible();
	}
	public Double maxRequestByResponsible(){
		return administratorRepository.maxRequestByResponsible();
	}
	public Double stddevRequestByResponsible(){
		return administratorRepository.stddevRequestByResponsible();
	}
	
	public List<Object[]> bestRatedIncidences(){
		return administratorRepository.bestRatedIncidences();
	}
	public List<Object[]> worstRatedTechnicianOfIncidences(){
		return administratorRepository.worstRatedTechnicianOfIncidences();
	}

}
