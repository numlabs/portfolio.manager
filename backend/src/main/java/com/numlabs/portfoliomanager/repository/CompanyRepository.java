package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
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
        try {
            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Company findCompanyBySymbol(String symbol){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Company> criteriaQuery = builder.createQuery(Company.class);
        Root<Company> root = criteriaQuery.from(Company.class);
        criteriaQuery.where(builder.equal(root.get("tickerSymbol"), symbol));

        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Company findCompanyByTickerSymbolAndExchange(String tickerSymbol, Exchange exchange) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Company> criteriaQuery = builder.createQuery(Company.class);
        Root<Company> root = criteriaQuery.from(Company.class);
        criteriaQuery.where(builder.and(builder.equal(root.get("tickerSymbol"), tickerSymbol), builder.equal(root.get("exchange"), exchange)));

        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void remove(Company com) {
        entityManager.remove(com);
    }
}
