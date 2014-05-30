package com.prm.resources;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface PrmRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

}
