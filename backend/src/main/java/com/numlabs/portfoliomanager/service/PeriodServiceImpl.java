package com.numlabs.portfoliomanager.service;

import com.numlabs.portfoliomanager.Constants;
import com.numlabs.portfoliomanager.PortfolioManagerException;
import com.numlabs.portfoliomanager.model.*;
import com.numlabs.portfoliomanager.repository.PeriodRepository;
import com.numlabs.portfoliomanager.util.PriceUtil;
import jdk.nashorn.internal.parser.JSONParser;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    private PriceUtil priceUtil;

    @Override
    public void updateIndicators(Company company) {
        if(!company.getIndustrySector().getCode().equals(Constants.BANK_CODE)) {
            setPeriodIndicators(findPeriodsOfCompany(company));
        }
        // TODO: update BANKs indicators
    }

    @Override
    public Period findPeriodById(Long id) {
        return periodRepository.findById(id);
    }

    @Override
    public void calculatePeriodsPrices(Company company) {
        List<Period> periods = findPeriodsOfCompany(company);

        JSONObject prices = new JSONObject(company.getPriceData());

        if(prices != null && periods != null && !periods.isEmpty()) {
            priceUtil.calculatePeriodsPriceMargins(periods, prices);
        }
    }

    @Override
    public List<Period> findPeriodsOfCompany(Company company) {
        if(company.isBank()) {
            return findBankPeriodsOfCompany(company);
        } else {
            return findPeriodsOfRegularCompany(company);
        }
    }

    /*
     * Should not be called directly, use #findPeriodsOfCompany() instead.
     *
     * @param company
     * @return
     */
    private List<Period> findPeriodsOfRegularCompany(Company company) {
        List<Period> periods = this.periodRepository.findAllByCompany(company);

        for(Period period: periods) {
            period.setBalanceSheet(this.periodRepository.findBalanceSheetByPeriodId(period));
            period.setIncomeStatement(this.periodRepository.findIncomeStatementByPeriodId(period));
            period.setCashFlowStatement(this.periodRepository.findCashFlowStatementByPeriodId(period));
        }

        return  periods;
    }

    /*
     * Should not be called directly, use #findPeriodsOfCompany() instead.
     *
     * @param company
     * @return
     */
    private List<Period> findBankPeriodsOfCompany(Company company) {
        List<Period> periods = this.periodRepository.findAllByCompany(company);

        for(Period period: periods) {
            period.setBankStatement(this.periodRepository.findBankStatementOfPeriod(period));
        }

        return  periods;
    }

    @Override
    public void removeCompanyPeriods(Company company) {
        List<Period> periods = findPeriodsOfCompany(company);

        for(Period period: periods) {
            removePeriod(period);
        }
    }

    @Override
    public void addBankPeriod(Period period) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return;
        }

        period.getBankStatement().setPeriod(period);
        this.periodRepository.persist(period);
        this.periodRepository.persist(period.getBankStatement());
        companyService.calculateIndicators(period.getCompany());
    }

    @Override
    public void addPeriod(Period period) throws PortfolioManagerException {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return;
        }

        period.setCompany(company);

        if(period.getId() != null) {
            period.getBalanceSheet().setTotalDebt(period.getBalanceSheet().getShortTermDebt().add(period.getBalanceSheet().getLongTermDebt()).add(period.getBalanceSheet().getCurrentPortionOfLongTermDebt()));
            period.getBalanceSheet().setPeriod(period);
            period.getCashFlowStatement().setPeriod(period);
            period.getIncomeStatement().setPeriod(period);

            if (period.getName().endsWith("4")) {
                Period q3 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "3");
                Period q2 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "2");
                Period q1 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "1");

                if (q1 == null || q2 == null || q3 == null) {
                    throw new PortfolioManagerException("Required previous periods are missing!");
                }

                if(period.getId().equals(Long.valueOf(0))) {
                    subtractPeriod(period, q3);
                    subtractPeriod(period, q2);
                    subtractPeriod(period, q1);
                }
            } else if (period.getName().endsWith("3")) {
                Period q2 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "2");
                Period q1 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "1");

                if (q1 == null || q2 == null) {
                    throw new PortfolioManagerException("Required previous periods are missing!");
                }

                if(period.getId().equals(Long.valueOf(0))) {
                    subtractPeriod(period, q1);
                    subtractPeriod(period, q2);
                }
            } else if (period.getName().endsWith("2")) {
                Period q1 = findPeriodOfCompanyByPeriodName(company, period.getName().substring(0, period.getName().length() - 1) + "1");

                if (q1 == null) {
                    throw new PortfolioManagerException("Required previous periods are missing!");
                }

                if(period.getId().equals(Long.valueOf(0))) {
                    subtractPeriod(period, q1);
                }
            }
        }

        period.setId(null);
        this.periodRepository.persist(period);
        this.periodRepository.persist(period.getBalanceSheet());
        this.periodRepository.persist(period.getCashFlowStatement());
        this.periodRepository.persist(period.getIncomeStatement());

        setPeriodIndicators(findPeriodsOfCompany(company));
    }

    private void subtractPeriod(Period period, Period persisted) {
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

        period.setBalanceSheet(this.periodRepository.findBalanceSheetByPeriodId(period));
        period.setIncomeStatement(this.periodRepository.findIncomeStatementByPeriodId(period));
        period.setCashFlowStatement(this.periodRepository.findCashFlowStatementByPeriodId(period));
        period.setBankStatement(this.periodRepository.findBankStatementOfPeriod(period));

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
    }

    private void setPeriodIndicators(List<Period> periods) {
        if(periods == null || periods.isEmpty() ) {
            return;
        }

        boolean isBank = periods.get(0).getCompany().isBank();
        BigDecimal hundred = new BigDecimal(100);
        List<Period> orderedPeriods = periods.stream().sorted(Comparator.comparing(Period::getName).reversed()).collect(Collectors.toList());

        for(Period period: orderedPeriods) {
            if(isBank) {
                period.setBankStatement(this.periodRepository.findBankStatementOfPeriod(period));
            } else {
                period.setIncomeStatement(this.periodRepository.findIncomeStatementByPeriodId(period));
                period.setCashFlowStatement(this.periodRepository.findCashFlowStatementByPeriodId(period));
                period.setBalanceSheet(this.periodRepository.findBalanceSheetByPeriodId(period));
            }
            period.cleanIndicators();
        }

        try {
            // First period in the list is freshest(last announced) historically
            for(int i=0; i< orderedPeriods.size(); i++) {
                Period period = orderedPeriods.get(i);

                if(isBank) {
                    if(orderedPeriods.size() - i > 3) {
                        BankStatement bs = period.getBankStatement();
                        BigDecimal netIncome = bs.getNetIncome().add(orderedPeriods.get(i + 1).getBankStatement().getNetIncome())
                                .add(orderedPeriods.get(i + 2).getBankStatement().getNetIncome()).add(orderedPeriods.get(i + 3).getBankStatement().getNetIncome());

                        period.setRoe(netIncome.divide(period.getBankStatement().getEquity(), 4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                        period.setMoneyGenerated(netIncome);

                        this.periodRepository.update(period);
                    }

                    if(orderedPeriods.size() - i > 4) {
                        period.setEbitGrowth(((period.getBankStatement().getNetIncome().subtract(orderedPeriods.get(i+4).getBankStatement().getNetIncome())).divide(orderedPeriods.get(i+4).getBankStatement().getNetIncome(),2, BigDecimal.ROUND_HALF_UP)).multiply(hundred));
                        period.getEbitGrowth().setScale(0, RoundingMode.HALF_UP);
                        period.getEbitGrowth().stripTrailingZeros();
                    }
                    continue;
                }

                IncomeStatement is = period.getIncomeStatement();
                CashFlowStatement cf = period.getCashFlowStatement();

                // calculate quarterly performance
                period.setGrossMargin(is.getGrossProfit().divide(is.getRevenue(), 4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                period.setEbitMargin(is.getOperatingProfit().divide(is.getRevenue(), 4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                period.setNetProfitMargin(is.getNetProfit().divide(is.getRevenue(), 4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                period.setEbitdaMargin(is.getOperatingProfit().add(cf.getDepAndAmrtExpenses()).divide(is.getRevenue(), 4, BigDecimal.ROUND_HALF_UP).multiply(hundred));

                if(orderedPeriods.size() - i > 3) { // there are at least 4 periods backward
                    BigDecimal revenue = is.getRevenue().add(orderedPeriods.get(i+1).getIncomeStatement().getRevenue()).add(orderedPeriods.get(i+2).getIncomeStatement().getRevenue()).add(orderedPeriods.get(i+3).getIncomeStatement().getRevenue());
                    BigDecimal grossProfit = is.getGrossProfit().add(orderedPeriods.get(i+1).getIncomeStatement().getGrossProfit()).add(orderedPeriods.get(i+2).getIncomeStatement().getGrossProfit()).add(orderedPeriods.get(i+3).getIncomeStatement().getGrossProfit());
                    BigDecimal ebit = is.getOperatingProfit().add(orderedPeriods.get(i+1).getIncomeStatement().getOperatingProfit()).add(orderedPeriods.get(i+2).getIncomeStatement().getOperatingProfit()).add(orderedPeriods.get(i+3).getIncomeStatement().getOperatingProfit());
                    BigDecimal netProfit = is.getNetProfit().add(orderedPeriods.get(i+1).getIncomeStatement().getNetProfit()).add(orderedPeriods.get(i+2).getIncomeStatement().getNetProfit()).add(orderedPeriods.get(i+3).getIncomeStatement().getNetProfit());
                    BigDecimal depAmortExp = cf.getDepAndAmrtExpenses().add(orderedPeriods.get(i+1).getCashFlowStatement().getDepAndAmrtExpenses()).add(orderedPeriods.get(i+2).getCashFlowStatement().getDepAndAmrtExpenses()).add(orderedPeriods.get(i+3).getCashFlowStatement().getDepAndAmrtExpenses());
                    BigDecimal capex = cf.getCapitalExpenditures().add(orderedPeriods.get(i+1).getCashFlowStatement().getCapitalExpenditures()).add(orderedPeriods.get(i+2).getCashFlowStatement().getCapitalExpenditures()).add(orderedPeriods.get(i+3).getCashFlowStatement().getCapitalExpenditures());
                    BigDecimal dividends = cf.getDividendPayments().add(orderedPeriods.get(i+1).getCashFlowStatement().getDividendPayments()).add(orderedPeriods.get(i+2)
                            .getCashFlowStatement().getDividendPayments()).add(orderedPeriods.get(i+3).getCashFlowStatement().getDividendPayments());
                    BigDecimal ebitda = ebit.add(depAmortExp);
                    period.setGrossMarginTTM(grossProfit.divide(revenue,4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                    period.setEbitMarginTTM(ebit.divide(revenue,4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                    period.setEbitdaMarginTTM(ebitda.divide(revenue,4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                    period.setNetProfitMarginTTM(netProfit.divide(revenue,4, BigDecimal.ROUND_HALF_UP).multiply(hundred));
                    period.setRoe(netProfit.divide(period.getBalanceSheet().getEquity(), 4, BigDecimal.ROUND_HALF_UP).multiply(hundred));

                    period.setMoneyGenerated(netProfit.add(depAmortExp)); // Not that Capex is a negative number and Dep&Amr a positive
                    period.setCompanyValue(period.getBalanceSheet().getEquity().subtract(period.getBalanceSheet().getIntangibleAssets()));
                }

                if(orderedPeriods.size() - i > 4) {
                    period.setEbitGrowth(((period.getIncomeStatement().getOperatingProfit().subtract(orderedPeriods.get(i+4).getIncomeStatement().getOperatingProfit())).divide(orderedPeriods.get(i+4).getIncomeStatement().getOperatingProfit(),2, BigDecimal.ROUND_HALF_UP)).multiply(hundred));
                    period.getEbitGrowth().setScale(0, RoundingMode.HALF_UP);
                    period.getEbitGrowth().stripTrailingZeros();
                }

                this.periodRepository.update(period);
            }
        } catch (NullPointerException np) {
            np.printStackTrace();
        }

        companyService.calculateIndicators(periods.get(0).getCompany());
    }

    @Override
    public void update(Period period) {
        Company company = companyService.findCompanyByTickerSymbolAndExchange(period.getCompany().getTickerSymbol(), period.getCompany().getExchange());

        if(company == null) {
            return;
        }
        List<Period> periods = findPeriodsOfCompany(company);
        List<Period> orderedPeriods = periods.stream().sorted(Comparator.comparing(Period::getName).reversed()).collect(Collectors.toList());

        for(int i=0; i < orderedPeriods.size(); i++) {
            if(orderedPeriods.get(i).getName().equals(period.getName())) {
               break;
            }
        }

        period.setCompany(company);

        if(company.isBank()) {
            period.getBankStatement().setPeriod(period);
            this.periodRepository.update(period.getBankStatement());
        } else {
            period.getBalanceSheet().setTotalDebt(period.getBalanceSheet().getShortTermDebt().add(period.getBalanceSheet().getLongTermDebt()).add(period.getBalanceSheet().getCurrentPortionOfLongTermDebt()));
            period.getIncomeStatement().setPeriod(period);
            period.getBalanceSheet().setPeriod(period);
            period.getCashFlowStatement().setPeriod(period);

            this.periodRepository.update(period.getBalanceSheet());
            this.periodRepository.update(period.getCashFlowStatement());
            this.periodRepository.update(period.getIncomeStatement());
        }

        this.periodRepository.update(period);

        setPeriodIndicators(findPeriodsOfCompany(company));
    }

    @Override
    public void remove(Period period) {
        Company company = period.getCompany();
        List<Period> periodToDelete = new ArrayList<>();
        List<Period> periods = findPeriodsOfCompany(company);
        List<Period> orderedPeriods = periods.stream().sorted(Comparator.comparing(Period::getName).reversed()).collect(Collectors.toList());

        for(int i=0; i < orderedPeriods.size(); i++) {
            if(orderedPeriods.get(i).getName().equals(period.getName())) {
                break;
            }
            periodToDelete.add(orderedPeriods.get(i));
        }

        periodToDelete.forEach(p-> removePeriod(p));
        removePeriod(period);
        company.cleanIndicators();
        companyService.update(company);
        updateIndicators(company);
    }

    private void removePeriod(Period period) {
        pricingPeriodService.remove(period);
        this.periodRepository.removeBalanceSheetOfPeriod(period);
        this.periodRepository.removeIncomeStatementOfPeriod(period);
        this.periodRepository.removeCashFlowStatementOfPeriod(period);
        this.periodRepository.removeBankStatementOfPeriod(period);
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
        Period period = this.periodRepository.findById(id);

        if(period != null) {
            if(period.getCompany().isBank()) {
                period.setBankStatement(periodRepository.findBankStatementOfPeriod(period));
            } else {
                period.setBalanceSheet(periodRepository.findBalanceSheetByPeriodId(period));
                period.setIncomeStatement(periodRepository.findIncomeStatementByPeriodId(period));
                period.setCashFlowStatement(periodRepository.findCashFlowStatementByPeriodId(period));
            }
        }

        return period;
    }
}
