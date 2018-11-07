package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.CompanyNote;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CompanyNoteRepository extends PortfolioManagerRepository<CompanyNote> {

    public List<CompanyNote> findAllCompanyNotes(Company company) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CompanyNote> criteriaQuery = builder.createQuery(CompanyNote.class);
        Root<CompanyNote> root = criteriaQuery.from(CompanyNote.class);

        criteriaQuery.where(builder.equal(root.get("company"), company));
        try {
            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
