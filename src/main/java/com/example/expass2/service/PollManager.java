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
    private final Map<String, Vote> votes = new HashMap<>();

    private int pollIds =0;
    private int votesIds =0;

//USER---------------------------------------------------

    public User addUser(User user) {
        users.put(user.getUsername(), user);
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

//POLL---------------------------------------------------


    public Poll addPoll(Poll poll) {
        poll.setId(""+pollIds);
        polls.put(poll.getId(), poll);
        pollIds += 1;
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

//VOTE---------------------------------------------------

    // add a new vote. If it's not anonymous, the user can only have one vote, the last one user did
    public Vote addVote(Vote vote, String pollId) {
        polls.get(pollId).addVotes();
        vote.setId((long) votesIds);
        votesIds++;
        vote.setPublishedAt(Instant.now());
        if(vote.getUser()!=null) {
            votes.put(vote.getUser().getUsername()+pollId, vote);
        }else {
            votes.put(vote.getId().toString(), vote);
        }
        return vote;
    }

    public List<Vote> getVotes() {
        return  new ArrayList<>(votes.values());
    }

    public Vote getVote(String id) {
        return votes.get(id);
    }

    public void deleteVote(String id) {
        polls.get(id).deleteVotes();
        votes.remove(id);
    }

}
