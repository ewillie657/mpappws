package com.appsdeveloperblog.app.ws.mobileappws.io.repositories;

import com.appsdeveloperblog.app.ws.mobileappws.io.entity.AuthorityEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Long> {

    AuthorityEntity findByName(String name);

}