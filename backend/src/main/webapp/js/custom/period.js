var parseQueryString = function(url) {
 	var urlParams = {};

 	url.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0, $1, $2, $3) {
 		urlParams[$1] = $3;
 	});

 	return urlParams;
 }

$(document).ready(function() {
	var urlToParse = location.search;
	var result = parseQueryString(urlToParse);

    $("#delete-btn").hide();
    resetFields();

	$.get("/portfoliomng/exchange/all", function(data, status) {
        if (status == 'success') {
            var exchanges = "";

            for (var i = 0; i < data.length; i++) {
                exchanges += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }

            $('#exchange').append(exchanges);
            $('#search-exchange').append(exchanges);
        } else {
            alert("No exchanges found registered!");
        }
    });
});

function uploadPeriodsFile() {
    $("#recalculate-div").show();
    resetFields();
    $("#delete-btn").hide();

    var fileInput = document.getElementById("file");

    if (fileInput.files.length == 0) {
        alert("Please choose a file");
        return;
    }
    var xhr = new XMLHttpRequest();

    xhr.onload = function() {
        if (xhr.status == 200) {
            //setTimeout(function(){alert("Parsing completed successfully.")}, 1000);
        } else {
            alert("Error! Parsing failed");
        }
    };

    xhr.onerror = function() {
        alert("Error! Upload failed. Can not connect to server.");
    };

     xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            loadPeriodData(xhr.response);
        }
      }

    xhr.open("POST", "/portfoliomng/period/file/upload", true);

    var formData = new FormData();
    formData.append("file", fileInput.files[0]);
    xhr.send(formData);
}

function loadPeriodData(data){
    var period = data;

    if (typeof data === 'string' || data instanceof String) {
        period = JSON.parse(data);
    }

    $('#name').val(period.name);
    $('#earnings-date').val(period.earningsDate.substring(0, 16));

    $('#c-assets').val(period.balanceSheet.currentAssets);
    $('#cash-equv').val(period.balanceSheet.cashAndEquivalents);
    $('#trd-receivables').val(period.balanceSheet.tradeReceivables);
    $('#inventories').val(period.balanceSheet.inventories);
    $('#prepayments').val(period.balanceSheet.prepayments);
    $('#t-assets').val(period.balanceSheet.totalAssets);
    $('#prop-plant-equp').val(period.balanceSheet.propertyPlantEquipment);
    $('#i-assets').val(period.balanceSheet.intangibleAssets);
    $('#c-liabilities').val(period.balanceSheet.currentLiabilities);
    $('#s-debt').val(period.balanceSheet.shortTermDebt);
    $('#cp-long-debt').val(period.balanceSheet.currentPortionOfLongTermDebt);

    $('#t-payables').val(period.balanceSheet.tradePayables);
    $('#t-liabilities').val(period.balanceSheet.totalLiabilities);
    $('#l-debt').val(period.balanceSheet.longTermDebt);
    $('#equity').val(period.balanceSheet.equity);
    $('#r-earnings').val(period.balanceSheet.retainedEarnings);

    $('#revenue').val(period.incomeStatement.revenue);
    $('#sales-abroad').val(period.incomeStatement.salesAbroad);
    $('#sales-local').val(period.incomeStatement.salesLocal);
    $('#g-profit').val(period.incomeStatement.grossProfit);
    $('#g-a-expenses').val(period.incomeStatement.generalAdministrativeExpenses);
    $('#smd-expenses').val(period.incomeStatement.sellingMarketingDistributionExpenses);
    $('#rd-expenses').val(period.incomeStatement.researchDevelopmentExpenses);
    $('#oth-opr-income').val(period.incomeStatement.otherOperatingIncome);
    $('#oth-opr-expenses').val(period.incomeStatement.otherOperatingExpense);
    $('#ebit').val(period.incomeStatement.operatingProfit);
    $('#n-ebit').val(period.incomeStatement.nonOperatingProfit);
    $('#fin-income').val(period.incomeStatement.financialIncome);
    $('#fin-expenses').val(period.incomeStatement.financialExpenses);
    $('#tax-expenses').val(period.incomeStatement.taxExpenses);
    $('#n-profit').val(period.incomeStatement.netProfit);

    $('#cash-operations').val(period.cashFlowStatement.operatingActivitiesCash);
    $('#dep-amort').val(period.cashFlowStatement.depAndAmrtExpenses);
    $('#cash-investments').val(period.cashFlowStatement.investingActivitiesCash);
    $('#capex').val(period.cashFlowStatement.capitalExpenditures);
    $('#cash-finacing').val(period.cashFlowStatement.financingAtivitiesCash);
    $('#dividend').val(period.cashFlowStatement.dividendPayments);
    $('#debt-issued').val(period.cashFlowStatement.debtIssued);
    $('#debt-repaid').val(period.cashFlowStatement.debtPayments);
    $('#cash').val(period.cashFlowStatement.cash);
    $('#higher-price').val(period.higherPrice);
    $('#fair-price').val(period.fairPrice);
    $('#lower-price').val(period.lowerPrice);
}

