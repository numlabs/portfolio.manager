package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.Constants;
import com.numlabs.portfoliomanager.model.*;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PeriodRepository extends PortfolioManagerRepository<Period> {

    public List<Period> findAllByCompany(Company company) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Period> criteriaQuery = builder.createQuery(Period.class);
        Root<Period> root = criteriaQuery.from(Period.class);

        criteriaQuery.where(builder.equal(root.get("company"), company));
        criteriaQuery.orderBy(builder.desc(root.get("name")));
        List<Period> periods = entityManager.createQuery(criteriaQuery).getResultList();
        return periods;
    }

    public void removeCompanyPeriods(Company company){
        Query q = entityManager.createQuery ("DELETE FROM Period p WHERE p.company = :com");
        q.setParameter ("com", company);
        q.executeUpdate ();
    }

    public Period findPeriodOfCompanyByPeriodName(Company company, String periodName) {
        Period period = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Period> criteriaQuery = builder.createQuery(Period.class);
        Root<Period> root = criteriaQuery.from(Period.class);

        criteriaQuery.where(builder.equal(root.get("company"), company));
        criteriaQuery.where(builder.and(builder.equal(root.get("company"), company), builder.equal(root.get("name"), periodName)));
        criteriaQuery.orderBy(builder.desc(root.get("name")));

        try {
            period = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch(NoResultException n) {
            return null;
        }

        return period;
    }

    public void persist(BalanceSheet entity) {
        this.entityManager.persist(entity);
    }

    public void persist(IncomeStatement entity) {
        this.entityManager.persist(entity);
    }

    public void persist(CashFlowStatement entity) {
        this.entityManager.persist(entity);
    }

    public BalanceSheet findBalanceSheetByPeriodId(Long id) {
        Assert.notNull(id, Constants.ID_MUST_NOT_BE_NULL);
        return this.entityManager.find(BalanceSheet.class, id);
    }

    public IncomeStatement findIncomeStatementByPeriodId(Long id) {
        Assert.notNull(id, Constants.ID_MUST_NOT_BE_NULL);
        return this.entityManager.find(IncomeStatement.class, id);
    }

    public CashFlowStatement findCashFlowStatementByPeriodId(Long id) {
        Assert.notNull(id, Constants.ID_MUST_NOT_BE_NULL);
        return this.entityManager.find(CashFlowStatement.class, id);
    }
}
