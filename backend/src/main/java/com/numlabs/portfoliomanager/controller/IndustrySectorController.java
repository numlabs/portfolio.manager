package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.IndustrySector;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.service.IndustrySectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class IndustrySectorController {

    @Autowired
    private IndustrySectorService industrySectorService;

    @GetMapping("industrysector/all")
    public ResponseEntity<List<IndustrySector>> getIndustries() {
        List<IndustrySector> industries = industrySectorService.findAll();

        if(industries != null) {
            industries = industries.stream().sorted(Comparator.comparing(IndustrySector::getName)).collect(Collectors.toList());
        }

        return new ResponseEntity<>(industries, HttpStatus.OK);
    }
}
