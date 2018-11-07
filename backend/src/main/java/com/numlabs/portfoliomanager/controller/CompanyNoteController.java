package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.CompanyNote;
import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.service.CompanyNoteService;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
public class CompanyNoteController {

    @Autowired
    private CompanyNoteService companyNoteService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ExchangeService exchangeService;

    @PostMapping("company/note/add")
    public ResponseEntity<String> addCompanyNote(@RequestBody CompanyNote newCompanyNote) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(newCompanyNote.getCompany().getTickerSymbol(), newCompanyNote.getCompany().getExchange());

        if(company == null) {
            return new ResponseEntity<> (String.format("The company with the %s does not exist.", newCompanyNote.getCompany().getTickerSymbol()), HttpStatus.CONFLICT);
        }
        newCompanyNote.setId(null);
        newCompanyNote.setCompany(company);
        companyNoteService.persist(newCompanyNote);
        return new ResponseEntity<> ("Note added successfully.", HttpStatus.OK);
    }

    @PostMapping("company/note/update")
    public ResponseEntity<String> updateCompanyNote(@RequestBody CompanyNote companyNote) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(companyNote.getCompany().getTickerSymbol(), companyNote.getCompany().getExchange());

        if(company == null) {
            return new ResponseEntity<> (String.format("The company with the %s does not exist.", companyNote.getCompany().getTickerSymbol()), HttpStatus.CONFLICT);
        }

        companyNote.setCompany(company);

        if(companyNote.getId() == null) {
            return new ResponseEntity<>("Missing id" , HttpStatus.EXPECTATION_FAILED);
        }

        companyNoteService.update(companyNote);
        return new ResponseEntity<> ("Note updated successfully.", HttpStatus.OK);
    }

    @RequestMapping("company/note/remove/{id}")
    public ResponseEntity<String> removeCompanyNoteById(@PathVariable Long id) {
        if(id == null) {
            return new ResponseEntity<>("Missing id" , HttpStatus.EXPECTATION_FAILED);
        }

        CompanyNote note = companyNoteService.getById(id);

        if(note == null) {
            return new ResponseEntity<>("Missing note" , HttpStatus.EXPECTATION_FAILED);
        }

        companyNoteService.remove(note);

        return new ResponseEntity<> ("Note removed successfully.", HttpStatus.OK);
    }

    @RequestMapping("company/note/search/{searchCriteria}")
    public ResponseEntity<List<CompanyNote>> searchCompanyNote(@PathVariable String searchCriteria) {
        List<CompanyNote> notes = Collections.EMPTY_LIST;

        if(searchCriteria.isEmpty() || !searchCriteria.contains(".")) {
            return new ResponseEntity<> (notes, HttpStatus.EXPECTATION_FAILED);
        }

        String[] splitted = searchCriteria.split("\\.");
        Exchange exchange = exchangeService.findExchange(Long.valueOf(splitted[1]));

        if(exchange == null) {
            return new ResponseEntity<> (notes, HttpStatus.NOT_FOUND);
        }

        Company company = companyService.findCompanyByTickerSymbolAndExchange(splitted[0], exchange);

        if(company == null) {
            return new ResponseEntity<> (notes, HttpStatus.NOT_FOUND);
        }

        notes = companyNoteService.getAll(company);

        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

}
