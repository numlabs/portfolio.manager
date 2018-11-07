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
    var name = $('#name').val().replace(/[,|.]/g, '').trim();
    var tickerSymbol = $('#ticker-symbol').val().replace(/[,|.]/g, '').trim();
    var earningsDate = $('#earnings-date').val().replace(/[,|.]/g, '').trim();
    var exchange = $('#exchange').val().replace(/[,|.]/g, '').trim();
	var shares = $('#shares-outstanding').val().replace(/[,|.]/g, '').trim();
	var netIncome = $('#n-income').val().replace(/[,|.]/g, '').trim();
    var assets = $('#assets').val().replace(/[,|.]/g, '').trim();
    var intangibleAssets =  $('#i-assets').val().replace(/[,|.]/g, '').trim();
    var liabilities = $('#liabilities').val().replace(/[,|.]/g, '').trim();
    var equity =  $('#equity').val().replace(/[,|.]/g, '').trim();
    var iIncome = $('#i-income').val().replace(/[,|.]/g, '').trim();
    var iExpenses = $('#i-expenses').val().replace(/[,|.]/g, '').trim();
    var loans = $('#loans').val().replace(/[,|.]/g, '').trim();
    var deposits =  $('#deposits').val().replace(/[,|.]/g, '').trim();
    var dividends = $('#dividends').val().replace(/[,|.]/g, '').trim();

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
					alert("The operation completed successfully. Responded with " + data.responseText);
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
    $('#shares-outstanding').val('');

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

function loadPeriodData(period) {
    $('#name').val(period.name);
    $('#earnings-date').val(period.earningsDate.substring(0, 16));
    $('#ticker-symbol').val(period.tickerSymbol);
    $('#shares-outstanding').val(period.sharesOutstanding);

  	$('#n-income').val(period.bankStatement.netIncome);
    $('#assets').val(period.bankStatement.assets);
    $('#i-assets').val(period.bankStatement.intangibleAssets);
    $('#liabilities').val(period.bankStatement.liabilities);
    $('#equity').val(period.bankStatement.equity);
    $('#i-income').val(period.bankStatement.interestIncome);
    $('#i-expenses').val(period.bankStatement.interestExpenses);
    $('#loans').val(period.bankStatement.loans);
    $('#deposits').val(period.bankStatement.deposits);
    $('#dividends').val(period.bankStatement.dividends);
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

                    $('#id').val(data.id);
                    $('#bank-id').val(data.bankStatement.id);

                    $("#delete-btn").show();
                    $("#submit-btn").html("Update");
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
