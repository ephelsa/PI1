package com.citophonapp.integrador.citophonapp.entity;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String callId;
    private String email;
    private String name;
    private Boolean vigilant;
    private String document;
    private String age;
    private String localNumber;
    private List<String> members;

    public User() {
    }

    public User(String callId, String email, String name, Boolean vigilant, String document, String age, List<String> members) {
        this.callId = callId;
        this.email = email;
        this.name = name;
        this.vigilant = vigilant;
        this.document = document;
        this.age = age;
        this.members = members;
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

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getLocalNumber() {
        return localNumber;
    }

    public void setLocalNumber(String localNumber) {
        this.localNumber = localNumber;
    }
}
