package com.numlabs.portfoliomanager.repository;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.model.PriceData;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PriceRepository extends PortfolioManagerRepository<PriceData>{

    public List<PriceData> getCompanyPriceData(Company company) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PriceData> criteriaQuery = builder.createQuery(PriceData.class);
        Root<PriceData> root = criteriaQuery.from(PriceData.class);

        criteriaQuery.where(builder.equal(root.get("company"), company));
        List<PriceData> dataList = entityManager.createQuery(criteriaQuery).getResultList();

        return dataList;
    }
}
