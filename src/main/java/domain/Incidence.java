package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Incidence extends DomainEntity {

	private String ticker;

	private String title;

	private String description;

	private Date publicationDate;

	private Boolean cancelled;

	private String cancelationReason;

	private Date startingDate;

	private Date endingDate;

	private Servant Servant;

	private User user;

	private Technician technician;


	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "^INC-([012][1-9]|[3][01]|20)([0][1-9]|[1][0-2])\\d\\d-\\d{0,8}$") // "INC-MMDDYY-XXXXXXXX"
	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public String getCancelationReason() {
		return cancelationReason;
	}

	public void setCancelationReason(String cancelationReason) {
		this.cancelationReason = cancelationReason;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDay) {
		this.startingDate = startingDay;
	}

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Date endingDay) {
		this.endingDate = endingDay;
	}
	

	@ManyToOne(optional = true)
	public Servant getServant() {
		return Servant;
	}

	public void setServant(Servant Servant) {
		this.Servant = Servant;
	}

	@NotNull
	@ManyToOne(optional = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Technician getTechnician() {
		return technician;
	}

	public void setTechnician(Technician tecnician) {
		this.technician = tecnician;
	}


}
