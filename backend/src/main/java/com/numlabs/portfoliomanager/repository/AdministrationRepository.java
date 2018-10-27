package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.model.Property;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class AdministrationRepository extends PortfolioManagerRepository<Property> {

    public Property getProperty(String key) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> criteriaQuery = builder.createQuery(Property.class);
        Root<Property> root = criteriaQuery.from(Property.class);

        criteriaQuery.where(builder.equal(root.get("key"), key));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
