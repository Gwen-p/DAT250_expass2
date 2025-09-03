package com.example.expass2.model;

import java.time.Instant;

public class Vote {
    private Long id;
    private Instant publishedAt;
    private VoteOption voteOption;
    private User user;

    public Vote( VoteOption voteOption, User user) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
