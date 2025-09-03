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

    // Create a vote
    @PostMapping
    public Vote createPoll(@RequestBody Vote vote) {
        return pollManager.addVote(vote);
    }

    // Change the voteOption of the vote
    @PutMapping("/{id}")
    public VoteOption updateVote(@PathVariable Long id, @RequestBody VoteOption voteOption) {
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
    public Vote getPoll(@PathVariable Long id) {
        return pollManager.getVote(id);
    }

    // Delete a vote
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pollManager.deleteVote(id);
    }
}
