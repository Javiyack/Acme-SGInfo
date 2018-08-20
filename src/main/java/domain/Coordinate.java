package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Coordinate extends DomainEntity {

	public Double longitude;

	public Double latitude;

}
