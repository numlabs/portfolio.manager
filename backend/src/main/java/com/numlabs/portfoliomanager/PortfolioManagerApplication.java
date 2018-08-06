package com.numlabs.portfoliomanager;

import com.numlabs.portfoliomanager.util.PriceUtil;
import com.numlabs.portfoliomanager.util.YahooUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class PortfolioManagerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(PortfolioManagerApplication.class, args);
		PriceUtil priceUtil = ctx.getBean(PriceUtil.class);
		YahooUtils yUtil = ctx.getBean(YahooUtils.class);
      //  yUtil.getPriceForStock("ASELS.IS");
     //   priceUtil.parseCompanyPrices("ASELS.IS.csv");

       // priceUtil.calculatePtoBVRatios("ASELS");
      //  priceUtil.calculateEBITtoEVRatios("ASELS");

	}

}
