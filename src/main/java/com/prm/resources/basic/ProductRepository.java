package com.prm.resources.basic;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.Product;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "product", path = "product")
public interface ProductRepository extends PrmRepository<Product, Long> {
	
	List<Product> findByCode(@Param("code") String code);
	
	List<Product> findByCodeLike(@Param("code") String code);
	
	List<Product> findByName(@Param("name") String name);

	List<Product> findByNameLike(@Param("name") String name);
}
