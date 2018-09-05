
package services;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Administrator;
import domain.Configuration;
import repositories.ConfigurationRepository;

@Service
@Transactional
public class ConfigurationService {

	// Managed repositories-----------------------------------------------------
	@Autowired
	private ConfigurationRepository	configurationRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------
	public ConfigurationService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Configuration findOne(final int configurationId) {
		Configuration configuration;
		configuration = this.configurationRepository.findOne(configurationId);
		Assert.notNull(configuration);
		return configuration;
	}

	public Collection<Configuration> findAll() {
		Collection<Configuration> configurations;
		configurations = this.configurationRepository.findAll();
		Assert.notNull(configurations);
		return configurations;
	}

	public Configuration save(final Configuration configuration) {
		final Actor actor = this.actorService.findByPrincipal();
		Assert.notNull(actor, "msg.not.loged.block");
		Assert.notNull(actor instanceof Administrator, "msg.not.owned.block");
		Configuration saved;

		saved = this.configurationRepository.save(configuration);

		Assert.notNull(saved, "Configuration haven`t been saved.");

		return saved;
	}

	//Other methods -----------------------------------------------------

	public String findWelcomeMessage(final String locale) {
		String result = null;

		switch (locale) {
		case "es":
			result = this.configurationRepository.findWelcomeMessageEs();
			break;
		case "en":
			result = this.configurationRepository.findWelcomeMessageEn();
			break;
		}

		return result;
	}

	public String findLogo() {
		String result;

		result = this.configurationRepository.findLogo();

		return result;
	}

	public String findName() {
		String result;

		result = this.configurationRepository.findName();

		return result;
	}

	public void flush() {
		this.configurationRepository.flush();

	}

	public Double findHourPrice() {
		Double result;

		result = this.configurationRepository.findHourPrice();

		return result;
	}

	public Double findIVA() {
		Double result;

		result = this.configurationRepository.findIVA();

		return result;
	}

	public String findPassKey() {
		String result;

		result = this.configurationRepository.findPassKey();

		return result;
	}

	public List<String> findFolderNames() {
		Configuration cfg = (Configuration) configurationRepository.findCfg();
		System.out.println();
		return (List<String>) cfg.getFolderNames();
	}
}
