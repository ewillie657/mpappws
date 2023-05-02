package com.appsdeveloperblog.app.ws.mobileappws.shared;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import com.appsdeveloperblog.app.ws.mobileappws.security.SecurityConstants;

// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// @Component
@Service
public class Utils{

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    // private final int ITERATION = 10000;
    // private final int KEY_LENGTH = 256;

    public String generateUserId(int length){
        return generateRandomString(length);
    }

    public String generateAddressId(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length){

        StringBuilder returnValue = new StringBuilder();

        for(int i = 0; i < length; i++){
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public static boolean hastokenExpired(String token)
    {
        boolean returnValue = false;

        try{

            Claims claims = Jwts.parser()
            .setSigningKey(SecurityConstants.getTokenSecret())
            .parseClaimsJws(token).getBody();

            Date tokenExpirationDate = claims.getExpiration();
            Date todayDate = new Date();

            returnValue = tokenExpirationDate.before(todayDate);

        } catch(ExpiredJwtException ex)
        {
            returnValue = true;
        }
        

        return returnValue;
    }

    public String generateEmailVerificationToken(String userId)
    {
        String token = Jwts.builder()
            .setSubject(userId)
            .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
            .compact();

        return token;
    }

    public String generatePasswordResetToken(String userId){

        String token = Jwts.builder()
            .setSubject(userId)
            .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
            .compact();

        return token;

    }

}

