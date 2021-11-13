$(document).ready(function () {

    function getCookie(name) {
        var value = "; " + document.cookie;
        var parts = value.split("; " + name + "=");
        if (parts.length == 2) return parts.pop().split(";").shift();
    }


    if (getCookie("language") === "de") {
        $('#users').DataTable({
            language: {
                url: 'dataTable/de_de.json'
            },
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            }]
        })
    } else {
        $('#users').DataTable({
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            }]
        })
    }
    ;
    if (getCookie("language") === "de") {
        $('#job_logistiker').DataTable({
            language: {
                url: 'dataTable/de_de.json'
            },
            order: [[4, "desc"]],
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    "targets": [ 5 ],
                    "visible": false,
                    "searchable": false
                },
                {
                    targets: [ 4 ],
                    orderData: [ 5 ]
                }
                ]
        })
    } else {
        $('#job_logistiker').DataTable({
            order: [[4, "desc"]],
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    "targets": [ 5 ],
                    "visible": false,
                    "searchable": false
                },
                {
                    targets: [ 4 ],
                    orderData: [ 5 ]
                }
            ]
        })
    }
    ;
    if (getCookie("language") === "de") {
        $('#workstation_technologe').DataTable({
            language: {
                url: 'dataTable/de_de.json'
            },
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            }]
        })
    } else {
        $('#workstation_technologe').DataTable({
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            }]
        })
    }
    ;
    if (getCookie("language") === "de") {
        $('#priorities').DataTable({
            language: {
                url: 'dataTable/de_de.json'
            },
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    targets: [ 1 ],
                    orderData: [ 0 ]
                }
            ]
        })
    } else {
        $('#priorities').DataTable({
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    targets: [ 1 ],
                    orderData: [ 0 ]
                }
                ]
        })
    };
    if (getCookie("language") === "de") {
        $('#jobs').DataTable({
            language: {
                url: 'dataTable/de_de.json'
            },
            order: [[5, "desc"]],
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    "targets": [ 6 ],
                    "visible": false,
                    "searchable": false
                },
                {
                    targets: [ 5 ],
                    orderData: [ 6 ]
                }
            ]
        })
    } else {
        $('#jobs').DataTable({
            order: [[5, "desc"]],
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    "targets": [ 6 ],
                    "visible": false,
                    "searchable": false
                },
                {
                    targets: [ 5 ],
                    orderData: [ 6 ]
                }
            ]
        })
    };
    if (getCookie("language") === "de") {
        $('#pendingjobs').DataTable({
            language: {
                url: 'dataTable/de_de.json'
            },
            order: [[4, "desc"]],
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    "targets": [ 5 ],
                    "visible": false,
                    "searchable": false
                },
                {
                    targets: [ 4 ],
                    orderData: [ 5 ]
                }
            ]
        })
    } else {
        $('#pendingjobs').DataTable({
            order: [[4, "desc"]],
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    "targets": [ 5 ],
                    "visible": false,
                    "searchable": false
                },
                {
                    targets: [ 4 ],
                    orderData: [ 5 ]
                }
            ]
        })
    }
    if (getCookie("language") === "de") {
        if($('[data-toggle="tooltip"]').length > 0) {
            $('[data-toggle="tooltip"]').tooltip();
        }
        $('#curren-procedures').DataTable({
            language: {
                url: 'dataTable/de_de.json'
            },
            order: [[2, "desc"]],
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    "targets": [ 3 ],
                    "visible": false,
                    "searchable": false
                },
                {
                    targets: [ 2 ],
                    orderData: [ 3 ]
                }
            ]
        })
    } else {
        if($('[data-toggle="tooltip"]').length > 0) {
            $('[data-toggle="tooltip"]').tooltip();
        }
        $('#curren-procedures').DataTable({
            order: [[2, "desc"]],
            columnDefs: [{
                orderable: false,
                targets: "no-sort"
            },
                {
                    "targets": [ 3],
                    "visible": false,
                    "searchable": false
                },
                {
                    targets: [ 2],
                    orderData: [ 3 ]
                }
            ]
        })
    }
})

