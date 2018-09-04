package com.numlabs.portfoliomanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "BANK_STATEMENT")
public class BankStatement {
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", referencedColumnName = "ID")
    @JsonIgnore
    private Period period;

    @Column(name = "assets")
    private BigDecimal assets;

    @Column(name = "liabilities")
    private BigDecimal liabilities;

    @Column(name = "equity")
    private BigDecimal equity;

    @Column(name = "shares_outstanding")
    private BigDecimal sharesOutstanding;

    @Column(name = "net_income")
    private BigDecimal netIncome;

    @Column(name = "interest_income")
    private BigDecimal interestIncome;

    @Column(name = "interest_expenses")
    private BigDecimal interestExpenses;

    @Column(name = "loans")
    private BigDecimal loans;

    @Column(name = "deposits")
    private BigDecimal deposits;

    @Column(name = "intangible_assets")
    private BigDecimal intangibleAssets;

    @Column(name = "dividends")
    private BigDecimal dividends;

    public BankStatement() {
        this.id = null;
        this.assets = new BigDecimal(0);
        this.liabilities = new BigDecimal(0);
        this.equity = new BigDecimal(0);
        this.sharesOutstanding = new BigDecimal(0);
        this.netIncome = new BigDecimal(0);
        this.interestIncome = new BigDecimal(0);
        this.interestExpenses = new BigDecimal(0);
        this.loans = new BigDecimal(0);
        this.deposits = new BigDecimal(0);
        this.intangibleAssets = new BigDecimal(0);
        this.dividends = new BigDecimal(0);
    }

    public BigDecimal getDividends() {
        return dividends;
    }

    public void setDividends(BigDecimal dividends) {
        this.dividends = dividends;
    }

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

    public BigDecimal getAssets() {
        return assets;
    }

    public void setAssets(BigDecimal assets) {
        this.assets = assets;
    }

    public BigDecimal getLiabilities() {
        return liabilities;
    }

    public void setLiabilities(BigDecimal liabilities) {
        this.liabilities = liabilities;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    public BigDecimal getSharesOutstanding() {
        return sharesOutstanding;
    }

    public void setSharesOutstanding(BigDecimal sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(BigDecimal netIncome) {
        this.netIncome = netIncome;
    }

    public BigDecimal getInterestIncome() {
        return interestIncome;
    }

    public void setInterestIncome(BigDecimal interestIncome) {
        this.interestIncome = interestIncome;
    }

    public BigDecimal getInterestExpenses() {
        return interestExpenses;
    }

    public void setInterestExpenses(BigDecimal interestExpenses) {
        this.interestExpenses = interestExpenses;
    }

    public BigDecimal getLoans() {
        return loans;
    }

    public void setLoans(BigDecimal loans) {
        this.loans = loans;
    }

    public BigDecimal getDeposits() {
        return deposits;
    }

    public void setDeposits(BigDecimal deposits) {
        this.deposits = deposits;
    }

    public BigDecimal getIntangibleAssets() {
        return intangibleAssets;
    }

    public void setIntangibleAssets(BigDecimal intangibleAssets) {
        this.intangibleAssets = intangibleAssets;
    }
}
