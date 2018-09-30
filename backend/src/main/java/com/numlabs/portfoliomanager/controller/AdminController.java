package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.service.PeriodService;
import com.numlabs.portfoliomanager.util.CompanyUtil;
import com.numlabs.portfoliomanager.util.IsYatirimParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AdminController {

    @Autowired
    private PeriodService periodService;

    @Autowired
    private CompanyUtil companyUtil;

    @Autowired
    private IsYatirimParser isYatirimParser;

    @RequestMapping("admin/period/margins/calculate")
    public ResponseEntity<String> calculatePeriodMargins() {
        periodService.calculatePeriodMargins();
        return new ResponseEntity<>("Operation completed successfully.", HttpStatus.OK);
    }

    @RequestMapping("companies/update/prices")
    public ResponseEntity<String> updateCompanyPrices() {
        try {
            String response = isYatirimParser.updateBISTPrices();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error updating prices.", HttpStatus.OK);
        }
    }

}
