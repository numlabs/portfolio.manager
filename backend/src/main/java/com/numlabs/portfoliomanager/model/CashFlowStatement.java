package com.numlabs.portfoliomanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "CASH_FLOW_STATEMENT")
public class CashFlowStatement implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", referencedColumnName = "ID")
    @JsonIgnore
    private Period period;

    @Column(name = "operating_activities_cash")
    private BigDecimal operatingActivitiesCash;

    @Column(name = "dep_and_amrt_expenses")
    private BigDecimal depAndAmrtExpenses;

    @Column(name = "investing_activities_cash")
    private BigDecimal investingActivitiesCash;

    @Column(name = "capital_expenditures")
    private BigDecimal capitalExpenditures;

    @Column(name = "financing_ativities_cash")
    private BigDecimal financingAtivitiesCash;

    @Column(name = "dividend_payments")
    private BigDecimal dividendPayments;

    @Column(name = "debt_issued")
    private BigDecimal debtIssued;

    @Column(name = "debt_payments")
    private BigDecimal debtPayments;

    @Column(name = "cash")
    private BigDecimal cash;

    public CashFlowStatement() {
        this.id = null;
        this.operatingActivitiesCash = new BigDecimal(0);
        this.depAndAmrtExpenses = new BigDecimal(0);
        this.investingActivitiesCash = new BigDecimal(0);
        this.capitalExpenditures = new BigDecimal(0);
        this.financingAtivitiesCash = new BigDecimal(0);
        this.dividendPayments = new BigDecimal(0);
        this.debtIssued = new BigDecimal(0);
        this.debtPayments = new BigDecimal(0);
        this.cash = new BigDecimal(0);
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

    public BigDecimal getOperatingActivitiesCash() {
        return operatingActivitiesCash;
    }

    public void setOperatingActivitiesCash(BigDecimal operatingActivitiesCash) {
        this.operatingActivitiesCash = operatingActivitiesCash;
    }

    public BigDecimal getDepAndAmrtExpenses() {
        return depAndAmrtExpenses;
    }

    public void setDepAndAmrtExpenses(BigDecimal depAndAmrtExpenses) {
        this.depAndAmrtExpenses = depAndAmrtExpenses;
    }

    public BigDecimal getInvestingActivitiesCash() {
        return investingActivitiesCash;
    }

    public void setInvestingActivitiesCash(BigDecimal investingActivitiesCash) {
        this.investingActivitiesCash = investingActivitiesCash;
    }

    public BigDecimal getCapitalExpenditures() {
        return capitalExpenditures;
    }

    public void setCapitalExpenditures(BigDecimal capitalExpenditures) {
        this.capitalExpenditures = capitalExpenditures;
    }

    public BigDecimal getFinancingAtivitiesCash() {
        return financingAtivitiesCash;
    }

    public void setFinancingAtivitiesCash(BigDecimal financingAtivitiesCash) {
        this.financingAtivitiesCash = financingAtivitiesCash;
    }

    public BigDecimal getDividendPayments() {
        return dividendPayments;
    }

    public void setDividendPayments(BigDecimal dividendPayments) {
        this.dividendPayments = dividendPayments;
    }

    public BigDecimal getDebtIssued() {
        return debtIssued;
    }

    public void setDebtIssued(BigDecimal debtIssued) {
        this.debtIssued = debtIssued;
    }

    public BigDecimal getDebtPayments() {
        return debtPayments;
    }

    public void setDebtPayments(BigDecimal debtPayments) {
        this.debtPayments = debtPayments;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }
}
