package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.model.PricingPeriod;
import com.numlabs.portfoliomanager.model.PricingPeriodType;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PricingPeriodRepository extends PortfolioManagerRepository<PricingPeriod> {

    public List<PricingPeriod> getCompanyPricingPeriods(Company company){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PricingPeriod> criteriaQuery = builder.createQuery(PricingPeriod.class);
        Root<PricingPeriod> root = criteriaQuery.from(PricingPeriod.class);

        criteriaQuery.where(builder.equal(root.get("company"), company));
        criteriaQuery.orderBy(builder.desc(root.get("startDate")));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public void removePeriods(Period period) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<PricingPeriod> criteriaQuery = builder.createCriteriaDelete(PricingPeriod.class);
        Root<PricingPeriod> root = criteriaQuery.from(PricingPeriod.class);

        criteriaQuery.where(builder.equal(root.get("period"), period));

        this.entityManager.createQuery(criteriaQuery).executeUpdate();
    }

    public List<PricingPeriodType> getAllPricingPeriodTypes() {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<PricingPeriodType> query = builder.createQuery(PricingPeriodType.class);
        query.from(PricingPeriodType.class);

        return this.entityManager.createQuery(query).getResultList();
    }
}
