package com.example.expass2.model;

import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class User {
    private String username;
    private String email;

    private List<Poll> createdPolls;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.createdPolls = Lists.newArrayList();
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
}
