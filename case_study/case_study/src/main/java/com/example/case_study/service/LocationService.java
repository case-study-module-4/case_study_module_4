package com.example.case_study.service;

import com.example.case_study.model.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class LocationService {
    private Map<String, Location> locations;

    public LocationService() {
        loadLocations();
    }

    // Đọc dữ liệu từ JSON
    private void loadLocations() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File("src/main/resources/static/data/tinh_tp.json");
            locations = mapper.readValue(file, mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Location.class));
        } catch (IOException e) {
            e.printStackTrace();
            locations = new HashMap<>();
        }
    }

    // Lấy danh sách tỉnh/thành phố
    public Map<String, Location> getLocations() {
        return locations;
    }
}
