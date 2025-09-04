package com.example.expass2.model;

import com.fasterxml.jackson.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Poll {
    @JsonIdentityReference(alwaysAsId = true)
    private User creator;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private Set<VoteOption> options;
    private int id = 0;
    private boolean isPrivate = false;
    private Map<Long, Vote> votesMap = new LinkedHashMap<>();

    public Poll(String question, Instant validUntil, Set<VoteOption> options) {
        this.question = question;
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

    public void addVoteOption(VoteOption option) {
        option.setPresentationOrder(options.size() + 1);
        this.options.add(option);
    }

    public void setId(int pollIds) {
        this.id = pollIds;
    }

    public int getId() {
        return id;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public int getNumVotes() {
        return votesMap.size();
    }

    public void addVote(Vote vote) {
        votesMap.put(vote.getId(), vote);
    }

    public void deleteVote(Long voteId) {
        votesMap.remove(voteId);
    }

    public VoteOption getOption(int optionId) {
        return options.stream().filter(op -> op.getPresentationOrder() == optionId).findFirst().orElse(null);
    }

}