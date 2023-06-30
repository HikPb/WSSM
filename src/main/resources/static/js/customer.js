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
                orderable: false,
                defaultContent: '<button class="btn btn-default btn-xs btn-delete" data-toggle="tooltip" data-original-title="Delete"><i class="fa-solid fa-trash"></i></button>'
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
            console.log(table.row().count())
          },
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
        dom: '<"tabletop"if>tr<"pagetable"lp><"clear">',
        search: {
            "addClass": 'form-control input-lg col-xs-12'
        },
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
                columns: [2,3,4,5,6,7]
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
			//$("#e-sform").attr("action", "/api/products/"+data.id);
			$("#updated-val").html(moment(data.updatedAt).format('HH:mm DD-MM-YYYY'));
            $("#e-name").val(data.name);
            $("#e-phone").val(data.phone);
            $("#e-dob").val(moment(data.dob).format('YYYY-MM-DD'));
            $("#e-address").val(data.address);
            $("#e-np").text(data.npCus);
            $("#e-nso").text(data.nsoCus);
            $("#e-oc").text(data.npCus-data.nsoCus);
            $("#e-tm").text(data.tmoney);
        })
        $("#co-table").DataTable().clear().destroy();
        $("#co-table").DataTable({
            processing: true,
            responsive: true,
            paging: false,
            scrollCollapse: true,
            //scrollX: true,
            //scrollY: "200px",
            ajax: {
                url: "/api/order/customer/"+data["id"],
                dataSrc: '',
                type: "GET",
                dataType: "json",
                contentType: "application/json",
                dataSrc: 'data'
            },
            columns: [
                {defaultContent: '',data: null,},
                { data: "id"},
                { data: "owe"},
                { data: "revenue" },
                { data: "createdAt", render: function(data, type, row){ return moment(data).format("DD/MM/YYYY");}},
                { data: "status"}
            ],
            dom: "t",
            //order: [[ 1, 'desc' ]],
        })
        $("#e-cus-modal").modal("show");
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

    $("#btnClear").on("click", function (e) {
      e.preventDefault();
      table.ajax.reload(null, false)
      window.location="/customer"
    });
    
  });