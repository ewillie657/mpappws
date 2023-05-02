package com.appsdeveloperblog.app.ws.mobileappws.ui.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.appsdeveloperblog.app.ws.mobileappws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.UserRest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    UserDto userDto;

    final String USER_ID = "bjdbvdbfuhfuefefaue";

    @BeforeEach
    void setup() throws Exception{
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setFirstName("Edward");
        userDto.setLastName("Willie");
        userDto.setEmail("test@test.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setAddresses(getAddressesDto());
        userDto.setEncryptedPassword("cacadfwfsfb434");

    }
        
    @Test
    final void testGetUser() {

        when(userService.getUserByUserId(anyString())).thenReturn(null);

        UserRest userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
        
    }

    private List<AddressDTO> getAddressesDto()
    {
        AddressDTO addressDto = new AddressDTO();
        addressDto.setType("shipping");
        addressDto.setCity("Mobay");
        addressDto.setCountry("Jamaica");
        addressDto.setPostalCode("ABC123");
        addressDto.setStreetName("123 Street name");

        AddressDTO billingAddressDto = new AddressDTO();
        billingAddressDto.setType("shipping");
        billingAddressDto.setCity("Mobay");
        billingAddressDto.setCountry("Jamaica");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("123 Street name");

        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDto);
        addresses.add(billingAddressDto);

        return addresses;
    }
}