
package forms;

import domain.Actor;
import domain.Customer;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ActorForm {

	private String name;
	private String surname;
	private String email;
	private String phone; // Optional
	private String address; // Optional
	private String username;
	private String password;
	private String newPassword;
	private String confirmPassword;
	private String authority;
	private Customer customer;
	private String passKey;
	private boolean agree;
	private AccountForm account;
	private int id;
	private int version;

	// Constructors -------------------------

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
		this.account.setAuthority(authority);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		this.account.setUsername(username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.account.setPassword(password);
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
		this.account.setConfirmPassword(confirmPassword);
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
		this.account.setNewPassword(newPassword);
	}

	public AccountForm getAccount() {
		return account;
	}

	public void setAccount(AccountForm account) {
		this.account = account;
	}

	@NotNull
	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	public ActorForm() {
		super();
		this.id = 0;
		this.version = 0;
		this.account = new AccountForm();
	}

	public ActorForm(final Actor actor) {
		super();
		this.id = actor.getId();
		this.setName(actor.getName());
		this.setSurname(actor.getSurname());
		this.setEmail(actor.getEmail());
		this.setPhone(actor.getPhone());
		this.setAddress(actor.getAddress());
		this.setCustomer(actor.getCustomer());
		this.setAccount(new AccountForm(actor));
		this.setAuthority(this.getAccount().getAuthority());
		this.setUsername(actor.getUserAccount().getUsername());

	}

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

	@NotBlank
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
