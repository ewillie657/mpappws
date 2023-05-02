package com.appsdeveloperblog.app.ws.mobileappws.ui.controller;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.appsdeveloperblog.app.ws.mobileappws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.mobileappws.service.AddressService;
import com.appsdeveloperblog.app.ws.mobileappws.service.UserService;
import com.appsdeveloperblog.app.ws.mobileappws.shared.Roles;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.PasswordResetModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.PasswordResetRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.AddressesRest;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.mobileappws.ui.model.response.UserRest;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.*;

@RestController
@RequestMapping("/users") //http://localhost:8080/users
// @CrossOrigin(origins = "http://localhost:8083") FOR JUST SPECIFIC ACCESS
// @CrossOrigin(origins = "*") // FOR ALL ACCESS
public class UserController{

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @Autowired
    AddressService addressesService;

    @PostAuthorize("hasRole=('ADMIN') or returnObject.userId == principal.userId")
    @ApiOperation(value = "The Get User Details Web Service Endpont",
        notes = "${userController.GetUser.ApiOperation.Notes}")
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id)
    {
        UserRest returnValue = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;
    }

    @PostMapping( consumes = {MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception
    {
        UserRest returnValue = new UserRest();

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        // Used to map one object
        // UserDto userDto = new UserDto();
        // BeanUtils.copyProperties(userDetails, userDto);

        // USED TO MAO MORE THAN ONE OBJECT
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));

        UserDto createdUser = userService.createUser(userDto);
        // BeanUtils.copyProperties(createdUser, returnValue); // to copy for single data
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path = "/{id}",
        consumes = {MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
    {
        UserRest returnValue = new UserRest();

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;

    }

    // @Secured("ROLE_ADMIN")
    // @PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ApiImplicitParams(@ApiImplicitParam(name = "authorization", value = "${userController.authorization.description}", paramType = "header"))
    public OperationStatusModel deleteUser(@PathVariable String id)
    {
        OperationStatusModel returnValue = new OperationStatusModel();

        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "authorization", value = "${userController.authorizationHeader.description}", paramType = "header")
    })
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value="page", defaultValue = "0") int page,
    @RequestParam(value="limit", defaultValue = "1") int limit){

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page,limit);

        for (UserDto userDto : users){

            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }
        return returnValue;
    }

    // http://localhost:8080/mobile-app-ws/users/fkffkfkdkdfbfd/addresses
    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    // public List<AddressesRest> getUserAddresses(@PathVariable String id) // Without hal
    public CollectionModel<AddressesRest> getUserAddresses(@PathVariable String id)
    {
        List<AddressesRest> addressesListRestModel = new ArrayList<>();

        List<AddressDTO> addressesDTO = addressesService.getAddresses(id);

        if(addressesDTO != null && !addressesDTO.isEmpty())
        {
            java.lang.reflect.Type listType = new TypeToken<List<AddressesRest>>(){}.getType();
            addressesListRestModel = new ModelMapper().map(addressesDTO, listType);

            for(AddressesRest addressRest : addressesListRestModel)
            {
                Link addressLink = linkTo(methodOn(UserController.class).getUserAddrress(id, addressRest.getAddressId())).withSelfRel();
                addressRest.add(addressLink);

                Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
                addressRest.add(userLink);
            }

        }
        
        // return addressesListRestModel;
        return new CollectionModel<>(addressesListRestModel);
    }

    @GetMapping(path="/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
    // public AddressesRest getUserAddrress(@PathVariable String userId,@PathVariable String addressId)
    public EntityModel getUserAddrress(@PathVariable String userId,@PathVariable String addressId)
    {
        AddressDTO addressDTO = addressService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();

        // FOR HATEOAS START
        Link addressLink = linkTo(methodOn(UserController.class).getUserAddrress(userId, addressId)).withSelfRel();
        Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
        // FOR HATEOAS END

        AddressesRest addressesRestModel = modelMapper.map(addressDTO, AddressesRest.class);

        addressesRestModel.add(addressLink);
        addressesRestModel.add(userLink);
        addressesRestModel.add(addressesLink);

        // return addressesRestModel;
        return new EntityModel<>(addressesRestModel);
    }

    /**
     * http://localhost:8080/mobile-app-ws/users/email-verification?token=sdfsdf
     */
    @GetMapping(path = "/email-verification", produces = {MediaType.APPLICATION_XML_VALUE,
        MediaType.APPLICATION_JSON_VALUE})
        // @CrossOrigin(origins = "*") // FOR JUST THIS SPECIFIC ROUTE
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        if(isVerified)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else{
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }

        return returnValue;
    }

    /**
     * http://localhost:8080/mobile-app-ws/users/password-reset-request
     */
    @PostMapping(path = "/password-reset-request",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel){

        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if(operationResult)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }

    @PostMapping(path = "/password-reset",
    consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel){

        OperationStatusModel returnValue = new OperationStatusModel();

        boolean operationResult = userService.resetPassword(
            passwordResetModel.getToken(),
            passwordResetModel.getPassword()
        );

        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if(operationResult)
        {
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }

        return returnValue;
    }

}