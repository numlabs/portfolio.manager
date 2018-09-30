package com.numlabs.portfoliomanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Period implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "ID")
    private Company company;

    @Column(name = "shares_outstanding")
    private BigDecimal sharesOutstanding;

    @Column(name = "earnings_date")
    private Date earningsDate;

    @Transient
    private BalanceSheet balanceSheet;

    @Transient
    private IncomeStatement incomeStatement;

    @Transient
    private CashFlowStatement cashFlowStatement;

    @Transient
    private BankStatement bankStatement;

    @Column(name = "gross_margin")
    private BigDecimal grossMargin;

    @Column(name = "ebit_margin")
    private BigDecimal ebitMargin;

    @Column(name = "net_profit_margin")
    private BigDecimal netProfitMargin;

    @Column(name = "roe")
    private BigDecimal roe;

    @Column(name = "gross_margin_TTM")
    private BigDecimal grossMarginTTM;

    @Column(name = "ebit_margin_TTM")
    private BigDecimal ebitMarginTTM;

    @Column(name = "net_profit_margin_TTM")
    private BigDecimal netProfitMarginTTM;

    @Column(name = "debt_to_net_profit_margin")
    private BigDecimal debtToNetProfitMargin;

    @Column(name = "money_generated")
    private BigDecimal moneyGenerated;  // Money Generated = Net Income + Amrt.&Depr. - Capex ; TODO: to be tuned

    @Column(name = "company_value")
    private BigDecimal companyValue;  // Company Value = Equity - Intangible Assets ; TODO: to be tuned

    @Column(name = "higher_price")
    private BigDecimal higherPrice;

    @Column(name = "fair_price")
    private BigDecimal fairPrice;

    @Column(name = "lower_price")
    private BigDecimal lowerPrice;

    @Column(name = "ebit_growth")
    private BigDecimal ebitGrowth; // EBIT growth compered the the same period previous year

    public Period() {}

    public BigDecimal getEbitGrowth() {
        return ebitGrowth;
    }

    public void setEbitGrowth(BigDecimal ebitGrowth) {
        this.ebitGrowth = ebitGrowth;
    }

    public BigDecimal getHigherPrice() {
        return higherPrice;
    }

    public void setHigherPrice(BigDecimal higherPrice) {
        this.higherPrice = higherPrice;
    }

    public BigDecimal getFairPrice() {
        return fairPrice;
    }

    public void setFairPrice(BigDecimal fairPrice) {
        this.fairPrice = fairPrice;
    }

    public BigDecimal getLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(BigDecimal lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public Period(Company com, String name) {
        this.company = com;
        this.name = name;
    }

    public BankStatement getBankStatement() {
        return bankStatement;
    }

    public void setBankStatement(BankStatement bankStatement) {
        this.bankStatement = bankStatement;
    }

    public BigDecimal getDebtToNetProfitMargin() {
        return debtToNetProfitMargin;
    }

    public void setDebtToNetProfitMargin(BigDecimal debtToNetProfitMargin) {
        this.debtToNetProfitMargin = debtToNetProfitMargin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public BigDecimal getSharesOutstanding() {
        return sharesOutstanding;
    }

    public void setSharesOutstanding(BigDecimal sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    public Date getEarningsDate() {
        return earningsDate;
    }

    public void setEarningsDate(Date earningsDate) {
        this.earningsDate = earningsDate;
    }

    public BalanceSheet getBalanceSheet() {
        return balanceSheet;
    }

    public void setBalanceSheet(BalanceSheet balanceSheet) {
        this.balanceSheet = balanceSheet;
    }

    public IncomeStatement getIncomeStatement() {
        return incomeStatement;
    }

    public void setIncomeStatement(IncomeStatement incomeStatement) {
        this.incomeStatement = incomeStatement;
    }

    public CashFlowStatement getCashFlowStatement() {
        return cashFlowStatement;
    }

    public void setCashFlowStatement(CashFlowStatement cashFlowStatement) {
        this.cashFlowStatement = cashFlowStatement;
    }

    public BigDecimal getGrossMargin() {
        return grossMargin;
    }

    public void setGrossMargin(BigDecimal grossMargin) {
        this.grossMargin = grossMargin;
    }

    public BigDecimal getEbitMargin() {
        return ebitMargin;
    }

    public void setEbitMargin(BigDecimal ebitMargin) {
        this.ebitMargin = ebitMargin;
    }

    public BigDecimal getNetProfitMargin() {
        return netProfitMargin;
    }

    public void setNetProfitMargin(BigDecimal netProfitMargin) {
        this.netProfitMargin = netProfitMargin;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
    }

    public BigDecimal getGrossMarginTTM() {
        return grossMarginTTM;
    }

    public void setGrossMarginTTM(BigDecimal grossMarginTTM) {
        this.grossMarginTTM = grossMarginTTM;
    }

    public BigDecimal getEbitMarginTTM() {
        return ebitMarginTTM;
    }

    public void setEbitMarginTTM(BigDecimal ebitMarginTTM) {
        this.ebitMarginTTM = ebitMarginTTM;
    }

    public BigDecimal getNetProfitMarginTTM() {
        return netProfitMarginTTM;
    }

    public void setNetProfitMarginTTM(BigDecimal netProfitMarginTTM) {
        this.netProfitMarginTTM = netProfitMarginTTM;
    }

    public BigDecimal getMoneyGenerated() {
        return moneyGenerated;
    }

    public void setMoneyGenerated(BigDecimal moneyGenerated) {
        this.moneyGenerated = moneyGenerated;
    }

    public BigDecimal getCompanyValue() {
        return companyValue;
    }

    public void setCompanyValue(BigDecimal companyValue) {
        this.companyValue = companyValue;
    }

    public void cleanIndicators() {
        this.companyValue = new BigDecimal(0);
        this.roe = new BigDecimal(0);
        this.netProfitMarginTTM = new BigDecimal(0);
        this.netProfitMargin = new BigDecimal(0);
        this.grossMarginTTM = new BigDecimal(0);
        this.grossMargin = new BigDecimal(0);
        this.ebitMargin = new BigDecimal(0);
        this.ebitMarginTTM = new BigDecimal(0);
        this.debtToNetProfitMargin = new BigDecimal(0);
        this.moneyGenerated = new BigDecimal(0);
    }
}
