package com.numlabs.portfoliomanager.parser;

import com.numlabs.portfoliomanager.Constants;
import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.model.Exchange;
import com.numlabs.portfoliomanager.model.PriceData;
import com.numlabs.portfoliomanager.model.Property;
import com.numlabs.portfoliomanager.service.AdministrationService;
import com.numlabs.portfoliomanager.service.CompanyService;
import com.numlabs.portfoliomanager.service.ExchangeService;
import com.numlabs.portfoliomanager.service.PriceService;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.Action;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class AlphaVantageClient {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private AdministrationService administrationService;

    public void getData(String tickerSymbol) throws Exception {
        Property apiKey = administrationService.getProperty(Constants.ALPHA_VANTAGE_API_KEY);

        if(apiKey == null){
            throw new Exception("Alpha Api Key cannot be retrieved");
        }

        String stockUrl =  "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + tickerSymbol + "&apikey=" + apiKey.getValue() + "&outputsize=full";

        String[] pair = tickerSymbol.split("\\.");
        Exchange exchange = null;

        if(pair[1].toUpperCase().equals("IS")) {
            exchange = exchangeService.getExchangeByCode("BIST");
        } else {
            exchange = exchangeService.getExchangeByCode("EU");
        }

        Company company = companyService.findCompanyByTickerSymbolAndExchange(pair[0], exchange);

        if(company == null) {
            System.out.println("Missing company for exchange \n" + pair[1]);
            return;
        }

        String json = Jsoup.connect(stockUrl).ignoreContentType(true).execute().body();
        JSONObject obj = new JSONObject(json);
        JSONObject series = (JSONObject)obj.get("Time Series (Daily)");

        if(series == null) {
            System.out.println("Missing Data \n" + json);
            return;
        }

        List elements = series.keySet().stream().sorted().collect(Collectors.toList());

        PriceData pd = new PriceData();
        pd.setCompany(company);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date startDate = null;

        try {
            startDate = format.parse((String)elements.get(0));
            pd.setFirstDate(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Missing start date \n" + elements.get(0));
            return;
        }

        Date endDate = null;
        try {
            endDate = format.parse((String)elements.get(elements.size()-1));
            pd.setLastDate(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Missing start date \n" + elements.get(elements.size()-1));
            return;
        }

        pd.setModified(false);
        pd.setPriceData(json);

        priceService.persist(pd);
    }

    public String updatePrices() {
        String response = "EU prices cannot be updated";
        List<Company> companies = companyService.findAllCompanies(Constants.EU_EXCHANGE_CODE);

        if(companies != null && !companies.isEmpty()) {
            Property apiKey = administrationService.getProperty(Constants.ALPHA_VANTAGE_API_KEY);

            if(apiKey == null){
                return response + "\n Alpha Api Key cannot be retrieved";
            }

           for(Company company: companies) {
               try {
                   String json = Jsoup.connect("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" +
                           company.getTickerSymbol() + "." + company.getCountryCode() +
                           "&apikey=" + apiKey.getValue() + "&outputsize=full").ignoreContentType(true).execute().body();

                   JSONObject obj = new JSONObject(json);
                   JSONObject series = (JSONObject) obj.get("Global Quote");
                   String price = (String) series.get("05. price");

                   company.setPrice(new BigDecimal(price));
                   company.setPriceDate(new Date());
                   companyService.update(company);
               } catch (Exception e) {
                   return response + ". Error when updating " + company.getTickerSymbol() + "." + company.getCountryCode();
               }
           }
        }


        return "EU prices updated successfully";
    }

}
