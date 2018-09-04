package com.numlabs.portfoliomanager.model;

import com.numlabs.portfoliomanager.Constants;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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

    @Column(name="price_date")
    private Date priceDate;

    @Column(name="stock_url")
    private String stockUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "industry_sector_id", referencedColumnName = "ID")
    private IndustrySector industrySector;

    @Column(name="description")
    private String description;

    @Column(name="ev_ebit_max")
    private BigDecimal evToEbitMax;

    @Column(name="ev_ebit_min")
    private BigDecimal evToEbitMin;

    @Column(name="ebit")
    private BigDecimal ebit;

    @Column(name="ebit_last_period")
    private BigDecimal ebitLastPeriod;

    @Column(name="equity")
    private BigDecimal equity;

    @Column(name="total_debt")
    private BigDecimal totalDebt;

    @Column(name="cash_equivalents")
    private BigDecimal cashEquivalents;

    @Column(name="shares_outstanding")
    private BigDecimal sharesOutstanding;

    @Column(name="money_generated")
    private BigDecimal moneyGenerated;

    @Column(name="company_value")
    private BigDecimal companyValue;

    @Column(name="gross_margin")
    private BigDecimal grossMargin;

    @Column(name="ebit_margin")
    private BigDecimal ebitMargin;

    @Column(name="net_profit_margin")
    private BigDecimal netProfitMargin;

    @Column(name="roe")
    private BigDecimal roe;

    @Column(name="net_profit")
    private BigDecimal netProfit;

    @Column(name="book_value")
    private BigDecimal bookValue;

    @Column(name="kap_url")
    private String kapUrl;

    // transient fields

    @Transient
    private BigDecimal evToEbit;

    @Transient
    private BigDecimal evToEbitLastPeriod;

    @Transient
    private BigDecimal evToMg;

    @Transient
    private BigDecimal evToCv;

    @Transient
    private BigDecimal pe; // price to earnings

    @Transient
    private BigDecimal pb; // price to book value

    @Transient
    private List<Period> periods;

    public Company() {}

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }

    /**
     * Used for error messages when handling a company.
     * @param description
     */
    public Company(String description) {
        this.description = description;
    }

    public String getKapUrl() {
        return kapUrl;
    }

    public void setKapUrl(String kapUrl) {
        this.kapUrl = kapUrl;
    }

    public BigDecimal getBookValue() {
        return bookValue;
    }

    public void setBookValue(BigDecimal bookValue) {
        this.bookValue = bookValue;
    }

    public BigDecimal getEvToEbitLastPeriod() {
        return evToEbitLastPeriod;
    }

    public void setEvToEbitLastPeriod(BigDecimal evToEbitLastPeriod) {
        this.evToEbitLastPeriod = evToEbitLastPeriod;
    }

    public BigDecimal getEbitLastPeriod() {
        return ebitLastPeriod;
    }

    public void setEbitLastPeriod(BigDecimal ebitLastPeriod) {
        this.ebitLastPeriod = ebitLastPeriod;
    }

    public BigDecimal getEbit() {
        return ebit;
    }

    public void setEbit(BigDecimal ebit) {
        this.ebit = ebit;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    public BigDecimal getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(BigDecimal totalDebt) {
        this.totalDebt = totalDebt;
    }

    public BigDecimal getCashEquivalents() {
        return cashEquivalents;
    }

    public void setCashEquivalents(BigDecimal cashEquivalents) {
        this.cashEquivalents = cashEquivalents;
    }

    public BigDecimal getSharesOutstanding() {
        return sharesOutstanding;
    }

    public void setSharesOutstanding(BigDecimal sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
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

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

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

    @Transient
    public boolean isBank() {
        if(this.industrySector != null && this.industrySector.getCode().equals(Constants.BANK_CODE)) {
            return true;
        }

        return false;
    }
}
