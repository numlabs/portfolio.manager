package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;

import java.util.List;

public interface CompanyService {

    public Company findCompany(Long id);

    public List<Company> findAllCompanies();

    public List<Company> findAllCompanies(String exchange);

    public List<Company> findAllCompaniesDetailed(String exchange);

    public void update(Company com);

    Company findCompanyBySymbol(String symbol);
}
