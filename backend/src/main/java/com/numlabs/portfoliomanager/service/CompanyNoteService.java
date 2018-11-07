package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.CompanyNote;

import java.util.List;

public interface CompanyNoteService {
    void persist(CompanyNote newCompanyNote);

    void update(CompanyNote companyNote);

    void remove(CompanyNote companyNote);

    List<CompanyNote> getAll(Company company);

    CompanyNote getById(Long id);
}
