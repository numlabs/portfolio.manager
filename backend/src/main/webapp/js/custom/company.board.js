var parseQueryString = function(url) {
 	var urlParams = {};

 	url.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0, $1, $2, $3) {
 		urlParams[$1] = $3;
 	});

 	return urlParams;
 }

$(document).ready(function() {
	var urlToParse = location.search;
	var urlParameters = parseQueryString(urlToParse);

	$.get("/portfoliomng/company/board/" + urlParameters.id).done(function(data) {
		var tableData = "";
        var periods = data.periods;

        if(periods.length == 0) {
            $('#period-table-headers').append("No Periods registered.");
            return;
        }

		tableData += "<th style='text-align: left;' >" + data.tickerSymbol + "</th>";

		for (i = 0; i < periods.length; i++) {
			tableData += "<th style='text-align: right;' >" + periods[i].name + "</th>";
		}

		$('#period-table-headers').append(tableData);
		$('#period-table-body').not(':first').not(':last').remove();

        if(urlParameters.companySectorCode === 'BANK') {
            tableData = getBankPeriodData(periods);
        } else {
		    tableData = getPeriodData(periods);
		}

		$('#period-table-body').append(tableData);
	}).fail(function() {
		alert("The process could not be completed.");
	});
});

function getBankPeriodData(data) {
    var tableRow = "";

	tableRow += "<tr><td>Assets</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].bankStatement.assets) + "</td>";
    }

    tableRow += "</tr><td>Liabilities</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].bankStatement.liabilities) + "</td>";
    }

    tableRow += "</tr><td><b>Equity</b></td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'><b>" + formatValue(data[i].bankStatement.equity) + "</b></td>";
    }

    tableRow += "</tr><td>Loans</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].bankStatement.loans) + "</td>";
    }

    tableRow += "</tr><td>Deposits</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].bankStatement.deposits) + "</td>";
    }

    tableRow += "</tr><td>Intangible Assets</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].bankStatement.intangibleAssets) + "</td>";
    }

    tableRow += "</tr><td>Dividends</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].bankStatement.dividends) + "</td>";
    }

    tableRow += "</tr><td>Interest Income</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].bankStatement.interestIncome) + "</td>";
    }

    tableRow += "</tr><td>Interest Expenses</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].bankStatement.interestExpenses) + "</td>";
    }

    tableRow += "</tr><td><b>Net Income</b></td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'><b>" + formatValue(data[i].bankStatement.netIncome) + "</b></td>";
    }

	tableRow += "</tr>";

	return tableRow;

}

