package io.ethertale.findadicethymeleaf.moderation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ModerationService {

    private final RestTemplate restTemplate;
    @Value("${openapi.api.key}")
    private String apiKey;

    private static final String MODERATION_URL = "https://api.openai.com/v1/moderations";

    @Autowired
    public ModerationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isFlagged(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, String> body = Map.of("input", text);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ModerationResponse response = restTemplate.postForEntity(
                MODERATION_URL, request, ModerationResponse.class
        ).getBody();

        return response != null
                && !response.results().isEmpty()
                && response.results().get(0).flagged();
    }

    record ModerationResponse(List<Result> results) {}
    record Result(boolean flagged, Map<String, Boolean> categories) {}
}
