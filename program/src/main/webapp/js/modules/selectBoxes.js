function select2() {
    $('.select').select2({
        minimumResultsForSearch: -1,
        width: '100%'
    });
}

$(document).ready(function () {
    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    /* OPTIONS FOR SELECT
    disabled = true/false   -> Can't be used
    placeholder = "test"    -> Set a placeholder
    minimumResultsForSearch -> The minimum number of results required to display the search box.
    maximumSelectionLength  -> The maximum/minimum of selectable items.
     */

    $('#add_user').on('show.bs.modal', async function (e) {
        if ($('.select').length > 0) {
            await sleep(300);
            $('.select').select2({
                minimumResultsForSearch: -1,
                width: '100%'
            })
        }
    });
    $('#edit_user').on('show.bs.modal', async function (e) {
        if ($('.select').length > 0) {
            await sleep(300);
            $('.select').select2({
                minimumResultsForSearch: -1,
                width: '100%'
            })
        }
    });
    $('#add_assembly').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_assembly').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#add_workstation').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_workstation').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#add_carrier').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_carrier').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#add_carriertype').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_carriertype').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#add_job').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_job').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#add_processstep').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_processstep').on('shown.bs.modal', async function (e) {
        await sleep(400);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#view_processstep').on('shown.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#add_processchain').on('show.bs.modal', async function (e) {
        await sleep(400);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_processchain').on('show.bs.modal', async function (e) {
        await sleep(400);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#add_statemachine').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_statemachine').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#add_job').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_job').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#edit_job_logistiker').on('show.bs.modal', async function (e) {
        await sleep(300);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#view_job').on('show.bs.modal', async function (e) {
        await sleep(400);
        $('.select').select2({
            minimumResultsForSearch: -1,
            disabled: true,
            width: '100%'
        });
        if ($('[data-toggle="tooltip"]').length > 0) {
            $('[data-toggle="tooltip"]').tooltip();
        }
    });
    $('.select').select2({
        minimumResultsForSearch: -1,
        width: '100%'
    });
    $('#add-job-assembly').on('show.bs.modal', async function (e) {
        await sleep(400);
        $('.select').select2({
            minimumResultsForSearch: -1,
            width: '100%'
        })
    });
    $('#start_processchain').on('shown.bs.modal', async function (e) {
        if ($('.select').length > 0) {
            await sleep(300);
            $('.select').select2({
                minimumResultsForSearch: -1,
                width: '100%'
            })
        }
    });
})
