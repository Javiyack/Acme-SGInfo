/**
 * 
 */

package repositories;

import java.util.List;

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

	@Query("select c.logo from Configuration c")
	String findLogo();

	@Query("select c.companyName from Configuration c")
	String findName();

	@Query("select c.hourPrice from Configuration c")
	Double findHourPrice();

	@Query("select c.iva from Configuration c")
	Double findIVA();

	@Query("select c.passKey from Configuration c")
	String findPassKey();

	
	@Query("select c from Configuration c")
	Configuration findCfg();
}
