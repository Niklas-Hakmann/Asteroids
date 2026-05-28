package dk.sdu.cbse.scoreclient;

import dk.sdu.cbse.common.services.IPointService;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ScoreServiceClient implements IPointService {

    private static final String DEFAULT_BASE_URL = "http://localhost:8080/score";

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private int lastKnownScore = 0;
    private boolean warned = false;

    public ScoreServiceClient() {
        this(new RestTemplate(), resolveBaseUrl());
    }

    public ScoreServiceClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    private static String resolveBaseUrl() {
        String configured = System.getProperty("score.service.url");
        return configured != null ? configured : DEFAULT_BASE_URL;
    }

    @Override
    public int getScore() {
        try {
            lastKnownScore = parse(restTemplate.getForObject(baseUrl, String.class));
        } catch (RestClientException | NumberFormatException e) {
            warnOnce(e);
        }
        return lastKnownScore;
    }

    @Override
    public void addPoint() {
        try {
            lastKnownScore = parse(restTemplate.postForObject(baseUrl + "/add", null, String.class));
        } catch (RestClientException | NumberFormatException e) {
            warnOnce(e);
            lastKnownScore++;
        }
    }

    @Override
    public void deductPoint() {
        try {
            lastKnownScore = parse(restTemplate.postForObject(baseUrl + "/deduct", null, String.class));
        } catch (RestClientException | NumberFormatException e) {
            warnOnce(e);
            if (lastKnownScore > 0) {
                lastKnownScore--;
            }
        }
    }

    @Override
    public void reset() {
        try {
            lastKnownScore = parse(restTemplate.postForObject(baseUrl + "/reset", null, String.class));
        } catch (RestClientException | NumberFormatException e) {
            warnOnce(e);
            lastKnownScore = 0;
        }
    }

    private int parse(String body) {
        return Integer.parseInt(body.trim());
    }

    private void warnOnce(Exception e) {
        if (!warned) {
            warned = true;
            System.err.println("ScoreService unreachable at " + baseUrl
                    + " - falling back to local score. Cause: " + e.getMessage());
        }
    }
}
