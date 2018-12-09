
package forms;

import domain.Bill;
import domain.Money;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

public class BillForm {

	private String		ticker;
	private Date		moment;
	private Money 		amount;
	private int			month;
	private int 		year;
	private int			id;
	private int			version;


	//Constructors -------------------------

	public BillForm() {
		super();
		this.id = 0;
		this.version = 0;
		}

	public BillForm(Bill bill) {
		super();
		this.id = bill.getId();
		this.version = bill.getVersion();
		this.setMoment(bill.getMoment());
		this.setAmount(bill.getAmount());
		this.setYear(bill.getYear());
		this.setMonth(bill.getMonth());;
		}
	

	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTicker() {
		return ticker;
	}
	
	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getMoment() {
		return this.moment;
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

	
	public void setMoment(Date creationMoment) {
		this.moment = creationMoment;
	}

	public Money getAmount() {
		return amount;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}		
	
}
