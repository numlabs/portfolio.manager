package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.repository.CompanyRepository;
import com.numlabs.portfoliomanager.util.CompanyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private PeriodService periodService;

    @Override
    public Company findCompany(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public List<Company> findAllCompanies() {
        List<Company> companies = new ArrayList<>();
        companyRepository.findAll().forEach(e->companies.add(e));
        return companies;
    }

    @Override
    public List<Company> findAllCompanies(String exchangeCode) {
        Exchange exchange = exchangeService.getExchangeByCode(exchangeCode);
        return companyRepository.findExchangeAllCompanies(exchange);
    }

    @Override
    public List<Company> findAllCompaniesDetailed(String exchange) {
        List<Company> companies = findAllCompanies(exchange);
        companies.forEach(e->{
                    e.setPeriods(periodService.findPeriodsOfCompany(e));
                    CompanyUtil.calculateIndicators(e);
                });
        return companies;
    }

    @Override
    public void update(Company com) {
        companyRepository.update(com);
    }

    public void persist(Company com) {
        companyRepository.persist(com);
    }

    @Override
    public Company findCompanyByTickerSymbol(String tickerSymbol) {
        return companyRepository.findCompanyBySymbol(tickerSymbol);
    }

    @Override
    public Company findCompanyByTickerSymbolAndExchange(String tickerSymbol, Exchange exchange) {
        return companyRepository.findCompanyByTickerSymbolAndExchange(tickerSymbol, exchange);
    }

    @Override
    public void remove(Long id) {
        Company com = findCompany(id);
        periodService.removeCompanyPeriods(com);
        companyRepository.remove(com);
    }
}
