package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.CompanyNote;
import com.numlabs.portfoliomanager.repository.CompanyNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CompanyNoteServiceImpl implements CompanyNoteService {

    @Autowired
    private CompanyNoteRepository companyNoteRepository;

    @Override
    public void persist(CompanyNote newCompanyNote) {
        companyNoteRepository.persist(newCompanyNote);
    }

    @Override
    public void update(CompanyNote companyNote) {
        companyNoteRepository.update(companyNote);
    }

    @Override
    public void remove(CompanyNote companyNote) {
        companyNoteRepository.remove(companyNote);
    }

    @Override
    public List<CompanyNote> getAll(Company company) {
        return companyNoteRepository.findAllCompanyNotes(company);
    }

    @Override
    public CompanyNote getById(Long id) {
        return companyNoteRepository.findById(id);
    }
}
