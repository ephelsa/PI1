package com.citophonapp.integrador.citophonapp.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String callId;
    private String email;
    private String name;
    private Boolean vigilant;

    public User() {
    }

    public User(String callId, String email, String name, Boolean vigilant) {
        this.callId = callId;
        this.email = email;
        this.name = name;
        this.vigilant = vigilant;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVigilant() {
        return vigilant;
    }

    public void setVigilant(Boolean vigilant) {
        this.vigilant = vigilant;
    }
}
