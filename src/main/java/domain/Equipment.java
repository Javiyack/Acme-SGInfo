package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Equipment extends DomainEntity {

	public String name;

	public String description;

	public Date alta;

	public Date baja;

	public Money price;

	private EquipmentCategory equipmentCategoy;

	private Customer customer;

	private Location location;

}
