package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class IncidenceBill extends DomainEntity {

	public Date moment;

	public Money amount;

}
