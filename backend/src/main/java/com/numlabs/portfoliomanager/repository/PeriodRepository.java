package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.Constants;
import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PeriodRepository {
    private EntityManager entityManager;

    public List<Period> findAllByCompany(Company company) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Period> criteriaQuery = builder.createQuery(Period.class);
        Root<Period> root = criteriaQuery.from(Period.class);

        criteriaQuery.where(builder.equal(root.get("company"), company));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Period getPeriodById(Long id) {
        Assert.notNull(id, Constants.ID_MUST_NOT_BE_NULL);
        return this.entityManager.find(Period.class, id);
    }
}
