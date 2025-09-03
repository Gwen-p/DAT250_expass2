package com.example.expass2.model;

import java.time.Instant;
import java.util.Set;

public class Poll {
    private User creator;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private Set<VoteOption> options ;

    public Poll(User creator, String question, Instant publishedAt, Instant validUntil, Set<VoteOption> options) {
        this.creator = creator;
        this.question = question;
        this.publishedAt = publishedAt;
        this.validUntil = validUntil;
        this.options = options;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public Set<VoteOption> getOptions() {
        return options;
    }

    public void setOptions(Set<VoteOption> options) {
        this.options = options;
    }
}
