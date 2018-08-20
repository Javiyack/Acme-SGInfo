package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Technician;
import repositories.TechnicianRepository;
import security.UserAccountService;

@Service
@Transactional
public class TechnicianService {
	//Repositories
	@Autowired
	private TechnicianRepository technicianRepository;
	//Services
	@Autowired
	private UserAccountService userAccountService;
	@Autowired
	private ActorService actorService;
	
	//Constructor
	public TechnicianService() {
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

		//Create
		public Technician create() {
			
			final Technician result = new Technician();
			

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

		public Technician findLessOcuped() {
			Technician result = null;
			Collection<Object> results = this.technicianRepository.findeLessOcuped();
			if(!results.isEmpty()) {
				final Object[] datos = (Object[]) results.toArray()[0];
				result = (Technician)datos[0];
			}			
			return result;
		}

		public Technician save(Technician technician) {
			return technicianRepository.save(technician);
			
		}

		public Collection<Technician> findAll() {
			return technicianRepository.findAll();
		}

		
		
	
	

}
