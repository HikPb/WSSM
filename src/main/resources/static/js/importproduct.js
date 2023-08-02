"use strict";

const toast = new bootstrap.Toast($("#toast"));

const searchbox = document.getElementById("c-searchbox");
const warehouseEl = document.getElementById("c-warehouse");
const supplierEl = document.getElementById("c-supplier");
const createdDateEl = document.getElementById("date1");
const ipDateEl = document.getElementById("date2");
const shipFeeEl = document.getElementById("c-shipfee");
const noteEl = document.getElementById("c-note");
const totalProductEl = document.getElementById("t-prod");
const totalMoneyEl = document.getElementById("t-money");
const totalQEl = document.getElementById("t-qty");

const listItemsEl = document.getElementById("ip-items");

const searchbox2 = document.getElementById("e-searchbox");
const supplier2El = document.getElementById("e-supplier");
const ipDate2El = document.getElementById("e-date2");
const note2El = document.getElementById("e-note");
const shipFee2El = document.getElementById("e-shipfee");
const etotalProductEl = document.getElementById("et-prod");
const etotalMoneyEl = document.getElementById("et-money");
const etotalQEl = document.getElementById("et-qty");

const ctable = document.getElementById("c-table");
const etable = document.getElementById("e-table");

const table = $("#ipTable").DataTable( {
    processing: true,
    responsive: true,
    fixedHeader: true,

    ajax: {
        url: "/api/import",
        dataSrc: 'data',
        type: "GET",
        dataType: "json",
        contentType: "application/json",
    },
    columns: [
        {
            defaultContent: '',
            data: null,
            orderable: false,
            className: 'select-checkbox',
        },
        { 
            data: 'id',
            className: 'td-data'
        },
        { 
            data: 'employee.username',
            className: 'td-data'
        },
        { 
            data: 'note', 
            searchable: false,
            className: 'td-data',
            render: function(data, type, row){
                if(data!==""){
                    return data;
                }
                return `<span style="color: red;">Chưa có</span>`

            }
        },
        { 
            data: 'warehouse.name', 
            className: 'td-data'
            
        },
        {
            data: 'totalQty',
            className: 'td-data',
            searchable: false,
        },
        {
            data: 'totalMoney',
            className: 'td-data',
            searchable: false,
        },
        {
            data: 'shipFee',
            className: 'td-data',
            searchable: false,
        },
        { 
            data: 'createdAt',
            className: 'td-data',
            searchable: false,
            render: function(data, type, row){
                return moment(data).format('HH:mm DD-MM-YYYY')
            }
        },
        { 
            data: 'updatedAt',
            className: 'td-data',
            searchable: false,
            render: function(data, type, row){
                return moment(data).format('HH:mm DD-MM-YYYY')
            }
        },
        {
            data: 'status',
            orderable: false,
            searchable: false,
            render: function(data, type, row){
                if(data==0){
                    return  `<div class="btn-group">
                                <button class="btn btn-danger" style="width: 150px;"> Đã hủy </button>
                            </div>`
                }else if(data==1){
                    return  `<div class="btn-group">
                                <button class="btn btn-primary dropdown-toggle" style="width: 150px;" data-bs-toggle="dropdown" aria-expanded="false"> Mới <i class="fa fa-angle-down"></i></button>
                                <ul class="dropdown-menu" x-placement="bottom-start" style="position: absolute; transform: translate(-40px, 36px); top: 0px; left: 0px; will-change: transform;">
                                    <li>
                                        <div class="dropdown-item" onclick="changeStatus(${row.id},${2})">Đã nhập hàng</div>
                                    </li>
                                    <li>
                                        <div class="dropdown-item" onclick="changeStatus(${row.id},${0})">Hủy</div>
                                    </li>
                                </ul>
                            </div>`
                }else{
                    return  `<div class="btn-group">
                                <button class="btn btn-success" style="width: 150px;"> Đã nhập hàng </button>
                            </div>`
                }
            }
        },
    ],
    columnDefs: [
       { className: "dt-head-center", targets: [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ] },
       { className: "dt-body-center", targets: [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ] }
    ],
    buttons: [
        {
            text: '<i class="fa-solid fa-rotate"></i><span> Tải lại</span>',
            className: 'btn-tools',
            action: function ( e, dt, node, config ) {
                dt.ajax.reload(null, false);
            }
        },
        {
            extend:    'excel',
            text:      '<i class="fa-solid fa-file-export"></i><span> Xuất excel</span>',
            titleAttr: 'Excel',
            className: 'btn-tools',
            exportOptions: {
                columns: ':visible'
            }
        },
        {
            extend: "selected",
            text: '<i class="fa fa-print"></i><span> In phiếu </span>',
            className: 'btn-tools',
            action: function ( e, dt, node, config ) {
                var data = table.rows( { selected: true } ).data();
                console.log(data)
                var printElem = "";
                if(data.length>0){
                    data.each(obj =>{
                        var tbBody = "";
                        if(obj.items.length>0){
                            var i = 1;
                            obj.items.forEach(it =>{
                                let tbRow = 
                                `
                                <tr>
                                    <td>${i}</td>
                                    <td class="text-center">${it.item.product.productName}</td>
                                    <td class="text-center">${it.item.product.barcode}</td>
                                    <td class="text-center">${numberWithCommas(it.purcharsePrice)}</td>
                                    <td class="text-center">${it.qty}</td>
                                    <td class="text-center">${numberWithCommas(it.purcharsePrice*it.qty)}</td>
                                </tr>
                                `
                                i++;
                                tbBody += tbRow;
                            })
                        }
                        var elem = 
                        `<div class="page-break"></div>
                        <div class="container px-1 py-5">
                            <div class="row">
                                <div class="col">
                                    <div class="text-center mb-5">
                                        <span class="fw-bold fs-3">PHIẾU NHẬP KHO</span>
                                        <br>
                                        <span class="fst-italic">Mã phiếu: #
                                            <span>${obj.id}</span>
                                        </span>
                                    </div>
                                    <div class="row gx-5 pb-1">
                                        <div class="col">
                                            <span class="fw-bold">Người tạo phiếu:
                                            </span>
                                            <span>${obj.employee.username}</span>
                                        </div>
                                        <div class="col">
                                            <span class="fw-bold">Ngày tạo phiếu:
                                            </span>
                                            <span>${moment(obj.createdAt).format('DD/MM/YYYY')}</span>
                                        </div>
                                    </div>
                                    <div class="row gx-5 pb-1">
                                        <div class="col">
                                            <span class="fw-bold">Kho hàng:
                                            </span>
                                            <span>${obj.warehouse.name}</span>
                                        </div>
                                        <div class="col">
                                            <span class="fw-bold">Sđt:
                                            </span>
                                            <span>${obj.warehouse.phone}</span>
                                        </div>
                                    </div>
                                    <div class="row pb-4">
                                        <div class="col">
                                            <span class="fw-bold">Địa chỉ: 
                                            </span>
                                            <span>${obj.warehouse.address}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col">
                                    <div class="table-responsive">
                                        <table class="table table-bordered border-primary">
                                            <thead class="table-active">
                                                <tr>
                                                    <td>
                                                        <strong>#</strong>
                                                    </td>
                                                    <td class="text-center">
                                                        <strong>Tên sản phẩm</strong>
                                                    </td>
                                                    <td class="text-center">
                                                        <strong>Barcode</strong>
                                                    </td>
                                                    <td class="text-center">
                                                        <strong>Giá nhập</strong>
                                                    </td>
                                                    <td class="text-center">
                                                        <strong>Số lượng</strong>
                                                    </td>
                                                    <td class="text-center">
                                                        <strong>Thành tiền</strong>
                                                    </td>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                `+ tbBody +
                                                `
                                            </tbody>
                                            <tfooter>
                                                <tr>
                                                    <td colspan="2" class="text-center fw-bold">Phí vận chuyển</td>
                                                    <td colspan="3"></td>
                                                    <td class="text-center">${numberWithCommas(obj.shipFee)}</td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2" class="text-center fw-bold">Tổng cộng</td>
                                                    <td colspan="3"></td>
                                                    <td class="text-center">${numberWithCommas(obj.totalMoney)}</td>
                                                </tr>
                                            </tfooter>
                                        </table>
                                    </div>
                                    <div class="row mb-4">
                                        <span>Tổng tiền bằng chữ: ${numberToWords(obj.totalMoney)} đồng
                                        </span>
                                    </div>
                                   
                                    <div class="row">
                                        <div class="col-sm-3 text-center">
                                            <span></span><br>
                                            <span class="fw-bold">Người lập phiếu</span>
                                            <br>
                                            <span class="fst-italic">(Ký họ tên)</span>
                                        </div>
                                        <div class="col-sm-3 text-center">
                                            <span></span><br>
                                            <span class="fw-bold">Người giao hàng</span>
                                            <br>
                                            <span class="fst-italic">(Ký họ tên)</span>
                                        </div>

                                        <div class="col-sm-3 text-center">
                                            <span></span><br>
                                            <span class="fw-bold">Thủ kho</span>
                                            <br>
                                            <span class="fst-italic">(Ký họ tên)</span>
                                        </div>

                                        <div class="col-sm-3 text-center">
                                            <span>${moment(obj.createdAt).locale('vi').format('LL')}</span>
                                            <br>
                                            <span class="fw-bold">Kế toán trưởng</span>
                                            <br>
                                            <span class="fst-italic">(Ký họ tên)</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        `

                        printElem += elem;
                    })

                    var openWindow = window.open("", "title", "attributes");
                    openWindow.document.write('<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">');
                    openWindow.document.write('<style>@media print{.page-break  { display:block; page-break-before:always; }}</style>');
                    openWindow.document.write(printElem);
                    openWindow.document.close();
                    openWindow.focus();
                    // openWindow.print();
                    setTimeout(function () {
                        openWindow.print();
                        openWindow.close();
                    }, 500);
                }
            }
        },
    ],
    paging: true, 
    pagingType: 'numbers',
    lengthMenu: [ [20, 30, 50, -1], [20, 30, 50, "All"] ],
    language: {
        "search": "_INPUT_",            
        "searchPlaceholder": "Tìm kiếm",
        "lengthMenu": "_MENU_/trang",
        "zeroRecords": "Không có sản phẩm nào!",
        "info": "Trang _PAGE_/_PAGES_",
        "infoEmpty": "Không có sản phẩm",
        "infoFiltered": "(lọc từ _MAX_ kết quả)"
    },
    dom: '<"tabletop"Bif>tr<"pagetable"lp><"clear">',
    select: {
        style:    'multi',
        selector: 'td:first-child'
    },
    order: [[ 1, 'desc' ]]
});

