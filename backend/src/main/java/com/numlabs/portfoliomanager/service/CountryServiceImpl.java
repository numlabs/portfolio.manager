package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Country;
import com.numlabs.portfoliomanager.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> findAll() {
        List<Country> elements = new ArrayList<>();
        countryRepository.findAll().forEach(e->elements.add(e));

        return elements;
    }

    @Override
    public Country findCountry(Long id) {
        Optional<Country> countryOpt = countryRepository.findById(id);

        if(countryOpt.isPresent()) {
            return countryRepository.findById(id).get();
        }

        return null;
    }
}
