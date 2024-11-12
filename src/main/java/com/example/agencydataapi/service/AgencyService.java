package com.example.agencydataapi.service;
import com.example.agencydataapi.model.AgencyRefData;
import com.example.agencydataapi.util.AgencyUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.agencydataapi.util.AgencyUtils.writeAgencyRefData;

@Service
public class AgencyService {
    @Value("${agency-api.fetch-url}")
    private String fetchUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();
    public List<AgencyRefData> fetchAndTransformAgencyData() {
        String response = restTemplate.getForObject(fetchUrl,
                String.class);
        try {
            List<Map<String, Object>> agencies =
                    objectMapper.readValue(response, new TypeReference<List<Map<String,
                            Object>>>() {});
            return
                    agencies.stream().map(this::mapToAgencyRefData).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching or processing agency data", e);
        }
    }
    private AgencyRefData mapToAgencyRefData(Map<String, Object>
                                                     agencyMap) {
        AgencyRefData refData = new AgencyRefData();
        refData.setName((String) agencyMap.get("name"));
        refData.setCode((String) agencyMap.get("code"));
        return refData;
    }
    public void saveAgencyRefData(List<AgencyRefData>
                                          agencyRefDataList)
    {
        writeAgencyRefData(agencyRefDataList);
    }
        }
