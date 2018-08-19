package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.Constants;
import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.model.Period;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class PortfolioManagerRepository<T> {
    protected EntityManager entityManager;
    Class<T> type;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<T> findAll() {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(getType());
        query.from(getType());

        return this.entityManager.createQuery(query).getResultList();
    }

    public T findById(Long id) {
        Assert.notNull(id, Constants.ID_MUST_NOT_BE_NULL);
        return (T)this.entityManager.find(getType(), id);
    }

    protected Class<T> getType() {
        ResolvableType resolvableType = ResolvableType.forClass(getClass()).as(PortfolioManagerRepository.class);
        Class clazz = null;

        try {
            clazz = Class.forName(resolvableType.getGenerics()[0].getType().getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public void update(T element){
        this.entityManager.merge(element);
    }

    public void persist(T element){
        this.entityManager.persist(element);
    }

    public void remove(T element) {
        this.entityManager.remove(element);
    }
}
