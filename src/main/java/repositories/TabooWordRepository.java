
package repositories;

import domain.TabooWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TabooWordRepository extends JpaRepository<TabooWord, Integer> {
	

	@Query("select t from TabooWord t where ?1 like concat('%',t.text,'%') or ?2 like concat('%',t.text,'%')")
	Collection<TabooWord> getTabooWordFromMessageSubjectAndBody(String subject, String body);

}
