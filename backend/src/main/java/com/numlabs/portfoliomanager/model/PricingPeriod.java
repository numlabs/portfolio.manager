package com.numlabs.portfoliomanager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "PRICING_PERIOD")
public class PricingPeriod  implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pricing_period_type_id", referencedColumnName = "ID")
    private PricingPeriodType pricingPeriodType;

    @Column
    private String name;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "shares_outstanding")
    private BigDecimal sharesOutstanding;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "period_id", referencedColumnName = "ID")
    private Period period;

    @Column(name = "lower_price")
    private BigDecimal lowerPrice;

    @Column(name = "higher_price")
    private BigDecimal higherPrice;

    @Column(name = "average_price")
    private BigDecimal averagePrice;

    @Column(name = "dividend_amount")
    private BigDecimal dividendAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "ID")
    private Company company;

    public PricingPeriod(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public BigDecimal getDividendAmount() {
        return dividendAmount;
    }

    public void setDividendAmount(BigDecimal dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

    public PricingPeriodType getPricingPeriodType() {
        return pricingPeriodType;
    }

    public void setPricingPeriodType(PricingPeriodType pricingPeriodType) {
        this.pricingPeriodType = pricingPeriodType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getSharesOutstanding() {
        return sharesOutstanding;
    }

    public void setSharesOutstanding(BigDecimal sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public BigDecimal getLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(BigDecimal lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public BigDecimal getHigherPrice() {
        return higherPrice;
    }

    public void setHigherPrice(BigDecimal higherPrice) {
        this.higherPrice = higherPrice;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }
}
