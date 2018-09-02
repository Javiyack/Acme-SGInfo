
package forms;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import domain.Bill;
import domain.Incidence;
import domain.IncidenceBill;
import domain.Labor;

public class LaborForm {


	private int id ;
	private int version;

	private Date moment;

	private String title;

	private String description;

	private Date time;

	private Incidence incidence;

	private Bill bill;

	public LaborForm() {
		super();
		id=0 ;
		version=0;

	}
	public LaborForm(Labor labor) {
		super();
		id=labor.getId() ;
		version=labor.getVersion();
		this.setTitle(labor.getTitle());
		this.setDescription(labor.getDescription());
		this.setIncidence(labor.getIncidence());
		this.setMoment(labor.getMoment());
		this.setTime(labor.getTime());
		this.setBill(labor.getBill());
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

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date momment) {
		this.moment = momment;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Incidence getIncidence() {
		return incidence;
	}

	public void setIncidence(Incidence incidence) {
		this.incidence = incidence;
	}

	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill bill) {
		this.bill = bill;
	}

}
