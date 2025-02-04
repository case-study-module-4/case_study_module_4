package com.example.case_study.controller;

import com.example.case_study.model.RealEstateType;
import com.example.case_study.service.IRealEstateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/real-estate-types")
public class RealEstateTypeController {

    @Autowired
    private IRealEstateTypeService realEstateTypeService;

    @GetMapping
    public ResponseEntity<List<RealEstateType>> getAllRealEstateTypes() {
        return ResponseEntity.ok(realEstateTypeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RealEstateType> getRealEstateTypeById(@PathVariable Integer id) {
        Optional<RealEstateType> realEstateType = realEstateTypeService.findById(id);
        return realEstateType.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RealEstateType> createRealEstateType(@RequestBody RealEstateType realEstateType) {
        return ResponseEntity.ok(realEstateTypeService.save(realEstateType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RealEstateType> updateRealEstateType(@PathVariable Integer id, @RequestBody RealEstateType realEstateType) {
        if (realEstateTypeService.findById(id).isPresent()) {
            realEstateType.setId(id);
            return ResponseEntity.ok(realEstateTypeService.save(realEstateType));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRealEstateType(@PathVariable Integer id) {
        if (realEstateTypeService.findById(id).isPresent()) {
            realEstateTypeService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
