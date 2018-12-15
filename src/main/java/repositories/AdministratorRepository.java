/*
 * AdministratorRepository.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package repositories;

import java.util.List;

import domain.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
	
	//DASHBOARD
	
	//Ranking users top 3 with more incidences
	@Query("select i.user.name,i.user.surname, count(i) from Incidence i group by i.user order by count(i) desc")
	List<Object[]> usersWithMoreIncidences();
	
	//Ranking technician top 3 with less incidences
	@Query("select i.technician.name,i.technician.surname, count(i) from Incidence i group by i.user order by count(i) asc")
	List<Object[]> techniciansWithLessIncidences();
	
	//Percentage of message that have sended the differents actors
	@Query("select count(m)*1.0/(select count(me) from Message me),m.sender.name from Message m group by m.sender order by count(m) desc")
	List<Object[]> percMessagesSenderByActor();
	
	//The average, the minimum, the maximum, and the standard desviation of the number of request per responsible.
	@Query(value = "select avg(requests.count) from (select r.name, count(re.id) count from `acme-crm`.Responsible r left join `acme-crm`.Request re on re.responsible_id=r.id group by r.id) as requests", nativeQuery = true)
	Double avgRequestByResponsible();
	
	@Query(value = "select min(requests.count) from (select r.name, count(re.id) count from `acme-crm`.Responsible r left join `acme-crm`.Request re on re.responsible_id=r.id group by r.id) as requests", nativeQuery = true)
	Double minRequestByResponsible();
	
	@Query(value = "select max(requests.count) from (select r.name, count(re.id) count from `acme-crm`.Responsible r left join `acme-crm`.Request re on re.responsible_id=r.id group by r.id) as requests", nativeQuery = true)
	Double maxRequestByResponsible();
	
	@Query(value = "select stddev(requests.count) from (select r.name, count(re.id) count from `acme-crm`.Responsible r left join `acme-crm`.Request re on re.responsible_id=r.id group by r.id) as requests", nativeQuery = true)
	Double stddevRequestByResponsible();
	
	//Best rated incidences
	@Query("select v.value,v.incidence.title from Valuation v where v.value=(select max(va.value) from Valuation va)")
	List<Object[]> bestRatedIncidences();
	
	//Technicians that have resulted in the worst rated incidences
	@Query("select v.value,v.incidence.technician.name from Valuation v where v.value=(select min(va.value) from Valuation va)")
	List<Object[]> worstRatedTechnicianOfIncidences();



}
