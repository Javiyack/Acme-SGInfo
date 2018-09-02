
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import domain.DomainEntity;

public class ConfigurationForm {

	private String	companyName;
	private String	passKey;
	private String	logo;
	private String	welcomeMessageEs;
	private String	welcomeMessageEn;
	private double	benefitsPercentage;

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getWelcomeMessageEs() {
		return this.welcomeMessageEs;
	}

	public void setWelcomeMessageEs(final String welcomeMessageEs) {
		this.welcomeMessageEs = welcomeMessageEs;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getWelcomeMessageEn() {
		return this.welcomeMessageEn;
	}

	public void setWelcomeMessageEn(final String wecolmeMessageEn) {
		this.welcomeMessageEn = wecolmeMessageEn;
	}

	@Min(0)
	@Max(100)
	@NotNull
	public double getBenefitsPercentage() {
		return this.benefitsPercentage;
	}

	public void setBenefitsPercentage(final double benefitsPercentage) {
		this.benefitsPercentage = benefitsPercentage;
	}


	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

}
