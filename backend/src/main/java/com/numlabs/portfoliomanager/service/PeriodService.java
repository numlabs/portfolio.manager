package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;

import java.util.List;

public interface PeriodService {
    public Period getPeriodById(Long id);
    public List<Period> findPeriodsOfCompany(Company company);
}
