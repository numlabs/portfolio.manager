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

    $.get("/portfoliomng/companies/detailed/" + result.exchange).done(function(data) {
    		var html = '';
            $('#companies-tbody').not(':first').not(':last').remove();

           	for(i=0; i<data.length; i++) {
            	html = html + addCompanyToTable(data[i]);
         	}

            $('#exchangeName').html("Companies at " + data[0].exchange.name);
          	$('#companies-tbody').append(html);
    	}).fail(function() {
    		alert("The companies could not be retrieved.");
    });
});

function addCompanyToTable(data) {
	var tableRow = "<tr id='" + data.tickerSymbol + "'>";

	tableRow += "<td align='left' style='color:red; width:2px;'><a href='/portfoliomng/company-board.html?id=" +
	            data.id + "&companySectorCode=" + data.industrySector.code + "'>" + data.tickerSymbol + "</a></td>";
   	tableRow += "<td align='left' style='color:red;'><a href='/portfoliomng/company-board.html?id=" +
                	            data.id + "&companySectorCode=" + data.industrySector.code + "'>" + data.name + "</a></td><td align='right'>";
    tableRow += "<b>" + formatValue(data.price) + "</b> (" + data.priceDate.substring(8,10) + "/" + data.priceDate.substring(5,7) + "/" + data.priceDate.substring(2,4) + ")</td> <td align='right'>";
	tableRow += formatValue(data.evToMg) + "</td> <td align='right'>";
	tableRow += formatValue(data.pe) + "</td> <td align='right'>";
    tableRow += formatValue(data.pb) + "</td> <td align='right'>";
	tableRow += formatValue(data.evToEbit) + "</td> <td align='right'>";
	tableRow += formatValue(data.evToEbitLastPeriod) + "</td> <td align='right'>";
    tableRow += formatValue(data.evToEbitMax) + "</td> <td align='right'>";
	tableRow += formatValue(data.evToEbitMin) + "</td> <td align='right'>";

	tableRow += formatValue(data.evToCv) + "</td> <td align='right'>";
	tableRow += formatPercentValue(data.roe) + "</td> <td align='right'>";
    tableRow += formatPercentValue(data.grossMargin) + "</td> <td align='right'>";
    tableRow += formatPercentValue(data.ebitMargin) + "</td> <td align='right'>";
    tableRow += formatPercentValue(data.ebitdaMargin) + "</td> <td align='right'>";
    tableRow += formatPercentValue(data.netProfitMargin) + "</td> <td align='right'> ";
    tableRow += ((data.ebitGrowth == null)? '' : data.ebitGrowth) + "</td> <td align='left'> ";
    tableRow += "<a href='" + data.stockUrl + "' target='_blank'>inv.com</a></td> <td align='left'>";
    tableRow += "<a href='" + data.kapUrl + "' target='_blank'>kap</a></td> <td align='left'>";
    tableRow += "<a href='" + data.website + "' target='_blank'>web</a></td> <td align='left'></td> </tr>" ;

	return tableRow;
}

function formatPercentValue(value) {
    if(value == null || value == undefined) {
        return "";
    }

    return formatValue(value) + " %";
}

function formatValue(value) {
    if(value == null || value == undefined) {
        return "";
    }

    var newValue = value.toString();
    var counter = 0;
    var dotIndex = 0;
    var isNegativeNumber = false;
    var tempValue = '';

    if(value < 0) {
        isNegativeNumber = true;
    }

    if(newValue.includes('.') || newValue.includes(',')) {
        return value;
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
