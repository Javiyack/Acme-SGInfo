package domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Customer extends DomainEntity {

	private String name;

	private String description;

	private Date fechaAlta;

	private String webSite;

	private String address;

	private String billingAddress;
	
	private String nif;

	private String passKey;

	private Boolean active;

	private String email;

	private String phone;

	private String logo;
	
	@URL
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Email
	@NotBlank
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}



	//@Pattern(regexp = "[0-9+()]{4,32}")
	//@NotBlank
	//@Size(min = 4,max = 32)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	@NotBlank
	@Size(min = 5, max = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	@NotBlank
	@Size(min = 5, max = 100)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@SafeHtml
	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^CUS-([012][1-9]|[3][01]|10|20)([0][1-9]|[1][0-2])\\d\\d-\\d{8}$") // "CUS-MMDDYY-XXXXXXXX"
	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

	public Boolean getActive() {
		return active;
	}
	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	/* 	DNI_REGEX = /^(\d{8})([A-Z])$/;
  		CIF_REGEX = /^([ABCDEFGHJKLMNPQRSUVW])(\d{7})([0-9A-J])$/;
  	 	NIE_REGEX = /^[XYZ]\d{7,8}[A-Z]$/;	 */
	
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "(^(\\d{8})([a-zA-Z])$)|(^([abcdefghjklmnprrsuvwABCDEFGHJKLMNPQRSUVW])(\\d{7})([0-9a-jA-J])$)|(^[xyzXYZ]\\d{7,8}[a-zA-Z]$)") // Identificaciop fiscal
	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	
}
