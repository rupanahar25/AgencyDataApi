package com.example.agencydataapi.service;
import com.example.agencydataapi.model.AgencyRefData;
import com.example.agencydataapi.util.AgencyUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class AgencyService {
    @Value("${agency-api.fetch-url}")
    private String fetchUrl;
    @Value("${agency-api.username}")
    private String username;
    @Value("${agency-api.password}")
    private String password;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    public List<AgencyRefData> fetchAndTransformAgencyData() {
// Create headers with Basic Authentication
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", createBasicAuthHeader(username,
                password));
        HttpEntity<String> entity = new HttpEntity<>(headers);
// Make the request to the AgencyAPI
        ResponseEntity<String> response = restTemplate.exchange(
                fetchUrl,
                HttpMethod.GET,
                entity,
                String.class
        );
// Process the response
        try {
            List<Map<String, Object>> agencies =
                    objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            return
                    agencies.stream().map(this::mapToAgencyRefData).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching or processing agency data", e);
        }
    }
    private String createBasicAuthHeader(String username, String
            password) {
        String auth = username + ":" + password;
        byte[] encodedAuth =
                Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }
    private AgencyRefData mapToAgencyRefData(Map<String, Object>
                                                     agencyMap) {
        AgencyRefData refData = new AgencyRefData();
        refData.setName((String) agencyMap.get("name"));
        refData.setCode((String) agencyMap.get("code"));
        return refData;
    }

    public void saveAgencyRefData(List<AgencyRefData> agencyRefDataList) {
        AgencyUtils.writeAgencyRefData(agencyRefDataList);
    }
}