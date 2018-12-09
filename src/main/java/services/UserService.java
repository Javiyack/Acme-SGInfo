package services;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import repositories.UserRepository;
import security.UserAccountService;

import java.util.Collection;

@Service
@Transactional
public class UserService {
	//Repositories
	@Autowired
	private UserRepository userRepository;
	//Services
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private ActorService actorService;
	
	//Constructor
	public UserService() {
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

		//Create
		public User create() {
			
			final User result = new User();
			

			return result;
		}
		
		//Save
		
//		//Other
//		public Manager reconstruct(final UserRegisterForm userForm) {
//			Manager res;
//			res = this.create();
//
//			res.setName(userForm.getName());
//			res.setPhone(userForm.getPhone());
//			res.setSurname(userForm.getSurname());
//			res.setEmail(userForm.getEmail());
//			res.setAddress(userForm.getAddress());
//			res.getUserAccount().setUsername(userForm.getUsername());
//			res.getUserAccount().setPassword(userForm.getPassword());
//			res.setAdult(userForm.isAdult());
//
//			return res;
//		}


		public User save(User user) {
			return userRepository.save(user);
			
		}

		public Collection<User> findAll() {
			return userRepository.findAll();
		}

		public Collection<User> findAllByCustomerId(int customerId) {
			Actor actor = actorService.findByPrincipal();
			Assert.notNull(actor);
			Assert.notNull(actor instanceof Technician || actor instanceof Manager);
			Collection<User> result = this.userRepository.findAllByCustomerId(customerId);
			return result;
		}

		public Collection<User> findAllByPrincipal() {
			Actor actor = actorService.findByPrincipal();
			Assert.notNull(actor);
			Assert.notNull(actor instanceof Responsible || actor instanceof User);
			Collection<User> result = this.userRepository.findAllByCustomerId(actor.getCustomer().getId());
			return result;
		}

		
		
	
	

}
