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
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PollApplicationTests2 {

    @Autowired
    private TestRestTemplate restTemplate;

    private static String user1Id = "bob";
    private static String user2Id = "carla";
    private static Integer pollId;

    @Test
    @Order(1)
    void createUser1() {
        // ### 1 Create a new user
        User user = new User(user1Id, "bob@example.com");
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(user1Id);
    }

    @Test
    @Order(2)
    void listUsersAfterFirst() {
        // ### 2 List all users (-> shows bob)
        ResponseEntity<List> response = restTemplate.getForEntity("/users", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @Order(3)
    void createUser2() {
        // ### 3 Create another user
        User user = new User(user2Id, "carla@example.com");
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUsername()).isEqualTo(user2Id);
    }

    @Test
    @Order(4)
    void listUsersAfterSecond() {
        // ### 4 List all users again (-> shows bob and carla)
        ResponseEntity<List> response = restTemplate.getForEntity("/users", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @Order(5)
    void user1CreatesPoll() {
        // ### 5 User 1 (bob) creates a new poll
        Poll poll = new Poll();
        poll.setQuestion("What is your favourite sport?");
        poll.setValidUntil(Instant.parse("2030-09-15T10:00:00Z"));
        VoteOption option1 = new VoteOption("Soccer", 1);
        VoteOption option2 = new VoteOption("Volleyball", 2);
        poll.setOptions(Set.of(option1, option2));

        ResponseEntity<Poll> response = restTemplate.postForEntity("/polls/" + user1Id, poll, Poll.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        pollId = response.getBody().getId();
    }

    @Test
    @Order(6)
    void listPolls() {
        // ### 6 List polls (-> shows the new poll)
        ResponseEntity<List> response = restTemplate.getForEntity("/polls", List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @Order(7)
    void addVoteOptionBasketball() {
        // ### 7 Add new vote option: Basketball
        VoteOption option = new VoteOption("Basketball", 3);
        ResponseEntity<VoteOption> response = restTemplate.postForEntity("/polls/" + pollId + "/option", option, VoteOption.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(8)
    void addVoteOptionTennis() {
        // ### 8 Add new vote option: Tennis
        VoteOption option = new VoteOption("Tennis", 4);
        ResponseEntity<VoteOption> response = restTemplate.postForEntity("/polls/" + pollId + "/option", option, VoteOption.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(9)
    void listAllVoteOptions() {
        // ### 9 List all options of the poll (-> should show 4 options)
        ResponseEntity<Set> response = restTemplate.getForEntity("/polls/" + pollId + "/options", Set.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(4);
    }

    @Test
    @Order(10)
    void user2VotesForTennis() {
        // ### 10 User 2 (carla) votes for tennis (option 4)
        ResponseEntity<Vote> response = restTemplate.postForEntity("/votes/" + pollId + "/4/" + user2Id, null, Vote.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(11)
    void user1VotesForBasketball() {
        // ### 11 User 1 (bob) votes for basketball (option 3)
        ResponseEntity<Vote> response = restTemplate.postForEntity("/votes/" + pollId + "/3/" + user1Id, null, Vote.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(12)
    void listVotes() {
        // ### 12 List votes from the poll (-> shows both votes)
        ResponseEntity<List> response = restTemplate.getForEntity("/votes/" + pollId, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }
}
