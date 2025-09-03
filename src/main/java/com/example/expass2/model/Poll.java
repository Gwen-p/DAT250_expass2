package com.example.expass2.model;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

public class Poll {
    private User creator;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private Set<VoteOption> options;
    private String id = "0";
    private int numVotes = 0;


    public Poll(User creator, String question, Instant validUntil) {
        this.creator = creator;
        this.question = question;
        this.validUntil = validUntil;
        setOptions(new LinkedHashSet<>());
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

    public void addVoteOption(VoteOption option) {
        option.setPresentationOrder(options.size() + 1);
        this.options.add(option);
    }

    public void setId(String pollIds) {
        id = pollIds;
    }

    public String getId() {
        return id;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public void addVotes() {
        this.numVotes++;
    }

    public void deleteVotes() {
        this.numVotes--;
    }
}