function addPeriod() {
    var name = $('#name').val();
    var tickerSymbol = $('#ticker-symbol').val();
    var earningsDate = $('#earnings-date').val();
    var exchange = $('#exchange').val();
	var shares = $('#shares-oustanding').val();

	var revenue = $('#revenue').val();
	var salesAbroad = $('#sales-abroad').val();
	var salesLocal = $('#sales-local').val();
	var grossProfit = $('#g-profit').val();
	var operatingProfit = $('#ebit').val();
	var netProfit = $('#n-profit').val();

    var operatingActivitiesCash = $('#cash-operations').val();
    var investingActivitiesCash = $('#cash-investments').val();
    var capitalExpenditures = $('#capex').val();
    var financingAtivitiesCash = $('#cash-finacing').val();
    var cash = $('#cash').val();

    var currentAssets =  $('#c-assets').val();
    var cashAndEquivalents =  $('#cash-equv').val();
    var totalAssets =  $('#t-assets').val();
    var intangibleAssets =  $('#i-assets').val();
    var currentLiabilities =  $('#c-liabilities').val();
    var totalLiabilities =  $('#t-liabilities').val();
    var longTermDebt =  $('#l-debt').val();
    var equity =  $('#equity').val();
    var shortTermDebt =  $('#s-debt').val();
    var currentPortionOfLongTermDebt =  $('#cp-long-debt').val();
	var description = $('#description').val();
	var recalculate = $('#recalculate').is(':checked');

	var higherPrice = $('#higher-price').val();
	var fairPrice = $('#fair-price').val();
	var lowerPrice = $('#lower-price').val();

	if (tickerSymbol == '' || exchange == '0' || name == '' || earningsDate == '' || shares == ''
	    || revenue == '' || grossProfit == '' || operatingProfit == '' || netProfit == ''
	    || cash == '' || financingAtivitiesCash == '' || capitalExpenditures == '' || investingActivitiesCash == '' || operatingActivitiesCash == ''
	    || shortTermDebt == '' || equity == '' || longTermDebt == '' || totalLiabilities == '' || currentLiabilities == '' || intangibleAssets == ''
	    || totalAssets == '' || cashAndEquivalents == '' || currentAssets == ''
	) {
		alert("Please fill all required fields!");
	} else {

		var period = {
			"name" : name,
			"earningsDate" : earningsDate,
			"sharesOutstanding" : shares,
			"higherPrice": higherPrice,
			"fairPrice": fairPrice,
			"lowerPrice": lowerPrice,
            "cashFlowStatement": {
                "operatingActivitiesCash": operatingActivitiesCash ,
                "depAndAmrtExpenses": $('#dep-amort').val(),
                "investingActivitiesCash": investingActivitiesCash ,
                "capitalExpenditures": capitalExpenditures,
                "financingAtivitiesCash": financingAtivitiesCash,
                "dividendPayments": $('#dividend').val(),
                "debtIssued": $('#debt-issued').val(),
                "debtPayments": $('#debt-repaid').val(),
                "cash": cash
            },
            "balanceSheet": {
                "currentAssets": currentAssets ,
                "cashAndEquivalents": cashAndEquivalents ,
                "tradeReceivables":  $('#trd-receivables').val(),
                "inventories": $('#inventories').val(),
                "prepayments": $('#prepayments').val(),
                "totalAssets": totalAssets ,
                "propertyPlantEquipment": $('#prop-plant-equp').val(),
                "intangibleAssets": intangibleAssets ,
                "currentLiabilities": currentLiabilities ,
                "shortTermDebt": shortTermDebt ,
                "currentPortionOfLongTermDebt" :currentPortionOfLongTermDebt,
                "tradePayables": $('#t-payables').val(),
                "totalLiabilities": totalLiabilities ,
                "longTermDebt": longTermDebt ,
                "equity": equity ,
                "retainedEarnings": $('#r-earnings').val()
            },
            "incomeStatement": {
                "revenue": revenue,
                "salesAbroad" : salesAbroad,
                "salesLocal" : salesLocal,
                "grossProfit": grossProfit,
                "sellingMarketingDistributionExpenses": $('#smd-expenses').val(),
                "generalAdministrativeExpenses": $('#g-a-expenses').val(),
                "researchDevelopmentExpenses": $('#rd-expenses').val(),
                "otherOperatingIncome": $('#oth-opr-income').val(),
                "otherOperatingExpense": $('#oth-opr-expenses').val(),
                "operatingProfit": operatingProfit,
                "nonOperatingProfit": $('#n-ebit').val(),
                "financialIncome": $('#fin-income').val(),
                "financialExpenses":  $('#fin-expenses').val(),
                "taxExpenses": $('#tax-expenses').val(),
                "netProfit": netProfit
             },
            "company" :{
                "tickerSymbol" : tickerSymbol,
                "exchange" : {
                    "id": exchange
                }
            }
		};

		if(recalculate == true) {
            period.id = -1;
		} else {
		    period.id = 0;
		}

		var execUrl = "/portfoliomng/period/add";
		var oprMode = $('#mode').val();

        if(oprMode == "edit") {
            execUrl = "/portfoliomng/period/update";

            period.id = $('#id').val();
            period.incomeStatement.id = $('#income-id').val();
            period.balanceSheet.id = $('#balance-id').val();
            period.cashFlowStatement.id = $('#cash-id').val();
        }

		jQuery.ajax({
			url : execUrl,
			type : "POST",
			data : JSON.stringify(period),
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			complete : function(data) {
				if (data.status == 200) {
					alert("The operation completed successfully.");
				} else if (data.status == 417) {
                    alert(data.responseText);
                }
			}
		});
	}
}

