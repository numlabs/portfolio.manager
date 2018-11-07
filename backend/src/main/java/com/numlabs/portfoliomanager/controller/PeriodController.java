package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.PortfolioManagerException;
import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.parser.KAPParser;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.service.ExchangeService;
import com.numlabs.portfoliomanager.service.PeriodService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class PeriodController {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private PeriodService periodService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private KAPParser kapParser;

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

    @PostMapping("/period/file/upload")
    public ResponseEntity<Period> handleFileUpload(@RequestParam("file") MultipartFile file) {
        Period period = null;

        if(file.getOriginalFilename().endsWith(".xls")) {
            try {
                period =  kapParser.parseKAPPeriodExcelFile(file.getInputStream());
                return new ResponseEntity<>(period, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(period, HttpStatus.OK);
    }

    @PostMapping("/period/bank/add")
    public ResponseEntity<String> addBankPeriod(@RequestBody Period period) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return new ResponseEntity<>("A company with the specified " + period.getCompany().getTickerSymbol() +
                    " ticker symbol and " + period.getCompany().getExchange().getId() + " exchange id does not exist.",
                    HttpStatus.EXPECTATION_FAILED);
        }

        period.setCompany(company);

        Period existingPeriod = periodService.findPeriodOfCompanyByPeriodName(company, period.getName());

        if(existingPeriod != null) {
            return new ResponseEntity<>("Period already exist.", HttpStatus.EXPECTATION_FAILED);
        }

        if(!period.getName().matches("20[1-9]{2}_Q[1-4]{1}")) {
            return new ResponseEntity<>("Name format is not in correct format.", HttpStatus.EXPECTATION_FAILED);
        }

        int periodNumber = Integer.parseInt(period.getName().substring(period.getName().length() - 1, period.getName().length()));

        if(periodNumber != 1) {
            existingPeriod = periodService.findPeriodOfCompanyByPeriodName(company,
                    period.getName().substring(0, period.getName().length() - 1) + (periodNumber - 1));

            if(existingPeriod == null) {
                return new ResponseEntity<>("Required previous period is missing.", HttpStatus.EXPECTATION_FAILED);
            }
        }

        try {
            periodService.addBankPeriod(period);
        } catch (PortfolioManagerException e) {
            e.printStackTrace();
            return new ResponseEntity<>("There was an issue at the service level.", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("Period added successfully.", HttpStatus.OK);
    }

    @PostMapping("/period/add")
    public ResponseEntity<String> addPeriod(@RequestBody Period period) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return new ResponseEntity<>("A company with the specified " + period.getCompany().getTickerSymbol() +
                    " ticker symbol and " + period.getCompany().getExchange().getId() + "  exchange id does not exist.",
                    HttpStatus.EXPECTATION_FAILED);
        }

        Period existingPeriod = periodService.findPeriodOfCompanyByPeriodName(company, period.getName());

        if(existingPeriod != null) {
            return new ResponseEntity<>("Period already exist.", HttpStatus.EXPECTATION_FAILED);
        }

        if(!period.getName().matches("20[0-9]{2}_Q[1-4]{1}")) {
            return new ResponseEntity<>("Name format is not in correct format.", HttpStatus.EXPECTATION_FAILED);
        }

        int periodNumber = Integer.parseInt(period.getName().substring(period.getName().length() - 1, period.getName().length()));

        if(periodNumber != 1) {
            existingPeriod = periodService.findPeriodOfCompanyByPeriodName(company,period.getName().substring(0, period.getName().length() - 1) + (periodNumber - 1));

            if(existingPeriod == null) {
                return new ResponseEntity<>("Required previous period is missing.", HttpStatus.EXPECTATION_FAILED);
            }
        }

        try {
            periodService.addPeriod(period);
        } catch (PortfolioManagerException e) {
            e.printStackTrace();
            return new ResponseEntity<>("There was an issue at the service level.", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/period/update")
    public ResponseEntity<String> updatePeriod(@RequestBody Period period) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return new ResponseEntity<>("A company with the specified " + period.getCompany().getTickerSymbol() +
                    " ticker symbol and " + period.getCompany().getExchange().getId() + "  exchange id does not exist.",
                    HttpStatus.PRECONDITION_FAILED);
        }

        Period existingPeriod = periodService.findPeriodOfCompanyByPeriodName(company, period.getName());

        if(existingPeriod == null) {
            return new ResponseEntity<String>("Period does not exist.", HttpStatus.OK);
        }

        periodService.update(period);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/period/bank/update")
    public ResponseEntity<String> updateBankPeriod(@RequestBody Period period) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return new ResponseEntity<>("A company with the specified " + period.getCompany().getTickerSymbol() +
                    " ticker symbol and " + period.getCompany().getExchange().getId() + "  exchange id does not exist.",
                    HttpStatus.PRECONDITION_FAILED);
        }

        Period existingPeriod = periodService.findPeriodById(period.getId());

        if(existingPeriod == null) {
            return new ResponseEntity<String>("Period does not exist.", HttpStatus.OK);
        }

        periodService.update(period);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping("period/search/{searchCriteria}")
    public ResponseEntity<Period> searchPeriod(@PathVariable String searchCriteria) {

        if(searchCriteria.isEmpty() || !searchCriteria.contains(".")) {
            return new ResponseEntity<> (new Period(), HttpStatus.OK);
        }

        String[] splitted = searchCriteria.split("\\.");
        Exchange exchange = exchangeService.findExchange(Long.valueOf(splitted[1]));

        if(exchange == null) {
            MultiValueMap<String,String> params = new LinkedMultiValueMap<String,String>();
            params.set("error.message", "Missing exchange for provided id: " + splitted[1]);
            return new ResponseEntity<> (new Period(), params, HttpStatus.OK);
        }

        Company company = companyService.findCompanyByTickerSymbolAndExchange(splitted[0], exchange);

        if(company == null) {
            return new ResponseEntity<> (new Period(), HttpStatus.OK);
        }

        Period period = periodService.findPeriodOfCompanyByPeriodName(company,splitted[2]);

        if(period == null) {
            return new ResponseEntity<> (new Period(), HttpStatus.OK);
        }

        return new ResponseEntity<>(period, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping("period/remove/{id}")
    public ResponseEntity<String> removePeriodById(@PathVariable Long id) {
        Period period = periodService.getPeriodById(id);
        periodService.remove(period);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
