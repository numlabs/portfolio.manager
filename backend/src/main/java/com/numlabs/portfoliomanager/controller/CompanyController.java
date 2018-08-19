package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.service.ExchangeService;
import com.numlabs.portfoliomanager.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private PeriodService periodService;

    @PostMapping("company/add")
    public ResponseEntity<String> addCompany(@RequestBody Company newCompany) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(newCompany.getTickerSymbol(), newCompany.getExchange());

        if(company != null) {
            return new ResponseEntity<> (String.format("The company with the %s already exist.", newCompany.getTickerSymbol()), HttpStatus.OK);
        }

        companyService.persist(newCompany);

        return new ResponseEntity<> (HttpStatus.OK);
    }

    @PostMapping("company/update")
    public ResponseEntity<String> updateCompany(@RequestBody Company updCompany) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(updCompany.getTickerSymbol(), updCompany.getExchange());

        if(company == null) {
            return new ResponseEntity<> (String.format("The company with the %s does not exist.", updCompany.getTickerSymbol()), HttpStatus.EXPECTATION_FAILED);
        }

        companyService.update(updCompany);

        return new ResponseEntity<> (HttpStatus.OK);
    }

    @RequestMapping("company/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return new ResponseEntity<>(companyService.findCompany(id), HttpStatus.OK);
    }

    @RequestMapping("company/remove/{id}")
    public ResponseEntity<String> removeCompanyById(@PathVariable Long id) {
        companyService.remove(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping("company/reset/{id}")
    public ResponseEntity<String> resetCompanyById(@PathVariable Long id) {
        Company company = companyService.findCompany(id);

        if(company == null) {
            return new ResponseEntity<> (String.format("The company with the %s does not exist.", id), HttpStatus.OK);
        }
        companyService.reset(company);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping("company/search/{searchCriteria}")
    public ResponseEntity<Company> searchCompany(@PathVariable String searchCriteria) {

        if(searchCriteria.isEmpty() || !searchCriteria.contains(".")) {
            return new ResponseEntity<> (new Company(), HttpStatus.EXPECTATION_FAILED);
        }

        String[] splitted = searchCriteria.split("\\.");
        Exchange exchange = exchangeService.findExchange(Long.valueOf(splitted[1]));

        if(exchange == null) {
            return new ResponseEntity<> (new Company(), HttpStatus.NOT_FOUND);
        }

        Company company = companyService.findCompanyByTickerSymbolAndExchange(splitted[0], exchange);

        if(company == null) {
            return new ResponseEntity<> (new Company(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @RequestMapping("companies/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return new ResponseEntity<>(companyService.findAllCompanies(), HttpStatus.OK);
    }

    @RequestMapping("companies/detailed/{exchange}")
    public ResponseEntity<List<Company>> getCompaniesByExchangeCode(@PathVariable String exchange) {
        List<Company> companies = companyService.findAllCompanies(exchange);
        List<Company> selected = new ArrayList<>();

        for(Company company: companies) {
            if(company.getEbit() == null) {
                companyService.calculateIndicators(company);

                if(company.getEbit() == null) {
                    continue;
                }
            }

            if(company.getEvToEbit() == null) {
                continue;
            }

            BigDecimal price = company.getPrice().multiply(company.getSharesOutstanding());
            company.setPe(price.divide(company.getNetProfit(), 2, RoundingMode.HALF_UP));
            company.setPb(price.divide(company.getBookValue(), 2, RoundingMode.HALF_UP));

            BigDecimal ev = price.add(company.getTotalDebt()).subtract(company.getCashEquivalents());
            company.setEvToEbit(ev.divide(company.getEbit(), 2, RoundingMode.HALF_UP));
            company.setEvToMg(ev.divide(company.getMoneyGenerated(),2, RoundingMode.HALF_UP));
            company.setEvToCv(ev.divide(company.getCompanyValue(), 2, RoundingMode.HALF_UP));
            company.setEvToEbitLastPeriod(ev.divide(company.getEbitLastPeriod().multiply(new BigDecimal(4)), 2, RoundingMode.HALF_UP));

            selected.add(company);
        }

        selected = selected.stream().sorted(Comparator.comparing(Company::getEvToEbit)).collect(Collectors.toList());

        return new ResponseEntity<>(selected, HttpStatus.OK);
    }

    @RequestMapping("company/board/{id}")
    public ResponseEntity<Company> getCompanyDetailedById(@PathVariable Long id) {
        Company company = companyService.findCompany(id);

        if(company == null) {
            return new ResponseEntity<> (new Company(), HttpStatus.NOT_FOUND);
        }

        List<Period> periods = periodService.findPeriodsOfCompany(company);

        company.setPeriods(periods);

        periods.stream().forEach((p) ->{p.setCompany(null);});

        return new ResponseEntity<> (company, HttpStatus.OK);
    }
 }
