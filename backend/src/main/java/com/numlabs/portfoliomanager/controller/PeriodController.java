package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PeriodController {

    @Autowired
    private PeriodService periodService;

    @RequestMapping("period/{id}")
    public ResponseEntity<Period> getPeriodById(@PathVariable("id") Long periodId) {
        Period period = periodService.getPeriodById(periodId);
        return new ResponseEntity<>(period, HttpStatus.OK);
    }

    @RequestMapping("periods/company/{id}")
    public ResponseEntity<List<Period>> getPeriodsOfCompany(@PathVariable("id") Long companyId) {
        List<Period> periods = periodService.findPeriodsOfCompany(new Company(companyId));
        return new ResponseEntity<>(periods, HttpStatus.OK);
    }
}
