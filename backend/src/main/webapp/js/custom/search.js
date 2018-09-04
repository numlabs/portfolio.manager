function searchCompany() {
    var searchCriteria = $('#company-search-text').val();

    if(searchCriteria.trim() != '') {
        searchCriteria = btoa(searchCriteria);

        $.get("/portfoliomng/company/search/" + searchCriteria, function(data, status) {
            if (status == 'success') {
                if(data.name != null && data.name != undefined) {
                    window.location.href = "/portfoliomng/company.html?operation=edit&id=" + data.id;
                }

            } else {
                alert("No exchanges found registered!");
            }
        });
     }

}