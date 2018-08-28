
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

	@Query("select c.welcomeMessageEs from Configuration c")
	String findWelcomeMessageEs();

	@Query("select c.welcomeMessageEn from Configuration c")
	String findWelcomeMessageEn();

	@Query("select c.banner from Configuration c")
	String findBanner();

	@Query("select c.companyName from Configuration c")
	String findCompanyName();

	@Query("select c.benefitsPercentage from Configuration c")
	Double findBenefitsPercentage();
}
