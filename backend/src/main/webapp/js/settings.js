function calculatePeriodMargins(){

    $.get("/portfoliomng/admin/period/margins/calculate" , function(data, status) {
        if (status == 'success') {
            alert("Operation completed successfully!");

        } else {
            alert("Operation failed!");
        }
    });
}

function updatePrices() {
    $.get("/portfoliomng/companies/update/prices" , function(data, status) {
        if (status == 'success') {
            alert("Operation completed successfully!");

        } else {
            alert("Operation failed!");
        }
    });

}