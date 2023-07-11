var table;
function loadDatatable(url){
    table = $("#orderTable").DataTable( {
        processing: true,
        responsive: true,
        fixedHeader: true,
    
        ajax: {
            url: url,
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
                data: 'deliveryUnitId',
                className: 'td-data',
                render: function(data, type, row){
                    if(data!==""){
                        return data;
                    }
                    return `<span style="color: red;">Chưa có</span>`
    
                }
            },
            { 
                data: 'customer.name', 
                searchable: false,
                className: 'td-data',
            },
            { 
                data: 'customer.phone', 
                className: 'td-data'
                
            },
            {
                data: 'internalNote',
                className: 'td-data',
                searchable: false,
            },
            {
                data: 'address',
                className: 'td-data',
                searchable: false,
            },
            { 
                data: 'orderItems',
                className: 'td-data',
                render: function(data, type, row){
                    let list=""
                    data.forEach(it => {
                         list+= it.item.product.barcode + " x " + it.qty +"; ";
                    });
                    return list;
                }
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
                data: 'revenue',
                className: 'td-data'
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
                                </div>`
                    }else if(data==2){
                        return  `<div class="btn-group">
                                    <button class="btn btn-info dropdown-toggle" style="width: 150px;" data-bs-toggle="dropdown" aria-expanded="false"> Chờ chuyển hàng <i class="fa fa-angle-down"></i></button>
                                </div>`
                    }else if(data==3){
                        return  `<div class="btn-group dropdown">
                                    <button class="btn btn-success" style="width: 150px;" data-bs-toggle="dropdown" aria-expanded="false"> Đang gửi hàng <i class="fa fa-angle-down"></i></button>
                                    <ul class="dropdown-menu" x-placement="bottom-start" style="position: absolute; transform: translate(-40px, 36px); top: 0px; left: 0px; will-change: transform;">
                                        <li>
                                            <div class="dropdown-item" onclick="changeStatus(${row.id},${4})">Đơn hàng delay</div>
                                        </li>
                                        <li>
                                            <div class="dropdown-item" onclick="changeStatus(${row.id},${5})">Đã gửi hàng</div>
                                        </li>
                                    </ul>
                                </div>`
                    }else if(data==4){
                        return  `<div class="btn-group dropdown">
                                    <button class="btn btn-warning" style="width: 150px;" data-bs-toggle="dropdown" aria-expanded="false"> Đơn hàng delay <i class="fa fa-angle-down"></i></button>
                                    <ul class="dropdown-menu" x-placement="bottom-start" style="position: absolute; transform: translate(-40px, 36px); top: 0px; left: 0px; will-change: transform;">
                                        <li>
                                            <div class="dropdown-item" onclick="changeStatus(${row.id},${6})">Đang hoàn</div>
                                        </li>
                                        <li>
                                            <div class="dropdown-item" onclick="changeStatus(${row.id},${5})">Đã gửi hàng</div>
                                        </li>
                                    </ul>
                                </div>`
                    }else if(data==5){
                        return  `<div class="btn-group">
                                    <button class="btn btn-success" style="width: 150px;"> Đã gửi hàng </button>
                                </div>`
                    }else if(data==6){
                        return  `<div class="btn-group dropdown">
                                    <button class="btn btn-danger" style="width: 150px;" data-bs-toggle="dropdown" aria-expanded="false"> Đang hoàn <i class="fa fa-angle-down"></i></button>
                                    <ul class="dropdown-menu" x-placement="bottom-start" style="position: absolute; transform: translate(-40px, 36px); top: 0px; left: 0px; will-change: transform;">
                                        <li>
                                            <div class="dropdown-item" onclick="changeStatus(${row.id},${7})">Đã hoàn</div>
                                        </li>
                                    </ul>
                                </div>`
                    }else if(data==7){
                        return  `<div class="btn-group">
                                    <button class="btn btn-danger" style="width: 150px;"> Đã hoàn </button>
                                </div>`
                    }
                }
            },
        ],
        columnDefs: [
            //{ orderable: false,className: 'select-checkbox', targets: 0 },
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
                    var printElem = "";
                    if(data.length>0){
                        data.each(obj =>{
                            var tbBody = "";
                            if(obj.orderItems.length>0){
                                var i = 1;
                                obj.orderItems.forEach(it =>{
                                    let tbRow = 
                                    `
                                    <tr>
                                        <td>${i}</td>
                                        <td class="text-center">${it.item.product.productName}</td>
                                        <td class="text-center">${it.item.product.barcode}</td>
                                        <td class="text-center">${numberWithCommas(it.price)}</td>
                                        <td class="text-center">${it.qty}</td>
                                        <td class="text-center">${numberWithCommas(it.price*it.qty)}</td>
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
                                            <span class="fw-bold fs-3">PHIẾU GIAO HÀNG</span>
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
                                                <span class="fw-bold">Khách hàng:
                                                </span>
                                                <span>${obj.customer.name}</span>
                                            </div>
                                            <div class="col">
                                                <span class="fw-bold">Sđt:
                                                </span>
                                                <span>${obj.customer.phone}</span>
                                            </div>
                                        </div>
                                        <div class="row gx-5 pb-1">
                                            <div class="col">
                                                <span class="fw-bold">Người nhận hàng:
                                                </span>
                                                <span>${obj.receiverName}</span>
                                            </div>
                                            <div class="col">
                                                <span class="fw-bold">Sđt:
                                                </span>
                                                <span>${obj.receiverPhone}</span>
                                            </div>
                                        </div>
                                        <div class="row pb-4">
                                            <div class="col">
                                                <span class="fw-bold">Địa chỉ: 
                                                </span>
                                                <span>${obj.address}</span>
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
                                                            <strong>Giá xuất</strong>
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
                                                        <td colspan="5" class="text-start fw-bold">Phí vận chuyển</td>
                                                        <td class="text-center">${numberWithCommas(obj.shippingFee)}</td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="5" class="text-start fw-bold">Chiết khấu</td>
                                                        <td class="text-center">${numberWithCommas(obj.totalDiscount)}</td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="5" class="text-start fw-bold">Tổng cộng</td>
                                                        <td class="text-center">${numberWithCommas(obj.totalMoney)}</td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="5" class="text-start fw-bold">Tiền đã gửi</td>
                                                        <td class="text-center">${numberWithCommas(obj.receivedMoney)}</td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="5" class="text-start fw-bold">COD</td>
                                                        <td class="text-center">${numberWithCommas(obj.owe)}</td>
                                                    </tr>
                                                </tfooter>
                                            </table>
                                        </div>
                                        <div class="row mb-4">
                                            <span>Ghi chú: </span>
                                            <span><hr style="border: none; border-top: 1px dotted #000; color: #fff; background-color: #fff;height: 1px; width: 100%;"></span>
                                            <span><hr style="border: none; border-top: 1px dotted #000; color: #fff; background-color: #fff;height: 1px; width: 100%;"></span>
                                        </div>
                                       
                                        <div class="row">
                                            <div class="col-sm-4 text-center">
                                                <span></span><br>
                                                <span class="fw-bold">Người nhận hàng</span>
                                                <br>
                                                <span class="fst-italic">(Ký họ tên)</span>
                                            </div>
    
                                            <div class="col-sm-4 text-center">
                                                <span></span><br>
                                                <span class="fw-bold">Người giao hàng</span>
                                                <br>
                                                <span class="fst-italic">(Ký họ tên)</span>
                                            </div>
    
                                            <div class="col-sm-4 text-center">
                                                <span>${moment().locale('vi').format('LL')}</span>
                                                <br>
                                                <span class="fw-bold">Người lập phiếu</span>
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
            search: "_INPUT_",            
            searchPlaceholder: "Tìm kiếm",
            lengthMenu: "_MENU_/trang",
            zeroRecords: "Không có kết quả nào!",
            info: "Trang _PAGE_/_PAGES_",
            infoEmpty: "Không có kết quả nào",
            infoFiltered: "(lọc từ _MAX_ kết quả)",
            select: {
                rows: {
                    _: 'Đã chọn %d',
                    0: ''
                }
            }

        },
        dom: 'B<"tabletop"if>tr<"pagetable"lp><"clear">',
        select: {
            style:    'multi',
            selector: 'td:first-child'
        },
        order: [[ 1, 'desc' ]]
    });
    table.buttons().container().appendTo('#dt-buttons');

    table.on("click", "th.select-checkbox", function() {
        if ($("th.select-checkbox").hasClass("selected")) {
            table.rows().deselect();
            $("th.select-checkbox").removeClass("selected");
        } else {
            table.rows().select();
            $("th.select-checkbox").addClass("selected");
        }
    }).on("select deselect", function() {
        if (table.rows({
                selected: true
            }).count() !== table.rows().count()) {
            $("th.select-checkbox").removeClass("selected");
        } else {
            $("th.select-checkbox").addClass("selected");
        }
    });

    table.on('click', 'tbody tr .td-data', async function (e) {
        e.preventDefault();
        var modalElem="";
        $("#order-info").empty();
        let data = table.row(this).data();
        let url = "/api/order/"+data["id"];

        await fetch(url, {
            method: "GET",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json",
            },
        })
        .then(response => {
            if(!response.ok) throw Error(response.statusText);
            return response.json();
        })
        .then(data =>{
            if(data.status=="ok") {
                var obj = data.data;
                var tbBody = "";
                if(obj.orderItems.length>0){
                    var i = 1;
                    obj.orderItems.forEach(it =>{
                        let tbRow = 
                        `
                        <tr>
                            <td>${i}</td>
                            <td class="text-center">${it.item.product.productName}</td>
                            <td class="text-center">${it.item.product.barcode}</td>
                            <td class="text-center">${numberWithCommas(it.price)}</td>
                            <td class="text-center">${it.qty}</td>
                            <td class="text-center">${numberWithCommas(it.price*it.qty)}</td>
                        </tr>
                        `
                        i++;
                        tbBody += tbRow;
                    })
                }

                modalElem = 
                `<div class="modal fade " id="oiModal" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                            <h1 class="modal-title fs-5" id="modalLabel">Thông tin đơn hàng</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <div class="container px-1 py-5">
                                    <div class="row">
                                        <div class="col">
                                            <div class="text-center mb-5">
                                                <span class="fw-bold fs-3">PHIẾU GIAO HÀNG</span>
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
                                                    <span class="fw-bold">Khách hàng:
                                                    </span>
                                                    <span>${obj.customer.name}</span>
                                                </div>
                                                <div class="col">
                                                    <span class="fw-bold">Sđt:
                                                    </span>
                                                    <span>${obj.customer.phone}</span>
                                                </div>
                                            </div>
                                            <div class="row gx-5 pb-1">
                                                <div class="col">
                                                    <span class="fw-bold">Người nhận hàng:
                                                    </span>
                                                    <span>${obj.receiverName}</span>
                                                </div>
                                                <div class="col">
                                                    <span class="fw-bold">Sđt:
                                                    </span>
                                                    <span>${obj.receiverPhone}</span>
                                                </div>
                                            </div>
                                            <div class="row pb-4">
                                                <div class="col">
                                                    <span class="fw-bold">Địa chỉ: 
                                                    </span>
                                                    <span>${obj.address}</span>
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
                                                                <strong>Giá xuất</strong>
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
                                                            <td colspan="5" class="text-start fw-bold">Phí vận chuyển</td>
                                                            <td class="text-center">${numberWithCommas(obj.shippingFee)}</td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="5" class="text-start fw-bold">Chiết khấu</td>
                                                            <td class="text-center">${numberWithCommas(obj.totalDiscount)}</td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="5" class="text-start fw-bold">Tổng cộng</td>
                                                            <td class="text-center">${numberWithCommas(obj.totalMoney)}</td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="5" class="text-start fw-bold">Tiền đã gửi</td>
                                                            <td class="text-center">${numberWithCommas(obj.receivedMoney)}</td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="5" class="text-start fw-bold">COD</td>
                                                            <td class="text-center">${numberWithCommas(obj.owe)}</td>
                                                        </tr>
                                                    </tfooter>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>`;

                $("#order-info").append(modalElem);
            }
        })
        .catch(error => console.error(error));

        $("#oiModal").modal("show");
    });
}

