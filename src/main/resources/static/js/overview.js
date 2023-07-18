"use strict";
var startDate = moment().subtract(6, 'days').format('MM/DD/YYYY'),
    endDate = moment().format('MM/DD/YYYY');

var barchart, dnchart;

const barchartElem = document.getElementById("bar_chart").getContext("2d");
const dnchartElem = document.getElementById("doughnut_chart").getContext("2d");

renderChart(startDate,endDate);
loadEtable(startDate, endDate);
loadPtable(startDate, endDate);
loadWtable(startDate, endDate);

function loadWtable(startDate, endDate){
    $("#w-table").DataTable({
        processing : true,
        serverSide : true,
        responsive : true,
        ajax: {
            url: "/api/sbw?start="+startDate+"&end="+endDate,
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            dataSrc: 'data'
        },
        columns: [
            { data: 'name' },
            { data: 'trevenue', render: function(data, type, row){ return numberWithCommas(data)} },
            { data: 'tsales', render: function(data, type, row){ return numberWithCommas(data)} },
            { data: 'tdiscount', render: function(data, type, row){ return numberWithCommas(data)} },
            { data: 'torder' },
            { data: 'tproduct' },
        ],
        dom: 't',
        order: [[ 4, 'desc' ]],
        drawCallback: function(settings){
            var wareData = [],
                orderDataset = [],
                salesDataset = [];

            for(var count = 0; count < settings.aoData.length; count++){
                wareData.push(settings.aoData[count]._aData['name']);
                orderDataset.push(parseInt(settings.aoData[count]._aData['torder']));
                salesDataset.push(parseInt(settings.aoData[count]._aData['tsales']));
            }

            var doughnutData = {
                labels: wareData,
                datasets: [{
                    label: "Doanh số",
                    data: salesDataset,
                    backgroundColor: ["#d42a6e", "#053e69", "#1f626b", "#076909", "#660d78", "#e336cc"]
                },{
                    label: "SL đơn",
                    data: orderDataset,
                    backgroundColor: ["#8d26d1", "#32bf49", "#32bfbf", "#e8e225", "#e86625", "#96053f"]
                }]
            };
        
        
            var doughnutOptions = {
                responsive: true,
                plugins: {
                  legend: {
                    position: 'left',
                  },
                  title: {
                    display: false,
                    text: 'Chart.js Doughnut Chart'
                  }
                }
            };
        
            if(dnchart){
                dnchart.destroy();
            }
            dnchart = new Chart(dnchartElem, { type: 'doughnut', data: doughnutData, options: doughnutOptions });   
        }
    });
}

function loadPtable(startDate, endDate){
    $("#p-table").DataTable({
        processing : true,
        serverSide : true,
        responsive : true,
        ajax: {
            url: "/api/sbp?start="+startDate+"&end="+endDate,
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            dataSrc: 'data'
        },
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
}

function loadEtable(startDate, endDate){
    $("#e-table").DataTable({
        processing : true,
        serverSide : true,
        responsive : true,
        ajax: {
            url: "/api/sbe?start="+startDate+"&end="+endDate,
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            dataSrc: 'data'
        },
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
}

async function renderChart(startDate, endDate){
    var profit = [], 
        revenue = [],
        sales = [], 
        order = [], 
        product = [],
        day = [];
    
    var tprofit=0, tsales=0, torder=0, tproduct=0, trevenue=0, torder1=0;
    
    await fetch("/api/sbd?start="+startDate+"&end="+endDate,{
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    }).then(response =>{
        if (!response.ok) throw Error(response.statusText);
        return response.json();
    })
    .then(data => {
        data.data.map(it =>{
            day.push(moment(it.day).format("DD/MM"));
            profit.push(it.tprofit);
            revenue.push(it.trevenue);
            sales.push(it.tsales);
            order.push(it.torder);
            product.push(it.tproduct);
            tprofit+=it.tprofit;
            tsales+=it.tsales;
            torder+=it.torder;
            torder1+=it.torder1;
            tproduct+=it.tproduct;
            trevenue+=it.trevenue;
        });
        
        $("#orderEl").html(torder);
        $("#salesEl").html(numberWithCommas(tsales));
        $("#profitEl").html(numberWithCommas(tprofit));
        $("#torder1El").html(numberWithCommas(torder1));
        $("#productEl").html(numberWithCommas(tproduct));
        $("#revenueEl").html(numberWithCommas(trevenue));

        var chart_data = {
            labels: day,
            datasets: [{
                label: "Doanh số",
                borderColor:"#1196cf",
                backgroundColor:"#1196cf",
                fill: false,
                tension: 0.4,
                data: sales
            }, {
                label: "Doanh thu",
                borderColor:"#11cf33",
                backgroundColor:"#11cf33",
                fill: false,
                tension: 0.4,
                data: revenue
            }, {
                label: "Lợi nhuận",
                borderColor:"#bccf11",
                backgroundColor:"#bccf11",
                fill: false,
                tension: 0.4,
                data: profit
            }, {
                label: "SL đơn",
                borderColor:"#8f11cf",
                backgroundColor:"#8f11cf",
                fill: false,
                tension: 0.4,
                data: order
            },{
                label: "SL sản phẩm",
                borderColor:"#250966",
                backgroundColor:"#250966",
                fill: false,
                tension: 0.4,
                data: product
            }]
        };
    
        if(barchart){
            barchart.destroy();
        }

        barchart = new Chart(barchartElem, {
            type: "line",
            data: chart_data,
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
    })
    .catch(error => console.log(error));
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
    }, function(start, end, label) {
        let startDate = moment(start).format("MM/DD/YYYY");
        let endDate = moment(end).format("MM/DD/YYYY");
        
        //wtable.clear().rows.add(sbw).draw();
        $("#w-table").DataTable().clear().destroy();
        $("#p-table").DataTable().clear().destroy();
        $("#e-table").DataTable().clear().destroy();
    
        renderChart(startDate,endDate);
        loadEtable(startDate, endDate);
        loadPtable(startDate, endDate);
        loadWtable(startDate, endDate);
    });
});

