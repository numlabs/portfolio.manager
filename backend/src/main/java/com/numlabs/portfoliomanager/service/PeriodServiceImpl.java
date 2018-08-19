package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.PortfolioManagerException;
import com.numlabs.portfoliomanager.model.CashFlowStatement;
import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.IncomeStatement;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.repository.PeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PeriodServiceImpl implements PeriodService {

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private PricingPeriodService pricingPeriodService;

    @Autowired
    private CompanyService companyService;

    @Override
    public List<Period> findPeriodsOfCompany(Company company) {

        List<Period> periods = this.periodRepository.findAllByCompany(company);

        for(Period period: periods) {
            period.setBalanceSheet(this.periodRepository.findBalanceSheetByPeriodId(period.getId()));
            period.setIncomeStatement(this.periodRepository.findIncomeStatementByPeriodId(period.getId()));
            period.setCashFlowStatement(this.periodRepository.findCashFlowStatementByPeriodId(period.getId()));
        }

        return  periods;
    }

    @Override
    public void removeCompanyPeriods(Company company) {
        List<Period> periods = findPeriodsOfCompany(company);

        for(Period period: periods) {
            pricingPeriodService.removeOfPeriod(period);

            this.periodRepository.removeBalanceSheetOfPeriod(period);
            this.periodRepository.removeIncomeStatementOfPeriod(period);
            this.periodRepository.removeCashFlowStatementOfPeriod(period);
            this.periodRepository.remove(period);
        }
    }

    @Override
    public void addPeriod(Period period) throws PortfolioManagerException {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return;
        }

        period.setCompany(company);

        if(period.getId() != null && period.getId().equals(Long.valueOf(0))) {
            period.setId(null);
            period.getBalanceSheet().setTotalDebt(period.getBalanceSheet().getShortTermDebt().add(period.getBalanceSheet().getLongTermDebt()));
            period.getBalanceSheet().setPeriod(period);
            period.getCashFlowStatement().setPeriod(period);
            period.getIncomeStatement().setPeriod(period);

            if (period.getName().endsWith("4")) {
                Period q3 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "3");
                Period q2 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "2");
                Period q1 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "1");

                if (q1 == null || q2 == null || q3 == null) {
                    throw new PortfolioManagerException("Required previous periods are mising!");
                }

                substractPeriod(period, q3);
                substractPeriod(period, q2);
                substractPeriod(period, q1);
            } else if (period.getName().endsWith("3")) {
                Period q2 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "2");
                Period q1 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "1");

                if (q1 == null || q2 == null) {
                    throw new PortfolioManagerException("Required previous periods are mising!");
                }

                substractPeriod(period, q1);
                substractPeriod(period, q2);
            } else if (period.getName().endsWith("2")) {
                Period q1 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "1");

                if (q1 == null) {
                    throw new PortfolioManagerException("Required previous periods are mising!");
                }

                substractPeriod(period, q1);
            }
        }

        this.periodRepository.persist(period);
        this.periodRepository.persist(period.getBalanceSheet());
        this.periodRepository.persist(period.getCashFlowStatement());
        this.periodRepository.persist(period.getIncomeStatement());

        setPeriodIndicators(findPeriodsOfCompany(company));
    }

    private void substractPeriod(Period period, Period persisted) {
        IncomeStatement isIncorect = period.getIncomeStatement();
        CashFlowStatement cfIncorect = period.getCashFlowStatement();
        CashFlowStatement cfPersisted = persisted.getCashFlowStatement();
        IncomeStatement isExisting = persisted.getIncomeStatement();

        isIncorect.setRevenue(isIncorect.getRevenue().subtract(isExisting.getRevenue()));
        isIncorect.setGrossProfit(isIncorect.getGrossProfit().subtract(isExisting.getGrossProfit()));
        isIncorect.setGeneralAdministrativeExpenses(isIncorect.getGeneralAdministrativeExpenses().subtract(isExisting.getGeneralAdministrativeExpenses()));
        isIncorect.setSellingMarketingDistributionExpenses(isIncorect.getSellingMarketingDistributionExpenses().subtract(isExisting.getSellingMarketingDistributionExpenses()));
        isIncorect.setResearchDevelopmentExpenses(isIncorect.getResearchDevelopmentExpenses().subtract(isExisting.getResearchDevelopmentExpenses()));
        isIncorect.setOtherOperatingIncome(isIncorect.getOtherOperatingIncome().subtract(isExisting.getOtherOperatingIncome()));
        isIncorect.setOtherOperatingExpense(isIncorect.getOtherOperatingExpense().subtract(isExisting.getOtherOperatingExpense()));
        isIncorect.setOperatingProfit(isIncorect.getOperatingProfit().subtract(isExisting.getOperatingProfit()));
        isIncorect.setNonOperatingProfit(isIncorect.getNonOperatingProfit().subtract(isExisting.getNonOperatingProfit()));
        isIncorect.setFinancialIncome(isIncorect.getFinancialIncome().subtract(isExisting.getFinancialIncome()));
        isIncorect.setFinancialExpenses(isIncorect.getFinancialExpenses().subtract(isExisting.getFinancialExpenses()));
        isIncorect.setTaxExpenses(isIncorect.getTaxExpenses().subtract(isExisting.getTaxExpenses()));
        isIncorect.setNetProfit(isIncorect.getNetProfit().subtract(isExisting.getNetProfit()));

        cfIncorect.setOperatingActivitiesCash(cfIncorect.getOperatingActivitiesCash().subtract(cfPersisted.getOperatingActivitiesCash()));
        cfIncorect.setDepAndAmrtExpenses(cfIncorect.getDepAndAmrtExpenses().subtract(cfPersisted.getDepAndAmrtExpenses()));
        cfIncorect.setInvestingActivitiesCash(cfIncorect.getInvestingActivitiesCash().subtract(cfPersisted.getInvestingActivitiesCash()));
        cfIncorect.setCapitalExpenditures(cfIncorect.getCapitalExpenditures().subtract(cfPersisted.getCapitalExpenditures()));
        cfIncorect.setFinancingAtivitiesCash(cfIncorect.getFinancingAtivitiesCash().subtract(cfPersisted.getFinancingAtivitiesCash()));
        cfIncorect.setDividendPayments(cfIncorect.getDividendPayments().subtract(cfPersisted.getDividendPayments()));
        cfIncorect.setDebtPayments(cfIncorect.getDebtPayments().subtract(cfPersisted.getDebtPayments()));
        cfIncorect.setDebtIssued(cfIncorect.getDebtIssued().subtract(cfPersisted.getDebtIssued()));
    }

    public Period findPeriodOfCompanyByPeriodName(Company company, String periodName) {
        Period period = this.periodRepository.findPeriodOfCompanyByPeriodName(company, periodName);

        if(period == null) {
            return period;
        }

        period.setBalanceSheet(this.periodRepository.findBalanceSheetByPeriodId(period.getId()));
        period.setIncomeStatement(this.periodRepository.findIncomeStatementByPeriodId(period.getId()));
        period.setCashFlowStatement(this.periodRepository.findCashFlowStatementByPeriodId(period.getId()));

        return period;
    }

    @Override
    public void calculatePeriodMargins() {
        List<Period> periods = this.periodRepository.findAll();
        Map<Company, List<Period>> allPeriods = periods.stream().collect(Collectors.groupingBy(Period::getCompany));
        Iterator it = allPeriods.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            List<Period> cPeriods = (List<Period>) pair.getValue();

            setPeriodIndicators(cPeriods);
        }

        System.out.println(periods.size());
    }

    private void setPeriodIndicators(List<Period> periods) {
        BigDecimal hundred = new BigDecimal(100);
        List<Period> orderedPeriods = periods.stream().sorted(Comparator.comparing(Period::getName).reversed()).collect(Collectors.toList());

        for(Period p: orderedPeriods) {
            p.setBalanceSheet(this.periodRepository.findBalanceSheetByPeriodId(p.getId()));
            p.setIncomeStatement(this.periodRepository.findIncomeStatementByPeriodId(p.getId()));
            p.setCashFlowStatement(this.periodRepository.findCashFlowStatementByPeriodId(p.getId()));
        }

        for(int i=0; i< orderedPeriods.size(); i++) {
            Period p = orderedPeriods.get(i);

            IncomeStatement is = p.getIncomeStatement();
            CashFlowStatement cf = p.getCashFlowStatement();

            p.setGrossMargin(is.getGrossProfit().divide(is.getRevenue(), 4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
            p.setEbitMargin(is.getOperatingProfit().divide(is.getRevenue(), 4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
            p.setNetProfitMargin(is.getNetProfit().divide(is.getRevenue(), 4, BigDecimal.ROUND_HALF_UP).multiply(hundred));

            if(orderedPeriods.size() - i > 3) { // there are at least 4 periods backward
                BigDecimal revenue = is.getRevenue().add(orderedPeriods.get(i+1).getIncomeStatement().getRevenue()).add(orderedPeriods.get(i+2).getIncomeStatement().getRevenue()).add(orderedPeriods.get(i+3).getIncomeStatement().getRevenue());
                BigDecimal grossProfit = is.getGrossProfit().add(orderedPeriods.get(i+1).getIncomeStatement().getGrossProfit()).add(orderedPeriods.get(i+2).getIncomeStatement().getGrossProfit()).add(orderedPeriods.get(i+3).getIncomeStatement().getGrossProfit());
                BigDecimal ebit = is.getOperatingProfit().add(orderedPeriods.get(i+1).getIncomeStatement().getOperatingProfit()).add(orderedPeriods.get(i+2).getIncomeStatement().getOperatingProfit()).add(orderedPeriods.get(i+3).getIncomeStatement().getOperatingProfit());
                BigDecimal netProfit = is.getNetProfit().add(orderedPeriods.get(i+1).getIncomeStatement().getNetProfit()).add(orderedPeriods.get(i+2).getIncomeStatement().getNetProfit()).add(orderedPeriods.get(i+3).getIncomeStatement().getNetProfit());
                BigDecimal depAmortExp = cf.getDepAndAmrtExpenses().add(orderedPeriods.get(i+1).getCashFlowStatement().getDepAndAmrtExpenses()).add(orderedPeriods.get(i+2).getCashFlowStatement().getDepAndAmrtExpenses()).add(orderedPeriods.get(i+3).getCashFlowStatement().getDepAndAmrtExpenses());
                BigDecimal capex = cf.getCapitalExpenditures().add(orderedPeriods.get(i+1).getCashFlowStatement().getCapitalExpenditures()).add(orderedPeriods.get(i+2).getCashFlowStatement().getCapitalExpenditures()).add(orderedPeriods.get(i+3).getCashFlowStatement().getCapitalExpenditures());

                p.setGrossMarginTTM(grossProfit.divide(revenue,4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                p.setEbitMarginTTM(ebit.divide(revenue,4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                p.setNetProfitMarginTTM(netProfit.divide(revenue,4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                p.setRoe(netProfit.divide(p.getBalanceSheet().getEquity(),4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                p.setMoneyGenerated(netProfit.add(depAmortExp).subtract(capex));
                p.setCompanyValue(p.getBalanceSheet().getEquity().subtract(p.getBalanceSheet().getIntangibleAssets()));
            }
            this.periodRepository.update(p);
        }

        companyService.calculateIndicators(periods.get(0).getCompany());
    }

    @Override
    public void update(Period period) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return;
        }

        period.setCompany(company);
        period.getBalanceSheet().setTotalDebt(period.getBalanceSheet().getShortTermDebt().add(period.getBalanceSheet().getLongTermDebt()));

        period.getIncomeStatement().setPeriod(period);
        period.getBalanceSheet().setPeriod(period);
        period.getCashFlowStatement().setPeriod(period);

        this.periodRepository.update(period.getBalanceSheet());
        this.periodRepository.update(period.getCashFlowStatement());
        this.periodRepository.update(period.getIncomeStatement());

        this.periodRepository.update(period);
    }

    @Override
    public void remove(Period period) {
        this.periodRepository.removeBalanceSheetOfPeriod(period);
        this.periodRepository.removeIncomeStatementOfPeriod(period);
        this.periodRepository.removeCashFlowStatementOfPeriod(period);
        this.periodRepository.remove(period);
    }

    @Override
    public List<Period> findPeriodsOfCompanyForYear(Company company, String year) {
        return this.periodRepository.findPeriodsOfCompanyForYear(company, year);
    }

    @Override
    public void resetPeriodIndicators(Period period) {

    }

    public Period getPeriodById(Long id) {
        return this.periodRepository.findById(id);
    }
}
