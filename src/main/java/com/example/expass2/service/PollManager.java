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
    private final Map<Integer, Poll> polls = new HashMap<>();
    private int pollIds =1;


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

    public Poll addPoll(Poll poll, String userId) {
        poll.setId(pollIds);
        poll.setPublishedAt(Instant.now());
        poll.setCreator(users.get(userId));
        users.get(userId).addCreatedPoll(poll);
        polls.put(poll.getId(), poll);
        pollIds += 1;
        return poll;
    }

    public List<Poll> getPolls() {
        return new ArrayList<>(polls.values());
    }

    public Poll getPoll(Integer id) {
        return polls.get(id);
    }

    public void deletePoll(Integer id) {
        polls.get(id).getCreator().deletePoll(polls.get(id));
        polls.remove(id);
    }

//VOTE---------------------------------------------------

    // add a new vote. If it's not anonymous, the user can only have one vote, the last one user did
    public Vote addVote(int optionId, Integer pollId, String userId) {
        Vote vote = null;
        if (polls.get(pollId).getValidUntil().isAfter(Instant.now())){
            if(userId!=null) {
                vote =  polls.get(pollId).addVote(optionId, users.get(userId));
            }else{
                vote = polls.get(pollId).addVote(optionId);
            }
        }
        return vote;
    }

    public Vote updateVote( Integer pollId, Long voteId, Integer optionId) {
        if (polls.get(pollId).getValidUntil().isAfter(Instant.now())){
            return polls.get(pollId).updateVote(voteId, optionId);
        }
        return null;
    }

    public List<Vote> getVotes(Integer pollId) {
        return  new ArrayList<>(polls.get(pollId).getVotes().values());
    }

    public Vote getVote(Integer pollId, Long id) {
        return polls.get(pollId).getVote(id);
    }

}
