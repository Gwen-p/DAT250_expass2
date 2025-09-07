package com.example.expass2.controller;

import com.example.expass2.model.Poll;
import com.example.expass2.model.VoteOption;
import com.example.expass2.service.PollManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
@RequestMapping("/polls")
public class PollController {

    private final PollManager pollManager;

    public PollController(PollManager _pollManager) {
        this.pollManager = _pollManager;
    }

    // Create a poll
    @PostMapping("/{userId}")
    public Poll createPoll(@RequestBody Poll _poll, @PathVariable String userId) {
        if (pollManager.getUser(userId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return pollManager.addPoll(_poll, userId);
    }

    // Create a vote option of a poll
    @PostMapping("/{idPoll}/option")
    public void addVoteOptions(@RequestBody VoteOption option, @PathVariable Integer idPoll) {
        Poll poll = pollManager.getPoll(idPoll);
        if (poll == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        poll.addVoteOption(option);
    }

    // Obtain the list of polls
    @GetMapping
    public Collection<Poll> getAllPolls() {
        return pollManager.getPolls();
    }

    // Obtain a poll by id
    @GetMapping("/{id}")
    public Poll getPoll(@PathVariable Integer id) {
        Poll poll = pollManager.getPoll(id);
        if (poll == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        return poll;
    }

    // List all options of a poll
    @GetMapping("/{idPoll}/options")
    public Collection<VoteOption> getVoteOptions(@PathVariable Integer idPoll) {
        Poll poll = pollManager.getPoll(idPoll);
        if (poll == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        return poll.getOptions();
    }

    // Delete a poll
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        if (pollManager.getPoll(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        pollManager.deletePoll(id);
    }
}
