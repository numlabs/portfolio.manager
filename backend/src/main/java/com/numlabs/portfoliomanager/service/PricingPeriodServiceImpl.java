package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.model.PricingPeriod;
import com.numlabs.portfoliomanager.repository.PricingPeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PricingPeriodServiceImpl implements PricingPeriodService {

    @Autowired
    private PricingPeriodRepository pricingPeriodRepository;


    @Override
    public List<PricingPeriod> getCompanyPricingPeriods(Company company) {
        return pricingPeriodRepository.getCompanyPricingPeriods(company);
    }

    @Override
    public void removeOfPeriod(Period p) {
        pricingPeriodRepository.removePeriods(p);
    }
}
