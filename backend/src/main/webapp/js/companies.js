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

    $.get("/portfoliomng/companies/", {
    		exchange : result.exchange
    	}).done(function(data) {
    		var html = '';

            $('#companies-tbody').not(':first').not(':last').remove();

           	for(i=0; i<data.length; i++) {
            	html = html + addCompanyToTable(data[i]);
         	}
            $('#exchangeName').html("Companies at " + data[0].exchange.name);
          	$('#companies-tbody').append(html);
    	}).fail(function() {
    		alert("The companies could not be completed.");
    });
});

function addCompanyToTable(data) {
	var tableRow = "<tr id='" + data.tickerSymbol + "'>";

	tableRow += "<td align='left' style='color:red;'><a href='/portfoliomng/company.html?operation=view&symbol=" +
	            data.tickerSymbol + "." + data.exchange.id + "'>" +  data.tickerSymbol + " - " + data.name + "</a></td><td align='right'>";

	tableRow += data.evToEbit + "</td> <td align='right'>";
    tableRow += data.evToEbitMax + "</td> <td align='right'>";
	tableRow += data.evToEbitMin + "</td> <td align='right'>";
	tableRow += data.evToMg + "</td> <td align='right'>";
	tableRow += data.evToCv + "</td> <td align='right'>";
	tableRow += data.price + "</td> <td align='right'>";
	tableRow += data.pe + "</td> <td align='right'>";
	tableRow += data.bv + "</td> <td align='right'>";
	tableRow += data.roe + "</td> <td align='right'>";
    tableRow += data.grossMargin + "</td> <td align='right'>";
    tableRow += data.ebitMargin + "</td> <td align='right'>";
    tableRow += data.netProfitMargin + "</td> </tr>" ;

	return tableRow;
}


function formatValue(value) {
    var newValue = value.toString();
    var counter = 0;
    var dotIndex = 0;
    var isNegativeNumber = false;
    var tempValue = '';

    if(value < 0) {
        isNegativeNumber = true;
    }

    if(newValue.length < 4) {
        return value;
    } else {
        if(value < 0) {
            dotIndex = (newValue.length-1) % 3;
            value = value * -1;
            newValue = value.toString();
            tempValue = '-';
        } else {
            dotIndex = newValue.length % 3;
        }
    }

    for(var i=0; i<newValue.length ; i++) {
        tempValue += newValue.charAt(i);
        dotIndex--;

        if(dotIndex == 0 && (i+1)<newValue.length ) {
            tempValue += '.';
            dotIndex = 3;
        } else if(dotIndex == -1) {
            dotIndex = 2;
        }
    }

    return tempValue;
}
