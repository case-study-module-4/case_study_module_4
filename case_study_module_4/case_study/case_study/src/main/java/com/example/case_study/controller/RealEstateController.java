package com.example.case_study.controller;

import com.example.case_study.model.RealEstate;
import com.example.case_study.service.IRealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/real-estates")
public class RealEstateController {

    @Autowired
    private IRealEstateService realEstateService;

    @GetMapping
    public ResponseEntity<List<RealEstate>> getAllRealEstates() {
        return ResponseEntity.ok(realEstateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RealEstate> getRealEstateById(@PathVariable Integer id) {
        Optional<RealEstate> realEstate = realEstateService.findById(id);
        return realEstate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RealEstate> createRealEstate(@RequestBody RealEstate realEstate) {
        return ResponseEntity.ok(realEstateService.save(realEstate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RealEstate> updateRealEstate(@PathVariable Integer id, @RequestBody RealEstate realEstate) {
        if (realEstateService.findById(id).isPresent()) {
            realEstate.setId(id);
            return ResponseEntity.ok(realEstateService.save(realEstate));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRealEstate(@PathVariable Integer id) {
        if (realEstateService.findById(id).isPresent()) {
            realEstateService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
