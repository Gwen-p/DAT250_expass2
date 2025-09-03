package com.example.expass2.controller;

import com.example.expass2.model.Poll;
import com.example.expass2.model.User;
import com.example.expass2.model.VoteOption;
import com.example.expass2.service.PollManager;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;

@RestController
@RequestMapping("/polls")
public class PollController {

    private final PollManager pollManager;

    public PollController(PollManager _pollManager) {
        this.pollManager = _pollManager;
    }

    // Create a poll
    @PostMapping
    public Poll createPoll(@RequestBody Poll poll) {
        poll.setPublishedAt(Instant.now());
        return pollManager.addPoll(poll);
    }

    // Create a vote option of a poll
    @PutMapping("/{id}")
    public void addVoteOptions(@RequestBody VoteOption option, @PathVariable String id) {
        pollManager.getPoll(id).addVoteOption(option);
    }

    // Obtain the list of polls
    @GetMapping
    public Collection<Poll> getAllPolls() {
        return pollManager.getPolls();
    }

    // Obtain a poll by id
    @GetMapping("/{id}")
    public Poll getPoll(@PathVariable String id) {
        return pollManager.getPoll(id);
    }

    // Delete a user
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        pollManager.deletePoll(id);
    }
}
