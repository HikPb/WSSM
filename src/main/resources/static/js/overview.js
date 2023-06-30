"use strict";
var startDate = moment().subtract(6, 'days').format('MM/DD/YYYY'),
    endDate = moment().format('MM/DD/YYYY');
var sbd = getAjaxResponse("/api/sbd?start="+startDate+"&end="+endDate), 
    sbw = getAjaxResponse("/api/sbw?start="+startDate+"&end="+endDate), 
    sbp = getAjaxResponse("/api/sbp?start="+startDate+"&end="+endDate), 
    sbe = getAjaxResponse("/api/sbe?start="+startDate+"&end="+endDate);

const barchart = document.getElementById("bar_chart").getContext("2d");
const ctx4 = document.getElementById("doughnut_chart").getContext("2d");
const wtable = $("#w-table").DataTable({
    data: sbw,
    columns: [
        { data: 'name' },
        { data: 'trevenue', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'tsales', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'tdiscount', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'torder' },
        { data: 'tproduct' },
    ],
    dom: 't',
    order: [[ 4, 'desc' ]]
});
const ptable = $("#p-table").DataTable({
    data: sbp,
    columns: [
        { data: 'productName' },
        { data: 'trevenue', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'tsales', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'tproduct' },
        { data: 'torder' },
    ],
    scrollY: 200,
    scrollCollapse: true,
    dom: 't',
    order: [[ 3, 'desc' ]]
});
const etable = $("#e-table").DataTable({
    data: sbe,
    columns: [
        { data: 'username' },
        { data: 'trevenue', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'tsales', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'tdiscount', render: function(data, type, row){ return numberWithCommas(data)} },
        { data: 'torder' },
    ],
    scrollY: 200,
    scrollCollapse: true,
    dom: 't',
    order: [[ 4, 'desc' ]]
});

renderChart();

function renderChart(){
    var profit = [], 
        revenue = [],
        sales = [], 
        order = [], 
        product = [],
        day = [];
    sbd.map(it =>{
        day.push(moment(it.day).format("DD/MM"));
        profit.push(it.tprofit);
        revenue.push(it.trevenue);
        sales.push(it.tsales);
        order.push(it.torder);
        product.push(it.tproduct);
    })

    var a = {
        labels: day,
        datasets: [{
            label: "Doanh số",
            borderColor: "#15aebf",
            backgroundColor:"#000fff",
            fill: false,
            tension: 0.4,
            data: sales
        }, {
            label: "Doanh thu",
            borderColor: "#1246c9",
            backgroundColor:"#03133b",
            fill: false,
            tension: 0.4,
            data: revenue
        }, {
            label: "Lợi nhuận",
            borderColor: "rbg(13,13,13)",
            backgroundColor:"#03133b",
            fill: false,
            tension: 0.4,
            data: profit
        }, {
            label: "SL đơn",
            borderColor: "#1246c9",
            backgroundColor:"#03133b",
            fill: false,
            tension: 0.4,
            data: order
        },{
            label: "SL sản phẩm",
            borderColor: "#1246c9",
            backgroundColor:"#03133b",
            fill: false,
            tension: 0.4,
            data: product
        }]
    };

    new Chart(barchart, {
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


    var doughnutData = {
        labels: ["Desktop", "Tablet", "Mobile"],
        datasets: [{
            data: [47, 30, 23],
            backgroundColor: ["rgb(255, 99, 132)", "rgb(54, 162, 235)", "rgb(255, 205, 86)"]
        }]
    };


    var doughnutOptions = {
        responsive: true,
        legend: {
            display: false
        },
    };

    new Chart(ctx4, { type: 'doughnut', data: doughnutData, options: doughnutOptions });

}

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
        startDate: moment().subtract(6, 'days'),
        endDate: moment(),
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
        sbd = await loadData("/api/sbd?start="+startDate+"&end="+endDate);
        sbw = await loadData("/api/sbw?start="+startDate+"&end="+endDate);
        sbp = await loadData("/api/sbp?start="+startDate+"&end="+endDate);
        sbe = await loadData("/api/sbe?start="+startDate+"&end="+endDate);

        wtable.clear().rows.add(sbw).draw();
        ptable.clear().rows.add(sbp).draw();
        etable.clear().rows.add(sbe).draw();

        barchart.reset();
        ctx4.reset();

        renderChart()
    });
});

