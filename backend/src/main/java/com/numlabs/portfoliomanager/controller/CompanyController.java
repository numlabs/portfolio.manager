package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.util.CompanyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyUtil companyUtil;

    @RequestMapping("company/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        return new ResponseEntity<Company>(companyService.findCompany(id), HttpStatus.OK);
    }

    @RequestMapping("companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return new ResponseEntity<List<Company>>(companyService.findAllCompanies(), HttpStatus.OK);
    }

    @RequestMapping("companies/{exchange}")
    public ResponseEntity<List<Company>> getCompaniesOfExchange(@PathVariable String exchange) {
        return new ResponseEntity<List<Company>>(companyService.findAllCompanies(exchange), HttpStatus.OK);
    }

    @RequestMapping("companies/detailed/{exchange}")
    public ResponseEntity<List<Company>> getCompaniesDetailedOfExchange(@PathVariable String exchange) {
        List<Company> companies = companyService.findAllCompaniesDetailed(exchange);
        companies.forEach(e->e.getPeriods().forEach(z-> z.setCompany(null)));
        return new ResponseEntity<List<Company>>(companies, HttpStatus.OK);
    }

    @RequestMapping("companies/update/prices/{exchange}")
    public ResponseEntity<String> updateCompanyPrices(@PathVariable String exchange) {

        if(exchange.equals("bist")) {
            companyUtil.updateCompanyPricesOfBIST();
        }
        return new ResponseEntity<String>("", HttpStatus.OK);
    }
 }
