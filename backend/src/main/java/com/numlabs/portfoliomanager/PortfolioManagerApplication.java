package com.numlabs.portfoliomanager;

import com.numlabs.portfoliomanager.util.PriceUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class PortfolioManagerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(PortfolioManagerApplication.class, args);
		PriceUtil priceUtil = ctx.getBean(PriceUtil.class);
     //   priceUtil.parseCompanyPrices("ASELS.IS.csv");

        priceUtil.calculateRatios("ASELS");

	}

}
