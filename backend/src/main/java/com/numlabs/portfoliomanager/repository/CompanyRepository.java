package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class CompanyRepository extends PortfolioManagerRepository<Company> {

    public List<Company> findExchangeAllCompanies(Exchange exchange) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Company> criteriaQuery = builder.createQuery(Company.class);
        Root<Company> root = criteriaQuery.from(Company.class);

        criteriaQuery.where(builder.equal(root.get("exchange"), exchange));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }


    public Company findCompanyBySymbol(String symbol){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Company> criteriaQuery = builder.createQuery(Company.class);
        Root<Company> root = criteriaQuery.from(Company.class);

        criteriaQuery.where(builder.equal(root.get("symbol"), symbol));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
