
package forms;

import domain.Incidence;
import domain.Servant;
import domain.Technician;
import domain.User;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

public class IncidenceForm {

	private String		ticker;
	private String		title;
	private String		description;
	private Date		publicationDate;
	private Date		startingDate;
	private Date		endingDate;
	private Servant		Servant;
	private Technician	technician;
	private User		user;
	private Integer		customerId;
	private boolean		cancelled;
	private String 		cancellationReason;
	private int			id;
	private int			version;


	//Constructors -------------------------

	public IncidenceForm() {
		super();
		this.id = 0;
		this.version = 0;
		}

	public IncidenceForm(Incidence incid) {
		super();
		this.id = incid.getId();
		this.version = incid.getVersion();
		this.setTitle(incid.getTitle());
		this.setDescription(incid.getDescription());
		this.setTicker(incid.getTicker());
		this.setUser(incid.getUser());
		this.setTechnician(incid.getTechnician());
		this.setPublicationDate(incid.getPublicationDate());
		this.setStartingDate(incid.getStartingDate());
		this.setEndingDate(incid.getEndingDate());
		this.setServant(incid.getServant());
		this.setCancelled(incid.getCancelled());
		this.setCancellationReason(incid.getCancellationReason());
		}
	

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTicker() {
		return ticker;
	}
	
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return title;
	}	

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}
	
	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getStartingDate() {
		return startingDate;
	}	

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getEndingDate() {
		return endingDate;
	}	

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCancellationReason() {
		return cancellationReason;
	}

	public Servant getServant() {
		return Servant;
	}

	public void setServant(Servant servant) {
		Servant = servant;
	}

	public Technician getTechnician() {
		return technician;
	}

	public void setTechnician(Technician technician) {
		this.technician = technician;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
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

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	
}
