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
import javax.persistence.criteria.CriteriaDelete;
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

    public void removeCompanyPeriods(Company company) {
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

    public BalanceSheet findBalanceSheetByPeriodId(Period period) {
        Assert.notNull(period, Constants.ID_MUST_NOT_BE_NULL);

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<BalanceSheet> query = builder.createQuery(BalanceSheet.class);
        Root<BalanceSheet> root = query.from(BalanceSheet.class);
        query.where(builder.equal(root.get("period"), period));

        try {
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public IncomeStatement findIncomeStatementByPeriodId(Period period) {
        Assert.notNull(period, Constants.ID_MUST_NOT_BE_NULL);

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<IncomeStatement> query = builder.createQuery(IncomeStatement.class);
        Root<IncomeStatement> root = query.from(IncomeStatement.class);
        query.where(builder.equal(root.get("period"), period));

        try {
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public CashFlowStatement findCashFlowStatementByPeriodId(Period period) {
        Assert.notNull(period, Constants.ID_MUST_NOT_BE_NULL);

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<CashFlowStatement> query = builder.createQuery(CashFlowStatement.class);
        Root<CashFlowStatement> root = query.from(CashFlowStatement.class);
        query.where(builder.equal(root.get("period"), period));

        try {
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public BankStatement findBankStatementOfPeriod(Period period) {
        Assert.notNull(period, Constants.ID_MUST_NOT_BE_NULL);

        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<BankStatement> query = builder.createQuery(BankStatement.class);
        Root<BankStatement> root = query.from(BankStatement.class);
        query.where(builder.equal(root.get("period"), period));

        try {
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void update(BalanceSheet element){
        this.entityManager.merge(element);
    }

    public void update(CashFlowStatement element){
        this.entityManager.merge(element);
    }

    public void update(IncomeStatement element){
        this.entityManager.merge(element);
    }

    public List<Period> findPeriodsOfCompanyForYear(Company company, String year) {
        List<Period> periods = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Period> criteriaQuery = builder.createQuery(Period.class);
        Root<Period> root = criteriaQuery.from(Period.class);

        criteriaQuery.where(builder.and(builder.equal(root.get("company"), company), builder.like(root.get("name"), year)));
        criteriaQuery.orderBy(builder.desc(root.get("name")));

        try {
            periods = entityManager.createQuery(criteriaQuery).getResultList();
        } catch(NoResultException n) {
            return null;
        }

        return periods;
    }

    public void removeBalanceSheetOfPeriod(Period period) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<BalanceSheet> criteriaQuery = builder.createCriteriaDelete(BalanceSheet.class);
        Root<BalanceSheet> root = criteriaQuery.from(BalanceSheet.class);

        criteriaQuery.where(builder.equal(root.get("period"), period));
        this.entityManager.createQuery(criteriaQuery).executeUpdate();
    }

    public void removeIncomeStatementOfPeriod(Period period) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<IncomeStatement> criteriaQuery = builder.createCriteriaDelete(IncomeStatement.class);
        Root<IncomeStatement> root = criteriaQuery.from(IncomeStatement.class);

        criteriaQuery.where(builder.equal(root.get("period"), period));
        this.entityManager.createQuery(criteriaQuery).executeUpdate();
    }

    public void removeCashFlowStatementOfPeriod(Period period) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<CashFlowStatement> criteriaQuery = builder.createCriteriaDelete(CashFlowStatement.class);
        Root<CashFlowStatement> root = criteriaQuery.from(CashFlowStatement.class);

        criteriaQuery.where(builder.equal(root.get("period"), period));
        this.entityManager.createQuery(criteriaQuery).executeUpdate();
    }

    public void removeBankStatementOfPeriod(Period period) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<BankStatement> criteriaDeleteQuery = builder.createCriteriaDelete(BankStatement.class);
        Root<BankStatement> root = criteriaDeleteQuery.from(BankStatement.class);

        criteriaDeleteQuery.where(builder.equal(root.get("period"), period));
        this.entityManager.createQuery(criteriaDeleteQuery).executeUpdate();
    }

    public void persist(BankStatement entity) {
        this.entityManager.persist(entity);
    }

}
