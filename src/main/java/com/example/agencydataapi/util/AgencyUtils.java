package com.example.agencydataapi.util;
import com.example.agencydataapi.model.AgencyRefData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
public class AgencyUtils {
    private static final String FILE_PATH = "agencyRefData.json";
    private static final ObjectMapper objectMapper = new
            ObjectMapper();
    public static void writeAgencyRefData(List<AgencyRefData>
                                                  agencyRefDataList) {
        try {
            objectMapper.writeValue(new File(FILE_PATH),
                    agencyRefDataList);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to agencyRefData file", e);
        }
    }
}