function resetFields() {
    $('#name').val('');
    $('#earnings-date').val('');
    $('#ticker-symbol').val('');
    $('#exchange').val(0);
    $('#shares-oustanding').val('');
    $('#earnings-date').val('');

    $('#c-assets').val('');
    $('#cash-equv').val('');
    $('#trd-receivables').val('');
    $('#inventories').val('');
    $('#prepayments').val('');
    $('#t-assets').val('');
    $('#prop-plant-equp').val('');
    $('#i-assets').val('');
    $('#c-liabilities').val('');
    $('#s-debt').val('');
    $('#t-payables').val('');
    $('#t-liabilities').val('');
    $('#l-debt').val('');
    $('#equity').val('');
    $('#r-earnings').val('');
    $('#cp-long-debt').val('');
    $('#revenue').val('');
    $('#sales-abroad').val('');
    $('#sales-local').val('');
    $('#g-profit').val('');
    $('#g-a-expenses').val('');
    $('#smd-expenses').val('');
    $('#rd-expenses').val('');
    $('#oth-opr-income').val('');
    $('#oth-opr-expenses').val('');
    $('#ebit').val('');
    $('#n-ebit').val('');
    $('#fin-income').val('');
    $('#fin-expenses').val('');
    $('#tax-expenses').val('');
    $('#n-profit').val('');

    $('#cash-operations').val('');
    $('#dep-amort').val('');
    $('#cash-investments').val('');
    $('#capex').val('');
    $('#cash-finacing').val('');
    $('#dividend').val('');
    $('#debt-issued').val('');
    $('#debt-repaid').val('');
    $('#cash').val('');
    $('#higher-price').val('');
    $('#fair-price').val('');
    $('#lower-price').val('');
}

function searchPeriod() {
    var tickerSymbol = $('#company-search').val();
    var exchange = $('#search-exchange').val();
    var periodName = $('#search-name').val();

    if(tickerSymbol.trim() == '' || exchange == 0 || periodName == '')  {
        alert("Please fill required ticket symbol, exchange and period name fields. ");
    } else {
        resetFields();

        $("#delete-btn").hide();
        $("#submit-btn").html("Add Period");
        $("#recalculate-div").show();

        $.get("/portfoliomng/period/search/" + tickerSymbol + "." + exchange + "." + periodName, function(data, status) {
           if (status == 'success') {
                if(data.name != null && data.name != undefined) {
                    loadPeriodData(data);
                    $('#exchange').val(data.company.exchange.id);
                    $('#ticker-symbol').val(data.company.tickerSymbol);
                    $('#shares-oustanding').val(data.sharesOutstanding);

                    $('#id').val(data.id);
                    $('#balance-id').val(data.balanceSheet.id);
                    $('#income-id').val(data.incomeStatement.id);
                    $('#cash-id').val(data.cashFlowStatement.id);

                    $("#delete-btn").show();
                    $("#submit-btn").html("Update");
                    $("#submit-btn").show();
                    $('#mode').val("edit");
                    $("#recalculate-div").hide();
                } else {
                    alert("Period not found.");
                }
            } else {
                $('#mode').val("add");
                alert("No Period found for the specified criteria.");
                $("#recalculate-div").show();
                $("#delete-btn").hide();
            }
        });
    }
}

function removePeriod() {
    $.get("/portfoliomng/period/remove/" + $('#id').val(), function(data, status) {
       if (status == 'success') {
           alert("The operation completed successfully.");

           $("#delete-btn").hide();
           $("#submit-btn").hide();
       } else {
            alert("Error code: " + data.status + " returned.");
       }
   });
}