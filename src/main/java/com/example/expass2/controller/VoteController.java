package com.example.expass2.controller;

import com.example.expass2.model.Vote;
import com.example.expass2.model.VoteOption;
import com.example.expass2.service.PollManager;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/Votes")
public class VoteController {

    private final PollManager pollManager;

    public VoteController(PollManager _pollManager) {
        this.pollManager = _pollManager;
    }

    // Create an anonymous vote
    @PostMapping("/POLL{pollId}")
    public Vote createVote(@RequestBody Vote vote,@PathVariable String pollId ) {
        return pollManager.addVote(vote, pollId);
    }

    // Create a vote
    @PostMapping("/POLL{pollId}/USER{userId}")
    public Vote createUserVote(@RequestBody Vote vote,@PathVariable String userId,@PathVariable String pollId) {
        vote.setUser(pollManager.getUser(userId));
        return pollManager.addVote(vote, pollId);
    }

    // Change the voteOption of the vote
    @PutMapping("/{id}")
    public VoteOption updateVote(@PathVariable String id, @RequestBody VoteOption voteOption) {
        this.pollManager.getVote(id).setVoteOption(voteOption);
        return this.pollManager.getVote(id).getVoteOption();
    }

    // Obtain the list of votes
    @GetMapping
    public Collection<Vote> getAllVotes() {
        return pollManager.getVotes();
    }

    // Obtain a vote by id
    @GetMapping("/{id}")
    public Vote getPoll(@PathVariable String id) {
        return pollManager.getVote(id);
    }

    // Delete a vote
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        pollManager.deleteVote(id);
    }
}
