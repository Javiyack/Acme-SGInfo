
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	private String	companyName;
	private String	passKey;
	private String	logo;
	private String	defaultCurrency;
	private Collection<String> folderNames;
	private String	welcomeMessageEs;
	private String	welcomeMessageEn;
	private double	iva;
	private double	hourPrice;



	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String currency) {
		this.defaultCurrency = currency;
	}

	@ElementCollection
	@NotNull
	public Collection<String> getFolderNames() {
		return folderNames;
	}

	public void setFolderNames(Collection<String> folderNames) {
		this.folderNames = folderNames;
	}

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



	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Min(0)
	@Max(100)
	@NotNull
	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	@Min(0)
	@Max(1000)
	@NotNull
	public double getHourPrice() {
		return hourPrice;
	}

	public void setHourPrice(double hourPrice) {
		this.hourPrice = hourPrice;
	}

}
