
package forms;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import domain.Actor;
import domain.Customer;

public class ActorForm {

	private String	name;
	private String	surname;
	private String	email;
	private String	phone;							// Optional	
	private String	address;				// Optional
	private String	username;
	private String	password;
	private String	newPassword;
	private String	confirmPassword;
	private String	authority;
	private Customer customer;
	private String passKey;
	
	private int		id;
	private int		version;


	//Constructors -------------------------

	public ActorForm() {
		super();
		this.id = 0;
		this.version = 0;
	}

	public ActorForm(final Actor actor) {
		super();
		this.id = actor.getId();
		this.setName(actor.getName());
		this.setSurname(actor.getSurname());
		this.setEmail(actor.getEmail());
		this.setPhone(actor.getPhone());
		this.setAddress(actor.getAddress());
		this.setUsername(actor.getUserAccount().getUsername());
		this.setCustomer(actor.getCustomer());
		this.setAuthority(actor.getUserAccount().getAuthorities().iterator().next().getAuthority());
	}

	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Pattern(regexp = "^CUS-([012][1-9]|[3][01]|20)([0][1-9]|[1][0-2])\\d\\d-\\d{0,8}$") // "INC-MMDDYY-XXXXXXXX"
	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(final String authority) {
		this.authority = authority;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@Email
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Pattern(regexp = "()|^[6,7]\\d{8}$")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	@Size(min = 5, max = 32)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String userName) {
		this.username = userName;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String pasword) {
		this.password = pasword;
	}

	/**
	 * @return the newPassword
	 */
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Size(min = 5, max = 32)
	public String getNewPassword() {
		return this.newPassword;
	}

	/**
	 * @param newPassword
	 *            the newPassword to set
	 */
	public void setNewPassword(final String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the confirmPassword
	 */
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Size(min = 5, max = 32)
	public String getConfirmPassword() {
		return this.confirmPassword;
	}

	/**
	 * @param confirmPassword
	 *            the confirmPassword to set
	 */
	public void setConfirmPassword(final String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	
}
