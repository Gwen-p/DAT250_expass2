package com.example.expass2;

import com.example.expass2.model.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PollApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private static String user1Id = "alice";
    private static String user2Id = "marco";
    private static Integer pollId;
    private static Long voteIdMarco;

    @Test
    @Order(1)
    void createUser1() {
        User user = new User(user1Id, "alice@example.com");
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(user1Id);
    }

    @Test
    @Order(2)
    void listAllUsersAfterFirst() {
        ResponseEntity<List> response = restTemplate.getForEntity("/users", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        // Check that the first user is in the list
        Map<String, Object> firstUser = (Map<String, Object>) response.getBody().get(0);
        assertThat(firstUser.get("username")).isEqualTo(user1Id);
    }

    @Test
    @Order(3)
    void createUser2() {
        User user = new User(user2Id, "marco@example.com");
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(user2Id);
    }

    @Test
    @Order(4)
    void listAllUsersAfterSecond() {
        ResponseEntity<List> response = restTemplate.getForEntity("/users", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @Order(5)
    void user1CreatesPoll() {
        Poll poll = new Poll();
        poll.setQuestion("What is your favourite animal?");
        poll.setValidUntil(Instant.parse("2025-09-10T10:00:00Z"));
        // Set options
        VoteOption option1 = new VoteOption("Giraffe", 1);
        VoteOption option2 = new VoteOption("Snake", 2);
        poll.setOptions(Set.of(option1, option2));

        ResponseEntity<Poll> response = restTemplate.postForEntity("/polls/" + user1Id, poll, Poll.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        pollId = response.getBody().getId();
    }

    @Test
    @Order(6)
    void listPolls() {
        ResponseEntity<List> response = restTemplate.getForEntity("/polls", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @Order(7)
    void user2Votes() {
        // POST /votes/{pollId}/{optionId}/{userId}
        ResponseEntity<Vote> response = restTemplate.postForEntity("/votes/" + pollId + "/1/" + user2Id, null, Vote.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        voteIdMarco = response.getBody().getId();
    }

    @Test
    @Order(8)
    void anonymousVotes() {
        ResponseEntity<Vote> response = restTemplate.postForEntity("/votes/" + pollId + "/1", null, Vote.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(9)
    void user2ChangesVote() {
        // PUT /votes/{pollId}/{id}/{optionId}
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.PUT, URI.create("/votes/" + pollId + "/" + voteIdMarco + "/2"));
        ResponseEntity<Vote> response = restTemplate.exchange(request, Vote.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getVoteOption().getCaption()).isEqualTo("Snake");
    }

    @Test
    @Order(10)
    void listVotes() {
        ResponseEntity<List> response = restTemplate.getForEntity("/votes/" + pollId, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @Order(11)
    void deletePoll() {
        restTemplate.delete("/polls/" + pollId);
    }

    @Test
    @Order(12)
    void listVotesAfterDelete() {
        ResponseEntity<String> response = restTemplate.getForEntity("/votes/" + pollId, String.class);
        // Since the poll is deleted, we expect NOT_FOUND
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}