function getPeriodData(data) {
    var tableRow = "";

	tableRow += "<tr><td><b>BALANCE SHEET</b></td></tr>";

	tableRow += "</tr><td><b>Current Assets</b></td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'><b>" + formatValue(data[i].balanceSheet.currentAssets) + "</b></td>";
    }

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Cash</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.cashAndEquivalents) + "</td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Trade Receivables</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.tradeReceivables) + "</td>";
	}

    tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Inventories</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.inventories) + "</td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Prepayments</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.prepayments) + "</td>";
	}

	tableRow += "</tr><td><b>Total Assets</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].balanceSheet.totalAssets) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Prop., Plant & Eqp.</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.propertyPlantEquipment) + "</td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Int. Assets</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.intangibleAssets) + "</td>";
	}

    tableRow += "</tr><td><b>Current Liabilities</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].balanceSheet.currentLiabilities) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Trade Payables</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.tradePayables) + "</td>";
	}

	tableRow += "</tr><td><b>Total Liabilities</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].balanceSheet.totalLiabilities) + "</b></td>";
	}

	tableRow += "</tr><td><b>Equity</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].balanceSheet.equity) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Reatined Earnings</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.retainedEarnings) + "</td>";
	}

	tableRow += "</tr><td><b>Total Debt</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].balanceSheet.totalDebt) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;Short Term Debt</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.shortTermDebt) + "</td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;Long Term Debt</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].balanceSheet.longTermDebt) + "</td>";
	}

	tableRow += "</tr><tr><td></td></tr><tr><td><b>INCOME STATEMENT</b></td></tr>";

	tableRow += "</tr><td><b>Revenue</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].incomeStatement.revenue) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Sales Abroad (Yearly)</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.salesAbroad) + "</td>";
    }

    tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Sales Local Market (Yearly)</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.salesLocal) + "</td>";
    }

	tableRow += "</tr><td><b>Gross Profit</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].incomeStatement.grossProfit) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Sell., Mark. & Distr. Expenses</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.sellingMarketingDistributionExpenses) + "</td>";
    }

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Gen. Adm. Expenses</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.generalAdministrativeExpenses) + "</td>";
    }

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;R&D Expenses</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.researchDevelopmentExpenses) + "</td>";
    }

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Other Oper. Income</td>";

    for (i = 0; i < data.length; i++) {
        tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.otherOperatingIncome) + "</td>";
    }

    tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Other Oper. Expenses</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.otherOperatingExpense) + "</td>";
	}

	tableRow += "</tr><td><b>Operating Profit</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].incomeStatement.operatingProfit) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Tax Expenses</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.taxExpenses) + "</td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Finance Income</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.financialIncome) + "</td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Finance Cost</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].incomeStatement.financialExpenses) + "</td>";
	}

	tableRow += "</tr><td><b>Net Profit</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].incomeStatement.netProfit) + "</b></td>";
	}



	tableRow += "</tr><tr><td><b>CASH FLOW</b></td></tr><tr>";

	for (i = 0; i < data.length+1; i++) {
		tableRow += "<td align='right'></td>";
	}

	tableRow += "</tr><td><b>Operating Activities Cash</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].cashFlowStatement.operatingActivitiesCash) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Depr. & Amort. Exp.</td>";

    for (i = 0; i < data.length; i++) {
   		tableRow += "<td align='right'>" + formatValue(data[i].cashFlowStatement.depAndAmrtExpenses) + "</td>";
   	}

	tableRow += "</tr><td><b>Investing Activities Cash</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].cashFlowStatement.investingActivitiesCash) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Capex</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].cashFlowStatement.capitalExpenditures) + "</td>";
	}

	tableRow += "</tr><td><b>Financing Ativities Cash</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].cashFlowStatement.financingAtivitiesCash) + "</b></td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Dividend Payment</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].cashFlowStatement.dividendPayments) + "</td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Debt Issued</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].cashFlowStatement.debtIssued) + "</td>";
	}

	tableRow += "</tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Debt Payments</td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'>" + formatValue(data[i].cashFlowStatement.debtPayments) + "</td>";
	}

	tableRow += "</tr><td><b>Net Cash</b></td>";

	for (i = 0; i < data.length; i++) {
		tableRow += "<td align='right'><b>" + formatValue(data[i].cashFlowStatement.cash) + "</b></td>";
	}

	tableRow += "</tr>";

	return tableRow;
}

function formatPercentValue(value) {
     if(value == null || value == undefined) {
         return "";
     }

     return formatValue(value) + " %";
 }

 function formatValue(value) {
     if(value == null || value == undefined) {
         return "";
     }

     var newValue = value.toString();
     var counter = 0;
     var dotIndex = 0;
     var isNegativeNumber = false;
     var tempValue = '';

     if(value < 0) {
         isNegativeNumber = true;
     }

     if(newValue.includes('.') || newValue.includes(',')) {
         return value;
     }

     if(newValue.length < 4) {
         return value;
     } else {
         if(value < 0) {
             dotIndex = (newValue.length-1) % 3;
             value = value * -1;
             newValue = value.toString();
             tempValue = '-';
         } else {
             dotIndex = newValue.length % 3;
         }
     }

     for(var i=0; i<newValue.length ; i++) {
         tempValue += newValue.charAt(i);
         dotIndex--;

         if(dotIndex == 0 && (i+1)<newValue.length ) {
             tempValue += '.';
             dotIndex = 3;
         } else if(dotIndex == -1) {
             dotIndex = 2;
         }
     }

     return tempValue;
 }
