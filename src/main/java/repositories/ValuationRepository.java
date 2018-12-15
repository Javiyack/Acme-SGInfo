package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Valuation;

@Repository
public interface ValuationRepository extends JpaRepository<Valuation, Integer> {
	
	@Query("select count(v) from Valuation v where v.incidence.id=?1")
	Integer numValuationByUserInIncidence(Integer incidenceId);
	
	@Query("select v from Valuation v where v.incidence.id=?1")
	Valuation valuationByUserInIncidence(Integer incidenceId);
}
