package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class EquipmentCategory extends DomainEntity {

	public String name;

	public String description;

	private EquipmentCategory parent;

}
