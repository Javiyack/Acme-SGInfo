package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Labor extends DomainEntity {

	private Date moment;

	private String title;

	private String description;

	private Date time;

	private Incidence incidence;

	private Bill	bill;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date momment) {
		this.moment = momment;
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

	@NotNull
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Incidence getIncidence() {
		return incidence;
	}

	public void setIncidence(Incidence incidence) {
		this.incidence = incidence;
	}

	
	@ManyToOne(optional = true)
	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill Bill) {
		this.bill = Bill;
	}

}
