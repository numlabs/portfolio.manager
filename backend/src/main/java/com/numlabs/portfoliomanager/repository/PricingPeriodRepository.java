package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.model.PricingPeriod;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
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
        List<PricingPeriod> periods = entityManager.createQuery(criteriaQuery).getResultList();
        return periods;
    }

    public void removePeriods(Period period){
        Query q = entityManager.createQuery ("DELETE FROM com.numlabs.portfoliomanager.model.PricingPeriod p WHERE p.period = :per");
        q.setParameter ("per", period);
        q.executeUpdate ();
    }
}
