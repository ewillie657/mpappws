package com.appsdeveloperblog.app.ws.mobileappws.io.repositories;

import com.appsdeveloperblog.app.ws.mobileappws.io.entity.PasswordResetTokenEntity;

import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long> {

    PasswordResetTokenEntity findByToken(String token);

}