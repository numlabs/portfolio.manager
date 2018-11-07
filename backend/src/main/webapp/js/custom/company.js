var OPER_MODE_ADD = "add";
var OPER_MODE_EDIT = "edit";

var parseQueryString = function(url) {
 	var urlParams = {};

 	url.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0, $1, $2, $3) {
 		urlParams[$1] = $3;
 	});

 	return urlParams;
}

$(document).ready(function() {
	var urlToParse = location.search;
	var urlParams = parseQueryString(urlToParse);
	$('#file-upload-section').hide();

	$.get("/portfoliomng/exchange/all", function(data, status) {
        if (status == 'success') {
            var exchanges = "";

            for (var i = 0; i < data.length; i++) {
                exchanges += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }

            $('#exchange').append(exchanges);
            $('#search-exchange').append(exchanges);
         } else {
            alert("There are no defined exchanges!");
        }
    });

    $.get("/portfoliomng/industrysector/all", function(data, status) {
        if (status == 'success') {
            var sectors = "";

            for (var i = 0; i < data.length; i++) {
                sectors += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
            }

            $('#industry-sector').append(sectors);
        } else {
            alert("There are no defined industry sectors!");
        }
    });
});

// used when updating company
function setFields(company) {
	$('#name').val(company.name);
	$('#ticker-symbol').val(company.tickerSymbol);
	$('#industry-sector').val(company.industrySector.id);
	$('#price').val(company.price);
	$('#ev-ebit-min').val(company.evToEbitMin);
	$('#ev-ebit-max').val(company.evToEbitMax);   
	$('#exchange').val(company.exchange.id);
	$('#id').val(company.id);
	$('#description').val(company.description);
	$('#stock-url').val(company.stockUrl);
	$('#kap-url').val(company.kapUrl);
	$('#website').val(company.website);
	$('#buy-price').val(company.buyPrice);
	$('#sell-price').val(company.sellPrice);
	$('#country-code').val(company.countryCode);
	
	$('#mode').val(OPER_MODE_EDIT);
	$('#file-upload-section').show();
}

function resetFields() {
	$('#name').val('');
	$('#ticker-symbol').val('');
	$('#industry-sector').val(0);
	$('#price').val('');
	$('#ev-ebit-min').val('');
	$('#ev-ebit-max').val('');
	$('#stock-url').val('');
	$('#exchange').val(0);
	$('#id').val(0);
	$('#description').val('');
	$('#kap-url').val('');
	$('#website').val('');
	$('#buy-price').val(0);
	$('#sell-price').val(0);
	$('#country-code').val('');

	$('#mode').val(OPER_MODE_ADD);
	$('#file-upload-section').hide();
}

function addCompany() {
	var company = {
		    "id" : $('#id').val(),
			"name" : $('#name').val().trim(),
			"industrySector" : {
					"id": $('#industry-sector').val()
				},
			"description" : $('#description').val().trim(),
			"tickerSymbol" : $('#ticker-symbol').val().trim(),
			"price" : $('#price').val().trim(),
			"evToEbitMax" : $('#ev-ebit-max').val().trim(),
			"evToEbitMin" : $('#ev-ebit-min').val().trim(),
			"stockUrl": $('#stock-url').val().trim(),
            "exchange": {
					"id": $('#exchange').val()
				},
            "kapUrl": $('#kap-url').val().trim(),
            "website": $('#website').val().trim(),
            "buyPrice": $('#buy-price').val().trim(),
            "sellPrice": $('#buy-price').val().trim(),
            "countryCode": $('#country-code').val().trim()
		};
	
	if(isValid(company)) {
		var execUrl = "/portfoliomng/company/add";
		var oprMode = $('#mode').val();

		if(oprMode == OPER_MODE_EDIT) {
		    execUrl = "/portfoliomng/company/update";
		}

		jQuery.ajax({
			url : execUrl,
			type : "POST",
			data : JSON.stringify(company),
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			complete : function(data) {
				if (data.status == 200) {
					alert("The operation completed successfully.");
					//resetFields();
					$('#file-upload-section').show();
				}
			},
			error: function(data, status) {
			    if (data.status != 200) {
			        alert(data.responseText + " Status: " + status);
			        $('#file-upload-section').hide();
			    }
			}
		});
	}
}

function isValid(company) {
	var message = "";
	
	if(company.name == '') {
		message += " Name fields is required. \n";
	}
	
	if(company.industrySector.id == 0) {
		message += " Industry sector fields is required. \n";
	}

	if(company.tickerSymbol == '') {
		message += " Ticker symbol fields is required. \n";
	}

	if(company.price == '') {
		message += " Price fields is required. \n";
	}

	if(company.exchange.id == 0) {
		message += " Exchange fields is required. \n";
	}

	if(company.website == '') {
		message += " Website fields is required. \n";
	}

	if(message == '') {
		return true;
	} else {
		alert(message);

		return false;
	}
}

function removeCompany() {
	var id = $('#id').val();

	if(id != 0) {
		$.get("/portfoliomng/company/remove/" + $('#id').val(), function(data, status) {
			if (status == 'success') {
				alert("Company was removed successfully.");

				$("#submit-btn").html("Add Company");
				$('#mode').val(OPER_MODE_ADD);
				resetFields();
			}
	   }).fail(function(data, status) {
			alert("The company could not be removed, status: " + status);
	   });
	}
}

function searchCompany() {
    var tickerSymbol = $('#company-search').val();
    var exchange = $('#search-exchange').val();

    if(tickerSymbol.trim() == '' || exchange == 0)  {
        alert("Please fill required ticket symbol and exchange fields. ");
    } else {
        resetFields();

        $("#submit-btn").html("Add Company");
        $('#mode').val(OPER_MODE_ADD);

        $.get("/portfoliomng/company/search/" + tickerSymbol + "." + exchange, function(data, status) {
            if (status == 'success') {
                if(data.name != null && data.name != undefined) {
                    setFields(data);
                
                    $("#submit-btn").html("Update");
                    $('#mode').val(OPER_MODE_EDIT);
                    $('#file-upload-section').show();
                }
            }
        }).fail(function(data, status) {
            alert(data.responseJSON.description);
            $('#file-upload-section').hide();
        });
    }
}

function removeCompanyPeriods() {
    $.get("/portfoliomng/company/remove/periods/" + $('#id').val(), function(data, status) {
       if (status == 'success') {
           alert("Operation completed successfully.");
       }
    }).fail(function(data, status) {
        alert("Operation failed, status: " + status);
    });
}

function calculatePeriodPrices() {
    var id = $('#id').val();

    if(id == '' || id == 0) {
        alert("Please select a company.");
        return;
    }

    $.get("/portfoliomng/company/periods/prices/calculate/" + $('#id').val(), function(data, status) {
       if (status == 'success') {
           alert("Operation completed successfully.");
       }
    }).fail(function(data, status) {
        alert("Operation failed, status: " + status);
    });
}

function uploadPricesFile() {
    var fileInput = document.getElementById("prices-file");

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
            alert("Upload completed " + xhr.response);
        }
      }

    xhr.open("POST", "/portfoliomng/company/prices/file/upload", true);

    var formData = new FormData();
    formData.append("file", fileInput.files[0]);
    formData.append("id", $('#id').val());
    xhr.send(formData);
}