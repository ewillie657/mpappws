package com.appsdeveloperblog.app.ws.mobileappws.ui.model.response;

import java.util.Date;

public class ErrorMessage{

    private Date timestamp;
    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }

    public Date getTimestamo() {
        return timestamp;
    }

    public void setTimestamo(Date timestamo) {
        this.timestamp = timestamo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    

    
}


