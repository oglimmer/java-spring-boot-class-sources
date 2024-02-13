package com.oglimmer.kniffel;

import com.oglimmer.kniffel.service.GameService;
import com.oglimmer.kniffel.web.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GameEndpointIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GameService gameService;

    @Test
    void game_create_ok() {
        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setPlayerNames(new String[]{"oli", "ilo"});
        ResponseEntity<GameResponse> gameResponse = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/game/", createGameRequest, GameResponse.class);
        assertThat(gameResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(gameResponse.getBody()).isNotNull();
        assertThat(gameResponse.getBody().getGameId()).isNotBlank();
        String gameId = gameResponse.getBody().getGameId();
        assertThat(gameId).isEqualTo(gameService.getGameInfo(gameId).getGameId());
    }

    @Test
    void game_create_failDuplicate() {
        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setPlayerNames(new String[]{"oli", "oli"});

        ResponseEntity<GameResponse> gameResponse = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/game/", createGameRequest, GameResponse.class);

        assertThat(gameResponse.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void game_create_failOnly1Name() {
        CreateGameRequest createGameRequest = new CreateGameRequest();
        createGameRequest.setPlayerNames(new String[]{"oli"});

        ResponseEntity<GameResponse> gameResponse = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/game/", createGameRequest, GameResponse.class);

        assertThat(gameResponse.getStatusCode().is4xxClientError()).isTrue();
    }
}