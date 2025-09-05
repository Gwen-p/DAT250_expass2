package com.example.expass2.controller;

import com.example.expass2.model.Vote;
import com.example.expass2.service.PollManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private final PollManager pollManager;

    public VoteController(PollManager _pollManager) {
        this.pollManager = _pollManager;
    }

    // Create an anonymous vote
    @PostMapping("/{pollId}/{optionId}")
    public Vote createVote(@PathVariable int optionId,@PathVariable int pollId ) {
        if (pollManager.getPoll(pollId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        if (this.pollManager.getPoll(pollId).getOption(optionId) != null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VoteOption not found");
        }
        Vote vote = pollManager.addVote(optionId, pollId, null);
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote after the deadline");
        }
        return vote;
    }

    // Create a vote
    @PostMapping("/{pollId}/{optionId}/{userId}")
    public Vote createUserVote(@PathVariable int optionId,@PathVariable String userId,@PathVariable Integer pollId) {
        if (pollManager.getUser(userId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (pollManager.getPoll(pollId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        if (this.pollManager.getPoll(pollId).getOption(optionId) != null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VoteOption not found");
        }
        Vote vote = pollManager.addVote(optionId, pollId, userId);
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote after the deadline");
        }
        return vote;
    }

    // Change the voteOption of the vote
    @PutMapping("{pollId}/{id}/{optionId}")
    public Vote updateVote(@PathVariable Integer pollId, @PathVariable Long id,@PathVariable Integer optionId) {
        if (pollManager.getPoll(pollId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found");
        }
        if (this.pollManager.getVote(pollId, id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found");
        }
        if (this.pollManager.getPoll(pollId).getOption(optionId) != null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "VoteOption not found");
        }
        if (this.pollManager.updateVote(pollId, id, optionId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote after the deadline");
        }
        return this.pollManager.getVote(pollId, id);
    }

    // Obtain the list of votes
    @GetMapping("/{pollId}")
    public Collection<Vote> getVotes(@PathVariable Integer pollId) {
        return pollManager.getVotes(pollId);
    }

    // Obtain a vote by id
    @GetMapping("/{pollId}/{id}")
    public Vote getPoll(@PathVariable Integer pollId, @PathVariable Long id) {
        return pollManager.getVote(pollId,id);
    }

}
