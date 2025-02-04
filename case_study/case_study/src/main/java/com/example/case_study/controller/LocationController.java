package com.example.case_study.controller;

import com.example.case_study.model.Location;
import com.example.case_study.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping("/provinces")
    public Map<String, Location> getProvinces() {
        return locationService.getLocations();
    }
}