async function changeStatus(id, status){
    let url = "/api/delivery/order-status?orderid="+id+"&status="+status;
    await fetch(url, {
            method: "POST"
        })
        .then(response => {
            if (!response.ok) throw Error(response.statusText);
            return response.json();
        })
        .then(data => {
            table.ajax.reload(null, false) 
            $("#toast-content").html("Cập nhật thành công");
            toast.show()
        })
        .catch(error => console.log(error));
}

function numberWithCommas(x) {
    if(x==null) {return '0'}
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

$(document).ready(function(){
    loadDatatable("/api/order/status?status-list=3,4,5,6,7");
    $("#tab-1").on("click", function(event){
        event.preventDefault();
        table.clear().destroy();
        loadDatatable("/api/order/status?status-list=3,4,5,6,7");
    });
    $("#tab-2").on("click", function(event){
        event.preventDefault();
        table.clear().destroy();
        loadDatatable("/api/order/status?status-list=3");
    });
    $("#tab-3").on("click", function(event){
        event.preventDefault();
        table.clear().destroy();
        loadDatatable("/api/order/status?status-list=4");
    });
    $("#tab-4").on("click", function(event){
        event.preventDefault();
        table.clear().destroy();
        loadDatatable("/api/order/status?status-list=5");
    });
    $("#tab-5").on("click", function(event){
        event.preventDefault();
        table.clear().destroy();
        loadDatatable("/api/order/status?status-list=6");
    });
    $("#tab-6").on("click", function(event){
        event.preventDefault();
        table.clear().destroy();
        loadDatatable("/api/order/status?status-list=7");
    });
})