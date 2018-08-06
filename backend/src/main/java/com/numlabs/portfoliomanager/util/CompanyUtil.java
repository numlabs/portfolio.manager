package com.numlabs.portfoliomanager.util;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.service.ExchangeService;
import com.numlabs.portfoliomanager.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

@Component
public class CompanyUtil {
    private static final String TURKEY_ALL_COMPANIES = "https://www.investing.com/indices/ise-all-shares-components";
    private static final String EXCHANGE_BIST_CODE = "bist";

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PeriodService periodService;

    public static void calculateIndicators(Company company) {
        List<Period> periods = company.getPeriods();

        if(periods == null || periods.isEmpty() || periods.size() < 4) {
            return;
        }

        Period lastPeriod = periods.get(0);
        BigDecimal lastSharesOut = lastPeriod.getSharesOutstanding();
        BigDecimal lastPrice = company.getPrice();
        BigDecimal last4monthsEarningsTotal = periods.get(0).getIncomeStatement().getNetProfit().add(periods.get(1).getIncomeStatement()
                .getNetProfit().add(periods.get(2).getIncomeStatement().getNetProfit().add(periods.get(3).getIncomeStatement().getNetProfit())));
        BigDecimal last4monthsEBITTotal = periods.get(0).getIncomeStatement().getOperatingProfit().add(periods.get(1).getIncomeStatement()
                .getOperatingProfit().add(periods.get(2).getIncomeStatement().getOperatingProfit().add(periods.get(3).getIncomeStatement().getOperatingProfit())));
        BigDecimal marketCap = lastPrice.multiply(lastSharesOut);
        BigDecimal equity = lastPeriod.getBalanceSheet().getTotalAssets().subtract(lastPeriod.getBalanceSheet().getTotalLiabilities());
        BigDecimal enterpriseValue = marketCap.add(lastPeriod.getBalanceSheet().getLongTermDebt().add(lastPeriod.getBalanceSheet().getShortTermDebt())).subtract(lastPeriod.getBalanceSheet().getCashAndEquivalents());

        company.setPe(marketCap.divide(last4monthsEarningsTotal, 2, BigDecimal.ROUND_HALF_UP));
       // company.setMarketCap(marketCap);
        company.setPb(marketCap.divide(equity, 2, BigDecimal.ROUND_HALF_UP));
       // company.setEquity(equity);
        company.setEvToEbit(enterpriseValue.divide(last4monthsEBITTotal, 2, BigDecimal.ROUND_HALF_UP ) );
      //  company.setPToEbit(marketCap.divide(last4monthsEBITTotal, 2, BigDecimal.ROUND_HALF_UP ));
       // company.setEv(enterpriseValue);
    }

    public void  updateCompanyPricesOfBIST() {
        List<Company> companies;

        try {
            companies = companyService.findAllCompanies(EXCHANGE_BIST_CODE);
        } catch (Exception e) {
            System.out.println("Error retrieving companies.");
            throw e;
        }

        if (companies == null || companies.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }

        Map<String, Company> companiesByUrl = new HashMap<>(companies.size());

        for (Company c : companies) {
            companiesByUrl.put(c.getStockUrl(), c);
        }

        try {
            updatePrices(companiesByUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updatePrices(Map<String, Company> companiesByUrl) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(TURKEY_ALL_COMPANIES).get();
        org.jsoup.nodes.Element stockElements = doc.getElementById("cr1");

        Elements elements = stockElements.getElementsByTag("tbody");
        org.jsoup.nodes.Element element = elements.get(0);

        for (org.jsoup.nodes.Node nodeTR : element.childNodes()) {
            String idStr = nodeTR.attr("id");
            String id = idStr.substring(idStr.indexOf("_") + 1);
            Elements price = doc.getElementsByClass("pid-" + id + "-last");

            for (Node child : nodeTR.childNodes()) {
                if (child.childNodes().size() == 2) {
                    String title = child.childNode(0).attr("title");
                    String href = child.childNode(0).attr("href");

                    if (title == null || title.isEmpty()) {
                        TextNode t = (TextNode) child.childNode(1).childNode(0);
                        title = t.getWholeText();
                        href = child.childNode(1).attr("href");

                    } else {
                        TextNode t = (TextNode) child.childNode(0).childNode(0);
                        href = child.childNode(0).attr("href");
                        title = t.getWholeText();
                    }

                    Company com = companiesByUrl.get("https://www.investing.com" + href);

                    if (com != null) {
                        com.setPrice(new BigDecimal(price.get(0).text().trim()));
                        companyService.update(com);
                    }
                }
            }
        }
    }
}
