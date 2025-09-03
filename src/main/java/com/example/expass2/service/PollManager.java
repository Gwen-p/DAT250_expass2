package com.example.expass2.service;

import com.example.expass2.model.Poll;
import com.example.expass2.model.User;
import com.example.expass2.model.Vote;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class PollManager {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Poll> polls = new HashMap<>();
    private final Map<Long, Vote> votes = new HashMap<>();


    public User addUser(User user) {
        users.put(user.getEmail(), user);
        return user;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUser(String id) {
        return users.get(id);
    }

    public void deleteUser(String id) {
        users.remove(id);
    }

    //---------------------------------------------------


    public Poll addPoll(Poll poll) {
        polls.put(poll.getQuestion(), poll);
        return poll;
    }

    public List<Poll> getPolls() {
        return new ArrayList<>(polls.values());
    }

    public Poll getPoll(String id) {
        return polls.get(id);
    }

    public void deletePoll(String id) {
        polls.remove(id);
    }

    //-----------------------------------------------------

    public Vote addVote(Vote vote) {
        vote.setPublishedAt(Instant.now());
        votes.put(vote.getId(), vote);
        return vote;
    }

    public List<Vote> getVotes() {
        return  new ArrayList<>(votes.values());
    }

    public Vote getVote(Long id) {
        return votes.get(id);
    }

    public void deleteVote(Long id) {
        votes.remove(id);
    }

}
