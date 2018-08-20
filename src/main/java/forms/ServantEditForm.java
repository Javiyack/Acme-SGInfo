
package forms;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Servant;
import domain.Technician;
import domain.User;

public class ServantEditForm {

	private String		ticker;
	private String		title;
	private String		description;
	private Date		publicationDate;
	private Date		startingDate;
	private Date		endingDate;
	private Servant		Servant;
	private Technician		technician;
	private User		user;
	private boolean		cancelled;
	private String		cancelationReason;
	private int			id;
	private int			version;


	//Constructors -------------------------

	public ServantEditForm() {
		super();
		this.id = 0;
		this.version = 0;
		}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getTicker() {
		return ticker;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(final Date moment) {
		this.publicationDate = moment;
	}



	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}

	public Servant getServant() {
		return Servant;
	}

	public void setServant(Servant Servant) {
		this.Servant = Servant;
	}

	public Technician getTechnician() {
		return technician;
	}

	public void setTechnician(Technician technician) {
		this.technician = technician;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCancelationReason() {
		return cancelationReason;
	}

	public void setCancelationReason(String cancelationReason) {
		this.cancelationReason = cancelationReason;
	}

	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

}
