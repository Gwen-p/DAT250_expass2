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
    private final Set<VoteOption> options;
    private int id = 0;
    private boolean isPrivate = false;
    private final Map<Long, Vote> votesMap = new LinkedHashMap<>();

    public Poll(String question, Instant validUntil, Set<VoteOption> options, boolean isPrivate) {
        this.question = question;
        this.validUntil = validUntil;
        this.options = options;
        this.isPrivate = isPrivate;
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

    public void deleteVote(Long voteId) {
        votesMap.remove(voteId);
    }

    public VoteOption getOption(int optionId) {
        return options.stream().filter(op -> op.getPresentationOrder() == optionId).findFirst().orElse(null);
    }

    public Vote addVote(int optionId, User user, Long votesIds) {
        votesMap.put(votesIds, new Vote(getOption(optionId)));
        Vote vote = votesMap.get(votesIds);
        vote.setPublishedAt(Instant.now());
        vote.setUser(user);
        vote.setId(votesIds);
        return vote;
    }

    public Vote addVote(int optionId, long votesIds) {
        votesMap.put(votesIds, new Vote(getOption(optionId)));
        Vote vote = votesMap.get(votesIds);
        vote.setPublishedAt(Instant.now());
        vote.setId(votesIds);
        return vote;
    }

    public Vote getVote(Long id) {
        if (votesMap.containsKey(id)) {
            return votesMap.get(id);
        }
        return null;
    }

    public Map<Long, Vote> getVotes() {
        return votesMap;
    }
}