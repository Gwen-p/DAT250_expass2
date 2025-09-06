package com.example.expass2;

import com.example.expass2.model.Vote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PollApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateUserAlice() {
        createUser("alice", "alice@example.com");
    }

    @Test
    public void testCreateUserMarco() {
        createUser("marco", "marco@example.com");
    }

    @Test
    public void testCreatePollByAlice() {
        createUser("alice", "alice@example.com");
        createPoll("alice", "What is your favourite animal?");
    }

    @Test
    public void testUserMarcoVotes() {
        createUser("alice", "alice@example.com");
        createUser("marco", "marco@example.com");
        createPoll("alice", "What is your favourite animal?");
        Vote voteMarco = castVote(1, 1, "marco");

        assertNotNull(voteMarco);
        assertEquals(1, voteMarco.getVoteOption().getPresentationOrder());
        assertEquals("marco", voteMarco.getUser().getUsername());
    }

    @Test
    public void testAnonymousVote() {
        createUser("alice", "alice@example.com");
        createPoll("alice", "What is your favourite animal?");
        Vote voteAnon = castVote(1, 1, null);

        assertNotNull(voteAnon);
        assertEquals(1, voteAnon.getVoteOption().getPresentationOrder());
        assertNull(voteAnon.getUser());
    }

    @Test
    public void testMarcoChangesVote() {
        createUser("alice", "alice@example.com");
        createUser("marco", "marco@example.com");
        createPoll("alice", "What is your favourite animal?");
        Vote voteMarco = castVote(1, 1, "marco");
        Vote updatedVote = updateVote(1, voteMarco.getId(), 2);

        assertNotNull(updatedVote);
        assertEquals(2, updatedVote.getVoteOption().getPresentationOrder());
    }

    @Test
    public void testListVotesIncludesMarco() {
        createUser("alice", "alice@example.com");
        createUser("marco", "marco@example.com");
        createPoll("alice", "What is your favourite animal?");
        Vote voteMarco = castVote(1, 1, "marco");
        updateVote(1, voteMarco.getId(), 2);

        Vote[] votes = listVotes(1);
        boolean found = false;
        for (Vote v : votes) {
            if (v.getUser() != null && "marco".equals(v.getUser().getUsername())) {
                assertEquals(2, v.getVoteOption().getPresentationOrder());
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testDeletePollAndCheckVotes() {
        createUser("alice", "alice@example.com");
        createPoll("alice", "What is your favourite animal?");
        deletePoll(1);

        ResponseEntity<Vote[]> response = restTemplate.getForEntity("/votes/1", Vote[].class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

// ------------------- Métodos auxiliares -------------------
    private void createUser(String username, String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = String.format("{\"username\":\"%s\",\"email\":\"%s\"}", username, email);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/users", request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private void createPoll(String username, String question) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = """
            {
              "question": "%s",
              "validUntil": "2025-09-10T10:00:00Z",
              "options": [
                {"caption": "Giraffe", "presentationOrder": 1},
                {"caption": "Snake", "presentationOrder": 2}
              ]
            }
            """.formatted(question);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/polls/" + username, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Vote castVote(int pollId, int optionId, String username) {
        String url = username != null ? "/votes/" + pollId + "/" + optionId + "/" + username : "/votes/" + pollId + "/" + optionId;
        ResponseEntity<Vote> response = restTemplate.postForEntity(url, null, Vote.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    private Vote updateVote(int pollId, Long voteId, int optionId) {
        String url = "/votes/" + pollId + "/" + voteId + "/" + optionId;
        ResponseEntity<Vote> response = restTemplate.exchange(url, HttpMethod.PUT, null, Vote.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    private Vote[] listVotes(int pollId) {
        ResponseEntity<Vote[]> response = restTemplate.getForEntity("/votes/" + pollId, Vote[].class);
        if (response.getStatusCode() == HttpStatus.OK) return response.getBody();
        return new Vote[0];
    }

    private void deletePoll(int pollId) {
        ResponseEntity<Void> response = restTemplate.exchange("/polls/" + pollId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
