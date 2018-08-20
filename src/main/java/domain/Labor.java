package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Labor extends DomainEntity {

	public Date momment;

	public String title;

	public String description;

	public Double time;

	private Incidence incidence;

	private IncidenceBill incidenceBill;

}