var listItems = [];
var listItems2 = [];

function addToListItems(id) {
    searchbox.value = "";
    if (listItems.some((item) => item.itemId === id)) {
        //changeNumberOfUnits("plus", id);
    } else {
        const item = itemData.find((product) => product.id === id);
        const customItem = {
            itemId: item.id,
            pprice: item.purcharsePrice,
            sku: item.sku,
            barcode: item.product?.barcode,
            productName: item.product?.productName
        };
        listItems.push({
        ...customItem,
        qty: 0,
        });
    }
    updateListItems();
}

function updateListItems() {
    renderListItems();
    renderSubtotal();
}

// calculate and render subtotal
function renderSubtotal() {
    let totalMoney = 0;
    let totalQty = 0;
    let totalProduct = listItems.length;

    listItems.forEach((item) => {
        totalMoney += Math.abs(item.qty * item.pprice);
        totalQty += item.qty;
    });

    totalMoneyEl.textContent = `${numberWithCommas(totalMoney)}`;
    totalProductEl.textContent = `${totalProduct}`;
    totalQEl.textContent = `${totalQty}`;
}

// render list items
function renderListItems() {
    $("#c-table").find("tbody").empty();
    if(listItems.length>0){
        listItems.forEach((item) => {
            let tr =    `<tr style="height: 20px;">
                        <td>${ctable.rows.length}</td>
                        <td>${item.productName}</td>
                        <td>3</td>
                        <td>${item.barcode}</td>
                        <td><input type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value=${item.pprice} onchange="changeItemPrice(this,${item.itemId})"></td>
                        <td><input type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value=${item.qty} onchange="changeItemQty(this,${item.itemId})"></td>
                        <td>${item.pprice * item.qty}</td>
                        <td><i class="fa-regular fa-circle-xmark" style="color: #ff0000;" onclick="removeItem(${item.itemId})"></i></td>
                        </tr>`
            $("#c-table").find("tbody").append(tr);
        });
    }
}

