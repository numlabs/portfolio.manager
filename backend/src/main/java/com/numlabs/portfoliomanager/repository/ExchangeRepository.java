package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.model.Exchange;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class ExchangeRepository extends PortfolioManagerRepository<Exchange> {

    public Exchange getExchangeByCode(String exchangeCode){
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Exchange> query = builder.createQuery(Exchange.class);
        Root<Exchange> root = query.from(Exchange.class);
        query.where(builder.equal(root.get("code"), exchangeCode));

        return entityManager.createQuery(query).getSingleResult();
    }
}
