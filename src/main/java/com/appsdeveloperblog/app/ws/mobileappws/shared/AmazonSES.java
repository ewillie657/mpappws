package com.appsdeveloperblog.app.ws.mobileappws.shared;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.UserDto;

// import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class AmazonSES {

    // THis address must be verified with Amazon SES.
    final String FROM = "williee541@gmail.com";

    // The subject line for the email.
    final String SUBJECT = "One last step to complete your registration with PhotoApp";

    final String PASSWORD_RESET_SUBJECT = "Password reset request";

    // The HTML body for the email.
    final String HTMLBODY = "<h1>Please verify yoour email address</h1>" +
                "<p>Thank you for registering with our mobile app. To complete registration process and be able to login</p>" +
                "<p>click on the following link: </p>" +
                // "<a href='http://localhost:8081/verification-service/email-verification.html?token=$tokenValue'>" +
                "<a href='http://ec2-18-218-17-190.us-east-2.compute.amazonaws.com:8080/verification-service/email-verification.html?token=$tokenValue'>" +
                "Final step to complete your registration" + "</a><br/><br/>" +
                "Thank you! And we are waiting for you inside!";
    
    // The email body for recipients with non-HTML email clients.
    final String TEXTBODY = "Please verify your email address. " +
                    "Thank you for registering with our mobile app. To complete registration process and be able to login " +
                    " open the following URL in your browser window: " +
                    " http://ec2-18-218-17-190.us-east-2.compute.amazonaws.com:8080/verification-service/email-verification.html?token=$tokenValue" +
                    " Thank you! And we are waiting for you inside!";

    final String PASSWORD_RESET_HTMLBODY = "<h1>A request to reset your password</h1>" +
            "<p>Hi, $firstName! </p>" +
            "<p>Someone has requested to reset your password with our project. If it were not you please ignore it." +
            " Otherwise please click on the link below to set a new password: " +
            "<a href='http://localhost:8080/verification-service/password-reset.html?token=$tokenValue'>" +
            " Click this link to Reset Password" +
            "</a><br/><br/>" +
            "Thank you!";
    
    //The email body for recipients with non-HTML email clients.
    final String PASSWORD_RESET_TEXTBODY = "A request to reset your password " +
            "Hi, $firstName! " +
            "Someone has requested to reset your password with our project. If it were not you please ignore it. " +
            "Otherwise please open the link below in your browser window to set a new password: " +
            "http://localhost:8080/verification-service/password-reset.html?token=$tokenValue " +
            "Thank you!";

    public void verifyEmail(UserDto userDto){

        System.setProperty("aws.accessKeyId", "AKIARNHKKVPW7JNN53V5"); 	
        System.setProperty("aws.secretKey", "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"); 

        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1)
        .build();

        String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
        String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(userDto.getEmail()))
                .withMessage(new Message()
                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
                        .withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
                        .withSource(FROM);
        
        client.sendEmail(request);

        System.out.println("Email sent!");

    }

    public boolean sendPasswordResetRequest(String firstName, String email, String token)
    {
        boolean returnValue = false;

        System.setProperty("aws.accessKeyId", "AKIARNHKKVPW7JNN53V5"); 	
        System.setProperty("aws.secretKey", "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"); 

        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
            .withRegion(Regions.US_EAST_1).build();

        String htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
                htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);
        
        String textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
                textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);
        
        SendEmailRequest request = new SendEmailRequest()
            .withDestination(new Destination().withToAddresses(email))
            .withMessage(new Message()
                .withBody(new Body()
                    .withHtml(new Content()
                        .withCharset("UTF-8").withData(htmlBodyWithToken))
                    .withText(new Content()
                        .withCharset("UTF-8").withData(textBodyWithToken)))
                .withSubject(new Content()
                    .withCharset("UTF-8").withData(PASSWORD_RESET_SUBJECT)))
            .withSource(FROM);
        
        SendEmailResult result = client.sendEmail(request);
        if(result != null && (result.getMessageId() != null && !result.getMessageId().isEmpty()))
        {
            return returnValue;
        }

        return returnValue;

    }

}