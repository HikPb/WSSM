$(document).ready(function () {
    var toast = new bootstrap.Toast($("#toast"));
    var table = $("#customerTable").DataTable( {
        processing: true,
        responsive: true,
        ajax: {
            url: "/api/customer",
            dataSrc: '',
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            dataSrc: 'data'
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
                data: 'name',
                className: 'td-data'
            },
            { 
                data: 'phone', 
                className: 'td-data'
            },
            { 
                data: 'dob',
                className: 'td-data',
                render: function(data, type, row){
                    if(!data){
                        return '<span style="color: red;">Chưa có</span>';
                    }
                	return moment(data).format('DD-MM-YYYY')
            	}
            },
            { 
                data: 'address', 
                className: 'td-data',
            },
            {
                data: null,
                orderable: false,
                searchable: false,
                render: function(data, type, row){
                    if(user.roles.includes("ROLE_SALES_ADMIN")){
                        return `<div>
                                    <button class="btn btn-default btn-xs btn-delete" data-toggle="tooltip" data-original-title="Delete"><i class="fa-solid fa-trash"></i></button>
                                </div>`
                    }
                    return "";
                }
            },
        ],
        columnDefs: [
            { className: "dt-head-center", targets: [ 0, 1, 2, 3, 4, 5, 6 ] },
            { className: "dt-body-center", targets: [ 0, 1, 2, 3, 4, 6 ] },
            {
                'targets': 0,
                'checkboxes': {
                   'selectRow': true
                }
             }
        ],
        initComplete: function(settings, json) {
            table.row().invalidate().draw();
            //console.log(table.row().count())
        },
        buttons: [
            {
                text: '<i class="fa-solid fa-rotate"></i><span> Tải lại</span>',
                className: 'btn-tools',
                action: function ( e, dt, node, config ) {
                    dt.ajax.reload(null, false);
                }
            },
            {
                extend:    'print',
                text:      '<i class="fa fa-print"></i> In',
                titleAttr: 'Print',
                className: 'btn-tools',
                exportOptions: {
                    columns: [2,3,4,5,6,7]
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
        ],
        paging: true, 
        pagingType: 'numbers',
        lengthMenu: [ [20, 30, 50, -1], [20, 30, 50, "All"] ],
        language: {
            "search": "_INPUT_",            
            "searchPlaceholder": "Tìm kiếm",
            "lengthMenu": "_MENU_/trang",
            "zeroRecords": "Không có kết quả nào!",
            "select": "Đã chọn %d",
            "info": "Trang _PAGE_/_PAGES_",
            "infoEmpty": "Không có kết quả",
            "infoFiltered": "(Lọc từ _MAX_ kết quả)"
        },
        dom: 'B<"tabletop"if>tr<"pagetable"lp><"clear">',
        search: {
            "addClass": 'form-control input-lg col-xs-12'
        },
        select: {
            style:    'multi',
            selector: 'td:first-child'
        },
        order: [[ 1, 'desc' ]]
    });

    table.buttons().container().appendTo('#action-tools');

    table.on("click", "th.select-checkbox", function() {
        if ($("th.select-checkbox").hasClass("selected")) {
            table.rows().deselect()
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

    $('#customerTable').on('click', 'tbody tr .td-data', function (e) {
        e.preventDefault();
        data = table.row(this).data();
        href = "api/customer/"+data["id"];
        $.get(href, function(response, status){
            let dt = response.data;
			//$("#e-sform").attr("action", "/api/products/"+data.id);
			$("#updated-val").html(moment(dt.updatedAt).format('HH:mm DD-MM-YYYY'));
            $("#e-name").val(dt.name);
            $("#e-phone").val(dt.phone);
            $("#e-dob").val(moment(dt.dob).format('YYYY-MM-DD'));
            $("#e-address").val(dt.address);
            $("#e-np").text(dt.npCus);
            $("#e-nso").text(dt.nsoCus);
            $("#e-oc").text(dt.nroCus);
            $("#e-tm").text(numberWithCommas(dt.tmoney));
            $("#e-to").text(numberWithCommas(dt.towe));
        })
        $("#co-table").DataTable().clear().destroy();
        let tb = $("#co-table").DataTable({
            processing: true,
            responsive: true,
            paging: false,
            scrollCollapse: true,
            scrollX: true,
            scrollY: "200px",
            ajax: {
                url: "/api/order/customer/"+data["id"],
                dataSrc: '',
                type: "GET",
                dataType: "json",
                contentType: "application/json",
                dataSrc: 'data'
            },
            columns: [
                {
                    defaultContent: '',
                    data: null,
                    className: 'td-data',
                },
                { 
                    data: "id",
                    className: 'td-data',
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
                    data: "revenue", 
                    className: 'td-data',
                    render: function(data, type, row){ return numberWithCommas(data); } 
                },
                { 
                    data: "createdAt",
                    className: 'td-data',
                    render: function(data, type, row){ return moment(data).format("DD/MM/YYYY");}
                },
                { 
                    data: "status",
                    orderable: false,
                    searchable: false,
                    render: function(data, type, row){
                        if(data==0){
                            return  `<div class="btn-group">
                                        <button class="btn btn-danger" style="width: 150px;" type="button" disabled> Đã hủy </button>
                                    </div>`
                        }else if(data==1){
                            return  `<div class="btn-group">
                                        <button class="btn btn-primary" style="width: 150px;" type="button" disabled> Mới </button>
                                    </div>`
                        }else if(data==2){
                            return  `<div class="btn-group">
                                        <button class="btn btn-info" style="width: 150px;" type="button" disabled> Chờ chuyển hàng </button>
                                    </div>`
                        }else if(data==3){
                            return  `<div class="btn-group">
                                        <button class="btn btn-success" style="width: 150px;" type="button" disabled> Đang gửi hàng </button>
                                    </div>`
                        }else if(data==4){
                            return  `<div class="btn-group">
                                        <button class="btn btn-warning" style="width: 150px;" type="button" disabled> Đơn hàng delay </button>
                                    </div>`
                        }else if(data==5){
                            return  `<div class="btn-group">
                                        <button class="btn btn-success" style="width: 150px;" type="button" disabled> Đã gửi hàng </button>
                                    </div>`
                        }else if(data==6){
                            return  `<div class="btn-group">
                                        <button class="btn btn-danger" style="width: 150px;" type="button" disabled> Đang hoàn </button>
                                    </div>`
                        }else if(data==7){
                            return  `<div class="btn-group">
                                        <button class="btn btn-danger" style="width: 150px;" type="button" disabled> Đã hoàn </button>
                                    </div>`
                        }
                    }
            
                }
            ],
            dom: "t",
            order: [[ 1, 'desc' ]],
        });
        $("#e-cus-modal").modal("show");
    });

    $('#e-cus-modal').on('shown.bs.modal', function(e){
        $($.fn.dataTable.tables(true)).DataTable()
           .columns.adjust();
    });

    $("#e-form").on("submit", function (e) {
        e.preventDefault();
        let customer = JSON.stringify({
          name: $("#e-name").val(),
          phone: $("#e-phone").val(),
          dob: $("#e-dob").val(),
          address: $("#e-address").val(),
        });
        $.ajax({
            url: "/api/customer/"+data["id"],
            method: "put",
            data: customer,
            contentType: "application/json",
            success: function (response) {  
                table.ajax.reload(null, false) 
                $('#e-cus-modal form :input').val("");
                $("#e-cus-modal").modal("hide");
                $("#toast-content").html("Chỉnh sửa thành công: #"+response.data['id']+' - '+ response.data['name'])
                toast.show()
                //window.location.href = "/customer"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });

    

    $("#btnCreate").on("click", function(e){
        e.preventDefault();
        $("#c-cus-modal").modal("show");
    });
    $("#c-form").on("submit", function (e) {
        e.preventDefault();
        let customer = JSON.stringify({
          name: $("#c-name").val(),
          phone: $("#c-phone").val(),
          dob: $("#c-dob").val(),
          address: $("#c-address").val(),
        });
        $.ajax({
            url: "/api/customer",
            method: "post",
            data: customer,
            contentType: "application/json",
            success: function (response) {  
                table.ajax.reload(null, false) 
                $('#c-cus-modal form :input').val("");
                $("#c-cus-modal").modal("hide");
                $("#toast-content").html("Tạo mới thành công: #"+response.data['id']+' - '+ response.data['name'])
                toast.show()
                //window.location.href = "/customer"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });
      
    $("#customerTable tbody").on("click", ".btn-delete", function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        href = "/api/customer/"+data["id"]+"";
        $("#yesBtn").attr("href", href);
        $("#yesBtn").attr("p-id", data["id"]);

        $("#confirmText").html("Bạn muốn xoá khách hàng này: \<strong\>" + data["name"] + "\<\/strong\>?");
        $("#confirmModal").modal("show");
    });

    $("#yesBtn").on("click", function (e) {
        e.preventDefault();
        url = $(this).attr("href");
        id = $(this).attr("p-id");
        $.ajax({
            url: url,
            method: "delete",
            success: function (response) {  
                table.ajax.reload(null, false) 
                $("#confirmModal").modal("hide");
                $("#toast-content").html("Xóa thành công: #"+id)
                toast.show()
                //window.location.href = "/customer"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });
    
  });

  function numberWithCommas(x) {
    if(x==null || x=="") {return 0;}
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}