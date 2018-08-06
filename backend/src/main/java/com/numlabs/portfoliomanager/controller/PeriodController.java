package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.PortfolioManagerException;
import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.parser.KAPParser;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@RestController
public class PeriodController {

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

        return new ResponseEntity<>(period, HttpStatus.PRECONDITION_FAILED);
    }

    @PostMapping("/period/add")
    public ResponseEntity<String> addPeriod(@RequestBody Period period) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return new ResponseEntity<String>("A company with the specified " + period.getCompany().getTickerSymbol() +
                    " ticker symbol and " +period.getCompany().getExchange().getId() + "  exchange id does not exist.",
                    HttpStatus.PRECONDITION_FAILED);
        }

        Period existingPeriod = periodService.findPeriodOfCompanyByPeriodName(company, period.getName());

        if(existingPeriod != null) {
            return new ResponseEntity<String>("Period already exist.", HttpStatus.PRECONDITION_FAILED);
        }

        try {
            periodService.addPeriod(period);
        } catch (PortfolioManagerException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("There was an issue at the service level.", HttpStatus.NOT_ACCEPTABLE);
        }

        System.out.println(period);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
