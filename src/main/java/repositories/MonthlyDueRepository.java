
package repositories;

import domain.MonthlyDue;
import domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;

@Repository
public interface MonthlyDueRepository extends JpaRepository<MonthlyDue, Integer> {


}

