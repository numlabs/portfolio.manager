package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.service.ExchangeService;
import com.numlabs.portfoliomanager.util.CompanyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;

@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private CompanyUtil companyUtil;

    @PostMapping("company/add")
    public ResponseEntity<String> addCompany(@RequestBody Company newCompany) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(newCompany.getTickerSymbol(), newCompany.getExchange());

        if(company != null) {
            return new ResponseEntity<String> (String.format("The company with the %s already exist.", newCompany.getTickerSymbol()), HttpStatus.EXPECTATION_FAILED);
        }

        companyService.persist(newCompany);

        return new ResponseEntity<String> (HttpStatus.OK);
    }

    @PostMapping("company/update")
    public ResponseEntity<String> updateCompany(@RequestBody Company updCompany) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(updCompany.getTickerSymbol(), updCompany.getExchange());

        if(company == null) {
            return new ResponseEntity<String> (String.format("The company with the %s does not exist.", updCompany.getTickerSymbol()), HttpStatus.EXPECTATION_FAILED);
        }

        companyService.update(updCompany);

        return new ResponseEntity<String> (HttpStatus.OK);
    }

    @RequestMapping("company/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        return new ResponseEntity<Company>(companyService.findCompany(id), HttpStatus.OK);
    }

    @RequestMapping("company/remove/{id}")
    public ResponseEntity<String> removeCompany(@PathVariable Long id) {
        companyService.remove(id);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @RequestMapping("company/search/{searchCriteria}")
    public ResponseEntity<Company> searchCompany(@PathVariable String searchCriteria) {
        byte[] encoded = Base64.getMimeDecoder().decode(searchCriteria);
        String decSearchCriteria = new String(encoded);

        if(decSearchCriteria.isEmpty() || !decSearchCriteria.contains(".")) {
            return new ResponseEntity<Company> (new Company(), HttpStatus.EXPECTATION_FAILED);
        }

        String[] splitted = decSearchCriteria.split("\\.");

        Exchange exchange = exchangeService.getExchangeByCode(splitted[1].toUpperCase());

        if(exchange == null) {
            return new ResponseEntity<Company> (new Company(), HttpStatus.NOT_FOUND);
        }

        Company company = companyService.findCompanyByTickerSymbolAndExchange(splitted[0], exchange);

        if(company == null) {
            return new ResponseEntity<Company> (new Company(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Company>(company, HttpStatus.OK);
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
