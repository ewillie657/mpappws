package com.appsdeveloperblog.app.ws.mobileappws.ui.controller;

import com.appsdeveloperblog.app.ws.mobileappws.ui.model.request.LoginRequestModel;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.*;

@RestController
public class AuthenticationController {

    @ApiOperation("User Login")
    @ApiResponses( value = {
        @ApiResponse( code = 200, 
        message = "Response Headers",
        responseHeaders = {
            @ResponseHeader(name = "authorization",
                description = "Bearer <JWT value here>",
                response = String.class ),
            @ResponseHeader( name = "userId",
                description = "<Public User Id value here>",
                response = String.class )
        } )
    })
    @PostMapping("/users/login")
    public void theFakeLogin(@RequestBody LoginRequestModel loginRequestModel){
        throw new IllegalStateException("This method should not be called. This method is implemented by Spring Security");
    }

}