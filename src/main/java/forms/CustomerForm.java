
package forms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Customer;

public class CustomerForm {


	private int id ;
	private int version;
	private String name;

	private String description;

	private Date fechaAlta;

	private String webSite;

	private String address;

	private String billingAddress;

	private String passKey;
	
	private String nif;
	
	private Boolean active;	
	
	private String email;
	
	private String logo;
	
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}
	
	public CustomerForm() {
		super();
		this.id = 0;
		this.version = 0;
		}

	public CustomerForm(Customer cust) {
		super();
		this.id = cust.getId();
		this.version = cust.getVersion();
		this.setName(cust.getName());
		this.setDescription(cust.getDescription());
		this.setAddress(cust.getAddress());
		this.setBillingAddress(cust.getBillingAddress());
		this.setNif(cust.getNif());
		this.setFechaAlta(cust.getFechaAlta());
		this.setActive(cust.isActive());
		this.setEmail(cust.getEmail());
		this.setPassKey(cust.getPassKey());
		this.setLogo(cust.getLogo());
		this.setWebSite(cust.getWebSite());
		}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@NotBlank
	@Pattern(regexp = "(^(\\d{8})([A-Z])$)|(^([ABCDEFGHJKLMNPQRSUVW])(\\d{7})([0-9A-J])$)|(^[XYZ]\\d{7,8}[A-Z]$)") // Identificaciop fiscal
	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

}
