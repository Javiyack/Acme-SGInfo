package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"incidence_id"})})
public class Valuation extends DomainEntity {

	private Integer value;
	private String comments;
	private Incidence incidence;
	
	@NotNull
	@Range(min=0,max=5)
	public Integer getValue(){
		return value;
	}
	
	public void setValue(Integer value){
		this.value = value;
	}

	@SafeHtml
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	@NotNull
	@OneToOne(optional = false)
	public Incidence getIncidence() {
		return incidence;
	}

	public void setIncidence(Incidence incidence) {
		this.incidence = incidence;
	}

	
}
