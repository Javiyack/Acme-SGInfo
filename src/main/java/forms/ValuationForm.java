package forms;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import domain.Incidence;
import domain.Valuation;

public class ValuationForm {
	
	private int id ;
	private int version;

	private Integer value;

	private String comments;

	private Incidence incidence;

	public ValuationForm() {
		super();
		id=0 ;
		version=0;

	}
	public ValuationForm(Valuation valuation) {
		super();
		id=valuation.getId() ;
		version=valuation.getVersion();
		this.setValue(valuation.getValue());
		this.setComments(valuation.getComments());
		this.setIncidence(valuation.getIncidence());
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
	
	@Min(0)
	@Max(5)
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}


	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Incidence getIncidence() {
		return incidence;
	}

	public void setIncidence(Incidence incidence) {
		this.incidence = incidence;
	}

	

}
