package com.numlabs.portfoliomanager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "INCOME_STATEMENT")
public class IncomeStatement implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", referencedColumnName = "ID")
    private Period period;

    @Column(name = "revenue")
    private BigDecimal revenue;

    @Column(name = "gross_profit")
    private BigDecimal grossProfit;

    @Column(name = "selling_marketing_distribution_expenses")
    private BigDecimal sellingMarketingDistributionExpenses;

    @Column(name = "general_administrative_expenses")
    private BigDecimal generalAdministrativeExpenses;

    @Column(name = "research_development_expenses")
    private BigDecimal researchDevelopmentExpenses;

    @Column(name = "other_operating_income")
    private BigDecimal otherOperatingIncome;

    @Column(name = "other_operating_expense")
    private BigDecimal otherOperatingExpense;

    @Column(name = "operating_profit")
    private BigDecimal operatingProfit;

    @Column(name = "non_operating_profit")
    private BigDecimal nonOperatingProfit;

    @Column(name = "financial_income")
    private BigDecimal financialIncome;

    @Column(name = "financial_expenses")
    private BigDecimal financialExpenses;

    @Column(name = "tax_expenses")
    private BigDecimal taxExpenses;

    @Column(name = "net_profit")
    private BigDecimal netProfit;

    public IncomeStatement(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getSellingMarketingDistributionExpenses() {
        return sellingMarketingDistributionExpenses;
    }

    public void setSellingMarketingDistributionExpenses(BigDecimal sellingMarketingDistributionExpenses) {
        this.sellingMarketingDistributionExpenses = sellingMarketingDistributionExpenses;
    }

    public BigDecimal getGeneralAdministrativeExpenses() {
        return generalAdministrativeExpenses;
    }

    public void setGeneralAdministrativeExpenses(BigDecimal generalAdministrativeExpenses) {
        this.generalAdministrativeExpenses = generalAdministrativeExpenses;
    }

    public BigDecimal getResearchDevelopmentExpenses() {
        return researchDevelopmentExpenses;
    }

    public void setResearchDevelopmentExpenses(BigDecimal researchDevelopmentExpenses) {
        this.researchDevelopmentExpenses = researchDevelopmentExpenses;
    }

    public BigDecimal getOtherOperatingIncome() {
        return otherOperatingIncome;
    }

    public void setOtherOperatingIncome(BigDecimal otherOperatingIncome) {
        this.otherOperatingIncome = otherOperatingIncome;
    }

    public BigDecimal getOtherOperatingExpense() {
        return otherOperatingExpense;
    }

    public void setOtherOperatingExpense(BigDecimal otherOperatingExpense) {
        this.otherOperatingExpense = otherOperatingExpense;
    }

    public BigDecimal getOperatingProfit() {
        return operatingProfit;
    }

    public void setOperatingProfit(BigDecimal operatingProfit) {
        this.operatingProfit = operatingProfit;
    }

    public BigDecimal getNonOperatingProfit() {
        return nonOperatingProfit;
    }

    public void setNonOperatingProfit(BigDecimal nonOperatingProfit) {
        this.nonOperatingProfit = nonOperatingProfit;
    }

    public BigDecimal getFinancialIncome() {
        return financialIncome;
    }

    public void setFinancialIncome(BigDecimal financialIncome) {
        this.financialIncome = financialIncome;
    }

    public BigDecimal getFinancialExpenses() {
        return financialExpenses;
    }

    public void setFinancialExpenses(BigDecimal financialExpenses) {
        this.financialExpenses = financialExpenses;
    }

    public BigDecimal getTaxExpenses() {
        return taxExpenses;
    }

    public void setTaxExpenses(BigDecimal taxExpenses) {
        this.taxExpenses = taxExpenses;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }
}
