package com.example.expass2.controller;

import com.example.expass2.model.Poll;
import com.example.expass2.model.User;
import com.example.expass2.model.VoteOption;
import com.example.expass2.service.PollManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @PostMapping("/{userId}")
    public Poll createPoll(@RequestBody Poll _poll, @PathVariable String userId) {
        _poll.setPublishedAt(Instant.now());
        Poll poll = pollManager.addPoll(_poll, userId);
        if (poll == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User no encontrado");
        }
        return poll;
    }

    // Create a vote option of a poll
    @PutMapping("/{idPoll}/option")
    public void addVoteOptions(@RequestBody VoteOption option, @PathVariable Integer idPoll) {
        Poll poll = pollManager.getPoll(idPoll);
        if (poll == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Encuesta no encontrada");
        }
        poll.addVoteOption(option);
    }

    // Obtain the list of polls
    @GetMapping
    public Collection<Poll> getAllPolls() {
        return pollManager.getPolls();
    }

    // Obtain a poll by id---------------------------------------- TODO REVISAR
    @GetMapping("/{id}")
    public Poll getPoll(@PathVariable Integer id) {
        Poll poll = pollManager.getPoll(id);
        if (poll == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Encuesta no encontrada");
        }
        return poll;
    }


    // Delete a poll
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        pollManager.deletePoll(id);
    }
}
