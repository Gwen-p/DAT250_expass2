package com.example.expass2.model;

import java.time.Instant;

public class Vote {
    private String id;
    private Instant publishedAt;
    private VoteOption voteOption;
    private User user;

    public Vote(Instant publishedAt, VoteOption voteOption, User user) {
        this.publishedAt = publishedAt;
        this.voteOption = voteOption;
        this.user = user;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
