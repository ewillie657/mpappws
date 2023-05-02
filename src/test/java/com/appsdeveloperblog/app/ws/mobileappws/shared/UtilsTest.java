package com.appsdeveloperblog.app.ws.mobileappws.shared;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    final void testGenerateUserId(){
        
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);

        assertTrue(userId.length() == 30);
        assertTrue(!userId.equalsIgnoreCase(userId2));
    }

    @Test
    // @Disabled
    final void testHasTokenNotExpired(){
        
        String token = utils.generateEmailVerificationToken("54fidfahfiahfi");
        assertNotNull(token);

        boolean hasTokenExpred = Utils.hastokenExpired(token);
        assertFalse(hasTokenExpred);
    }

    @Test
    final void testHasTokenExpired()
    {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiZXhwIjoxNTg3NTA5NTMzfQ.qdTYThZYCnu2j0pMvD11C9ey4FGzrXwNOCZ1I7r5vpeCBbJe5FJqyrYk95lkasREvvSRnju8VyH7Xtbkms6T6A";
        boolean hastokenExpired = Utils.hastokenExpired(expiredToken);

        assertTrue(hastokenExpired);

    }

}