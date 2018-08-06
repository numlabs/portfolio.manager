package com.numlabs.portfoliomanager.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

@Component
public class YahooUtils {

    public void getPriceForStock(String symbol) {
        String url = "https://query1.finance.yahoo.com/v7/finance/download/ASELS.IS?period1=1262296800&period2=1531774800&interval=1d&events=history&crumb=wjZQ6PWfAuD";

        try {
            Document doc = Jsoup.connect(url).get();
/*
            Document e = doc.parse();
            String loc = e.location();

            int index = loc.indexOf("sessionId=");
            index = loc.indexOf('=', index);
            int end = loc.indexOf('&', index);
            String sessionId = loc.substring(index,end);
            System.out.println(loc.substring(index,end));

            //This will get you cookies
            Document doc2 = Jsoup.connect(url)
                    .cookie("SESSIONID", sessionId)
                    .get();

            System.out.println(doc2);
*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getUserAgent() {
        String os = System.getProperty("os.name", "unknown").toLowerCase();
        if (os.startsWith("windows"))
            return "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.77 Safari/537.36";
        else if (os.startsWith("mac"))
            return "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_1) AppleWebKit/537.73.11 (KHTML, like Gecko) Version/7.0.1 Safari/537.73.11";
        else
            return "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:25.0) Gecko/20100101 Firefox/25.0";
    }

}
