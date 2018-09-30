package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.Constants;
import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.model.IndustrySector;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.service.ExchangeService;
import com.numlabs.portfoliomanager.service.IndustrySectorService;
import com.numlabs.portfoliomanager.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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

    @Autowired
    private IndustrySectorService industrySectorService;

    @PostMapping("company/add")
    public ResponseEntity<String> addCompany(@RequestBody Company newCompany) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(newCompany.getTickerSymbol(), newCompany.getExchange());

        if(company != null) {
            return new ResponseEntity<> (String.format("The company with the %s already exist.", newCompany.getTickerSymbol()), HttpStatus.CONFLICT);
        }
        newCompany.setId(null);
        newCompany.setPriceDate(new Date());
        companyService.persist(newCompany);
        return new ResponseEntity<> ("Added.", HttpStatus.OK);
    }

    @PostMapping("company/update")
    public ResponseEntity<String> updateCompany(@RequestBody Company updCompany) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(updCompany.getTickerSymbol(), updCompany.getExchange());

        if(company == null) {
            return new ResponseEntity<> (String.format("The company with the %s does not exist.", updCompany.getTickerSymbol()), HttpStatus.EXPECTATION_FAILED);
        }

        IndustrySector newSector = industrySectorService.getById(updCompany.getIndustrySector().getId());
        updCompany.setIndustrySector(newSector);

        if((!company.isBank() && newSector.getCode().equals(Constants.BANK_CODE)) || (company.isBank() && !newSector.getCode().equals(Constants.BANK_CODE))) {
            companyService.update(updCompany, true);
        } else {
            companyService.update(updCompany, false);
        }

        return new ResponseEntity<> ("Successfully updated.", HttpStatus.OK);
    }

    @RequestMapping("company/remove/{id}")
    public ResponseEntity<String> removeCompanyById(@PathVariable Long id) {
        companyService.remove(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping("company/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return new ResponseEntity<>(companyService.findCompany(id), HttpStatus.OK);
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

    @RequestMapping("company/remove/periods/{id}")
    public ResponseEntity<String> removePeriodsByCompanyId(@PathVariable Long id) {
        Company company = companyService.findCompany(id);

        if(company == null) {
            return new ResponseEntity<> (String.format("The company with the %s does not exist.", id), HttpStatus.OK);
        }

        periodService.removeCompanyPeriods(company);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping("company/search/{searchCriteria}")
    public ResponseEntity<Company> searchCompany(@PathVariable String searchCriteria) {

        if(searchCriteria.isEmpty() || !searchCriteria.contains(".")) {
            return new ResponseEntity<> (new Company("Wrong search criteria: " + searchCriteria), HttpStatus.EXPECTATION_FAILED);
        }

        String[] splitted = searchCriteria.split("\\.");
        Exchange exchange = exchangeService.findExchange(Long.valueOf(splitted[1]));

        if(exchange == null) {
            return new ResponseEntity<> (new Company("Exchange is not found."), HttpStatus.NOT_FOUND);
        }

        Company company = companyService.findCompanyByTickerSymbolAndExchange(splitted[0], exchange);

        if(company == null) {
            return new ResponseEntity<> (new Company("Company with symbol " + splitted[0] + " and exchange " + exchange.getName() + " not found."), HttpStatus.NOT_FOUND);
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
        List<Company> selectedBanks = new ArrayList<>();

        for(Company company: companies) {
            if(company.getSharesOutstanding().equals(BigDecimal.valueOf(0))) {
                if(company.isBank()) {
                    selectedBanks.add(company);
                } else {
                    selected.add(company);
                }
                company.setEvToEbit(new BigDecimal(0));
                company.setEvToMg(new BigDecimal(0));
                company.setEvToCv(new BigDecimal(0));
                company.setEvToEbitLastPeriod(new BigDecimal(0));
                company.setPe(new BigDecimal(0));
                continue;
            }

            if(company.getNetProfit() == null || company.getNetProfit().equals(0)) {
                companyService.calculateIndicators(company);

                if(company.getNetProfit() == null) {
                    company.setEvToEbit(new BigDecimal(-1));
                    company.setEvToMg(new BigDecimal(-1));
                    company.setEvToCv(new BigDecimal(-1));
                    company.setEvToEbitLastPeriod(new BigDecimal(-1));
                    company.setPe(new BigDecimal(-1));
                    company.setPb(new BigDecimal(-1));
                    company.setRoe(new BigDecimal(-1));
                    company.setGrossMargin(new BigDecimal(-1));
                    company.setEbitMargin(new BigDecimal(-1));
                    company.setNetProfitMargin(new BigDecimal(-1));

                    if(company.isBank()) {
                        selectedBanks.add(company);
                    } else {
                        selected.add(company);
                    }
                    continue;
                }
            }

            BigDecimal price = company.getPrice().multiply(company.getSharesOutstanding());
            company.setPe(price.divide(company.getNetProfit(), 2, RoundingMode.HALF_UP));
            company.setPb(price.divide(company.getBookValue(), 2, RoundingMode.HALF_UP));

            if(company.isBank()) {
                company.setEvToEbit(new BigDecimal(0));
                company.setEvToMg(new BigDecimal(0));
                company.setEvToCv(new BigDecimal(0));
                company.setEvToEbitLastPeriod(new BigDecimal(0));
                selectedBanks.add(company);
            } else {
                BigDecimal ev = price.add(company.getTotalDebt()).subtract(company.getCashEquivalents());
                company.setEvToEbit(ev.divide(company.getEbit(), 2, RoundingMode.HALF_UP));
                company.setEvToMg(ev.divide(company.getMoneyGenerated(), 2, RoundingMode.HALF_UP));
                company.setEvToCv(ev.divide(company.getCompanyValue(), 2, RoundingMode.HALF_UP));
                company.setEvToEbitLastPeriod(ev.divide(company.getEbitLastPeriod().multiply(new BigDecimal(4)), 2, RoundingMode.HALF_UP));
                selected.add(company);
            }
        }

        selected = selected.stream().sorted(Comparator.comparing(Company::getEvToEbit)).collect(Collectors.toList());
        selectedBanks = selectedBanks.stream().sorted(Comparator.comparing(Company::getPe)).collect(Collectors.toList());
        selected.addAll(selectedBanks);

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

        periods.stream().forEach((p) ->{p.setCompany(null);}); // eliminate serializing issues

        return new ResponseEntity<> (company, HttpStatus.OK);
    }
 }
