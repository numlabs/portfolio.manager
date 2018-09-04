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

    $.get("/portfoliomng/period/pricing/types/all", function(data, status) {
            if (status == 'success') {
                var types = "";

                for (var i = 0; i < data.length; i++) {
                    types += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                }
                $('#pricing-period-type').append(types);
            } else {
                alert("No pricing period types found registered!");
            }
        });

});

function submit() {
    var tickerSymbol = $('#ticker-symbol').val();
    var earningsDate = $('#earnings-date').val();
    var exchange = $('#exchange').val();
	var shares = $('#shares-oustanding').val();
    var startDate = $('#start-date').val();

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
                "pricingPeriodType": {
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
