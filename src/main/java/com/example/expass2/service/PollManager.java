package com.example.expass2.service;

import com.example.expass2.model.Poll;
import com.example.expass2.model.User;
import com.example.expass2.model.Vote;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PollManager {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Poll> polls = new HashMap<>();
    private Map<Long, Vote> votes = new HashMap<>();


    public User addUser(User user) {
        users.put(user.getEmail(), user);
        return user;
    }

    public Poll addPoll(Poll poll) {
        polls.put(poll.getQuestion(), poll);
        return poll;
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

    public List<Poll> getPoll() {
        return new ArrayList<>(polls.values());
    }

    public Poll getPoll(String id) {
        return polls.get(id);
    }

    public void deletePoll(String id) {
        polls.remove(id);
    }
}
