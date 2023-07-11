"use strict";
const barchart = document.getElementById("bar_chart").getContext("2d");

var startDate = moment().startOf('month').format('MM/DD/YYYY'),
    endDate = moment().endOf('month').format('MM/DD/YYYY');
var url = "/api/sbd?start="+startDate+"&end="+endDate;
var chart; 

loadRevenueByDay(url);

function loadRevenueByDay(url){
    var table = $("#table").DataTable({
        ajax: {
            url: url,
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            dataSrc: 'data'
        },
        columns: [
            { data: 'day', render: function(data, type, row){ return moment(data).format("DD/MM/YYYY")} },
            { data: 'torder' },
            { data: 'tproduct'},
            { data: 'tsales', render: function(data, type, row){ return numberWithCommas(data)} },
            { data: 'tdiscount', render: function(data, type, row){ return numberWithCommas(data)} },
            { data: 'tshipfee', render: function(data, type, row){ return numberWithCommas(data)} },
            { data: 'trevenue', render: function(data, type, row){ return numberWithCommas(data)} },
            { data: 'tprofit', render: function(data, type, row){ if(data==null){return "0"}else{return numberWithCommas(data)}} }
    
        ],
        responsive: true,
        fixedColumns: true,
        paging: false,
        // scrollCollapse: true,
        // scrollX: true,
        // scrollY: "400px",
        dom: 'Bt',
        order: [[ 0, 'desc' ]],
        buttons: [
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
            var colNumber = [1,2];
            var colNumber2 = [3,4,5,6,7];
            var intVal = function (i) {
                if(i == null) return 0;
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
        },
        drawCallback: function(settings){
            var revenue = [],
                day = [];

            for(var count=0; count < settings.aoData.length; count++){
                day.push(moment(settings.aoData[count]._aData['day']).format("DD/MM"));
                revenue.push(parseInt(settings.aoData[count]._aData['trevenue']));
                // sales.push(parseInt(settings.aoData[count]._aData['tsales']));
                // order.push(parseInt(settings.aoData[count]._aData['torder']));
                // product.push(parseInt(settings.aoData[count]._aData['tproduct']));
            }

            var a = {
                labels: day,
                datasets: [{
                    label: "Doanh thu",
                    borderColor: "#00eec9",
                    backgroundColor:"#fea11b",
                    fill: false,
                    tension: 0.4,
                    data: revenue,
                },]
            };

            if(chart){
                chart.destroy();
            }

            chart = new Chart(barchart, {
                type: "line",
                data: a,
                options: {
                    responsive: true,
                    maintainAspectRatio: !1,
                    plugins: {
                        subtitle: {
                            display: false,
                            text: 'Custom Chart Subtitle'
                        },
                        title: {
                            display: false,
                            text: 'Custom Chart Title'
                        }
                    }
                }
            });
        }
    });

    table.buttons().container().appendTo('#action-tools');
}


function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

$(document).ready(function () {

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
        let url = "/api/sbd?start="+startDate+"&end="+endDate;
        $("#table").DataTable().clear().destroy();
        loadRevenueByDay(url);
    });
});

