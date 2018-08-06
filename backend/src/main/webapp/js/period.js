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

	$.get("/portfoliomng/exchange/all", function(data, status) {
            if (status == 'success') {
                var exchanges = "";

                for (var i = 0; i < data.length; i++) {
                    exchanges += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                }

                $('#exchange').append(exchanges);
            } else {
                alert("No exchanges found registered!");
            }
    });

    $( "#bubum" ).submit(function( event ) {

      event.preventDefault();
      $( "#s-report" ).submit();
    });



});

function uploadPeriodsFile() {
    var fileInput = document.getElementById("file");

    if (fileInput.files.length == 0) {
        alert("Please choose a file");
        return;
    }
    var xhr = new XMLHttpRequest();

    xhr.onload = function() {
        if (xhr.status == 200) {
            alert("Sucess! Parsing completed");
        } else {
            alert("Error! Parsing failed");
        }
    };

    xhr.onerror = function() {
        alert("Error! Upload failed. Can not connect to server.");
    };

     xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
          updatePeriodData(xhr.response);
        }
      }

    xhr.open("POST", "portfoliomng/period/file/upload", true);

    var formData = new FormData();
    formData.append("file", fileInput.files[0]);
    xhr.send(formData);
}

function updatePeriodData(data){
    var period = JSON.parse(data);

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
    $('#t-payables').val(period.balanceSheet.tradePayables);
    $('#t-liabilities').val(period.balanceSheet.totalLiabilities);
    $('#l-debt').val(period.balanceSheet.longTermDebt);
    $('#equity').val(period.balanceSheet.equity);
    $('#r-earnings').val(period.balanceSheet.retainedEarnings);





    $('#revenue').val(period.incomeStatement.revenue);
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
}


function addPeriod() {
    var name = $('#name').val();
    var tickerSymbol = $('#ticker-symbol').val();
    var earningsDate = $('#earnings-date').val();
    var exchange = $('#exchange').val();
	var shares = $('#shares-oustanding').val();

	var revenue = $('#revenue').val();
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


	var description = $('#description').val();

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
                "tradePayables": $('#t-payables').val(),
                "totalLiabilities": totalLiabilities ,
                "longTermDebt": longTermDebt ,
                "equity": equity ,
                "retainedEarnings": $('#r-earnings').val()
            },
            "incomeStatement": {
                "revenue": revenue,
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

		var execUrl = "/portfoliomng/period/add";

		jQuery.ajax({
			url : execUrl,
			type : "POST",
			data : JSON.stringify(period),
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			complete : function(data) {
				if (data.status == 200) {
					alert("The operation completed successfully.");
				} else {
                    alert("Error code: " + data.status + " returned.");
                }
			}
		});
	}
}

