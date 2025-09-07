package com.example.expass2.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import java.time.Instant;

public class Vote {
    private Long id = 0L;
    private Instant publishedAt;
    private VoteOption voteOption;
    @JsonIdentityReference(alwaysAsId = true)
    private User user;

    public Vote() {}

    public Vote( VoteOption voteOption) {
        this.voteOption = voteOption;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
