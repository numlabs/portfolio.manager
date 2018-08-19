package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.PortfolioManagerException;
import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;

import java.util.List;

public interface PeriodService {
    Period getPeriodById(Long id);
    List<Period> findPeriodsOfCompany(Company company);

    void removeCompanyPeriods(Company company);

    void addPeriod(Period period) throws PortfolioManagerException;

    Period findPeriodOfCompanyByPeriodName(Company company, String periodName);

    void calculatePeriodMargins();

    void update(Period period);

    void remove(Period period);

    List<Period> findPeriodsOfCompanyForYear(Company company, String year);

    void resetPeriodIndicators(Period period);
}
