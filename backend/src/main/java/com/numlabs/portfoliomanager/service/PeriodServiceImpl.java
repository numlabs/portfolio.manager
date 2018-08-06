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
import java.util.ArrayList;
import java.util.List;

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
        return this.periodRepository.findAllByCompany(company);
    }

    @Override
    public void removeCompanyPeriods(Company company) {
        List<Period> periods = findPeriodsOfCompany(company);

        for(Period p: periods) {
            pricingPeriodService.removeOfPeriod(p);
        }

        this.periodRepository.removeCompanyPeriods(company);
    }

    @Override
    public void addPeriod(Period period) throws PortfolioManagerException {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return;
        }

        period.setCompany(company);

        period.getBalanceSheet().setTotalDebt(period.getBalanceSheet().getShortTermDebt().add(period.getBalanceSheet().getLongTermDebt()));
        period.getBalanceSheet().setPeriod(period);
        period.getCashFlowStatement().setPeriod(period);
        period.getIncomeStatement().setPeriod(period);

        if(period.getName().endsWith("4")) {
            Period q3 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length()-1) + "3");
            Period q2 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length()-1) + "2");
            Period q1 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length()-1) + "1");

            if(q1 == null || q2 == null || q3 == null) {
                throw new PortfolioManagerException("Required previous periods are mising!");
            }

            extractPeriod(period, q3);
            extractPeriod(period, q2);
            extractPeriod(period, q1);
        } else if(period.getName().endsWith("3")) {
            Period q2 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length()-1) + "2");
            Period q1 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length()-1) + "1");

            if(q1 == null || q2 == null) {
                throw new PortfolioManagerException("Required previous periods are mising!");
            }

            extractPeriod(period, q1);
            extractPeriod(period, q2);
        } else if(period.getName().endsWith("2")) {
            Period q1 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length()-1) + "1");

            if(q1 == null) {
                throw new PortfolioManagerException("Required previous periods are mising!");
            }

            extractPeriod(period, q1);
        }

        this.periodRepository.persist(period);
        this.periodRepository.persist(period.getBalanceSheet());
        this.periodRepository.persist(period.getCashFlowStatement());
        this.periodRepository.persist(period.getIncomeStatement());
    }

    private void extractPeriod(Period period, Period persisted) {
        IncomeStatement isIncorect = period.getIncomeStatement();
        CashFlowStatement cfIncorect = period.getCashFlowStatement();
        CashFlowStatement cfPersisted = persisted.getCashFlowStatement();
        IncomeStatement isPersisted = persisted.getIncomeStatement();

        isIncorect.setRevenue(isIncorect.getRevenue().subtract(isPersisted.getRevenue()));
        isIncorect.setGrossProfit(isIncorect.getGrossProfit().subtract(isPersisted.getGrossProfit()));
        isIncorect.setGeneralAdministrativeExpenses(isIncorect.getGeneralAdministrativeExpenses().subtract(isPersisted.getGeneralAdministrativeExpenses()));
        isIncorect.setSellingMarketingDistributionExpenses(isIncorect.getSellingMarketingDistributionExpenses().subtract(isPersisted.getSellingMarketingDistributionExpenses()));
        isIncorect.setResearchDevelopmentExpenses(isIncorect.getResearchDevelopmentExpenses().subtract(isPersisted.getResearchDevelopmentExpenses()));
        isIncorect.setOtherOperatingIncome(isIncorect.getOtherOperatingIncome().subtract(isPersisted.getOtherOperatingIncome()));
        isIncorect.setOtherOperatingExpense(isIncorect.getOtherOperatingExpense().subtract(isPersisted.getOtherOperatingExpense()));
        isIncorect.setOperatingProfit(isIncorect.getOperatingProfit().subtract(isPersisted.getOperatingProfit()));
        isIncorect.setNonOperatingProfit(isIncorect.getNonOperatingProfit().subtract(isPersisted.getNonOperatingProfit()));
        isIncorect.setFinancialIncome(isIncorect.getFinancialIncome().subtract(isPersisted.getFinancialIncome()));
        isIncorect.setFinancialExpenses(isIncorect.getFinancialExpenses().subtract(isPersisted.getFinancialExpenses()));
        isIncorect.setTaxExpenses(isIncorect.getTaxExpenses().subtract(isPersisted.getTaxExpenses()));
        isIncorect.setNetProfit(isIncorect.getNetProfit().subtract(isPersisted.getNetProfit()));

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

    public Period getPeriodById(Long id) {
        return this.periodRepository.findById(id);
    }
}
