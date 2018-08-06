package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.IndustrySector;
import com.numlabs.portfoliomanager.service.IndustrySectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndustrySectorController {

    @Autowired
    private IndustrySectorService industrySectorService;

    @GetMapping("industrysector/all")
    public ResponseEntity<List<IndustrySector>> getIndustries() {
        return new ResponseEntity<List<IndustrySector>>(industrySectorService.findAll(), HttpStatus.OK);
    }
}
