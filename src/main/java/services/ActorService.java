
package services;

import domain.*;
import forms.ActorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class ActorService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ActorRepository actorRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private Validator validator;

	@Autowired
	private FolderService folderService;
	@Autowired
	private ConfigurationService configurationService;

	// Constructors -----------------------------------------------------------
	public ActorService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result;

		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;
	}

	public Actor save(final Actor actor) {
		Assert.notNull(actor);
		Actor result;

		if (actor.getId() == 0){
			actor.getUserAccount().setActive(false);
			Assert.isTrue(actor.getCustomer().isActive(), "msg.not.active.customer.actor.save.block" );
		}

		if (actor.getId() != 0){
			Actor principal = this.findByPrincipal();
			Assert.isTrue(actor.equals(principal)
					|| (actor instanceof  Responsible && actor.getCustomer().equals(principal.getCustomer()))
					|| actor instanceof  Manager, "not.allowed.action");
		}
		result = this.actorRepository.save(actor);
		this.flush();
		if (actor.getId() == 0) {
			this.folderService.createSystemFolders(result);
		}

		return result;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));
		Assert.isTrue(actor.equals(this.findByPrincipal()));

		this.actorRepository.delete(actor);
	}

	// Other business methods -------------------------------------------------

	public UserAccount findUserAccount(final Actor actor) {
		Assert.notNull(actor);

		UserAccount result;

		result = actor.getUserAccount();

		return result;
	}

	public Actor findByPrincipal() {
		Actor result = null;
		UserAccount userAccount;

		try {
			userAccount = LoginService.getPrincipal();
			Assert.notNull(userAccount, "msg.not.loged.block");
			result = this.findByUserAccount(userAccount);
			Assert.notNull(result, "msg.not.loged.block");
		} catch (final Throwable oops) {
		}

		return result;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Actor result;

		result = this.actorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public String getType(final UserAccount userAccount) {

		final List<Authority> authorities = new ArrayList<Authority>(userAccount.getAuthorities());

		return authorities.get(0).getAuthority();
	}

	public Actor reconstruct(final ActorForm actorForm, final BindingResult binding) {
		Actor logedActor = null;

		UserAccount useraccount = null;
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		if (actorForm.getId() == 0) {
			actorForm.getAccount().setNewPassword(actorForm.getAccount().getPassword());
			useraccount = new UserAccount();
			boolean internalAllowed = true;
			boolean externalAllowed = true;
			switch (actorForm.getAccount().getAuthority()) {
			case Authority.USER:
				logedActor = new User();
				externalAllowed = externalAllowedCheck(actorForm.getPassKey());
				break;
			case Authority.RESPONSIBLE:
				logedActor = new Responsible();
				externalAllowed = externalAllowedCheck(actorForm.getPassKey());
				break;
			case Authority.MANAGER:
				logedActor = new Manager();
				internalAllowed = internalAllowedCheck(actorForm.getPassKey());
				break;
			case Authority.TECHNICIAN:
				logedActor = new Technician();
				internalAllowed = internalAllowedCheck(actorForm.getPassKey());
				break;

			default:
				break;
			}
			useraccount = this.userAccountService.create(actorForm.getAccount().getAuthority());
			logedActor.setUserAccount(useraccount);

			logedActor.getUserAccount().setUsername(actorForm.getAccount().getUsername());
			logedActor.getUserAccount().setPassword(actorForm.getAccount().getPassword());
			logedActor.setName(actorForm.getName());
			logedActor.setSurname(actorForm.getSurname());
			logedActor.setEmail(actorForm.getEmail());
			logedActor.setAddress(actorForm.getAddress());
			logedActor.setPhone(actorForm.getPhone());
			logedActor.setCustomer(actorForm.getCustomer());
			this.validator.validate(actorForm, binding);
			this.validator.validate(actorForm.getAccount(), binding);
			if (!binding.hasErrors()) {
				Assert.isTrue(actorForm.getAccount().getPassword().equals(actorForm.getAccount().getConfirmPassword()),

						"msg.userAccount.repeatPassword.mismatch");
				logedActor.getUserAccount()
						.setPassword(encoder.encodePassword(actorForm.getAccount().getPassword(), null));
				// Al registrarse, el usuario esta desactivado. El admin debe de activarlo.
				useraccount.setActive(false);
				if (actorForm.getCustomer() != null)
					Assert.isTrue(actorForm.getPassKey().equals(actorForm.getCustomer().getPassKey()),
							"msg.customer.passkey.mismatch");
				Assert.isTrue(internalAllowed, "msg.not.host.internal.actor.creation.block");
				Assert.isTrue(externalAllowed, "msg.not.guest.external.actor.creation.block");
				Assert.isTrue(actorForm.isAgree(), "msg.not.terms.agree.block");
			}
		} else {
			this.validator.validate(actorForm, binding);
			final String formPass = encoder.encodePassword(actorForm.getAccount().getPassword(), null);
			logedActor = this.findByPrincipal();
			Assert.notNull(logedActor, "msg.not.loged.block");
			if (!binding.hasErrors()) {
				logedActor.setName(actorForm.getName());
				logedActor.setSurname(actorForm.getSurname());
				logedActor.setEmail(actorForm.getEmail());
				logedActor.setAddress(actorForm.getAddress());
				logedActor.setPhone(actorForm.getPhone());
			} // Si ha cambiado algún parámetro del Authority (Usuario, password)
			if (!actorForm.getAccount().getUsername().equals(logedActor.getUserAccount().getUsername())) {

				if (!actorForm.getAccount().getNewPassword().isEmpty()) {
					// Valida el la cuenta de usuario
					this.validator.validate(actorForm.getAccount(), binding);
					Assert.isTrue(
							actorForm.getAccount().getNewPassword().equals(actorForm.getAccount().getConfirmPassword()),
							"msg.userAccount.repeatPassword.mismatch");
					// Cambia la contraseña
					// Comprueba la contraseña y la cambia si todo ha ido bien
					Assert.isTrue(formPass.equals(logedActor.getUserAccount().getPassword()), "msg.wrong.password");
					Assert.isTrue(checkLength(actorForm.getAccount().getNewPassword()), "msg.password.length");
					logedActor.getUserAccount()
							.setPassword(encoder.encodePassword(actorForm.getAccount().getNewPassword(), null));
				} else {
					actorForm.setNewPassword(null);
					actorForm.getAccount().setConfirmPassword(null);
					// Valida el la cuenta de usuario
					this.validator.validate(actorForm.getAccount(), binding);
					// Comprueba la contraseña
					Assert.isTrue(formPass.equals(logedActor.getUserAccount().getPassword()), "msg.wrong.password");

				}

				// Cambia el nombre de usuario
				logedActor.getUserAccount().setUsername(actorForm.getAccount().getUsername());

			} else {
				if (!actorForm.getAccount().getPassword().isEmpty()) {
					if (!actorForm.getAccount().getNewPassword().isEmpty()) {
						// Valida el la cuenta de usuario
						this.validator.validate(actorForm, binding);
						Assert.isTrue(
								actorForm.getAccount().getNewPassword()
										.equals(actorForm.getAccount().getConfirmPassword()),
								"msg.userAccount.repeatPassword.mismatch");
						// Comprueba la contraseña
						Assert.isTrue(formPass.equals(logedActor.getUserAccount().getPassword()), "msg.wrong.password");
						Assert.isTrue(checkLength(actorForm.getAccount().getNewPassword()), "msg.password.length");
						logedActor.getUserAccount()
								.setPassword(encoder.encodePassword(actorForm.getAccount().getNewPassword(), null));
					} else {
						actorForm.getAccount().setNewPassword("XXXXX");
						actorForm.getAccount().setConfirmPassword("XXXXX");
						// Valida el la cuenta de usuario
						this.validator.validate(actorForm, binding);
						// Comprueba la contraseña
						Assert.isTrue(formPass.equals(logedActor.getUserAccount().getPassword()), "msg.wrong.password");
					}

				} else {
					// Como no ha cambiado ni usuario ni escrito contraseña seteamos temporalmente
					// el username y passwords para pasar la validacion de userAccount
					// Valida El formulario
					actorForm.getAccount().setPassword("XXXXX");
					actorForm.getAccount().setNewPassword("XXXXX");
					actorForm.getAccount().setConfirmPassword("XXXXX");
					this.validator.validate(actorForm.getAccount(), binding);
				}
			}
		}
		return logedActor;
	}

	private boolean internalAllowedCheck(String passKey) {
		String hostPasskey = configurationService.findPassKey();
		return hostPasskey.equals(passKey);
	}

	private boolean externalAllowedCheck(String passKey) {
		return !internalAllowedCheck(passKey);
	}

	private boolean checkLength(String newPassword) {
		return newPassword.length() > 4 && newPassword.length() < 33;
	}

	public void flush() {
		this.actorRepository.flush();

	}

	public Collection<Actor> findWorkers() {
		Actor ppal = this.findByPrincipal();
		Assert.notNull(ppal);
		return actorRepository.findCoworkers(ppal.getCustomer().getId());
	}
	public Collection<Actor> findWorkers(Actor actor) {
		return actorRepository.findCoworkers(actor.getCustomer().getId());
	}
	public Collection<Actor> findWorkers(Customer customer) {
		return actorRepository.findCoworkers(customer.getId());
	}

	public Collection<Actor> findInternalStaff() {
		Collection<Actor> result;
		result = actorRepository.findAllTecnicians();
		result.addAll(actorRepository.findAllManagers());
		return result;
	}

	public void activateUserAccount(int actorId) {
		Actor ppal = this.findByPrincipal();
		Assert.notNull(ppal);
		Actor actor = actorRepository.findOne(actorId);
		Assert.isTrue(ppal instanceof Manager || ppal instanceof Responsible || ppal.equals(actor), "msg.not.owned.block");
		actor.getUserAccount().setActive(true);
		this.userAccountService.save(actor.getUserAccount());
	}

	public void deactivateUserAccountByActorId(int actorId) {
		Actor ppal = this.findByPrincipal();
		Assert.notNull(ppal);
		Actor actor = actorRepository.findOne(actorId);
		Assert.isTrue(ppal instanceof Manager || ppal instanceof Responsible, "msg.not.owned.block");
		actor.getUserAccount().setActive(false);
		this.userAccountService.save(actor.getUserAccount());
	}

	public void synchronizeActivationStatus(Actor member) {
		member.getUserAccount().setActive(member.getCustomer().isActive());
		this.userAccountService.save(member.getUserAccount());
	}

	public void setActivationStatus(Actor actor, boolean status) {
		Actor ppal = this.findByPrincipal();
		Assert.notNull(ppal);
		Assert.isTrue(ppal instanceof Manager || ppal instanceof Responsible, "msg.not.owned.block");
		actor.getUserAccount().setActive(status);
		this.userAccountService.save(actor.getUserAccount());
	}
	public void toggleActivation(Actor actor){
		Actor ppal = this.findByPrincipal();
		Assert.notNull(ppal);
		Assert.isTrue((ppal instanceof Responsible && actor.getCustomer().equals(ppal.getCustomer())), "msg.not.owned.block");
		actor.getUserAccount().setActive(!actor.getUserAccount().isEnabled());
		this.userAccountService.save(actor.getUserAccount());
	}

}
