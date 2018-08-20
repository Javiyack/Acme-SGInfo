package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.Actor;
import domain.Servant;
import domain.User;
import forms.ServantEditForm;
import repositories.ServantRepository;
import security.UserAccountService;

@Service
@Transactional
public class ServantService  {
	//Repositories
	@Autowired
	private ServantRepository servantRepository;
	//Services
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private ActorService actorService;
	@Autowired
	private UserService userService;
	@Autowired
	private TechnicianService technicianService;
	@Autowired
	private Validator validator;
	
	//Constructor
	public ServantService() {
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

		//Create
		public Servant create() {
			
			final Servant result = new Servant();
			
			Actor actor = actorService.findByPrincipal();
			// Hack
			Assert.notNull(actor);
			Assert.isTrue(actor instanceof User);
			return result;
		}
		
		//Save
		public Servant save(final Servant servant) {
			Assert.notNull(servant);
			Servant result;

			if (servant.getId() == 0) {
				
				
			}
			
			result = this.servantRepository.save(servant);

			return result;
		}
		
		

		public Collection<Servant> findAll() {
			return servantRepository.findAll();
		}

		public Servant recontruct (ServantEditForm form, BindingResult binding) {
			Servant result = this.create();
			validator.validate(result, binding);
			return result;
			
		}

		public Servant findOne(int id) {
			return servantRepository.findOne(id);
		}
		
	
	

}
