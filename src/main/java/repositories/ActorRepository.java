/*
 * ActorRepository.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package repositories;

import domain.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id = ?1")
	Actor findByUserAccountId(int userAccountId);

	@Query("select a from Actor a " +
			"where a.id in (select u.id from User u where u.customer=?1) " +
			"or a.id in (select r.id from Responsible r where r.customer=?1)")
	Collection<Actor> findWorkers(int customerId);

	@Query("select a from Actor a where a.customer.id = ?1")
	Collection<Actor> findCoworkers(int customerId);


	@Query("select u from User u")
	Collection<Actor> findAllResponsibles();
	@Query("select r from Responsible r")
	Collection<Actor> findAllUsers();
	@Query("select t from Technician t")
	Collection<Actor> findAllTecnicians();
	@Query("select m from Manager m")
	Collection<Actor> findAllManagers();
}
