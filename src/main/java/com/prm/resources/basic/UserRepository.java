package com.prm.resources.basic;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.User;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends PrmRepository<User, Long> {

	List<User> findByName(@Param("name") String name);
	
	User findByUsername(@Param("username") String username);
}
