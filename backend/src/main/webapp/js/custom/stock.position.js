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

    $.get("/portfoliomng/position/all", function(data, status) {
            if (status == 'success') {
                var positons = "";

                for (var i = 0; i < data.length; i++) {
                    exchanges += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                }

                $('#positions').append(exchanges);
             } else {
                alert("No trades/positions found registered!");
            }
        });



	if(urlParams.operation == 'add') {
	    $('#form-title').html("Add Company");
	} else if(urlParams.operation == 'edit') { //FIXME; this part could no be used
        $.get("/portfoliomng/company/" + urlParams.id, function(data, status) {
            if (status == 'success') {
               if(data.name != null && data.name != undefined) {
                    setTimeout(function(){setFields(data) }, 1000);
                    $('#form-title').html("Edit Company");
                    $("#delete-btn").show();
               } else {
                    alert("Company could not be found.");
               }
            } else {
               alert("No exchanges found registered!");
            }
        }).fail(function(data, status) {
            alert(data.responseJSON.description);
        });
    } else if(urlParams.operation == 'delete') { //FIXME; this part could not be used
        $('#form-title').html("Delete Company");
        $("#delete-btn").show();
    } else if(urlParams.operation == 'view') { //FIXME; this part could not be used
        $('#form-title').html("Company");
    }

    $('#mode').val(urlParams.operation);
});

// used when updating company
function setFields(company) {
   $('#name').val(company.name);
   $('#ticker-symbol').val(company.tickerSymbol);
   $('#industry-sector').val(company.industrySector.id);
   $('#price').val(company.price);
   $('#ev-ebit-min').val(company.evEbitMin);
   $('#ev-ebit-max').val(company.evEbitMax);
   $('#stock-url').val(company.stockUrl);
   $('#exchange').val(company.exchange.id);
   $('#id').val(company.id);
   $('#description').val(company.description);
   $('#kap-url').val(company.kapUrl);
   $('#website').val(company.website);
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
}

function submitPosition() {
    var name = $('#name').val();
    var tickerSymbol = $('#ticker-symbol').val();
    var price = $('#price').val();
    var evEbitMin = $('#ev-ebit-min').val();
    var evEbitMax = $('#ev-ebit-max').val();
    var exchange = $('#exchange').val();
	var sector = $('#industry-sector').val();
    var stockUrl = $('#stock-url').val();
	var description = $('#description').val();
    var kapUrl = $('#kap-url').val();
    var id = $('#id').val();
    var website = $('#website').val();

	if (tickerSymbol == '' || exchange == '0' || price == '' || name == '' || sector == '0' || stockUrl == '') {
		alert("Please fill all required fields!");
	} else {
		var company = {
		    "id" : id,
			"name" : name,
			"industrySector" : {"id": sector},
			"description" : description,
			"tickerSymbol" : tickerSymbol,
			"price" : price,
			"evToEbitMax" : evEbitMax,
			"evToEbitMin" : evEbitMin,
			"stockUrl": stockUrl,
            "exchange": {"id": exchange},
            "kapUrl": kapUrl,
            "website": website
		};

		var execUrl = "/portfoliomng/company/add";
		var oprMode = $('#mode').val();

		if(oprMode == "edit") {
		    execUrl = "/portfoliomng/company/update";
		    company.id = $('#id').val();
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
					resetFields();
				}
			},
			error: function(data, status) {
			    if (data.status != 200) {
			        alert(data.responseText + " Status: " + status);
			    }
			}
		});
	}
}

function removePosition() {
    $.get("/portfoliomng/position/remove/" + $('#id').val(), function(data, status) {
       if (status == 'success') {
           alert("The operation completed successfully.");
           $("#delete-btn").hide();
           $("#submit-btn").html("Add Position");
       }
   }).fail(function(data, status) {
        alert("The position info could not be removed, status: " + status);
   });
}
