package domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Entity
@Access(AccessType.PROPERTY)
public class Bill extends DomainEntity {

	private Double currentIVA;
	private Double currentHourPrice;
	private Date moment;
	private int month;
	private int year;
	private Money amount;

	public Double getCurrentHourPrice() {
		return currentHourPrice;
	}

	public void setCurrentHourPrice(Double currentHourPrice) {
		this.currentHourPrice = currentHourPrice;
	}

	public Double getCurrentIVA() {
		return currentIVA;
	}

	public void setCurrentIVA(Double currentIVA) {
		this.currentIVA = currentIVA;
	}

	public Money getAmount() {
		return amount;
	}

	public void setAmount(Money amount) {
		this.amount = amount;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@NotNull
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@NotNull
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}


}
