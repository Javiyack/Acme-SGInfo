
package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "text")
})
public class TabooWord extends DomainEntity {

	private String text;


	@NotBlank
	@SafeHtml
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}
}
