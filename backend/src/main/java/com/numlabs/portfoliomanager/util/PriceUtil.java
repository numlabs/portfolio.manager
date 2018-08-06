package com.numlabs.portfoliomanager.util;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.model.PricingPeriod;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.service.PeriodService;
import com.numlabs.portfoliomanager.service.PricingPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceUtil {

    @Autowired
    private PricingPeriodService pricingPeriodService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PeriodService periodService;

    public void parseCompanyPrices(String filePath) {
        String symbol = "ASELS";
        String pathname = "D:\\projects\\portfolio.manager\\frontend\\ASELS.IS.csv";

       try(BufferedReader reader = new BufferedReader(new FileReader(new File(pathname)))) {
           List<DailyStockPrice> prices = new ArrayList();
           String line = reader.readLine();

           while((line = reader.readLine()) != null) {
               String[] elements = line.split(",");

               if(elements.length != 7) {
                   return;
               }
               String[] dateElem = elements[0].split("-");

               LocalDate date = LocalDate.of(Integer.valueOf(dateElem[0]),Integer.valueOf(dateElem[1]), Integer.valueOf(dateElem[2]));

               try {
                   prices.add(new DailyStockPrice(Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                           new BigDecimal(elements[1]),
                           new BigDecimal(elements[2]),
                           new BigDecimal(elements[3]),
                           new BigDecimal(elements[4]),
                           new BigDecimal(elements[6])
                   ));
               } catch (NumberFormatException n) {
                   System.out.println(line);
               }

           }

           prices = prices.stream().sorted((o1, o2)-> o1.getDay().compareTo(o2.getDay())).collect(Collectors.toList());
           System.out.println(prices);

           if(prices != null) {
               Company company = companyService.findCompanyByTickerSymbol(symbol);

               if(company != null) {
                   List<PricingPeriod> periods = pricingPeriodService.getCompanyPricingPeriods(company);

                   if(periods != null  && !periods.isEmpty()) {
                       for(PricingPeriod period: periods) {
                           updatePricingPeriod(period, prices);
                       }
                   }
               }
           }
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    private void updatePricingPeriod(PricingPeriod period, List<DailyStockPrice> prices) {
        LocalDateTime sDate = LocalDateTime.now();
        Date startDate = period.getStartDate();
        Date endDate = period.getEndDate();

        LocalDateTime start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        start = start.minusDays(1);

        LocalDateTime end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        end = end.plusDays(1);
    }

    public void calculatePERatio(String symbol) {
        Company company = companyService.findCompanyByTickerSymbol(symbol);

        if(company == null) {
            System.out.println("Company cannot be found!");
            return;
        }

        List<PricingPeriod> pPeriods = pricingPeriodService.getCompanyPricingPeriods(company);

        if(pPeriods == null || pPeriods.isEmpty()){
            System.out.println("No pricing periods could be found for the company!");
            return;
        }

        List<Period> periods = periodService.findPeriodsOfCompany(company);

        if(periods == null || periods.isEmpty()){
            System.out.println("No periods could be found for the company!");
            return;
        }

        pPeriods = pPeriods.stream().sorted((p1, p2) ->{return p1.getStartDate().compareTo(p2.getStartDate());}).collect(Collectors.toList());
        periods = periods.stream().sorted((p1, p2) ->{return p2.getName().compareTo(p1.getName());}).collect(Collectors.toList());

        System.out.println("date\t\t\t\t, low price ,\t\t aver price , \t\t high price");

        for(PricingPeriod p: pPeriods) {
            BigDecimal lowerPrice = p.getLowerPrice().multiply(p.getSharesOutstanding());
            BigDecimal averagePrice = p.getAveragePrice().multiply(p.getSharesOutstanding());
            BigDecimal highestPrice = p.getHigherPrice().multiply(p.getSharesOutstanding());

            BigDecimal netProfit = p.getPeriod().getIncomeStatement().getNetProfit().multiply(new BigDecimal(4));
            BigDecimal net4QProfit = getLast4PeriodsNetEarnings(p.getPeriod(), periods);//  p.getPeriod().getNetProfit().multiply(new BigDecimal(4));

            BigDecimal lowPE = lowerPrice.divide(netProfit, 2 , BigDecimal.ROUND_HALF_UP);
            BigDecimal averagePE = averagePrice.divide(netProfit, 2 , BigDecimal.ROUND_HALF_UP);
            BigDecimal highestPE = highestPrice.divide(netProfit, 2 , BigDecimal.ROUND_HALF_UP);

            BigDecimal low4PE = lowerPrice.divide(net4QProfit, 2 , BigDecimal.ROUND_HALF_UP);
            BigDecimal average4PE = averagePrice.divide(net4QProfit, 2 , BigDecimal.ROUND_HALF_UP);
            BigDecimal highest4PE = highestPrice.divide(net4QProfit, 2 , BigDecimal.ROUND_HALF_UP);


            System.out.println(p.getStartDate() + " ,\t\t " + low4PE  + "(" + p.getLowerPrice() + "),\t\t  " +  average4PE +
                    "(" + p.getAveragePrice() + "),\t\t " + highest4PE  + "(" + p.getHigherPrice() + ")" );

        }

    }

    private BigDecimal getLast4PeriodsNetEarnings(Period period, List<Period> periods) {
        BigDecimal total = period.getIncomeStatement().getNetProfit();
        BigDecimal p2;

        try {
            for (int i = 0; i < periods.size(); i++) {
                if (periods.get(i).getId().equals(period.getId())) {
                    if (periods.get(i + 1) != null) {
                        total = total.add(periods.get(i + 1).getIncomeStatement().getNetProfit());

                        if (periods.get(i + 2) != null) {
                            total = total.add(periods.get(i + 2).getIncomeStatement().getNetProfit());

                            if (periods.get(i + 3) != null) {
                                total = total.add(periods.get(i + 3).getIncomeStatement().getNetProfit());
                                return total;
                            }
                        }
                    }
                }
            }
        } catch(IndexOutOfBoundsException i) {
           // System.out.println();
        }

        return new BigDecimal(-1);
    }

    public void calculatePtoBVRatios(String symbol) {
        Company company = companyService.findCompanyByTickerSymbol(symbol);

        if(company == null) {
            System.out.println("Company cannot be found!");
            return;
        }

        List<PricingPeriod> pPeriods = pricingPeriodService.getCompanyPricingPeriods(company);

        if(pPeriods == null || pPeriods.isEmpty()) {
            System.out.println("No pricing periods could be found for the company!");
            return;
        }

        List<Period> periods = periodService.findPeriodsOfCompany(company);

        if(periods == null || periods.isEmpty()){
            System.out.println("No periods could be found for the company!");
            return;
        }

        pPeriods = pPeriods.stream().sorted((p1, p2) ->{return p1.getStartDate().compareTo(p2.getStartDate());}).collect(Collectors.toList());
       // periods = periods.stream().sorted((p1, p2) ->{return p2.getName().compareTo(p1.getName());}).collect(Collectors.toList());

        System.out.println("date\t\t\t\t, low price ,\t\t aver price , \t\t high price , \t\t equity");

        for(PricingPeriod p: pPeriods) {
            BigDecimal lowerPrice = p.getLowerPrice().multiply(p.getSharesOutstanding());
            BigDecimal averagePrice = p.getAveragePrice().multiply(p.getSharesOutstanding());
            BigDecimal highestPrice = p.getHigherPrice().multiply(p.getSharesOutstanding());

            BigDecimal equity = p.getPeriod().getBalanceSheet().getTotalAssets().subtract(p.getPeriod().getBalanceSheet().getTotalLiabilities());

            BigDecimal low4PBV = lowerPrice.divide(equity, 2 , BigDecimal.ROUND_HALF_UP);
            BigDecimal average4PBV = averagePrice.divide(equity, 2 , BigDecimal.ROUND_HALF_UP);
            BigDecimal highest4PBV = highestPrice.divide(equity, 2 , BigDecimal.ROUND_HALF_UP);

            System.out.println(p.getStartDate() + " ,\t\t " + low4PBV  + "(" + p.getLowerPrice() + "),\t\t  " +  average4PBV +
                    "(" + p.getAveragePrice() + "),\t\t " + highest4PBV  + "(" + p.getHigherPrice() + ") , " +  equity);
        }
    }

    public void calculateEBITtoEVRatios(String symbol) {
        Company company = companyService.findCompanyByTickerSymbol(symbol);

        if(company == null) {
            System.out.println("Company cannot be found!");
            return;
        }

        List<PricingPeriod> pPeriods = pricingPeriodService.getCompanyPricingPeriods(company);

        if(pPeriods == null || pPeriods.isEmpty()) {
            System.out.println("No pricing periods could be found for the company!");
            return;
        }

        List<Period> periods = periodService.findPeriodsOfCompany(company);

        if(periods == null || periods.isEmpty()) {
            System.out.println("No periods could be found for the company!");
            return;
        }

        pPeriods = pPeriods.stream().sorted((p1, p2) ->{return p1.getStartDate().compareTo(p2.getStartDate());}).collect(Collectors.toList());
        periods = periods.stream().sorted((p1, p2) ->{return p2.getName().compareTo(p1.getName());}).collect(Collectors.toList());

        System.out.println("date\t\t\t\t, low  ,\t\t aver  , \t\t high  , \t\t ebit");

        for(PricingPeriod p: pPeriods) {
            BigDecimal lowerPrice = p.getLowerPrice().multiply(p.getSharesOutstanding());
            BigDecimal averagePrice = p.getAveragePrice().multiply(p.getSharesOutstanding());
            BigDecimal highestPrice = p.getHigherPrice().multiply(p.getSharesOutstanding());

            //BigDecimal ebit = p.getPeriod().getOperatingProfit();
            BigDecimal ebit = getLast4PeriodsNetEBIT(p.getPeriod(), periods);//  p.getPeriod().getNetProfit().multiply(new BigDecimal(4));

            BigDecimal totalDebt = p.getPeriod().getBalanceSheet().getLongTermDebt().add(p.getPeriod().getBalanceSheet().getShortTermDebt());
            BigDecimal cash = p.getPeriod().getBalanceSheet().getCashAndEquivalents();
            BigDecimal evLow = lowerPrice.add(totalDebt).subtract(cash);
            BigDecimal evAvr = averagePrice.add(totalDebt).subtract(cash);
            BigDecimal evHigh = highestPrice.add(totalDebt).subtract(cash);

            BigDecimal lowEbitEV = evLow.divide(ebit, 2 , BigDecimal.ROUND_HALF_UP);
            BigDecimal avrEbitEV = evAvr.divide(ebit, 2 , BigDecimal.ROUND_HALF_UP);
            BigDecimal highEbitEV = evHigh.divide(ebit, 2 , BigDecimal.ROUND_HALF_UP);

            System.out.println(p.getStartDate() + " ,\t\t " + lowEbitEV  + "(" + p.getLowerPrice() + "),\t\t  " +  avrEbitEV +
                    "(" + p.getAveragePrice() + "),\t\t " + highEbitEV  + "(" + p.getHigherPrice() + ") , \t " +  ebit);

        }

    }

    private BigDecimal getLast4PeriodsNetEBIT(Period period, List<Period> periods) {
        BigDecimal total = period.getIncomeStatement().getOperatingProfit();
        BigDecimal p2;

        try {
            for (int i = 0; i < periods.size(); i++) {
                if (periods.get(i).getId().equals(period.getId())) {
                    if (periods.get(i + 1) != null) {
                        total = total.add(periods.get(i + 1).getIncomeStatement().getOperatingProfit());

                        if (periods.get(i + 2) != null) {
                            total = total.add(periods.get(i + 2).getIncomeStatement().getOperatingProfit());

                            if (periods.get(i + 3) != null) {
                                total = total.add(periods.get(i + 3).getIncomeStatement().getOperatingProfit());
                                return total;
                            }
                        }
                    }
                }
            }
        } catch(IndexOutOfBoundsException i) {
            // System.out.println();
        }

        return new BigDecimal(-1);
    }

    private class DailyStockPrice{
        private Date day;
        private BigDecimal close;
        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal volume;

        public DailyStockPrice(Date day,  BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, BigDecimal volume) {
            this.day = day;
            this.close = close;
            this.open = open;
            this.high = high;
            this.low = low;
            this.volume = volume;
        }

        public Date getDay() {
            return day;
        }

        public void setDay(Date day) {
            this.day = day;
        }

        public BigDecimal getClose() {
            return close;
        }

        public void setClose(BigDecimal close) {
            this.close = close;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public BigDecimal getHigh() {
            return high;
        }

        public void setHigh(BigDecimal high) {
            this.high = high;
        }

        public BigDecimal getLow() {
            return low;
        }

        public void setLow(BigDecimal low) {
            this.low = low;
        }

        public BigDecimal getVolume() {
            return volume;
        }

        public void setVolume(BigDecimal volume) {
            this.volume = volume;
        }
    }
}
