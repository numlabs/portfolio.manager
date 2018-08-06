package com.numlabs.portfoliomanager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "BALANCE_SHEET")
public class BalanceSheet implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", referencedColumnName = "ID")
    private Period period;

    @Column(name = "current_assets")
    private BigDecimal currentAssets;

    @Column(name = "cash_and_equivalents")
    private BigDecimal cashAndEquivalents;

    @Column(name = "inventories")
    private BigDecimal inventories;

    @Column(name = "prepayments")
    private BigDecimal prepayments;

    @Column(name = "trade_receivables")
    private BigDecimal tradeReceivables;

    @Column(name = "total_assets")
    private BigDecimal totalAssets;

    @Column(name = "property_plant_equipment")
    private BigDecimal propertyPlantEquipment;

    @Column(name = "intangible_assets")
    private BigDecimal intangibleAssets;

    @Column(name = "current_liabilities")
    private BigDecimal currentLiabilities;

    @Column(name = "short_term_debt")
    private BigDecimal shortTermDebt;

    @Column(name = "trade_payables")
    private BigDecimal tradePayables;

    @Column(name = "total_liabilities")
    private BigDecimal totalLiabilities;

    @Column(name = "long_term_debt")
    private BigDecimal longTermDebt;

    @Column(name = "total_debt")
    private BigDecimal totalDebt;

    @Column(name = "equity")
    private BigDecimal equity;

    @Column(name = "retained_earnings")
    private BigDecimal retainedEarnings;

    public BalanceSheet() {}

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

    public BigDecimal getCurrentAssets() {
        return currentAssets;
    }

    public void setCurrentAssets(BigDecimal currentAssets) {
        this.currentAssets = currentAssets;
    }

    public BigDecimal getCashAndEquivalents() {
        return cashAndEquivalents;
    }

    public void setCashAndEquivalents(BigDecimal cashAndEquivalents) {
        this.cashAndEquivalents = cashAndEquivalents;
    }

    public BigDecimal getInventories() {
        return inventories;
    }

    public void setInventories(BigDecimal inventories) {
        this.inventories = inventories;
    }

    public BigDecimal getPrepayments() {
        return prepayments;
    }

    public void setPrepayments(BigDecimal prepayments) {
        this.prepayments = prepayments;
    }

    public BigDecimal getTradeReceivables() {
        return tradeReceivables;
    }

    public void setTradeReceivables(BigDecimal tradeReceivables) {
        this.tradeReceivables = tradeReceivables;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getPropertyPlantEquipment() {
        return propertyPlantEquipment;
    }

    public void setPropertyPlantEquipment(BigDecimal propertyPlantEquipment) {
        this.propertyPlantEquipment = propertyPlantEquipment;
    }

    public BigDecimal getIntangibleAssets() {
        return intangibleAssets;
    }

    public void setIntangibleAssets(BigDecimal intangibleAssets) {
        this.intangibleAssets = intangibleAssets;
    }

    public BigDecimal getCurrentLiabilities() {
        return currentLiabilities;
    }

    public void setCurrentLiabilities(BigDecimal currentLiabilities) {
        this.currentLiabilities = currentLiabilities;
    }

    public BigDecimal getShortTermDebt() {
        return shortTermDebt;
    }

    public void setShortTermDebt(BigDecimal shortTermDebt) {
        this.shortTermDebt = shortTermDebt;
    }

    public BigDecimal getTradePayables() {
        return tradePayables;
    }

    public void setTradePayables(BigDecimal tradePayables) {
        this.tradePayables = tradePayables;
    }

    public BigDecimal getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(BigDecimal totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public BigDecimal getLongTermDebt() {
        return longTermDebt;
    }

    public void setLongTermDebt(BigDecimal longTermDebt) {
        this.longTermDebt = longTermDebt;
    }

    public BigDecimal getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(BigDecimal totalDebt) {
        this.totalDebt = totalDebt;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    public BigDecimal getRetainedEarnings() {
        return retainedEarnings;
    }

    public void setRetainedEarnings(BigDecimal retainedEarnings) {
        this.retainedEarnings = retainedEarnings;
    }
}
