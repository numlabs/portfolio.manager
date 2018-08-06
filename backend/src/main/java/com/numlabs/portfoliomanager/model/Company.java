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

    @Column(name="name")
    private String name;

    @Column(name="ticker_symbol")
    private String tickerSymbol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exchange_id", referencedColumnName = "ID")
    private Exchange exchange;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="stock_url")
    private String stockUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "industry_sector_id", referencedColumnName = "ID")
    private IndustrySector industrySector;

    @Column(name="description")
    private String description;
        // transient fields

    @Transient
    private BigDecimal evToEbit;

    @Transient
    private BigDecimal evToEbitMax;

    @Transient
    private BigDecimal evToEbitMin;

    @Transient
    private BigDecimal pe; // price to earnings

    @Transient
    private BigDecimal pb; // price to book value

    @Transient
    private BigDecimal evToMg;

    @Transient
    private BigDecimal evToCv;

    @Transient
    private List<Period> periods;

    @Transient
    private BigDecimal roe;

    @Transient
    private BigDecimal grossMargin;

    @Transient
    private BigDecimal ebitMargin;

    @Transient
    private BigDecimal netProfitMargin;

    public Company() {}

    public BigDecimal getEvToEbitMax() {
        return evToEbitMax;
    }

    public void setEvToEbitMax(BigDecimal evToEbitMax) {
        this.evToEbitMax = evToEbitMax;
    }

    public BigDecimal getEvToEbitMin() {
        return evToEbitMin;
    }

    public void setEvToEbitMin(BigDecimal evToEbitMin) {
        this.evToEbitMin = evToEbitMin;
    }

    public BigDecimal getRoe() {
        return roe;
    }

    public void setRoe(BigDecimal roe) {
        this.roe = roe;
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

    public Company(Long id) {
        this.id = id;
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

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public IndustrySector getIndustrySector() {
        return industrySector;
    }

    public void setIndustrySector(IndustrySector industrySector) {
        this.industrySector = industrySector;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getEvToMg() {
        return evToMg;
    }

    public void setEvToMg(BigDecimal evToMg) {
        this.evToMg = evToMg;
    }

    public BigDecimal getEvToCv() {
        return evToCv;
    }

    public void setEvToCv(BigDecimal evToCv) {
        this.evToCv = evToCv;
    }

    public BigDecimal getEvToEbit() {
        return evToEbit;
    }

    public void setEvToEbit(BigDecimal evToEbit) {
        this.evToEbit = evToEbit;
    }

    public BigDecimal getPe() {
        return pe;
    }

    public void setPe(BigDecimal pe) {
        this.pe = pe;
    }

    public BigDecimal getPb() {
        return pb;
    }

    public void setPb(BigDecimal pb) {
        this.pb = pb;
    }
}
