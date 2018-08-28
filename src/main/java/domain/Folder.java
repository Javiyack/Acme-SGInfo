
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "name") }, uniqueConstraints = @UniqueConstraint(columnNames = {
		"name", "parent_id"
	}))
public class Folder extends DomainEntity {

	private String name;
	private boolean systemFolder;
	// Relationships
	private Folder parent;
	private Actor actor;

	@NotBlank
	@SafeHtml
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotNull
	public boolean getSystemFolder() {
		return this.systemFolder;
	}

	public void setSystemFolder(final boolean systemFolder) {
		this.systemFolder = systemFolder;
	}

	@ManyToOne(optional = true)
	@Valid
	public Folder getParent() {
		return this.parent;
	}

	public void setParent(final Folder parentFolder) {
		this.parent = parentFolder;
	}

	@ManyToOne(optional = true)
	@Valid
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}
