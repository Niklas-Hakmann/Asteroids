package dk.sdu.cbse.scoreservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dk.sdu.cbse.common.services.IPointService;
import dk.sdu.cbse.scoreclient.ScoreServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScoreServiceClientIntegrationTest {

    @LocalServerPort
    private int port;

    private IPointService client;

    @BeforeEach
    void setUp() {
        client = new ScoreServiceClient(new RestTemplate(), "http://localhost:" + port + "/score");
        client.reset();
    }

    @Test
    void freshServiceReportsZero() {
        assertEquals(0, client.getScore());
    }

    @Test
    void addPointPersistsOnService() {
        client.addPoint();
        client.addPoint();
        assertEquals(2, client.getScore());
    }

    @Test
    void deductPointPersistsOnService() {
        client.addPoint();
        client.addPoint();
        client.addPoint();
        client.deductPoint();
        assertEquals(2, client.getScore());
    }

    @Test
    void deductIsFlooredAtZeroOnService() {
        client.deductPoint();
        assertEquals(0, client.getScore());
    }

    @Test
    void resetClearsScoreOnService() {
        client.addPoint();
        client.addPoint();
        client.reset();
        assertEquals(0, client.getScore());
    }

    @Test
    void independentClientSeesSharedServerState() {
        client.addPoint();
        client.addPoint();

        IPointService other = new ScoreServiceClient(new RestTemplate(), "http://localhost:" + port + "/score");
        assertEquals(2, other.getScore());
    }
}