// remove item
function removeItem(id) {
    listItems = listItems.filter((item) => item.itemId !== id);
    updateListItems();
}

function changeItemQty(elm, id) {
    let newQty = parseInt(elm.value.replace(/\,/g, ''),10);
    listItems = listItems.map((item)=>{
        if(item.itemId === id){
            item.qty = newQty;
        }
        return{
            ...item
        }
    });

    updateListItems();
}

function changeItemPrice(elm, id) {
    let newPrice = parseInt(elm.value.replace(/\,/g, ''),10);
    listItems = listItems.map((item)=>{
        if(item.itemId === id){
            item.pprice = newPrice;
        }
        return{
            ...item
        }
    });

    updateListItems();
}

function searchItem(){
    let key = searchbox.value;
    let wh = $("#c-warehouse").select2("data")[0].id;
    let searchResult=[];
    let li = "";
    $("#c-searchbox").parent().find('.search-items').remove();
    $("#c-searchbox").parent().append('<div class="search-items" id="listresult"></div>');
    if(key.length>0){
        searchResult = itemData.filter(function(s){
            return s.active == true && 
            (s.product.productName.includes(key) || s.product.barcode.includes(key)) &&
            s.warehouse.id == wh;
        })
        if(searchResult.length>0){
            $("#listresult").empty();
            searchResult.forEach(rs=>{
                $("#listresult").append($(`<div class="s-item media" onclick="addToListItems(${rs.id})">
                            <div class="media-img">
                                <img src="/img/product.jpg" width="50" height="50" >
                            </div>
                            <div class="media-body">
                                <div class="media-heading" style="margin-bottom:0px;">
                                    <span class="badge badge-info m-r-5 m-b-5">${rs.product.barcode}</span>
                                    <span class="font-14">${rs.product.productName}</span>
                                </div>
                                <div>
                                <span><i class="fa-solid fa-weight-scale"></i> <span style="color:#0af;">${rs.product.weight} g</span></span>
                                <span style="color:#0af; right:15px; position:absolute;">${numberWithCommas(rs.retailPrice)}đ</span>
                                </div>
                                <div class="font-13">
                                    <span class="m-r-20"><i class="fa-solid fa-warehouse m-r-5"></i><strong>Kho hàng: </strong>${rs.warehouse.name}</span>
                                    <span class="m-r-20"><strong>Tồn kho: </strong>${rs.qty}</span>
                                </div>
                            </div>
                        </div>`));
            })
        }else{
            $("#listresult").empty();
            $("#listresult").append($(`<div class="s-item media">
                                        Không tìm thấy kết quả nào
                                    </div>)`));
        }
    }
}

