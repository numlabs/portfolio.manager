package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Country;
import com.numlabs.portfoliomanager.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.Iterable;
import java.util.List;


public interface CountryService {

    public List<Country> findAll();

    Country findCountry(Long aLong);
}
