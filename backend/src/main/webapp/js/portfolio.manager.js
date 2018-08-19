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