function addToListItems2(id) {
    searchbox2.value = "";
    if (listItems2.some((item) => item.itemId === id)) {
        
    } else {
        const item = itemData.find((product) => product.id === id);
        const customItem = {
            itemId: item.id,
            pprice: item.purcharsePrice,
            sku: item.sku,
            barcode: item.product?.barcode,
            productName: item.product?.productName
        };
        listItems2.push({
        ...customItem,
        qty: 0,
        });
    }
    updateListItems2();
}

function updateListItems2() {
    renderListItems2();
    renderSubtotal2();
}

// calculate and render subtotal
function renderSubtotal2() {
    let totalMoney = 0;
    let totalQty = 0;
    let totalProduct = listItems2.length;

    listItems2.forEach((item) => {
        totalMoney += Math.abs(item.qty * item.pprice);
        totalQty += item.qty;
    });

    etotalMoneyEl.textContent = `${numberWithCommas(totalMoney)}`;
    etotalProductEl.textContent = `${totalProduct}`;
    etotalQEl.textContent = `${totalQty}`;
}

// render list items
function renderListItems2() {
    $("#e-table").find("tbody").empty();
    if(listItems2.length>0){
        listItems2.forEach((item) => {
            let tr =    `<tr style="height: 20px;">
                        <td>${etable.rows.length}</td>
                        <td>${item.productName}</td>
                        <td>3</td>
                        <td>${item.barcode}</td>
                        <td><input type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value=${item.pprice} onchange="changeItemPrice2(this,${item.itemId})"></td>
                        <td><input type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value=${item.qty} onchange="changeItemQty2(this,${item.itemId})"></td>
                        <td>${item.pprice * item.qty}</td>
                        <td><i class="fa-regular fa-circle-xmark" style="color: #ff0000;" onclick="removeItem2(${item.itemId})"></i></td>
                        </tr>`
            $("#e-table").find("tbody").append(tr);
        });
    }
}

function renderListItems3() {
    $("#e-table").find("tbody").empty();
    if(listItems2.length>0){
        listItems2.forEach((item) => {
            let tr =    `<tr style="height: 20px;">
                        <td>${etable.rows.length}</td>
                        <td>${item.productName}</td>
                        <td>3</td>
                        <td>${item.barcode}</td>
                        <td><input type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value=${item.pprice} disabled></td>
                        <td><input type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value=${item.qty} disabled></td>
                        <td>${item.pprice * item.qty}</td>
                        </tr>`
            $("#e-table").find("tbody").append(tr);
        });
    }
}

// remove item
function removeItem2(id) {
    listItems2 = listItems2.filter((item) => item.itemId !== id);
    updateListItems2();
}

function changeItemQty2(elm, id) {
    let newQty = parseInt(elm.value.replace(/\,/g, ''),10);
    listItems2 = listItems2.map((item)=>{
        if(item.itemId === id){
            item.qty = newQty;
        }
        return{
            ...item
        }
    });

    updateListItems2();
}

function changeItemPrice2(elm, id) {
    let newPrice = parseInt(elm.value.replace(/\,/g, ''),10);
    listItems2 = listItems2.map((item)=>{
        if(item.itemId === id){
            item.pprice = newPrice;
        }
        return{
            ...item
        }
    });

    updateListItems2();
}

