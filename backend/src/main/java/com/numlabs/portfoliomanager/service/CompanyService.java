package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;

import java.util.List;

public interface CompanyService {

    public Company findCompany(Long id);

    public List<Company> findAllCompanies();

    public List<Company> findAllCompanies(String exchange);

    public List<Company> findAllCompaniesDetailed(String exchange);

    public void update(Company com);

    public void persist(Company com);

    public Company findCompanyByTickerSymbol(String symbol);

    public Company findCompanyByTickerSymbolAndExchange(String tickerSymbol, Exchange exchange);

    public void remove(Long id);
}
