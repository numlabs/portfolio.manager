package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.Constants;
import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.repository.CompanyRepository;
import com.numlabs.portfoliomanager.util.CompanyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private PeriodService periodService;

    @Override
    public Company findCompany(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> findAllCompanies(String exchangeCode) {
        Exchange exchange = exchangeService.getExchangeByCode(exchangeCode);
        return companyRepository.findExchangeAllCompanies(exchange);
    }

    @Override
    public List<Company> findAllCompaniesDetailed(String exchange) {
        List<Company> companies = findAllCompanies(exchange);
        companies.forEach(e->{
                    e.setPeriods(periodService.findPeriodsOfCompany(e));
                    CompanyUtil.calculateIndicators(e);
                });
        return companies;
    }

    @Override
    public void update(Company company) {
        companyRepository.update(company);
        periodService.updateIndicators(company);
    }

    public void persist(Company com) {
        companyRepository.persist(com);
    }

    @Override
    public Company findCompanyByTickerSymbol(String tickerSymbol) {
        return companyRepository.findCompanyBySymbol(tickerSymbol);
    }

    @Override
    public Company findCompanyByTickerSymbolAndExchange(String tickerSymbol, Exchange exchange) {
        return companyRepository.findCompanyByTickerSymbolAndExchange(tickerSymbol, exchange);
    }

    @Override
    public void remove(Long id) {
        Company com = findCompany(id);
        periodService.removeCompanyPeriods(com);
        companyRepository.remove(com);
    }

    @Override
    public void calculateIndicators(Company company) {
        List<Period> periods = periodService.findPeriodsOfCompany(company);
        company.cleanIndicators();

        if (periods == null || periods.size() < 4) {
            return;
        }

        List<Period> ordered = periods.stream().sorted(Comparator.comparing(Period::getName).reversed()).collect(Collectors.toList());

        StringBuilder ebitGrowth = new StringBuilder("");

        for(int i=0; i < ordered.size() && i < 4; i++) {
            if(ordered.get(i).getEbitGrowth() != null) {
                ebitGrowth.append("#");
                ebitGrowth.append(ordered.get(i).getEbitGrowth().toBigInteger());
                ebitGrowth.append("% ");
            }
        }

        company.setEbitGrowth(ebitGrowth.toString());

        if(company.getIndustrySector().getCode().equals(Constants.BANK_CODE)) {
            BigDecimal netProfit = ordered.get(0).getBankStatement().getNetIncome().add(ordered.get(1).getBankStatement().getNetIncome()).
                    add(ordered.get(2).getBankStatement().getNetIncome()).add(ordered.get(3).getBankStatement().getNetIncome());

            company.setNetProfit(netProfit);
            company.setEquity(periods.get(0).getBankStatement().getEquity());
            company.setSharesOutstanding(periods.get(0).getSharesOutstanding());
            company.setBookValue(company.getEquity().subtract(periods.get(0).getBankStatement().getIntangibleAssets()));

            return;
        }

        BigDecimal ebit = ordered.get(0).getIncomeStatement().getOperatingProfit().add(ordered.get(1).getIncomeStatement().getOperatingProfit())
                .add(ordered.get(2).getIncomeStatement().getOperatingProfit()).add(ordered.get(3).getIncomeStatement().getOperatingProfit());
        BigDecimal netProfit = ordered.get(0).getIncomeStatement().getNetProfit().add(ordered.get(1).getIncomeStatement().getNetProfit())
                .add(ordered.get(2).getIncomeStatement().getNetProfit()).add(ordered.get(3).getIncomeStatement().getNetProfit());

        company.setEbit(ebit);
        company.setEquity(periods.get(0).getBalanceSheet().getEquity());
        company.setCashEquivalents(periods.get(0).getBalanceSheet().getCashAndEquivalents());
        company.setCompanyValue(periods.get(0).getCompanyValue());
        company.setEbitMargin(periods.get(0).getEbitMarginTTM());
        company.setEbitdaMargin(periods.get(0).getEbitdaMarginTTM());
        company.setGrossMargin(periods.get(0).getGrossMarginTTM());
        company.setNetProfitMargin(periods.get(0).getNetProfitMarginTTM());
        company.setRoe(periods.get(0).getRoe());
        company.setNetProfit(netProfit);
        company.setTotalDebt(periods.get(0).getBalanceSheet().getTotalDebt());
        company.setSharesOutstanding(periods.get(0).getSharesOutstanding());
        company.setEbitLastPeriod(periods.get(0).getIncomeStatement().getOperatingProfit());
        company.setBookValue(periods.get(0).getBalanceSheet().getEquity().subtract(periods.get(0).getBalanceSheet().getIntangibleAssets()));
        company.setMinorityInterest(periods.get(0).getBalanceSheet().getMinorityInterest());
        company.setMoneyGenerated(periods.get(0).getMoneyGenerated());
    }

    @Override
    public void reset(Company company) {
        company.setEvToCv(null);
        company.setEvToMg(null);
        company.setEvToEbit(null);
        company.setPb(null);
        company.setPe(null);
        company.setTotalDebt(null);
        company.setNetProfit(null);
        company.setRoe(null);
        company.setNetProfitMargin(null);
        company.setEvToEbitMin(null);
        company.setGrossMargin(null);
        company.setMoneyGenerated(null);
        company.setEbit(null);
        company.setCompanyValue(null);
        company.setEbitMargin(null);
        company.setNetProfitMargin(null);
        company.setCashEquivalents(null);
        company.setEvToEbitMax(null);
        company.setEquity(null);

        this.companyRepository.update(company);
    }

    @Override
    public void update(Company updCompany, boolean removePeriods) {
        if(removePeriods) {
            periodService.removeCompanyPeriods(updCompany);
        }
        this.update(updCompany);
    }
}
