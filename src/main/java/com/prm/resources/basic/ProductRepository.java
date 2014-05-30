package com.prm.resources.basic;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.prm.models.basic.Product;
import com.prm.resources.PrmRepository;

@RepositoryRestResource(collectionResourceRel = "product", path = "product")
public interface ProductRepository extends PrmRepository<Product, Long> {

}
