
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class ConfigurationParameter extends DomainEntity {

	private Double		vat;


	private String[]	systemFolders;

	private String		welcomeMmessage;

	private String[]	spamWords;

	private String[]	tags;

	private String[]	categories;

	private String		bannerUrl;
	
	private String		companyName;

	private Integer		cacheTime;

	private Integer		numerOfResuts;


	@Min(0)
	@Digits(integer = 9, fraction = 2)
	public Double getVat() {
		return this.vat;
	}

	public void setVat(final Double vat) {
		this.vat = vat;
	}

	@NotBlank
	public String[] getSystemFolders() {
		return this.systemFolders;
	}

	public void setSystemFolders(final String[] systemFolders) {
		this.systemFolders = systemFolders;
	}
	@NotBlank
	public String getWelcomeMmessage() {
		return this.welcomeMmessage;
	}

	public void setWelcomeMmessage(final String welcomeMmessage) {
		this.welcomeMmessage = welcomeMmessage;
	}
	@NotBlank
	public String[] getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final String[] spamWords) {
		this.spamWords = spamWords;
	}
	@NotBlank
	public String[] getTags() {
		return this.tags;
	}

	public void setTags(final String[] tags) {
		this.tags = tags;
	}
	@NotBlank
	public String[] getCategories() {
		return this.categories;
	}

	public void setCategories(final String[] categories) {
		this.categories = categories;
	}
	@NotBlank
	@URL
	public String getBannerUrl() {
		return this.bannerUrl;
	}

	public void setBannerUrl(final String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
		
	@NotBlank
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Range(min = 1, max = 24)
	public Integer getCacheTime() {
		return this.cacheTime;
	}

	public void setCacheTime(final Integer cacheTime) {
		this.cacheTime = cacheTime;
	}
	@Range(min = 1, max = 100)
	public Integer getNumerOfResuts() {
		return this.numerOfResuts;
	}

	public void setNumerOfResuts(final Integer numerOfResuts) {
		this.numerOfResuts = numerOfResuts;
	}

}
