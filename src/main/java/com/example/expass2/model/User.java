package com.example.expass2.model;


import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "username")
public class User {
    private String username;
    private String email;
    //
    private List<Poll> createdPolls;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.createdPolls =  new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Poll> getCreatedPolls() {
        return createdPolls;
    }

    public void setCreatedPolls(List<Poll> createdPolls) {
        this.createdPolls = createdPolls;
    }

    public void addCreatedPoll(Poll poll) {
        if (this.createdPolls == null) {
            this.createdPolls = new ArrayList<>();
        }
        this.createdPolls.add(poll);
    }
}
