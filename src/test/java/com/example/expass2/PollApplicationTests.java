package com.example.expass2;
import com.example.expass2.model.User;
import com.example.expass2.model.Vote;
import com.example.expass2.model.VoteOption;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PollApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test1() {
        //### 1 Create a new user
        User alice = createUser("alice", "alice@example.com");
        assertEquals("alice", alice.getUsername());

        //### 2 List all users (-> shows the newly created user)
        User[] users1 = listUsers();
        assertEquals(1, users1.length);

        //### 3 Create another user
        User marco = createUser("marco", "marco@example.com");
        assertEquals("marco", marco.getUsername());

        // 4 List all users again (-> shows two users)
        User[] users2 = listUsers();
        assertEquals(2, users2.length);

        //### 5 User 1 creates a new poll
        createPoll("alice", "What is your favourite animal?");

        //### 6 List polls (-> shows the new poll)
        String[] polls = listPolls();
        assertEquals(1, polls.length);

        //### 7 User 2 votes on the poll
        Vote voteMarco = castVote(1, 1, "marco");
        assertNotNull(voteMarco);
        assertEquals("marco", voteMarco.getUser().getUsername());

        //### 8 Anonymous user votes on the poll
        Vote voteAnon = castVote(1, 2, null);
        assertNotNull(voteAnon);
        assertNull(voteAnon.getUser());

        //### 9 User 2 changes his vote
        Vote updatedVote = updateVote(1, voteMarco.getId(), 2);
        assertEquals(2, updatedVote.getVoteOption().getPresentationOrder());

        //### 10 List votes from a poll(-> shows the most recent vote
        Vote[] votes = listVotes(1);
        boolean foundMarco = false;
        for (Vote v : votes) {
            if (v.getUser() != null && "marco".equals(v.getUser().getUsername())) {
                assertEquals(2, v.getVoteOption().getPresentationOrder());
                foundMarco = true;
            }
        }
        assertTrue(foundMarco);

        //### 11 Delete the poll by id
        deletePoll(1);

        //### 12 List votes from a poll(-> not found)
        ResponseEntity<Vote[]> response = restTemplate.getForEntity("/votes/1", Vote[].class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void test2() {
        User bob = createUser("bob", "bob@example.com");
        User carla = createUser("carla", "carla@example.com");

        assertEquals("bob", bob.getUsername());
        assertEquals("carla", carla.getUsername());

        createPoll("bob", "What is your favourite sport?");

        // Insertar nuevas opciones dinámicamente
        VoteOption basketball = new VoteOption("Basketball", 3);
        addVoteOption(1, basketball);

        VoteOption tennis = new VoteOption("Tennis", 4);
        addVoteOption(1, tennis);

        Set<VoteOption> options = listVoteOptions(1);
        assertEquals(4, options.size());

        // Carla vota por Tennis (opción 4)
        Vote voteCarla = castVote(1, 4, "carla");
        assertNotNull(voteCarla);
        assertEquals("carla", voteCarla.getUser().getUsername());
        assertEquals(4, voteCarla.getVoteOption().getPresentationOrder());

        // Bob vota por Basketball (opción 3)
        Vote voteBob = castVote(1, 3, "bob");
        assertNotNull(voteBob);
        assertEquals("bob", voteBob.getUser().getUsername());
        assertEquals(3, voteBob.getVoteOption().getPresentationOrder());

        // Listar votos -> debe incluir ambos usuarios
        Vote[] votes = listVotes(1);
        boolean foundBob = false;
        boolean foundCarla = false;
        for (Vote v : votes) {
            if (v.getUser() != null) {
                if (v.getUser().getUsername().equals("bob")) foundBob = true;
                if (v.getUser().getUsername().equals("carla")) foundCarla = true;
            }
        }
        assertTrue(foundBob);
        assertTrue(foundCarla);
    }

    private User createUser(String username, String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = String.format("{\"username\":\"%s\",\"email\":\"%s\"}", username, email);
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<User> response = restTemplate.postForEntity("/users", request, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    private User[] listUsers() {
        ResponseEntity<User[]> response = restTemplate.getForEntity("/users", User[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
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

    private String[] listPolls() {
        ResponseEntity<String[]> response = restTemplate.getForEntity("/polls", String[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
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

    private void addVoteOption(int pollId, VoteOption option) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VoteOption> request = new HttpEntity<>(option, headers);
        ResponseEntity<VoteOption> response = restTemplate.postForEntity("/polls/" + pollId + "/options", request, VoteOption.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Set<VoteOption> listVoteOptions(int pollId) {
        ResponseEntity<Set> response = restTemplate.getForEntity("/polls/" + pollId + "/options", Set.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }
}
