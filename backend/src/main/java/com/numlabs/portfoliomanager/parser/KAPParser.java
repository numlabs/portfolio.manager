package com.numlabs.portfoliomanager.parser;

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

    private Period readFile(Iterator<Row> iterator) throws ParseException {
        Period p = new Period();
        BalanceSheet bs = new BalanceSheet();
        IncomeStatement is = new IncomeStatement();
        CashFlowStatement cf = new CashFlowStatement();
    //    bs.setPeriod(p);
     //   is.setPeriod(p);
     //   cf.setPeriod(p);

        p.setBalanceSheet(bs);
        p.setIncomeStatement(is);
        p.setCashFlowStatement(cf);

        BigDecimal multiplier = null;
        int rowCursor = 1;

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Iterator<Cell> cellIter = currentRow.cellIterator();
            String currentElementKey = cellIter.next().getStringCellValue().trim();

            if (currentElementKey.startsWith("Publish Date") && rowCursor == 5) {
                String earningsDate = currentElementKey.substring(currentElementKey.indexOf(":") + 1, currentElementKey.indexOf("Di"));
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date dt = df.parse(earningsDate);
                p.setEarningsDate(dt);

                String year = currentElementKey.substring(currentElementKey.indexOf("r:") + 2, currentElementKey.indexOf("Period") - 1);
                char period = currentElementKey.trim().charAt(currentElementKey.length() - 1);
                p.setName(year + "_Q" + period);
            } else  if (currentElementKey.startsWith("Presentation Currency") && (rowCursor == 6 || rowCursor == 301 || rowCursor == 461)) {
                String currencyMultiplier = cellIter.next().getStringCellValue().trim();

                if(currencyMultiplier.length() > 2) {
                    String multi = currencyMultiplier.substring(0, currencyMultiplier.indexOf("TL") - 1).trim();

                    if(multi.length() != 0) {
                        multiplier = new BigDecimal(multi.replace(".", ""));
                    } else {
                        multiplier = new BigDecimal(1);
                    }
                } else {
                    multiplier = new BigDecimal(1);
                }
            } else if (rowCursor == 13) {
                bs.setCashAndEquivalents(readPeriodValue(cellIter, "Cash and cash equivalents", multiplier,3));
            } else if (rowCursor == 31) {
                bs.setTradeReceivables(readPeriodValue(cellIter, "Trade Receivables", multiplier,3));
            } else if (rowCursor == 48) {
                bs.setInventories(readPeriodValue(cellIter, "Inventories", multiplier,3));
            } else if (rowCursor == 51) {
                bs.setPrepayments(readPeriodValue(cellIter, "Prepayments", multiplier,3));
            } else if (rowCursor == 63) {
                bs.setCurrentAssets(readPeriodValue(cellIter, "Total current assets", multiplier,3));
            } else if (rowCursor == 102) {
                bs.setPropertyPlantEquipment(readPeriodValue(cellIter, "Property, plant and equipment", multiplier,3));
            } else if (rowCursor == 115) {
                bs.setIntangibleAssets(readPeriodValue(cellIter, "Intangible assets and goodwill", multiplier,3));
            } else if (rowCursor == 135) {
                bs.setTotalAssets(readPeriodValue(cellIter, "Total assets", multiplier,3));
            } else if (rowCursor == 138) {
                bs.setShortTermDebt(readPeriodValue(cellIter, "Current Borrowings", multiplier,3));
            } else if (rowCursor == 164) {
                bs.setTradePayables(readPeriodValue(cellIter, "Trade Payables", multiplier,3));
            } else if (rowCursor == 196) {
                bs.setCurrentLiabilities(readPeriodValue(cellIter, "Total current liabilities", multiplier,3));
            } else if (rowCursor == 198) {
                bs.setLongTermDebt(readPeriodValue(cellIter, "Long Term Borrowings", multiplier,3));
                bs.setTotalDebt(bs.getShortTermDebt().add(bs.getLongTermDebt()));
            } else if (rowCursor == 246) {
                bs.setTotalLiabilities(readPeriodValue(cellIter, "Total liabilities", multiplier,3));
            } else if (rowCursor == 296) {
                bs.setRetainedEarnings(readPeriodValue(cellIter, "Prior Years' Profits or Losses", multiplier,3));
            } else if (rowCursor == 299) {
                bs.setEquity(readPeriodValue(cellIter, "Total equity", multiplier,3));
            } else if (rowCursor == 307) {
                is.setRevenue(readPeriodValue(cellIter, "Revenue", multiplier, 4));
            } else if (rowCursor == 335) {
                is.setGrossProfit(readPeriodValue(cellIter, "GROSS PROFIT (LOSS)", multiplier, 4));
            } else if (rowCursor == 336) {
                is.setGeneralAdministrativeExpenses(readPeriodValue(cellIter, "General Administrative Expenses", multiplier, 4));
            } else if (rowCursor == 337) {
                is.setSellingMarketingDistributionExpenses(readPeriodValue(cellIter, "Marketing Expenses", multiplier, 4));
            } else if (rowCursor == 338) {
                is.setResearchDevelopmentExpenses(readPeriodValue(cellIter, "Research and development expense", multiplier, 4));
            } else if (rowCursor == 339) {
                is.setOtherOperatingIncome(readPeriodValue(cellIter, "Other Income from Operating Activities", multiplier, 4));
            } else if (rowCursor == 340) {
                is.setOtherOperatingExpense(readPeriodValue(cellIter, "Other Expenses from Operating Activities", multiplier, 4));
            } else if (rowCursor == 342) {
                is.setOperatingProfit(readPeriodValue(cellIter, "PROFIT (LOSS) FROM OPERATING ACTIVITIES", multiplier, 4));
            } else if (rowCursor == 354) {
                is.setFinancialIncome(readPeriodValue(cellIter, "Finance income", multiplier, 4));
            } else if (rowCursor == 355) {
                is.setFinancialExpenses(readPeriodValue(cellIter, "Finance costs", multiplier, 4));
            } else if (rowCursor == 358) {
                is.setTaxExpenses(readPeriodValue(cellIter, "Tax (Expense) Income, Continuing Operations", multiplier, 4));
            } else if (rowCursor == 363) {
                is.setNetProfit(readPeriodValue(cellIter, "PROFIT (LOSS)", multiplier, 4));
            } else if (rowCursor == 466) {
                cf.setOperatingActivitiesCash(readPeriodValue(cellIter, "CASH FLOWS FROM (USED IN) OPERATING ACTIVITIES", multiplier, 3));
            } else if (rowCursor == 471) {
                cf.setDepAndAmrtExpenses(readPeriodValue(cellIter, "Adjustments for depreciation and amortisation expense", multiplier, 3));
            } else if (rowCursor == 577) {
                cf.setInvestingActivitiesCash(readPeriodValue(cellIter, "CASH FLOWS FROM (USED IN) INVESTING ACTIVITIES", multiplier, 3));
            } else if (rowCursor == 591) {
                cf.setCapitalExpenditures(readPeriodValue(cellIter, "Purchase of property, plant and equipment", multiplier, 3));
            } else if (rowCursor == 618) {
                cf.setFinancingAtivitiesCash(readPeriodValue(cellIter, "CASH FLOWS FROM (USED IN) FINANCING ACTIVITIES", multiplier, 3));
            } else if (rowCursor == 634) {
                cf.setDebtIssued(readPeriodValue(cellIter, "Proceeds from borrowings", multiplier, 3));
            } else if (rowCursor == 639) {
                cf.setDebtPayments(readPeriodValue(cellIter, "Repayments of borrowings", multiplier, 3));
            } else if (rowCursor == 650) {
                cf.setDividendPayments(readPeriodValue(cellIter, "Dividends Paid", multiplier, 3));
            } else if (rowCursor == 662) {
                cf.setCash(readPeriodValue(cellIter, "CASH AND CASH EQUIVALENTS AT THE END OF THE PERIOD", multiplier, 3));
            }

            rowCursor++;
        }
       return p;
    }

    private BigDecimal readPeriodValue(Iterator<Cell> cellIter, String fieldName, BigDecimal multiplier, int passReads) throws ParseException {
        cellIter.next();
        String currentCell = cellIter.next().getStringCellValue().trim();

        if(currentCell.startsWith(fieldName)) {
            for(int i =0; i< passReads;i++){
                cellIter.next();
            }
            return getValue(cellIter.next(), multiplier);
        } else  {
            throw new ParseException(fieldName + " not found.", -1);
        }
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
