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
    private final Map<Long, Vote> votes = new HashMap<>();

    private int pollIds =0;
    private Long votesIds = 0L;

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


    public Poll addPoll(Poll _poll, String userId) {
        Poll poll = _poll;
        if (users.containsKey(userId)) {
            poll.setId(pollIds);
            poll.setCreator(users.get(userId));
            users.get(userId).addCreatedPoll(poll);
            polls.put(poll.getId(), poll);
            pollIds += 1;
        }else {
            poll = null;
        }

        return poll;
    }

    public List<Poll> getPolls() {
        return new ArrayList<>(polls.values());
    }

    public Poll getPoll(Integer id) {
        return polls.get(id);
    }

    public void deletePoll(String id) {
        polls.remove(id);
    }

//VOTE---------------------------------------------------

    // add a new vote. If it's not anonymous, the user can only have one vote, the last one user did
    public Vote addVote(int optionId, Integer pollId, String userId) {
        Vote vote = null;
        if (polls.get(pollId).getValidUntil().isBefore(Instant.now())) {
            vote = new Vote(polls.get(pollId).getOption(optionId));

            vote.setId(votesIds);
            votesIds++;
            vote.setPublishedAt(Instant.now());
            if(userId!=null) {
                vote.setUser(users.get(userId));
                votes.put(vote.getId(), vote);
            }
            polls.get(pollId).addVote(vote);
        }

        return vote;
    }

    public List<Vote> getVotes() {
        return  new ArrayList<>(votes.values());
    }

    public Vote getVote(Long id) {
        return votes.get(id);
    }

}
