package com.numlabs.portfoliomanager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Company implements Serializable {
    private static final long serialVersionUID = 1;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    private String name;

    private String symbol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "industry_sector_id", referencedColumnName = "ID")
    private IndustrySector industrySector;

    @Column(name="bist_group")
    private String group;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exchange_id", referencedColumnName = "ID")
    private Exchange exchange;

    private BigDecimal price;

    @Column(name="buy_below_price")
    private BigDecimal buyBelowPrice;

    @Column(name="sell_above_price")
    private BigDecimal sellAbovePrice;

    @Column(name="stock_url")
    private String stockUrl;

    private String description;

    @Transient
    private List<Period> periods;

    @Transient
    private BigDecimal pe; // price to earnings

    @Transient
    private BigDecimal pToEbit; // price to EBIT

    @Transient
    private BigDecimal pb; // price to book value

    @Transient
    private BigDecimal EVtoEBIT; // Enterprice value to EBIT

    @Transient
    private BigDecimal equity;

    @Transient
    private BigDecimal ev; // enterprise value

    @Transient
    private BigDecimal marketCap;

    public Company(){}

    public Company(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getpToEbit() {
        return pToEbit;
    }

    public void setpToEbit(BigDecimal pToEbit) {
        this.pToEbit = pToEbit;
    }

    public IndustrySector getIndustrySector() {
        return industrySector;
    }

    public void setIndustrySector(IndustrySector industrySector) {
        this.industrySector = industrySector;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public BigDecimal getEv() {
        return ev;
    }

    public void setEv(BigDecimal ev) {
        this.ev = ev;
    }

    public BigDecimal getPe() {
        return pe;
    }

    public void setPe(BigDecimal pe) {
        this.pe = pe;
    }

    public BigDecimal getPToEbit() {
        return pToEbit;
    }

    public void setPToEbit(BigDecimal pToEbit) {
        this.pToEbit = pToEbit;
    }

    public BigDecimal getPb() {
        return pb;
    }

    public void setPb(BigDecimal pb) {
        this.pb = pb;
    }

    public BigDecimal getEVtoEBIT() {
        return EVtoEBIT;
    }

    public void setEVtoEBIT(BigDecimal EVtoEBIT) {
        this.EVtoEBIT = EVtoEBIT;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public BigDecimal getBuyBelowPrice() {
        return buyBelowPrice;
    }

    public void setBuyBelowPrice(BigDecimal buyBelowPrice) {
        this.buyBelowPrice = buyBelowPrice;
    }

    public BigDecimal getSellAbovePrice() {
        return sellAbovePrice;
    }

    public void setSellAbovePrice(BigDecimal sellAbovePrice) {
        this.sellAbovePrice = sellAbovePrice;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStockUrl() {
        return stockUrl;
    }

    public void setStockUrl(String stockUrl) {
        this.stockUrl = stockUrl;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
