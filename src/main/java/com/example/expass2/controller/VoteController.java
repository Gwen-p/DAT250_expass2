package com.example.expass2.controller;

import com.example.expass2.model.Poll;
import com.example.expass2.model.Vote;
import com.example.expass2.model.VoteOption;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Encuesta no encontrada");
        }
        Vote vote = pollManager.addVote(optionId, pollId, null);
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voto fuera de plazo");
        }
        return vote;
    }

    // Create a vote
    @PostMapping("/{pollId}/{optionId}/{userId}")
    public Vote createUserVote(@PathVariable int optionId,@PathVariable String userId,@PathVariable Integer pollId) {
        if (pollManager.getUser(userId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrada");
        }
        if (pollManager.getPoll(pollId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Encuesta no encontrada");
        }
        Vote vote = pollManager.addVote(optionId, pollId, userId);
        if (vote == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voto fuera de plazo");
        }
        return vote;
    }

    // Change the voteOption of the vote // TODO really needed?
    @PutMapping("{pollId}/{id}")
    public VoteOption updateVote(@PathVariable Integer pollId, @PathVariable Long id, @RequestBody VoteOption voteOption) {
        if (this.pollManager.getVote(pollId, id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voto no encontrado");
        }
        if (this.pollManager.getVote(pollId, id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Voto fuera de plazo");
        }
        this.pollManager.getVote(pollId, id).setVoteOption(voteOption);
        return this.pollManager.getVote(pollId, id).getVoteOption();
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
