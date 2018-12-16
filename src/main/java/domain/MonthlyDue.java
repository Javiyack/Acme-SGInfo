package domain;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
		"year", "month", "request_id"}))
public class MonthlyDue extends DomainEntity {

	private int year;

	private int month;

	private Request request;

	private Bill	bill;

	@Range(min=2000, max = 2200)
	public int getYear() {
		return year;
	}

	public void setYear(int momment) {
		this.year = momment;
	}

	@Range(min=1, max = 12)
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	@NotNull
	@ManyToOne(optional = false)
	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	
	@ManyToOne(optional = true)
	public Bill getBill() {
		return bill;
	}

	public void setBill(Bill Bill) {
		this.bill = Bill;
	}

}
