package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.repository.PeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PeriodServiceImpl implements PeriodService {

    @Autowired
    private PeriodRepository periodRepository;

    @Override
    public List<Period> findPeriodsOfCompany(Company company) {
        return this.periodRepository.findAllByCompany(company);
    }

    public Period getPeriodById(Long id) {
        return this.periodRepository.getPeriodById(id);
    }
}
