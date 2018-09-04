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

function addPeriod() {
    var name = $('#name').val();
    var tickerSymbol = $('#ticker-symbol').val();
    var earningsDate = $('#earnings-date').val();
    var exchange = $('#exchange').val();
	var shares = $('#shares-oustanding').val();
	var netIncome = $('#n-income').val();
    var assets = $('#assets').val();
    var intangibleAssets =  $('#i-assets').val();
    var liabilities = $('#liabilities').val();
    var equity =  $('#equity').val();
    var iIncome = $('#i-income').val();
    var iExpenses = $('#i-expenses').val();
    var loans = $('#loans').val();
    var deposits =  $('#deposits').val();
    var dividends = $('#dividends').val();

	if (tickerSymbol == '' || exchange == '0' || name == '' || shares == '' || netIncome == '' || assets == '' || liabilities == '' ) {
		alert("Please fill all required fields!");
	} else {

		var period = {
			"name" : name,
			"earningsDate" : earningsDate,
			"sharesOutstanding" : shares,
            "bankStatement": {
                "loans": loans ,
                "deposits": deposits ,
                "intangibleAssets": intangibleAssets,
                "interestExpenses": iExpenses,
                "interestIncome": iIncome,
                "netIncome": netIncome,
                "equity": equity,
                "liabilities": liabilities,
                "assets": assets,
                "dividends": dividends,
                "sharesOutstanding" : shares
            },
            "company" :{
                "tickerSymbol" : tickerSymbol,
                "exchange" : {
                    "id": exchange
                }
            }
		};

		var execUrl = "/portfoliomng/period/bank/add";
		var oprMode = $('#mode').val();

        if(oprMode == "edit") {
            execUrl = "/portfoliomng/period/bank/update";

            period.id = $('#id').val();
            period.bankStatement.id = $('#bank-id').val();
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

  	$('#n-income').val('');
    $('#assets').val('');
    $('#i-assets').val('');
    $('#liabilities').val('');
    $('#equity').val('');
    $('#i-income').val('');
    $('#i-expenses').val('');
    $('#loans').val('');
    $('#deposits').val('');
    $('#dividends').val('');
}
