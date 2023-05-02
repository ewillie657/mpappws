package com.appsdeveloperblog.app.ws.mobileappws.io.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.appsdeveloperblog.app.ws.mobileappws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.mobileappws.io.entity.UserEntity;

import org.hibernate.annotations.SourceType;
// import org.h2.mvstore.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
    
@ExtendWith(SpringExtension.class)
@SpringBootTest    
public class UserRepositoryTest {

    @Autowired
    UserRepository userRespository;

    static boolean recordsCreated = false;

    @BeforeEach
    public void setup(){

       if(!recordsCreated) createRecords();

    }
        
    @org.junit.jupiter.api.Test
    public void testGetVerifiedUsers() {
        Pageable pageableRequest = PageRequest.of(0, 1);
        Page<UserEntity> page = userRespository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
        assertNotNull(page);

        List<UserEntity> userEntities = page.getContent();
        assertNotNull(userEntities);
        assertTrue(userEntities.size() == 1);
    }

    @Test
    final void testFindUserByFirstName()
    {
        String firstName = "Edward";
        List<UserEntity> users = userRespository.findUserByFirstName(firstName);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getFirstName().equals(firstName));
    }

    @Test
    final void testFindUserByLastName()
    {
        String lastName = "Willie";
        List<UserEntity> users = userRespository.findUserByLastName(lastName);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().equals(lastName));
    }

    @Test
    final void findUserByKeyword()
    {
        String keyword = "Will";
        List<UserEntity> users = userRespository.findUserByKeyword(keyword);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(
            user.getLastName().contains(keyword) || user.getFirstName().contains(keyword)
        );
    }

    @Test
    final void findUserFirstNameAndLastNameByKeyword()
    {
        String keyword = "Will";
        List<Object[]> users = userRespository.findUserFirstNameAndLastNameByKeyword(keyword);
        assertNotNull(users);
        assertTrue(users.size() == 2);

        Object[] user = users.get(0);

        assertTrue(user.length == 2);

        String userFirstName = String.valueOf(user[0]);
        String userLastName = String.valueOf(user[1]);

        assertNotNull(userFirstName);
        assertNotNull(userLastName);

        System.out.println("First Name = " + userFirstName);
        System.out.println("Last Name = " + userLastName);

    }

    @Test
    final void testUpdateUserEmailVerificationStatus()
    {
        boolean newEmailVerificationStatus = false;
        userRespository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");

        UserEntity storedUserDetails = userRespository.findByUserId("1a2b3c");

        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    }

    // JAVA PERSISTEMT QUERY LANGUAGE JPQL START

    @Test
    final void testFindUserEntityByUserId()
    {
        String userId = "1a2b2c";
        UserEntity userEntity = userRespository.findUserEntityByUserId(userId);

        assertNotNull(userEntity);
        assertTrue(userEntity.getUserId().equals(userId));
    }

    @Test
    final void testGetUserEntityFullNameById()
    {
        String userId = "1a2b3c";
        List<Object[]> records = userRespository.getUserEntityFullNameById(userId);

        assertNotNull(records);
        assertTrue(records.size() == 1);

        Object[] userDetails = records.get(0);

        String firstName = String.valueOf(userDetails[0]);
        String lastName = String.valueOf(userDetails[1]);

        assertNotNull(firstName);
        assertNotNull(lastName);
    }

    // @Test
    // final void testUpdateUserEntityEmailVerificationStatus()
    // {
    //     boolean newEmailVerificationStatus = false;
    //     userRespository.updateUserEntityEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");

    //     UserEntity storedUserDetails = userRespository.findByUserId("1a2b3c");

    //     boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();

    //     assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    // }

    //JAVA PERSISTEMT QUERY LANGUAGE JPQL END

    private void createRecords()
    {
         // Prepare User Entity
         UserEntity userEntity = new UserEntity();
         userEntity.setFirstName("Edward");
         userEntity.setLastName("Willie");
         userEntity.setUserId("1a2b3c");
         userEntity.setEncryptedPassword("xxx");
         userEntity.setEmail("test@test.com");
         userEntity.setEmailVerificationStatus(true);
 
         // Prepare User Addresses
         AddressEntity addressEntity = new AddressEntity();
         addressEntity.setType("shipping");
         addressEntity.setAddressId("ahgyt74hfy");
         addressEntity.setCity("Mobay");
         addressEntity.setCountry("Jamaica");
         addressEntity.setPostalCode("ABACCDA");
         addressEntity.setStreetName("123 Street Address");
 
         List<AddressEntity> addresses = new ArrayList<>();
         addresses.add(addressEntity);
 
         userEntity.setAddresses(addresses);
 
         userRespository.save(userEntity);
 
 
         // Prepare User Entity
         UserEntity userEntity2 = new UserEntity();
         userEntity2.setFirstName("Edward");
         userEntity2.setLastName("Willie");
         userEntity2.setUserId("1a2b3c");
         userEntity2.setEncryptedPassword("xxx");
         userEntity2.setEmail("test@test.com");
         userEntity2.setEmailVerificationStatus(true);
 
         // Prepare User Addresses
         AddressEntity addressEntity2 = new AddressEntity();
         addressEntity2.setType("shipping");
         addressEntity2.setAddressId("ahgyt74hfyddddd");
         addressEntity2.setCity("Mobay");
         addressEntity2.setCountry("Jamaica");
         addressEntity2.setPostalCode("ABACCDA");
         addressEntity2.setStreetName("123 Street Address");
 
         List<AddressEntity> addresses2 = new ArrayList<>();
         addresses2.add(addressEntity2);
 
         userEntity2.setAddresses(addresses2);
 
         userRespository.save(userEntity2);

         recordsCreated = true;
    }
}
    