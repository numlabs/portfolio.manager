package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;

import java.util.List;

public interface CompanyService {

    public Company findCompany(Long id);

    public List<Company> findAllCompanies();
}
