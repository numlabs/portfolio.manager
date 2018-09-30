package com.numlabs.portfoliomanager.util;

import com.numlabs.portfoliomanager.model.Company;
import com.numlabs.portfoliomanager.service.CompanyService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class IsYatirimParser {

    @Autowired
    public CompanyService companyService;

    /**
     * DOAS - XI_29
     * AKSA - XI_29
     * ECILC - XI_29
     * HALKB - UFRS
     */
    public void parseCompany(String symbolicName, int yearCounter, String finacialGroup) {
        String stockUrl = "";

        Map<String, List<BigDecimal>> periodic = new HashMap();
        Map<String, List<BigDecimal>> yearly = new HashMap();
        List<String> itemCodes = new ArrayList<>();
        Map<String, String> nameToCode = new HashMap<>();

        int YEARS = 9;
        boolean recalculate = false;

        try {
            for (int k = 0; k < YEARS; k++) {
                stockUrl = getStockURL(symbolicName, finacialGroup, String.valueOf(yearCounter--));

                org.jsoup.nodes.Document doc = Jsoup.connect(stockUrl).ignoreContentType(true).get();
                JSONObject json = new JSONObject(doc.getElementsByTag("body").text());

                System.out.println(json);
                JSONArray values = json.getJSONArray("value");

                for (int i = 0; i < values.length(); i++) {
                    JSONObject element = values.getJSONObject(i);
                    String keyName = element.getString("itemDescTr");
                    String itemCode = element.getString("itemCode");

                    if (periodic.get(itemCode) == null) {
                        periodic.put(itemCode, new ArrayList<BigDecimal>());
                        yearly.put(itemCode, new ArrayList<BigDecimal>());
                    }

                    if (k == 0) {
                        nameToCode.put(itemCode, keyName);
                        itemCodes.add(itemCode);
                    }

                    BigDecimal q1, q2, q3, q4;

                    try {
                        q1 = new BigDecimal(element.getString("value1"));
                    } catch (JSONException e) {
                        q1 = null;
                    }

                    try {
                        q2 = new BigDecimal(element.getString("value2"));
                    } catch (JSONException e) {
                        q2 = null;
                    }

                    try {
                        q3 = new BigDecimal(element.getString("value3"));
                    } catch (JSONException e) {
                        q3 = null;
                    }

                    try {
                        q4 = new BigDecimal(element.getString("value4"));
                    } catch (JSONException e) {
                        q4 = null;
                    }

                    yearly.get(itemCode).add(q1);

                    if(keyName.equals("Satış Gelirleri")) {
                        recalculate = true;
                    }

                    if(recalculate || keyName.equals("XXIII. NET DÖNEM KARI/ZARARI (XVII+XXII)")) {
                        if (q1 != null && q2 != null) {
                            q1 = q1.subtract(q2);
                        }

                        if (q2 != null && q3 != null) {
                            q2 = q2.subtract(q3);
                        }

                        if (q3 != null && q4 != null) {
                            q3 = q3.subtract(q4);
                        }
                    }

                    periodic.get(itemCode).add(q1);
                    periodic.get(itemCode).add(q2);
                    periodic.get(itemCode).add(q3);
                    periodic.get(itemCode).add(q4);
                }

                recalculate = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(PrintWriter pw = new PrintWriter(new File("output.csv"))) {
            pw.print("name");
            yearCounter += YEARS;

            for (int k = 0; k < YEARS; k++, yearCounter--) {
                pw.print(";Q4_" + yearCounter + ";" + "Q3_" + yearCounter + ";" + "Q2_" + yearCounter + ";" + "Q1_" + yearCounter);
            }

            yearCounter += YEARS;

            for (int k = 0; k < YEARS; k++, yearCounter--) {
                pw.print(";" + yearCounter);
            }

            pw.println();

            for(String code: itemCodes) {
                List<BigDecimal> elements = periodic.get(code);

                pw.print(nameToCode.get(code).replace(";", ","));

                for(BigDecimal value: elements) {
                    pw.print(";" + value);
                }

                elements = yearly.get(code);

                for(BigDecimal value: elements) {
                    pw.print(";" + value);
                }

                pw.println();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getStockURL(String symbol, String financialGroup, String year) {
        return "https://www.isyatirim.com.tr/_layouts/15/IsYatirim.Website/Common/Data.aspx/MaliTablo?" +
                "companyCode=" + symbol +
                "&exchange=TRY" +
                "&financialGroup=" + financialGroup +
                "&year1=" + year +
                "&period1=12" +
                "&year2=" + year +
                "&period2=9" +
                "&year3=" + year +
                "&period3=6" +
                "&year4=" + year +
                "&period4=3" +
                "&_=" + new Date().getTime();
    }

    public String updateBISTPrices() throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect("https://www.isyatirim.com.tr/tr-tr/analiz/hisse/Sayfalar/default.aspx").ignoreContentType(true).get();
        String result = doc.getElementById("allStockTable").text();
        int counter = 0;
        int allCompaniesDefined = 0;

        if(result != null && !result.isEmpty()) {
            List<Company> companies = companyService.findAllCompanies("BIST");

            if(companies == null || companies.isEmpty()) {
                return "No companies found";
            }

            Map<String, Company> pairs = companies.stream().collect(Collectors.toMap(Company::getTickerSymbol, company -> company));
            String[] elements = result.split(" ");
            allCompaniesDefined = companies.size();

            for(int i=0; i<elements.length; i++) {
                Company temp = pairs.get(elements[i].trim());

                if(temp != null) {
                    temp.setPrice(new BigDecimal(elements[i+2].replace(',', '.')));
                    temp.setPriceDate(new Date());
                    companyService.update(temp);
                    counter++;
                }
            }
        }
        return "Companies defined " + allCompaniesDefined + ", updated " + counter;
    }

}













































