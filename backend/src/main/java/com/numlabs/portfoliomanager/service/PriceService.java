package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.PriceData;

import java.util.List;

public interface PriceService {

    public void persist(PriceData priceData);

    public List<PriceData> getCompanyPriceData(Company company) ;
}
