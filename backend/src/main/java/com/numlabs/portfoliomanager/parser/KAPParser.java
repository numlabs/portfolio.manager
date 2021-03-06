package com.numlabs.portfoliomanager.parser;

import com.numlabs.portfoliomanager.PortfolioManagerException;
import com.numlabs.portfoliomanager.model.BalanceSheet;
import com.numlabs.portfoliomanager.model.CashFlowStatement;
import com.numlabs.portfoliomanager.model.IncomeStatement;
import com.numlabs.portfoliomanager.model.Period;
import com.numlabs.portfoliomanager.service.PeriodService;
import com.numlabs.portfoliomanager.util.Utils;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

@Component
public class KAPParser {

    public Period parseKAPPeriodExcelFile(InputStream inputStream) {
        try {
            return readFile(new HSSFWorkbook(inputStream).getSheet("Sheet1").iterator());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String[] BS_ELEMENTS = new String[] { "Cash and cash equivalents", "Trade Receivables", "Inventories",
            "Prepayments", "Total current assets", "Trade Receivables", "Property, plant and equipment", "Intangible assets and goodwill",
            "Prepayments", "Total assets",
            "Current Borrowings", "Current Portion of Non-current Borrowings", "Trade Payables", "Total current liabilities", "Long Term Borrowings", "Trade Payables",
            "Total liabilities", "Prior Years' Profits or Losses", "Non-controlling interests", "Total equity" };

    private static String[] IS_ELEMENTS = new String[] {"Revenue", "GROSS PROFIT (LOSS)", "General Administrative Expenses", "Marketing Expenses",
            "Research and development expense", "Other Income from Operating Activities", "Other Expenses from Operating Activities",
            "PROFIT (LOSS) FROM OPERATING ACTIVITIES", "Finance income", "Finance costs", "Tax (Expense) Income, Continuing Operations", "PROFIT (LOSS)", "Non-controlling Interests" };

    private static String[] CF_ELEMENTS = new String[] { "CASH FLOWS FROM (USED IN) OPERATING ACTIVITIES", "Adjustments for depreciation and amortisation expense",
            "CASH FLOWS FROM (USED IN) INVESTING ACTIVITIES", "Purchase of Property, Plant, Equipment and Intangible Assets",
            "CASH FLOWS FROM (USED IN) FINANCING ACTIVITIES", "Proceeds from borrowings", "Repayments of borrowings",
            "Dividends Paid", "CASH AND CASH EQUIVALENTS AT THE END OF THE PERIOD" };

    private Period readFile(Iterator<Row> iterator) throws ParseException, PortfolioManagerException {
        Period period = new Period();
        BalanceSheet bs = new BalanceSheet();
        IncomeStatement is = new IncomeStatement();
        CashFlowStatement cf = new CashFlowStatement();

        period.setBalanceSheet(bs);
        period.setIncomeStatement(is);
        period.setCashFlowStatement(cf);

        setEarningsDateAndName(period, iterator);
        readBalanceStatement(bs, iterator);
        readIncomeStatement(is, iterator);
        readCashFlowStatement(cf, iterator);

        return period;
    }

    private void readBalanceStatement(BalanceSheet bs, Iterator<Row> iterator) throws ParseException {
        int elementsCounter = 0;
        BigDecimal multiplier = null;

        while (iterator.hasNext()) {
            Iterator<Cell> cellIter = iterator.next().cellIterator();
            String currentElementKey = cellIter.next().getStringCellValue().trim();

            if (currentElementKey.startsWith("Presentation Currency")) {
                multiplier = readMultiplier(cellIter);
            } else {
                try {
                    cellIter.next();
                    currentElementKey = cellIter.next().getStringCellValue().trim();
                } catch(NoSuchElementException e) {
                    continue;
                }

                if (currentElementKey != null && currentElementKey.equals(BS_ELEMENTS[elementsCounter])) { // this guaranties the order of elements coming
                    if(currentElementKey.startsWith("Cash and cash equivalents")) {
                        bs.setCashAndEquivalents(readPeriodValue(cellIter, multiplier,3));
                    } else if(currentElementKey.startsWith("Trade Receivables")) {
                        BigDecimal temp = readPeriodValue(cellIter, multiplier,3);
                        bs.setTradeReceivables(bs.getTradeReceivables().add(temp));
                    } else if(currentElementKey.startsWith("Inventories")) {
                        BigDecimal temp = readPeriodValue(cellIter, multiplier,3);
                        bs.setInventories(bs.getInventories().add(temp));
                    }  else if(currentElementKey.startsWith("Prepayments")) {
                        BigDecimal temp = readPeriodValue(cellIter, multiplier,3);
                        bs.setPrepayments(bs.getPrepayments().add(temp));
                    } else if(currentElementKey.startsWith("Total current assets")) {
                        bs.setCurrentAssets(readPeriodValue(cellIter,  multiplier,3));
                    } else if(currentElementKey.startsWith("Property, plant and equipment")) {
                        bs.setPropertyPlantEquipment(readPeriodValue(cellIter, multiplier,3));
                    } else if(currentElementKey.startsWith("Intangible assets and goodwill")) {
                        bs.setIntangibleAssets(readPeriodValue(cellIter, multiplier,3));
                    } else if(currentElementKey.startsWith("Total assets")) {
                        bs.setTotalAssets(readPeriodValue(cellIter, multiplier,3));
                    } else if(currentElementKey.startsWith("Current Borrowings")) {
                        bs.setShortTermDebt(bs.getShortTermDebt().add(readPeriodValue(cellIter, multiplier,3)));
                    } else if(currentElementKey.startsWith("Current Portion of Non-current Borrowings")) {
                        bs.setCurrentPortionOfLongTermDebt(readPeriodValue(cellIter, multiplier,3));
                    } else if(currentElementKey.startsWith("Trade Payables")) {
                        BigDecimal temp = readPeriodValue(cellIter, multiplier,3);
                        bs.setTradePayables(bs.getTradePayables().add(temp));
                    } else if(currentElementKey.startsWith("Total current liabilities")) {
                        bs.setCurrentLiabilities(readPeriodValue(cellIter, multiplier,3));
                    } else if(currentElementKey.startsWith("Long Term Borrowings")) {
                        bs.setLongTermDebt(readPeriodValue(cellIter, multiplier,3));
                        bs.setTotalDebt(bs.getShortTermDebt().add(bs.getLongTermDebt()));
                    } else if(currentElementKey.startsWith("Total liabilities")) {
                        bs.setTotalLiabilities(readPeriodValue(cellIter, multiplier,3));
                    } else if(currentElementKey.startsWith("Prior Years' Profits or Losses")) {
                        bs.setRetainedEarnings(readPeriodValue(cellIter, multiplier,3));
                    } else if(currentElementKey.startsWith("Non-controlling interests")) {
                        bs.setMinorityInterest(readPeriodValue(cellIter, multiplier,3));
                    }  else if(currentElementKey.startsWith("Total equity")) {
                        bs.setEquity(readPeriodValue(cellIter, multiplier,3));
                    } else {
                        throw new ParseException("Unexpected element found " + currentElementKey, 0);
                    }

                    elementsCounter++;

                    if (elementsCounter == BS_ELEMENTS.length) {
                        return;
                    }
                }
            }
        }
    }

    private void readIncomeStatement(IncomeStatement is, Iterator<Row> iterator) throws ParseException {
        int elementsCounter = 0;
        BigDecimal multiplier = null;

        while (iterator.hasNext()) {
            Iterator<Cell> cellIter = iterator.next().cellIterator();
            String currentElementKey = cellIter.next().getStringCellValue().trim();

            if (currentElementKey.startsWith("Presentation Currency")) {
                multiplier = readMultiplier(cellIter);
            } else {
                cellIter.next();

                try {
                    currentElementKey = cellIter.next().getStringCellValue().trim();
                } catch(NoSuchElementException e) {
                    continue;
                }

                if (currentElementKey != null && currentElementKey.equals(IS_ELEMENTS[elementsCounter])) { // this guaranties the order of elements coming
                    if(currentElementKey.startsWith("Revenue")) {
                        is.setRevenue(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.equals("GROSS PROFIT (LOSS)")) {
                        is.setGrossProfit(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("General Administrative Expenses")) {
                        is.setGeneralAdministrativeExpenses(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("Marketing Expenses")) {
                        is.setSellingMarketingDistributionExpenses(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("Research and development expense")) {
                        is.setResearchDevelopmentExpenses(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("Other Income from Operating Activities")) {
                        is.setOtherOperatingIncome(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("Other Expenses from Operating Activities")) {
                        is.setOtherOperatingExpense(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("PROFIT (LOSS) FROM OPERATING ACTIVITIES")) {
                        is.setOperatingProfit(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("Finance income")) {
                        is.setFinancialIncome(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("Finance costs")) {
                        is.setFinancialExpenses(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("Tax (Expense) Income, Continuing Operations")) {
                        is.setTaxExpenses(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("PROFIT (LOSS)")) {
                        is.setNetProfit(readPeriodValue(cellIter, multiplier, 4));
                    } else if(currentElementKey.startsWith("Non-controlling Interests")) {
                        is.setMinorityInterest(readPeriodValue(cellIter, multiplier, 4));
                    } else {
                        throw new ParseException("Unexpected element found " + currentElementKey, 0);
                    }

                    elementsCounter++;

                    if (elementsCounter == IS_ELEMENTS.length) {
                        return;
                    }
                }
            }
        }
    }

    private void readCashFlowStatement(CashFlowStatement cf, Iterator<Row> iterator) throws ParseException {
        int elementsCounter = 0;
        BigDecimal multiplier = null;

        while (iterator.hasNext()) {
            Iterator<Cell> cellIter = iterator.next().cellIterator();
            String currentElementKey = cellIter.next().getStringCellValue().trim();

            if (currentElementKey.startsWith("Presentation Currency")) {
                multiplier = readMultiplier(cellIter);
            } else {
                cellIter.next();

                try {
                    currentElementKey = cellIter.next().getStringCellValue().trim();
                } catch(NoSuchElementException e) {
                    continue;
                }

                if (currentElementKey != null && currentElementKey.trim().equals(CF_ELEMENTS[elementsCounter])) { // this guaranties the order of elements coming
                    if(currentElementKey.startsWith("CASH FLOWS FROM (USED IN) OPERATING ACTIVITIES")) {
                        cf.setOperatingActivitiesCash(readPeriodValue(cellIter, multiplier, 3));
                    } else if(currentElementKey.startsWith("Adjustments for depreciation and amortisation expense")) {
                        cf.setDepAndAmrtExpenses(readPeriodValue(cellIter, multiplier, 3));
                    } else if(currentElementKey.startsWith("CASH FLOWS FROM (USED IN) INVESTING ACTIVITIES")) {
                        cf.setInvestingActivitiesCash(readPeriodValue(cellIter, multiplier, 3));
                    } else if(currentElementKey.startsWith("Purchase of Property, Plant, Equipment and Intangible Assets")) {
                        cf.setCapitalExpenditures(readPeriodValue(cellIter, multiplier, 3));
                    } else if(currentElementKey.startsWith("CASH FLOWS FROM (USED IN) FINANCING ACTIVITIES")) {
                        cf.setFinancingAtivitiesCash(readPeriodValue(cellIter, multiplier, 3));
                    } else if(currentElementKey.startsWith("Proceeds from borrowings")) {
                        cf.setDebtIssued(readPeriodValue(cellIter, multiplier, 3));
                    } else if(currentElementKey.startsWith("Repayments of borrowings")) {
                        cf.setDebtPayments(readPeriodValue(cellIter, multiplier, 3));
                    } else if(currentElementKey.startsWith("Dividends Paid")) {
                        cf.setDividendPayments(readPeriodValue(cellIter, multiplier, 3));
                    } else if(currentElementKey.startsWith("CASH AND CASH EQUIVALENTS AT THE END OF THE PERIOD")) {
                        cf.setCash(readPeriodValue(cellIter, multiplier, 3));
                    } else {
                        throw new ParseException("Unexpected element found " + currentElementKey, 0);
                    }
                    elementsCounter++;

                    if (elementsCounter == CF_ELEMENTS.length) {
                        return;
                    }
                }
            }
        }
    }

    private BigDecimal readMultiplier(Iterator<Cell> cellIter) {
        String currencyMultiplier = cellIter.next().getStringCellValue().trim();

        if(currencyMultiplier.length() > 2) {
            String multi = currencyMultiplier.substring(0, currencyMultiplier.indexOf("TL") - 1).trim();

            if(multi.length() != 0) {
                return new BigDecimal(multi.replace(".", ""));
            } else {
                return new BigDecimal(1);
            }
        } else {
            return new BigDecimal(1);
        }
    }

    private void setEarningsDateAndName(Period period, Iterator<Row> iterator) throws ParseException, PortfolioManagerException {

        while (iterator.hasNext()) {
            Iterator<Cell> cellIter = iterator.next().cellIterator();
            String currentElementKey = cellIter.next().getStringCellValue().trim();

            if (currentElementKey.startsWith("Publish Date")) {
                String earningsDate = currentElementKey.substring(currentElementKey.indexOf(":") + 1, currentElementKey.indexOf("Di"));
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date dt = df.parse(earningsDate);
                period.setEarningsDate(dt);

                String year = currentElementKey.substring(currentElementKey.indexOf("r:") + 2, currentElementKey.indexOf("Period") - 1);
                char periodNumber = currentElementKey.charAt(currentElementKey.length() - 1);
                period.setName(year + "_Q" + periodNumber);
                return;
            }
        }

        throw new PortfolioManagerException("Earning date and name could not be read.");
    }

    private BigDecimal readPeriodValue(Iterator<Cell> cellIter, BigDecimal multiplier, int passReads) throws ParseException {

        for(int i = 0; i < passReads; i++) {
            cellIter.next();
        }

        return getValue(cellIter.next(), multiplier);
    }

    private BigDecimal getValue(Cell cell,  BigDecimal multiplier) {
        cell.setCellType(Cell.CELL_TYPE_STRING);
        String cellValue = Utils.normalizeNumbericValue(cell.getStringCellValue());

        if (cellValue == null || cellValue.isEmpty()) {
            return new BigDecimal(0);
        }

        return new BigDecimal(cellValue.trim()).multiply(multiplier).setScale(0, BigDecimal.ROUND_HALF_UP);
    }
}
