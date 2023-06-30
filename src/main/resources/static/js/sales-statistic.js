"use strict";
var startDate = moment().startOf('month').format('MM/DD/YYYY'),
    endDate = moment().endOf('month').format('MM/DD/YYYY');
var sbe = getAjaxResponse("/api/sbe?start="+startDate+"&end="+endDate);
const table = $("#table").DataTable({
    data: sbe,
    columns: [
        { data: 'username' },
        { data: 'torder' },
        { data: 'tproduct'},
        { data: 'tsales', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'tdiscount', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'tshipfee', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'trevenue', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'torder' },
        { data: 'torder' },
        { data: 'torder' },
        { data: 'torder' },
        { data: 'torder' },
        { data: 'torder' },
        { data: 'torder' },
        { data: 'torder' },
        { data: 'torder' },
    ],
    responsive: true,
    fixedColumns: true,
    paging: false,
    scrollCollapse: true,
    scrollX: true,
    scrollY: "400px",
    dom: 'Bt',
    order: [[ 4, 'desc' ]],
    buttons: [
        // {
        //     text: '<i class="fa-solid fa-rotate"></i>',
        //     className: 'btn-tools',
        //     action: function ( e, dt, node, config ) {
        //         table.ajax.reload(null, false);
        //     }
        // },
        {
            extend:    'excel',
            text:      '<i class="fa-solid fa-file-excel"></i>',
            titleAttr: 'Excel',
            className: 'btn-tools',
            exportOptions: {
                columns: ':visible'
            }
        },
        {
            extend:    'pdf',
            text:      '<i class="fa-solid fa-file-pdf"></i>',
            titleAttr: 'PDF',
            className: 'btn-tools',
            exportOptions: {
                columns: ':visible'
            }
        },
        {
            extend:    'print',
            text:      '<i class="fa-solid fa-print"></i>',
            titleAttr: 'Print',
            className: 'btn-tools',
            exportOptions: {
                columns: ':visible'
            }
        },
    ],
    footerCallback: function(row, data, start, end, display) {
        var api = this.api();
        var colNumber = [1,2,7,8,9,10,11,12,13,14,15];
        var colNumber2 = [3,4,5,6];
        var intVal = function (i) {
            return typeof i === 'string' ? i.replace(/[\$,]/g, '') * 1 : typeof i === 'number' ? i : 0;
        };
       
        for (let i = 0; i < colNumber.length; i++) {
            var colNo = colNumber[i];
            var total = api
                    .column(colNo,{ page: 'current'})
                    .data()
                    .reduce(function (a, b) {
                        return intVal(a) + intVal(b);
                    }, 0);
            $(api.column(colNo).footer()).html(total);
        }

        for (let i = 0; i < colNumber2.length; i++) {
            var colNo = colNumber2[i];
            var total = api
                    .column(colNo,{ page: 'current'})
                    .data()
                    .reduce(function (a, b) {
                        return intVal(a) + intVal(b);
                    }, 0);
            $(api.column(colNo).footer()).html(numberWithCommas(total));
        }
    }
});

async function loadData(url){
    try{
        const response = await fetch(url);
        const data = await response.json();
        return data.data;
    }catch (error){
        console.error(error);
    }
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function getAjaxResponse( url ){
    let result= jQuery.ajax({
        url: url,
        type: 'get',
        async:false,
        contentType: "application/json",
        dataType: 'json',
        success:function(response){
            return response.data;
        } 
    }).responseJSON;
    return result.data;
}

$(document).ready(function () {

    table.buttons().container().appendTo('#action-tools');

    $("#daterange").daterangepicker({
        // parentEl: ".daterange-group",
        showCustomRangeLabel: true,
        minDate: moment().subtract(2, "years").subtract(1, "days"),
        ranges: {
            'Hôm nay': [moment(), moment()],
            'Hôm qua': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            '7 ngày trước': [moment().subtract(6, 'days'), moment()],
            '30 ngày trước': [moment().subtract(29, 'days'), moment()],
            'Tháng này': [moment().startOf('month'), moment().endOf('month')],
            'Tháng trước': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        startDate: moment().startOf('month'),
        endDate: moment().endOf('month'),
        opens: "right", 
        locale: {
            format: "DD/MM/YYYY",
            separator: " - ",
            applyLabel: "Đồng ý",
            cancelLabel: "Hủy",
            fromLabel: "Từ",
            toLabel: "Đến",
            customRangeLabel: "Tùy chỉnh",
            daysOfWeek: [
                "CN",
                "T2",
                "T3",
                "T4",
                "T5",
                "T6",
                "T7"
            ],
            monthNames: [
                "Tháng 1",
                "Tháng 2",
                "Tháng 3",
                "Tháng 4",
                "Tháng 5",
                "Tháng 6",
                "Tháng 7",
                "Tháng 8",
                "Tháng 9",
                "Tháng 10",
                "Tháng 11",
                "Tháng 12"
            ],
            firstDay: 1
        }
    }, async function(start, end, label) {
        let startDate = moment(start).format("MM/DD/YYYY");
        let endDate = moment(end).format("MM/DD/YYYY");
        sbe = await loadData("/api/sbe?start="+startDate+"&end="+endDate);

        table.clear().rows.add(sbe).draw();
    });
});

