package com.kavi.droid.exchange.models;

/**
 * Created by kavi707 on 11/28/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class EmailData {

    private String emailAdd;
    private String subject;
    private String body;

    public String getEmailAdd() {
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd) {
        this.emailAdd = emailAdd;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
