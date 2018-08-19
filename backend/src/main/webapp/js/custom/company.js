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
    $("#reset-btn").hide();

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

        $.get("/portfoliomng/industrysector/all", function(data, status) {
            if (status == 'success') {
                var sectors = "";

                for (var i = 0; i < data.length; i++) {
                    sectors += '<option value="' + data[i].id + '">' + data[i].name + '</option>';
                }

                $('#industry-sector').append(sectors);
            } else {
                alert("No industry sectors found registered!");
            }
        });

	if(result.operation == 'add') {
	    $('#form-title').html("Add Company");
	} else if(result.operation == 'edit') {
        $('#form-title').html("Edit Company");
        $("#delete-btn").show();
         $("#reset-btn").show();

        $.get("/portfoliomng/company/" + result.id, function(data, status) {
               if (status == 'success') {
                   if(data.name != null && data.name != undefined) {
                       // setFields(data);
                       setTimeout(function(){setFields(data) }, 1000);
                   }
               } else {
                   alert("No exchanges found registered!");
               }
           });
    } else if(result.operation == 'delete') {
        $('#form-title').html("Delete Company");
        $("deleteBtn").show();
    } else if(result.operation == 'view') {
        $('#form-title').html("Company");
    }

    $('#mode').val(result.operation);
});

function setFields(data) {
   $('#name').val(data.name);
   $('#ticker-symbol').val(data.tickerSymbol);
   $('#industry-sector').val(data.industrySector.id);
   $('#price').val(data.price);
   $('#stock-url').val(data.stockUrl);
   $('#exchange').val(data.exchange.id);
   $('#id').val(data.id);
   $('#description').val(data.description);
   $('#kap-url').val(data.kapUrl);
}

function resetFields() {
   $('#name').val('');
   $('#ticker-symbol').val('');
   $('#industry-sector').val(0);
   $('#price').val(0);
   $('#stock-url').val('');
   $('#exchange').val(0);
   $('#id').val(0);
   $('#description').val('');
   $('#kap-url').val('');
}

function addCompany() {
    var name = $('#name').val();
    var tickerSymbol = $('#ticker-symbol').val();
    var price = $('#price').val();
    var exchange = $('#exchange').val();
	var sector = $('#industry-sector').val();
    var stockUrl = $('#stock-url').val();
	var description = $('#description').val();
    var kapUrl = $('#kap-url').val();

	if (tickerSymbol == '' || exchange == '0' || price == '' || name == '' || sector == '0' || stockUrl == '') {
		alert("Please fill all required fields!");
	} else {
		var company = {
			"name" : name,
			"industrySector" : {"id": sector},
			"description" : description,
			"tickerSymbol" : tickerSymbol,
			"price" : price,
			"stockUrl": stockUrl,
            "exchange": { "id": exchange },
            "kapUrl": kapUrl
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
					cleanFileds();
				} else {
                    alert("Error code: " + data.status + " returned.");
                }
			}
		});
	}
}

function removeCompany() {
    $.get("/portfoliomng/company/remove/" + $('#id').val(), function(data, status) {
       if (status == 'success') {
           alert("The operation completed successfully.");
           $("#delete-btn").hide();
           $("#submit-btn").hide();
           $("#reset-btn").hide();
       } else {
            alert("Error code: " + data.status + " returned.");
       }
   });
}

function searchCompany() {
    var tickerSymbol = $('#company-search').val();
    var exchange = $('#search-exchange').val();

    if(tickerSymbol.trim() == '' || exchange == 0)  {
        alert("Please fill required ticket symbol and exchange fields. ");
    } else {
        resetFields();

        $("#delete-btn").hide();
        $("#submit-btn").html("Add Company");
        $("#reset-btn").hide();

        $.get("/portfoliomng/company/search/" + tickerSymbol + "." + exchange, function(data, status) {
           if (status == 'success') {
                if(data.name != null && data.name != undefined) {
                    setFields(data);
                    $("#delete-btn").show();
                    $("#reset-btn").show();
                    $("#submit-btn").html("Update");
                    $('#mode').val("edit");
                }
            } else {
                $('#mode').val("add");
                alert("No company found for the specified criteria.");
            }
        });
    }
}

function resetCompany() {
  $.get("/portfoliomng/company/reset/" + $('#id').val(), function(data, status) {
       if (status == 'success') {
           alert("The operation completed successfully.");
       } else {
            alert("Error code: " + data.status + " returned.");
       }
   });
}