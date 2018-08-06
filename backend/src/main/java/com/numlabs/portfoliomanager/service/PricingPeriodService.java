package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.model.PricingPeriod;

import java.util.List;

public interface PricingPeriodService {
    List<PricingPeriod> getCompanyPricingPeriods(Company company);

    void removeOfPeriod(Period p);
}
