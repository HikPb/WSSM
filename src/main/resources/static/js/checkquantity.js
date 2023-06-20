"use strict";

const toast = new bootstrap.Toast($("#toast"));
const searchbox = document.getElementById("c-searchbox");
const searchbox2 = document.getElementById("e-searchbox");
const listItemsEl = document.getElementById("cq-items");

const warehouseEl = document.getElementById("c-warehouse");
const createdDateEl = document.getElementById("date1");
const cqDateEl = document.getElementById("date2");
const noteEl = document.getElementById("c-note");
const cqDate2El = document.getElementById("e-date2");
const note2El = document.getElementById("e-note");

const totalProductEl = document.getElementById("t-prod");
const totalDiffEl = document.getElementById("t-diff");
const totalQEl = document.getElementById("t-qaa");

const etotalProductEl = document.getElementById("et-prod");
const etotalDiffEl = document.getElementById("et-diff");
const etotalQEl = document.getElementById("et-qaa");

const ctable = document.getElementById("table");
const etable = document.getElementById("etable");

const table = $("#cqTable").DataTable( {
    processing: true,
    responsive: true,
    fixedHeader: true,

    ajax: {
        url: "/api/checkqty",
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
            data: 'createdAt',
            className: 'td-data',
            searchable: false,
            render: function(data, type, row){
                return moment(data).format('HH:mm DD-MM-YYYY')
            }
        },
        { 
            data: 'checkqtyAt',
            className: 'td-data',
            searchable: false,
            render: function(data, type, row){
                if(data!=null){
                    return moment(data).format('HH:mm DD-MM-YYYY')
                }
                return ``;
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
                                <button class="btn btn-primary dropdown-toggle" style="width: 150px;" data-bs-toggle="dropdown" aria-expanded="false"> Đang kiểm hàng <i class="fa fa-angle-down"></i></button>
                                <ul class="dropdown-menu" x-placement="bottom-start" style="position: absolute; transform: translate(-40px, 36px); top: 0px; left: 0px; will-change: transform;">
                                    <li>
                                        <div class="dropdown-item" onclick="changeStatus(${row.id},${2})">Đã kiểm hàng</div>
                                    </li>
                                    <li>
                                        <div class="dropdown-item" onclick="changeStatus(${row.id},${0})">Hủy</div>
                                    </li>
                                </ul>
                            </div>`
                }else{
                    return  `<div class="btn-group">
                                <button class="btn btn-success" style="width: 150px;"> Đã kiểm hàng </button>
                            </div>`
                }
            }
        },
    ],
    columnDefs: [
       { className: "dt-head-center", targets: [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ] },
       { className: "dt-body-center", targets: [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ] }
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
    dom: '<"tabletop"if>tr<"pagetable"lp><"clear">',
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
        changeNumberOfUnits("plus", id);
    } else {
        const item = itemData.find((product) => product.id === id);
        const customItem = {
            itemId: item.id,
            sprice: item.retailPrice,
            inventory: item.qty,
            sku: item.sku,
            barcode: item.product?.barcode,
            productName: item.product?.productName
        };
        listItems.push({
        ...customItem,
        diff: 0,
        qaa: item.qty
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
    let totalDiff = 0;
    let totalQaas = 0;
    let totalProduct = listItems.length;

    listItems.forEach((item) => {
        totalDiff += Math.abs(item.diff);
        totalQaas += item.qaa;
    });

    totalDiffEl.textContent = `${totalDiff}`;
    totalProductEl.textContent = `${totalProduct}`;
    totalQEl.textContent = `${totalQaas}`;
}

// render list items
function renderListItems() {
    $("#table").find("tbody").empty();
    if(listItems.length>0){
        listItems.forEach((item) => {
            let tr =    `<tr style="height: 20px;">
                        <td>${ctable.rows.length}</td>
                        <td>${item.productName}</td>
                        <td>3</td>
                        <td>${item.barcode}</td>
                        <td>${item.inventory}</td>
                        <td>${item.diff}</td>
                        <td><input type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value=${item.qaa} onchange="changeItemQaa(this,${item.itemId})"></td>
                        <td><i class="fa-regular fa-circle-xmark" style="color: #ff0000;" onclick="removeItem(${item.itemId})"></i></td>
                        </tr>`
            $("#table").find("tbody").append(tr);
        });
    }
}

function removeItem(id) {
    listItems = listItems.filter((item) => item.itemId !== id);
    updateListItems();
}

function addToListItems2(id) {
    searchbox2.value = "";
    if (listItems2.some((item) => item.itemId === id)) {
        changeNumberOfUnits("plus", id);
    } else {
        const item = itemData.find((product) => product.id === id);
        const customItem = {
            itemId: item.id,
            sprice: item.retailPrice,
            inventory: item.qty,
            sku: item.sku,
            barcode: item.product?.barcode,
            productName: item.product?.productName
        };
        listItems2.push({
        ...customItem,
        diff: 0,
        qaa: item.qty
        });
    }
    updateListItems2();
}

function updateListItems2() {
    renderListItems2();
    renderSubtotal2();
}

function renderSubtotal2() {
    let totalDiff = 0;
    let totalQaas = 0;
    let totalProduct = listItems2.length;

    listItems2.forEach((item) => {
        totalDiff += Math.abs(item.diff);
        totalQaas += item.qaa;
    });

    etotalDiffEl.textContent = `${totalDiff}`;
    etotalProductEl.textContent = `${totalProduct}`;
    etotalQEl.textContent = `${totalQaas}`;
}

// render list items
function renderListItems2() {
    $("#etable").find("tbody").empty();
    if(listItems2.length>0){
        listItems2.forEach((item) => {
            let tr =    `<tr style="height: 20px;">
                        <td>${etable.rows.length}</td>
                        <td>${item.productName}</td>
                        <td>3</td>
                        <td>${item.barcode}</td>
                        <td>${item.inventory}</td>
                        <td>${item.diff}</td>
                        <td><input type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value=${item.qaa} onchange="changeItemQaa2(this,${item.itemId})"></td>
                        <td><i class="fa-regular fa-circle-xmark" style="color: #ff0000;" onclick="removeItem2(${item.itemId})"></i></td>
                        </tr>`
            $("#etable").find("tbody").append(tr);
        });
    }
}

function renderListItems3() {
    $("#etable").find("tbody").empty();
    if(listItems2.length>0){
        listItems2.forEach((item) => {
            let tr =    `<tr style="height: 20px;">
                        <td>${etable.rows.length}</td>
                        <td>${item.productName}</td>
                        <td>3</td>
                        <td>${item.barcode}</td>
                        <td>${item.inventory}</td>
                        <td>${item.diff}</td>
                        <td><input type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value=${item.qaa} disabled></td>
                        </tr>`
            $("#etable").find("tbody").append(tr);
        });
    }
}

function changeItemQaa2(elm, id) {
    let newQaa = parseInt(elm.value.replace(/\,/g, ''),10);
    listItems2 = listItems2.map((item)=>{
        if(item.itemId === id){
            item.qaa = newQaa;
            item.diff = item.qaa - item.inventory;
        }
        return{
            ...item
        }
    });

    updateListItems2();
}

// remove item
function removeItem2(id) {
    listItems2 = listItems2.filter((item) => item.itemId !== id);
    updateListItems2();
}

    // change number of units for an item
function changeNumberOfUnits(action, id) {
    listItems = listItems.map((item) => {
        let qty = item.qty;
        if (item.itemId === id) {
            if (action === "minus" && qty > 1) {
                qty--;
            } else if (action === "plus" && qty < item.inventory) {
                qty++;
            }
        }
        return {
        ...item,
        qty,
        };
    });
    updateListItems();
}

function changeItemQaa(elm, id) {
    let newQaa = parseInt(elm.value.replace(/\,/g, ''),10);
    listItems = listItems.map((item)=>{
        if(item.itemId === id){
            item.qaa = newQaa;
            item.diff = item.qaa - item.inventory;
        }
        return{
            ...item
        }
    });

    updateListItems();
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
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
    let url = "/api/checkqty/"+id+"/status/"+status;
    await fetch(url, {
            method: "POST"
        })
        .then(response => {
            if (!response.ok) throw Error(response.statusText);
            return response.json();
        })
        .then(data => {
            if(status==2){loadItemData();}
            table.ajax.reload(null, false) 
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

    $('#cqTable').on('click', 'tbody tr .td-data', function (e) {
        e.preventDefault();
        // var data = table.row($(this).parents('tr')).data();
        let data = table.row(this).data();
        let href = "/api/checkqty/"+data["id"];
        $.get(href, function(res){
            $("#e-form").attr("rid", data["id"]);
            $("#e-date1").val(moment(data.createdAt).format('YYYY-MM-DD'));
            $("#e-date2").val(moment(data.checkqtyAt).format('YYYY-MM-DD'));
            $("#e-warehouse").val(data.warehouse.id).trigger('change')
            $("#e-warehouse").prop("disabled", true);
            $("#e-note").val(res.data.note);
            
            listItems2.splice(0,listItems2.length);
            data.items.map(item=>{
                let customItem = {
                    id: item.id,
                    itemId: item.item.id,
                    sprice: item.item.retailPrice,
                    inventory: item.bf_qty,
                    sku: item.sku,
                    barcode: item.item.product.barcode,
                    productName: item.item.product.productName,
                    qaa: item.cr_qty,
                    diff: item.cr_qty - item.bf_qty
                };
                listItems2.push({
                ...customItem,
                });
            })
            if(data.status==0){
                $("#ecq-submit").prop('disabled', true);
                $("#e-statusBtn").text("Đã hủy");
                $("#e-statusBtn").removeClass();
                $("#e-statusBtn").addClass('btn btn-danger');
                renderListItems3();
                renderSubtotal2();
            }else if(data.status==1){
                $("#ecq-submit").prop('disabled', false);
                $("#e-statusBtn").text("Đang kiểm");
                $("#e-statusBtn").removeClass();
                $("#e-statusBtn").addClass('btn btn-primary');
                updateListItems2();
            }else{
                $("#ecq-submit").prop('disabled', true);
                $("#e-statusBtn").text("Đã kiểm");
                $("#e-statusBtn").removeClass();
                $("#e-statusBtn").addClass('btn btn-success');
                renderListItems3();
                renderSubtotal2();
            }
            
        })
        $("#cq-edit-modal").modal("show");
    });

    $("#btnCreate").on("click", function(e){
        e.preventDefault();
        listItems.splice(0,listItems.length);
        $("#cq-create-modal").modal("show"); 
        //$("#c-warehouse").val(null).trigger('change');
    
    });

    $("#c-form").on("submit", function (e) {
        e.preventDefault();
        let items = [];
        listItems.map(function (elem) {
            let obj = {
                itemId : elem.itemId,
                bfQty: elem.inventory,
                crQty : elem.qaa,
                sku : elem.sku,
            }
            items.push(obj)
        });
        let payload = JSON.stringify({
          note: noteEl.value,
          status: 1,
          empId: user.id,
          wareId: warehouseEl.value,
          checkqtyAt: cqDateEl.value,
          items: items
        });
        $.ajax({
            url: "/api/checkqty",
            method: "post",
            data: payload,
            contentType: "application/json",
            success: function (response) { 
                table.ajax.reload(null, false) 
                $('#cq-create-modal form').trigger("reset")
                listItems.splice(0,listItems.length);
                $("#cq-create-modal").modal("hide");
                $("#cq-create-modal").find('form').trigger('reset');
                $("#toast-content").html("Tạo mới thành công: # "+response.data['id']);
                toast.show();
                //window.location.href = "/products"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });

    $("#e-form").on("submit", function (e) {
        e.preventDefault();
        let items = [];
        listItems2.map(function (elem) {
            let obj = {
                id: elem.id,
                itemId : elem.itemId,
                bfQty: elem.inventory,
                crQty : elem.qaa,
                sku : elem.sku,
            }
            items.push(obj)
        });
        let payload = JSON.stringify({
            note: note2El.value,
            checkqtyAt: cqDate2El.value,
            items: items
        });
        $.ajax({
            url: "/api/checkqty/"+$("#e-form").attr("rid"),
            method: "put",
            data: payload,
            contentType: "application/json",
            success: function (response) { 
                table.ajax.reload(null, false) 
                $('#cq-edit-modal form').trigger("reset")
                listItems2.splice(0,listItems2.length);
                $("#cq-edit-modal").modal("hide");
                $("#cq-edit-modal").find('form').trigger('reset');
                $("#toast-content").html("Chỉnh sửa thành công: # "+response.data['id']);
                toast.show()
                //window.location.href = "/products"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
    });

    $("#cqTable tbody").on("click", ".btn-delete", function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        href = "products/api/"+data["productId"]+"";
        // link = $(this);
        // href = "products/delete/"+link.attr("productId")+"";
        $("#yesBtn").attr("href", href);
        $("#yesBtn").attr("productId", data["productId"]);

        $("#confirmText").html("Bạn muốn xoá sản phẩm này: \<strong\>" + data["productName"] + "\<\/strong\>?");
        $("#confirmModal").modal("show");
    });

    $("#yesBtn").on("click", function (e) {
        e.preventDefault();
        url = $(this).attr("href");
        id = $(this).attr("productId");
        $.ajax({
            url: url,
            method: "GET",
            success: function (data) {  
                window.location.href = "/checkqty"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
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

    $("#date1").val(moment().format('YYYY-MM-DD'));

    $("#c-searchbox").on("keyup", debounce(searchItem, 500));
    $("#e-searchbox").on("keyup", debounce(searchItem2, 500));
    $("#btnClear").on("click", function (e) {
        e.preventDefault();
        table.ajax.reload(null, false)
    });

    const popover = new bootstrap.Popover('#statusBtn', {
        container: 'body',
        html: true,
        sanitize: false,
        placement: 'top',
        trigger: 'hover focus',
        content: function(){
                    return "<div style='display: grid;'>"+
                            "<button class='btn btn-secondary'>Da xac nhan</button>"+
                            "<button class='btn btn-success'>Da nhap hang</button>"+
                            "</div>"
        }
    });
});
