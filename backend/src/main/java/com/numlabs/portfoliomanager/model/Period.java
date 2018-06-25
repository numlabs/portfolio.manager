package com.numlabs.portfoliomanager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Period implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", referencedColumnName = "ID")
    private Company company;

    @Column(name = "shares_outstanding")
    private BigDecimal sharesOutstanding;

    @Column(name = "total_assets")
    private BigDecimal totalAssets;

    @Column(name = "current_assets")
    private BigDecimal currentAssets;

    @Column(name = "total_liabilities")
    private BigDecimal totalLiabilities;

    @Column(name = "current_liabilities")
    private BigDecimal currentLiabilities;

    private BigDecimal revenue;

    @Column(name = "gross_profit")
    private BigDecimal grossProfit;

    @Column(name = "operating_profit")
    private BigDecimal operating_profit;

    @Column(name = "net_profit")
    private BigDecimal netProfit;

    @Column(name = "cash_from_operating_act")
    private BigDecimal cashFromOperatingAct;

    @Column(name = "cash_from_financing_act")
    private BigDecimal cashFromFinancingAct;

    @Column(name = "cash_from_investing_act")
    private BigDecimal cashFromInvestingAct;

    @Column(name = "net_cash")
    private BigDecimal netCash;

    @Column(name = "dividend_paid")
    private BigDecimal dividendPaid;

    @Column(name = "earnings_date")
    private Date earningsDate;

    public Period(){}

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

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getCurrentAssets() {
        return currentAssets;
    }

    public void setCurrentAssets(BigDecimal currentAssets) {
        this.currentAssets = currentAssets;
    }

    public BigDecimal getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(BigDecimal totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public BigDecimal getCurrentLiabilities() {
        return currentLiabilities;
    }

    public void setCurrentLiabilities(BigDecimal currentLiabilities) {
        this.currentLiabilities = currentLiabilities;
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

    public BigDecimal getOperating_profit() {
        return operating_profit;
    }

    public void setOperating_profit(BigDecimal operating_profit) {
        this.operating_profit = operating_profit;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public BigDecimal getCashFromOperatingAct() {
        return cashFromOperatingAct;
    }

    public void setCashFromOperatingAct(BigDecimal cashFromOperatingAct) {
        this.cashFromOperatingAct = cashFromOperatingAct;
    }

    public BigDecimal getCashFromFinancingAct() {
        return cashFromFinancingAct;
    }

    public void setCashFromFinancingAct(BigDecimal cashFromFinancingAct) {
        this.cashFromFinancingAct = cashFromFinancingAct;
    }

    public BigDecimal getCashFromInvestingAct() {
        return cashFromInvestingAct;
    }

    public void setCashFromInvestingAct(BigDecimal cashFromInvestingAct) {
        this.cashFromInvestingAct = cashFromInvestingAct;
    }

    public BigDecimal getNetCash() {
        return netCash;
    }

    public void setNetCash(BigDecimal netCash) {
        this.netCash = netCash;
    }

    public BigDecimal getDividendPaid() {
        return dividendPaid;
    }

    public void setDividendPaid(BigDecimal dividendPaid) {
        this.dividendPaid = dividendPaid;
    }

    public Date getEarningsDate() {
        return earningsDate;
    }

    public void setEarningsDate(Date earningsDate) {
        this.earningsDate = earningsDate;
    }
}
