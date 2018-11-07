var parseQueryString = function(url) {
 	var urlParams = {};

 	url.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0, $1, $2, $3) {
 		urlParams[$1] = $3;
 	});

 	return urlParams;
}

var cache;

$(document).ready(function() {
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

    $('#mode').val("add");
});

// used when updating company note
function setFields(notes) {
    if(notes.length == 0) {
        $('#notes').append("No Notes registered for the company.");
        return;
    }

    var tableData = "";
    cache = notes;

   $("#notes tr").remove();

    for(var i=0; i<notes.length; i++) {
        tableData += "<tr id='note-" + notes[i].id + "'><td>" + notes[i].title + "</td><td>" + notes[i].noteDate.substring(0,10) + "</td><td>"
            + notes[i].note + "</td><td><a href='javascript:void();' onclick='updateNote(" + i
            + ")'>Update</a>&nbsp;&nbsp;<a href='javascript:void();' onclick='removeCompanyNote(" + notes[i].id
            + ")'>Remove</a></td></tr>";
    }

    $('#notes').append(tableData);
}

function updateNote(index) {
    $('#title').val(cache[index].title);
    $('#ticker-symbol').val(cache[index].company.tickerSymbol);
    $('#note-date').val(cache[index].noteDate.substring(0,10) );
    $('#exchange').val(cache[index].company.exchange.id);
 	$('#note').val(cache[index].note);
 	$('#id').val(cache[index].id);

    $('#mode').val("edit");
 	$("#submit-btn").html("Update");
}

function resetFields() {
    $('#title').val('');
    $('#ticker-symbol').val('');
    $('#note-date').val('');
    $('#exchange').val(0);
 	$('#note').val('');
}

function submitCompanyNote() {
    var title = $('#title').val();
    var tickerSymbol = $('#ticker-symbol').val();
    var noteDate = $('#note-date').val();
    var exchangeId = $('#exchange').val();
	var note = $('#note').val();
	var id = $('#id').val();

	if (tickerSymbol == '' || exchangeId === 0 || title == '' || note == '' || noteDate == '') {
		alert("Please fill all required fields!");
	} else {
		var note = {
		    "id" : id,
			"title" : title,
			"note" : note,
			"tickerSymbol" : tickerSymbol,
			"noteDate" : noteDate,
			"company" : {
		        "tickerSymbol": tickerSymbol,
		        "exchange": {
		            "id": exchangeId
		        }
		    }
	    };

		var execUrl = "/portfoliomng/company/note/add";
		var oprMode = $('#mode').val();

		if(oprMode == "edit") {
		    execUrl = "/portfoliomng/company/note/update";
		    note.id = $('#id').val();
		}

		jQuery.ajax({
			url : execUrl,
			type : "POST",
			data : JSON.stringify(note),
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			complete : function(data) {
				if (data.status == 200) {
					alert("The operation completed successfully.");
					resetFields();
					$('#mode').val("add");
					$("#submit-btn").html("Add Note");
					getNotes(tickerSymbol, exchangeId)
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

function removeCompanyNote(id) {
    $.get("/portfoliomng/company/note/remove/" + id, function(data, status) {
       if (status == 'success') {
            $("#submit-btn").html("Add Note");
            $('#note-' + id).remove();
            alert("The operation completed successfully.");
       }
   }).fail(function(data, status) {
        alert("The company note could not be removed, status: " + status);
   });
}

function searchCompanyNote() {
    var tickerSymbol = $('#company-search').val();
    var exchange = $('#search-exchange').val();

    if(tickerSymbol.trim() == '' || exchange == 0)  {
        alert("Please fill required ticket symbol and exchange fields. ");
    } else {
       getNotes(tickerSymbol, exchange);
    }
}

function getNotes(tickerSymbol, exchangeId) {
    resetFields();

    $("#submit-btn").html("Add Note");

    $.get("/portfoliomng/company/note/search/" + tickerSymbol + "." + exchangeId, function(data, status) {
        if (status == 'success') {
            if(data.length != 0) {
                setFields(data);
                $('#mode').val("edit");
            }
        }
    }).fail(function(data, status) {
        alert(data.responseJSON.description);
    });
}
