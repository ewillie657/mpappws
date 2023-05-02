package com.appsdeveloperblog.app.ws.mobileappws.io.repositories;

import java.util.List;

import com.appsdeveloperblog.app.ws.mobileappws.io.entity.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


// public interface UserRepository extends CrudRepository<UserEntity, Long>{
@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long>{

    //QUERY DATABASE TABLE
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
    UserEntity findUserByEmailVerificationToken(String token);

    // NATIVE SQL QUERY START

    @Query(value="select * from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'", 
    countQuery="select COUNT(*) from Users u where u.EMAIL_VERIFICATION_STATUS = 'true'",
     nativeQuery = true)
    Page<UserEntity> findAllUsersWithConfirmedEmailAddress( Pageable pageableRequest);

    // Positional Parameter example
    @Query(value = "select * from Users u where u.first_name = ?1", nativeQuery = true)
    List<UserEntity> findUserByFirstName(String firstName);

    // Named Parameters Example
    @Query(value = "select * from Users u where u.last_name = :lastName", nativeQuery = true)
    List<UserEntity> findUserByLastName(@Param("lastName") String lastName);

    // LILKE Example
    @Query(value = "select * from Users u where first_name LIKE %:keyword% or last_name LIKE %:keyword%", nativeQuery = true)
    List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);

    // SELECT FIELDS Example
    @Query(value = "select u.first_name, u.last_name from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%", nativeQuery = true)
    List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);

    @Transactional
    @Modifying
    @Query(value ="update users u set u.EMAIL_VERIFICATION_STATUS = :emailVerificationStatus where u.user_id = :user_id" , nativeQuery = true)
    void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus, @Param("userId") String userId);

    // NATIVE SQL QUERY END

    // JAVA PERSISTEMT QUERY LANGUAGE JPQL START
    @Query("Select user from UserEntity user where user.userId = :userId")
    UserEntity findUserEntityByUserId(@Param("userId") String userId);

    @Query("select user.firstName, user.lastName from UserEntity user where user.userId = :userId")
    List<Object[]> getUserEntityFullNameById(@Param("userId") String userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u set u.emailVerificationStatus = :emailVerificationStatus where u.userId = :userId ")
    void updateUserEntityEmailVerificationStatus(
        @Param("emailVerificationStatus") boolean emailVerificationStatus,
        @Param("userId") String userId
    );

    // JAVA PERSISTEMT QUERY LANGUAGE JPQL END

}