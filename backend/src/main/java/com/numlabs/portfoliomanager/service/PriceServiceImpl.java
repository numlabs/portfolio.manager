package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.PriceData;
import com.numlabs.portfoliomanager.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PriceServiceImpl implements PriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Override
    public void persist(PriceData priceData) {
        priceRepository.persist(priceData);
    }

    @Override
    public List<PriceData> getCompanyPriceData(Company company) {
        return priceRepository.getCompanyPriceData(company);
    }


}
