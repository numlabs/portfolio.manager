package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.Country;
import com.numlabs.portfoliomanager.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping("countries")
    public ResponseEntity<List<Country>> getCountries() {
        return new ResponseEntity<List<Country>>(countryService.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "country/{id}")
    public ResponseEntity<Country> getCountryById(@PathVariable("id") Long id) {
        Country country = countryService.findCountry(Long.valueOf(id));
        return new ResponseEntity<>(country, HttpStatus.OK);
    }
}