function numberWithCommas(x) {
    if(x==null) { return '0'; }
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

function numberToWords(number) {
    var digit = [
      'không',
      'một',
      'hai',
      'ba',
      'bốn',
      'năm',
      'sáu',
      'bảy',
      'tám',
      'chín',
    ];
    var elevenSeries = [
      'mười',
      'mười một',
      'mười hai',
      'mười ba',
      'mười bốn',
      'mười lăm',
      'mười sáu',
      'mười bảy',
      'mười tám',
      'mười chín',
    ];
    var countingByTens = [
      'hai mươi',
      'ba mươi',
      'bốn mươi',
      'năm mươi',
      'sáu mươi',
      'bảy mươi',
      'tám mươi',
      'chín mươi',
    ];
    var shortScale = ['', 'nghìn', 'triệu', 'tỷ', 'nghìn tỷ'];
  
    number = number.toString();
    number = number.replace(/[\, ]/g, '');
    if (number != parseFloat(number)) return 'not a number';
    var x = number.indexOf('.');
    if (x == -1) x = number.length;
    if (x > 15) return 'too big';
    var n = number.split('');
    var str = '';
    var sk = 0;
    for (var i = 0; i < x; i++) {
      if ((x - i) % 3 == 2) {
        if (n[i] == '1') {
          str += elevenSeries[Number(n[i + 1])] + ' ';
          i++;
          sk = 1;
        } else if (n[i] != 0) {
          str += countingByTens[n[i] - 2] + ' ';
          sk = 1;
        }
      } else if (n[i] != 0) {
        str += digit[n[i]] + ' ';
        if ((x - i) % 3 == 0) str += 'trăm ';
        sk = 1;
      }
      if ((x - i) % 3 == 1) {
        if (sk) str += shortScale[(x - i - 1) / 3] + ' ';
        sk = 0;
      }
    }
    if (x != number.length) {
      var y = number.length;
      str += 'phẩy ';
      for (var i = x + 1; i < y; i++) str += digit[n[i]] + ' ';
    }
    str = str.replace(/\number+/g, ' ');
    var str2 = str.charAt(0).toUpperCase() + str.slice(1);
    return str2.trim();
}

function debounce(func, wait, immediate) {
    var timeout;
    return function() {
        var context = this, args = arguments;
        var later = function() {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        var callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
};

function closeAllLists(elmnt) {
    var x = document.getElementsByClassName("search-items");
    var inp = document.getElementById("c-searchbox");
    for (var i = 0; i < x.length; i++) {
    if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
    }
    }
}

document.addEventListener("click", function (e) {
    closeAllLists(e.target);
});

function searchItem2(){
    let key = searchbox2.value;
    let wh = $("#e-warehouse").select2("data")[0].id;
    let searchResult=[];
    let li = "";
    $("#e-searchbox").parent().find('.search-items').remove();
    $("#e-searchbox").parent().append('<div class="search-items" id="elistresult"></div>');
    if(key.length>0){
        searchResult = itemData.filter(function(s){
            return s.active == true && 
            (s.product.productName.includes(key) || s.product.barcode.includes(key)) &&
            s.warehouse.id == wh;
        })
        if(searchResult.length>0){
            $("#elistresult").empty();
            searchResult.forEach(rs=>{
                $("#elistresult").append($(`<div class="s-item media" onclick="addToListItems2(${rs.id})">
                            <div class="media-img">
                                <img src="/img/product.jpg" width="50" height="50" >
                            </div>
                            <div class="media-body">
                                <div class="media-heading" style="margin-bottom:0px;">
                                    <span class="badge badge-info m-r-5 m-b-5">${rs.product.barcode}</span>
                                    <span class="font-14">${rs.product.productName}</span>
                                </div>
                                <div>
                                <span><i class="fa-solid fa-weight-scale"></i> <span style="color:#0af;">${rs.product.weight} g</span></span>
                                <span style="color:#0af; right:15px; position:absolute;">${numberWithCommas(rs.retailPrice)}đ</span>
                                </div>
                                <div class="font-13">
                                    <span class="m-r-20"><i class="fa-solid fa-warehouse m-r-5"></i><strong>Kho hàng: </strong>${rs.warehouse.name}</span>
                                    <span class="m-r-20"><strong>Tồn kho: </strong>${rs.qty}</span>
                                </div>
                            </div>
                        </div>`));
            })
        }else{
            $("#elistresult").empty();
            $("#elistresult").append($(`<div class="s-item media">
                                        Không tìm thấy kết quả nào
                                    </div>)`));
        }
    }
}

async function loadItemData() {
    try {
        await fetch("/api/item", {
            method: "GET", // or 'PUT'
            headers: {
            "Content-Type": "application/json",
            }
        }).then(response =>{
            return response.json();
        }).then(data=>{
            itemData = data.data;
        })
    } catch (error) {
      console.error("Error:", error);
    }
  }

async function changeStatus(id, status){
    let url = "/api/import/"+id+"/status/"+status;
    await fetch(url, {
            method: "POST"
        })
        .then(response => {
            if (!response.ok) throw Error(response.statusText);
            return response.json();
        })
        .then(data => {
            if(status==2){loadItemData();sendMessage3(window.location.href);}
            //table.ajax.reload(null, false) 
            sendMessage2(window.location.href); 
            $("#toast-content").html("Cập nhật thành công: # "+data.data['id']);
            toast.show()
        })
        .catch(error => console.log(error));
}

$(document).ready(function () {
    new $.fn.dataTable.Buttons( table, {
        buttons: [             
            {
            extend:    'print',
            text:      '<i class="fa fa-print"></i> In',
            titleAttr: 'Print',
            className: 'btn-tools',
            exportOptions: {
                columns: ':visible'
            }
            },  
            {
                extend:    'excel',
                text:      '<i class="fa-solid fa-file-export"></i><span>Xuất excel</span>',
                titleAttr: 'Excel',
                className: 'btn-tools',
                exportOptions: {
                    columns: ':visible'
                }
                },
        ]
    } );
    table.buttons().container().appendTo('#action-tools');

    table.on("click", "th.select-checkbox", function() {
        if ($("th.select-checkbox").hasClass("selected")) {
            table.rows().deselect();
            $("th.select-checkbox").removeClass("selected");
        } else {
            table.rows().select();
            $("th.select-checkbox").addClass("selected");
        }
    }).on("select deselect", function() {
        ("Some selection or deselection going on")
        if (table.rows({
                selected: true
            }).count() !== table.rows().count()) {
            $("th.select-checkbox").removeClass("selected");
        } else {
            $("th.select-checkbox").addClass("selected");
        }
    });

    $('#ipTable').on('click', 'tbody tr .td-data', function (e) {
        e.preventDefault();
        // $("#editModalDiv").empty();
        // $("#editModalDiv").append(ipEditModalEl);

        let data = table.row(this).data();
        let href = "/api/import/"+data["id"];
        $.get(href, function(res){
            $("#e-form").attr("rid", data["id"]);
            $("#e-date1").val(moment(data.createdAt).format('YYYY-MM-DD'));
            $("#e-date2").val(moment(data.expected_at).format('YYYY-MM-DD'));
            $("#e-warehouse").val(data.warehouse.id).trigger('change');
            $("#e-warehouse").prop("disabled", true);
            $("#e-supplier").val('').trigger('change');
            if(data.supplier!=null){
                $("#e-supplier").val(data.supplier.id).trigger('change');
            }
            $("#e-shipfee").val(data.shipFee);
            $("#e-note").val(res.data.note);
            
            listItems2.splice(0,listItems2.length);
            data.items.map(item=>{
                let customItem = {
                    id: item.id,
                    itemId: item.item.id,
                    pprice: item.purcharsePrice,
                    sku: item.sku,
                    barcode: item.item.product.barcode,
                    productName: item.item.product.productName,
                    qty: item.qty,
                };
                listItems2.push({
                ...customItem,
                });
            })
            
            if(data.status==0){
                $("#eip-submit").prop('disabled', true);
                $("#e-statusBtn").text("Đã hủy");
                $("#e-statusBtn").removeClass();
                $("#e-statusBtn").addClass('btn btn-danger');
                renderListItems3();
                renderSubtotal2();
            }else if(data.status==1){
                $("#eip-submit").prop('disabled', false);
                $("#e-statusBtn").text("Mới");
                $("#e-statusBtn").removeClass();
                $("#e-statusBtn").addClass('btn btn-primary');
                updateListItems2();
            }else{
                $("#eip-submit").prop('disabled', true);
                $("#e-statusBtn").text("Đã nhập hàng");
                $("#e-statusBtn").removeClass();
                $("#e-statusBtn").addClass('btn btn-success');
                renderListItems3();
                renderSubtotal2();
            }
            
        })
        $("#ip-edit-modal").modal("show");
    });

    $("#e-form").on("submit", function (e) {
        e.preventDefault();

        let items = [];
        let totalQty = 0;
        let totalMoney = 0;
        listItems2.map(function (elem) {
            totalQty += elem.qty;
            totalMoney += elem.qty * elem.pprice;

            let obj = {
                id: elem.id,
                itemId : elem.itemId,
                qty: elem.qty,
                pprice : elem.pprice,
                sku : elem.sku,
            }
            items.push(obj)
        });

        let payload = JSON.stringify({
            note: note2El.value,
            supId: supplier2El.value,
            expectedAt: ipDate2El.value,
            totalQty: totalQty,
            totalMoney: totalMoney,
            shipFee: shipFee2El.value,
            items: items
        });
        $.ajax({
            url: "/api/import/"+$("#e-form").attr("rid"),
            method: "put",
            data: payload,
            contentType: "application/json",
            success: function (response) { 
                //table.ajax.reload(null, false) 
                $('#ip-edit-modal form').trigger("reset")
                listItems2.splice(0,listItems2.length);
                $("#ip-edit-modal").modal("hide");
                $("#ip-edit-modal").find('form').trigger('reset');
                sendMessage2(window.location.href); 
                $("#toast-content").html("Chỉnh sửa thành công: # "+response.data['id']);
                toast.show()
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
    });

    $("#e-warehouse").select2({
        data: $.map(wareData, function(s) {
            return {
                text: s.name,
                id: s.id
            }
        }),
        placeholder: "Chọn kho hàng",
        width: '100%',
        minimumResultsForSearch: Infinity,
        dropdownParent: ".ware-e-group"
    });

    $("#e-supplier").select2({
        data: $.map(supData, function(s) {
            return {
                text: s.supName,
                id: s.id
            }
        }),
        placeholder: "Chọn nhà cung cấp",
        width: '100%',
        minimumResultsForSearch: Infinity,
        dropdownParent: ".sup-e-group"
    });

    $("#e-searchbox").on("keyup", debounce(searchItem2, 500));

    $("#btnCreate").on("click", function(e){
        e.preventDefault();
        // $("#createModalDiv").empty();
        // $("#createModalDiv").append(ipCreateModalEl);

        $("#ip-create-modal").modal("show"); 
    });

    $("#c-warehouse").select2({
        data: $.map(wareData, function(s) {
            return {
                text: s.name,
                id: s.id
            }
        }),
        placeholder: "Chọn kho hàng",
        width: '100%',
        minimumResultsForSearch: Infinity,
        dropdownParent: ".ware-c-group"
    });

    $("#c-supplier").select2({
        data: $.map(supData, function(s) {
            return {
                text: s.supName,
                id: s.id
            }
        }),
        placeholder: "Chọn nhà cung cấp",
        width: '100%',
        minimumResultsForSearch: Infinity,
        dropdownParent: ".sup-c-group"
    });

    $("#date1").val(moment().format('YYYY-MM-DD'));

    $("#c-searchbox").on("keyup", debounce(searchItem, 500));

    $("#c-form").on("submit", function (e) {
        e.preventDefault();
        
        let items = [];
        let totalQty = 0;
        let totalMoney = 0;
        listItems.map(function (elem) {
            totalQty += elem.qty;
            totalMoney += elem.qty * elem.pprice;

            let obj = {
                itemId : elem.itemId,
                qty: elem.qty,
                pprice : elem.pprice,
                sku : elem.sku,
            }
            items.push(obj)
        });
        let payload = JSON.stringify({
          note: noteEl.value,
          status: 1,
          empId: user.id,
          wareId: warehouseEl.value,
          supId: supplierEl.value,
          expectedAt: ipDateEl.value,
          totalQty: totalQty,
          totalMoney: totalMoney,
          shipFee: shipFeeEl.value,
          items: items
        });
        $.ajax({
            url: "/api/import",
            method: "post",
            data: payload,
            contentType: "application/json",
            success: function (response) { 
                //table.ajax.reload(null, false) 
                //$("#c-table").find("tbody").empty();
                $("#c-supplier").val('').trigger('change');
                listItems.splice(0,listItems.length);
                updateListItems();
                $("#ip-create-modal").modal("hide");
                $("#ip-create-modal").find('form').trigger('reset');
                $("#toast-content").html("Tạo mới thành công: # "+response.data['id'])
                toast.show();
                sendMessage();
                sendMessage2(window.location.href); 
                //window.location.href = "/products"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
    });

});

const ipCreateModalEl = 
`<div class="modal fade" role="dialog" id="ip-create-modal">
    <div class="modal-dialog modal-xl" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <div class="modal-title">
                    Thông tin chi tiết phiếu nhập kho
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form action="#" enctype="multipart/form-data" method="post" id="c-form">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-3">
                            <div class="m-box">
                                <div class="form-group ware-c-group">
                                    <span class="form-title">Kho hàng:<span
                                            style="color: rgb(245, 34, 45); margin-right: 4px;">*</span></span>
                                    <select class="form-control" id="c-warehouse"></select>
                                </div>

                                <div class="form-group sup-c-group">
                                    <span class="form-title">Nhà cung cấp:</span>
                                    <select class="form-control" id="c-supplier">
                                        <option></option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <span class="form-title">Tiền vận chuyển:</span>
                                    <div class="box-input-group">
                                        <input class="form-control" role="spinbutton" id="c-shipfee" aria-valuenow="0" autocomplete="off" step="1000" value="0">
                                        <span class="input-group-addon">₫</span>
                                    </div>
                                </div>
                                

                                <div class="form-group">
                                    <span class="form-title">Ngày tạo phiếu:</span>
                                    <input type="date" class="form-control" id="date1">
                                </div>
                                
                                <div class="form-group">
                                    <span class="form-title">Ngày dự kiến nhận:</span>
                                    <input type="date" class="form-control" id="date2">
                                </div>
                                
                                <div class="form-group">
                                    <span class="form-title">Ghi chú:</span>
                                    <textarea rows="4" placeholder="Ghi chú" autosize="[object Object]"
                                        class="form-control" id="c-note"></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-9">
                            <div class="m-box">
                                <div class="input-group">
                                    <input class="form-control" id="c-searchbox" type="text" autocomplete="off" placeholder="Nhập tên, mã sản phẩm">
                                    <div class="input-group-btn">
                                        <button class="btn btn-success" type="button">Tìm kiếm</button>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="m-box" style="margin-top: 1 rem;">
                                <div class="table-container">
                                    <div style="display: block; overflow: auto; height: 360px;">
                                        <table class="table table-hover table-responsive-xl" id="c-table">
                                            <thead class="thead-light" style="position: sticky; top: 0; z-index: 1; background-color: #eee;">
                                                <tr>
                                                    <th>STT</th>
                                                    <th>Tên SP</th>
                                                    <th>Hình ảnh</th>
                                                    <th>Mã SP</th>
                                                    <th>Giá nhập</th>
                                                    <th>Số lượng</th>
                                                    <th>Tổng tiền</th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            <tbody style="display: table-row-group;"></tbody>
                                            <tfoot>
                            
                                            </tfoot>
                                        </table>
                                    </div>
                                    <div style="text-align: right;">
                                        <span class="font-14 p-r-10 p-l-10">Tổng SP: <span id="t-prod">0</span> </span>
                                        <span class="font-14 p-r-10 p-l-10" style="border-left: 1px solid #d9d9d9;border-right: 1px solid #d9d9d9;">Tổng SL: <span id="t-qty">0</span> </span>
                                        <span class="font-14 p-r-10 p-l-10"></span>Tổng tiền: <span id="t-money">0</span> </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" style="margin-right: 10px; width: 120px;" data-toggle="popover" id="statusBtn">
                        Mới
                    </button>
                    <button type="submit" class="btn btn-primary" id="btn-ip-submit">
                        <span>Lưu</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>`;

const ipEditModalEl =
`<div class="modal fade" role="dialog" id="ip-edit-modal">
    <div class="modal-dialog modal-xl" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <div class="modal-title">
                    Thông tin chi tiết phiếu nhập kho
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form action="#" enctype="multipart/form-data" method="post" id="e-form">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-3">
                            <div class="m-box">
                                <div class="form-group ware-e-group">
                                    <span class="form-title">Kho hàng:<span
                                            style="color: rgb(245, 34, 45); margin-right: 4px;">*</span></span>
                                    <select class="form-control" id="e-warehouse"></select>
                                </div>

                                <div class="form-group sup-e-group">
                                    <span class="form-title">Nhà cung cấp:</span>
                                    <select class="form-control" id="e-supplier">
                                        <option></option>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <span class="form-title">Tiền vận chuyển:</span>
                                    <div class="box-input-group">
                                        <input class="form-control" id="e-shipfee" role="spinbutton" aria-valuenow="0" autocomplete="off" step="1000" value="0">
                                        <span class="input-group-addon">₫</span>
                                    </div>
                                </div>
                                

                                <div class="form-group">
                                    <span class="form-title">Ngày tạo phiếu:</span>
                                    <input type="date" class="form-control" id="e-date1">
                                </div>
                                
                                <div class="form-group">
                                    <span class="form-title">Ngày dự kiến nhận:</span>
                                    <input type="date" class="form-control" id="e-date2">
                                </div>
                                
                                <div class="form-group">
                                    <span class="form-title">Ghi chú:</span>
                                    <textarea rows="4" placeholder="Ghi chú" autosize="[object Object]" id="e-note" class="form-control"></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-9">
                            <div class="m-box">
                                <div class="input-group">
                                    <input class="form-control" id="e-searchbox" type="text" autocomplete="off" placeholder="Nhập tên, mã sản phẩm">
                                    <div class="input-group-btn">
                                        <button class="btn btn-success" type="button">Tìm kiếm</button>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="m-box" style="margin-top: 1 rem;">
                                <div class="table-container">
                                    <div style="display: block; overflow: auto; height: 360px;">
                                        <table class="table table-hover table-responsive-xl" id="e-table">
                                            <thead class="thead-light" style="position: sticky; top: 0; z-index: 1; background-color: #eee;">
                                                <tr>
                                                    <th>STT</th>
                                                    <th>Tên SP</th>
                                                    <th>Hình ảnh</th>
                                                    <th>Mã SP</th>
                                                    <th>Giá nhập</th>
                                                    <th>Số lượng</th>
                                                    <th>Tổng tiền</th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            <tbody style="display: table-row-group;"></tbody>
                                            <tfoot>
                            
                                            </tfoot>
                                        </table>
                                    </div>
                                    
                                    <div style="text-align: right;">
                                        <span class="font-14 p-r-10 p-l-10">Tổng SP: <span id="et-prod">0</span> </span>
                                        <span class="font-14 p-r-10 p-l-10" style="border-left: 1px solid #d9d9d9;border-right: 1px solid #d9d9d9;">Tổng SL: <span id="et-qty">0</span> </span>
                                        <span class="font-14 p-r-10 p-l-10"></span>Tổng tiền: <span id="et-money">0</span> </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" style="margin-right: 10px; width: 120px;" id="e-statusBtn">
                    </button>
                    <button type="submit" class="btn btn-primary" id="eip-submit">
                        <span>Lưu</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>`;