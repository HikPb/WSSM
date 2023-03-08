const toast = new bootstrap.Toast($("#toast"));
const searchbox = document.getElementById("c-searchbox");
const listItemsEl = document.getElementById("cq-items");

const warehouseEl = document.getElementById("c-warehouse");
const createdDateEl = document.getElementById("date1");
const cqDateEl = document.getElementById("date2");
const noteEl = document.getElementById("c-note");

const table = document.getElementById("table");



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
        qty: 1,
        discount: 0
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
    let totalPrice = 0,
        totalItems = 0;
        totalDiscount = 0;
        
    discount = parseInt(discountEl.value.replace(/\,/g, ''),10);
    shippingFee = parseInt(shipFeeEl.value.replace(/\,/g, ''),10);
    receivedMoney = parseInt(reMoneyEl.value.replace(/\,/g, ''),10);

    listItems.forEach((item) => {
        totalPrice += item.sprice * item.qty;
        totalItems += item.qty;
        totalDiscount += parseInt(item.discount,10);
    });

    totalPrice += shippingFee;
    totalDiscount += discount;

    totalTEl.textContent = `${numberWithCommas(totalPrice)}`;
    totalDisTEl.textContent = `${numberWithCommas(totalDiscount)}`;
    paymentTEl.textContent = `${numberWithCommas(totalPrice - totalDiscount)}`;
    receivedTEl.textContent = `${numberWithCommas(receivedMoney)}`;
    oweTEl.textContent = `${numberWithCommas(totalPrice - totalDiscount - receivedMoney)}`;
    
    subTotalEl.textContent = `${numberWithCommas(totalPrice)}`;
    codEl.textContent = `${numberWithCommas(totalPrice - totalDiscount - receivedMoney)}`;
}

// render list items
function renderListItems() {
    $("#table").find("tbody").empty();
    if(listItems.length>0){
        listItems.forEach((item) => {
            tr =    `<tr style="height: 20px;">
                        <td>${table.rows.length-1}</td>
                        <td>${item.productName}</td>
                        <td>3</td>
                        <td>${item.barcode}</td>
                        <td>${item.inventory}</td>
                        <td>6</td>
                        <td>7</td>
                    </tr>`
            $("#table").find("tbody").append(tr);
        });
    }
}

// remove item from cart
function removeItemFromCart(id) {
    listItems = listItems.filter((item) => item.itemId !== id);
    updateListItems();
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

function changeItemQty(elm, id) {
    let newQty = elm.value;
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

function changeItemDiscount(elm, id) {
    let newDiscount = elm.value;
    listItems = listItems.map((item)=>{
        if(item.itemId === id){
            item.discount = newDiscount;
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

var listItems = [];



$(document).ready(function () {
    let table = $("#cqTable").DataTable( {
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
                orderable: false,
                searchable: false,
                defaultContent: `<div class="btn-group">
                                    <button class="btn btn-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false"> Mới <i class="fa fa-angle-down"></i></button>
                                    <ul class="dropdown-menu" x-placement="bottom-start" style="position: absolute; transform: translate(-40px, 36px); top: 0px; left: 0px; will-change: transform;">
                                        <li>
                                            <a class="dropdown-item" href="javascript:;">Đã xác nhận</a>
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="javascript:;">Hủy</a>
                                        </li>
                                    </ul>
                                </div>`
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
        data = table.row(this).data();
        href = "products/api/"+data["productId"];
        $.get(href, function(product, status){
            $("#id-val").val(product.productId);
            $("#name-val").val(product.productName);
            $("#barcode-val").val(product.barcode);
            $("#ip-val").val(product.importPrice);
            $("#sp-val").val(product.sellPrice);
            $("#cate-val").val(product.cateId);
            // $("#time-val").innerHTML += 'Hello';
        })
        $("#cq-edit-modal").modal("show");
    });

    $("#btnCreate").on("click", function(e){
        e.preventDefault();
        $("#cq-create-modal").modal("show"); 
    });

    $("#cqTable tbody").on("click", ".btn-delete", function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        href = "products/api/"+data["productId"]+"";
        console.log(data);
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
                window.location.href = "/products"
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
        dropdownParent: ".ware-c-group"
    });

    $("#date1").val(moment().format('YYYY-MM-DD'));

    $("#c-searchbox").on("keyup", debounce(searchItem, 500));
    $("#btnClear").on("click", function (e) {
      e.preventDefault();
      //table.ajax.reload(null, false)
    //   window.location="/products"
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
