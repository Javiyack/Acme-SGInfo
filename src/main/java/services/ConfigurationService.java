
package services;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
	private AdministratorService	administratorService;


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
		final Collection<Configuration> configurations = this.configurationRepository.findAll();
		Assert.notNull(configurations);
		return configurations;
	}

	public Configuration save(final Configuration configuration) {
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);
		Configuration saved;

		saved = this.configurationRepository.save(configuration);

		Assert.notNull(saved, "Configuration haven`t been saved.");

		return saved;
	}

	//Other methods -----------------------------------------------------

	public String findWelcomeMessage(final Locale locale) {
		String result = null;

		switch (locale.getLanguage()) {
		case "es":
			result = this.configurationRepository.findWelcomeMessageEs();
			break;
		case "en":
			result = this.configurationRepository.findWelcomeMessageEn();
			break;
		}
		return result;
	}

	public String findBanner() {
		return this.configurationRepository.findBanner();
	}

	public String findCompanyName() {
		return this.configurationRepository.findCompanyName();
	}

	public Double findBenefitsPercentage() {
		return this.configurationRepository.findBenefitsPercentage();
	}

	public void flush() {
		this.configurationRepository.flush();
	}
}
