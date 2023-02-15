$(document).ready(function () {
    var toast = new bootstrap.Toast($("#toast"));
    var table = $("#supplierTable").DataTable( {
        processing: true,
        responsive: true,
        ajax: {
            url: "/api/supplier",
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
                data: 'supName',
                className: 'td-data'
            },
            { 
                data: 'supPhone', 
                className: 'td-data'
            },
            { 
                data: 'address', 
                className: 'td-data',
            },
            { 
                data: 'createdAt',
                className: 'td-data',
                render: function(data, type, row){
                	return moment(data).format('HH:mm DD-MM-YYYY')
            	}
            },
            { 
                data: 'updatedAt',
                className: 'td-data',
                render: function(data, type, row){
                	return moment(data).format('HH:mm DD-MM-YYYY')
            	}
            },
            {
                defaultContent: '<button class="btn btn-default btn-xs btn-delete" data-toggle="tooltip" data-original-title="Delete"><i class="fa-solid fa-trash"></i></button>'
            },
        ],
        columnDefs: [
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
        dom: '<"top"if>rt<"bottom"pl><"clear">',
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

    $('#supplierTable').on('click', 'tbody tr .td-data', function (e) {
        e.preventDefault();
        data = table.row(this).data();
        href = "api/supplier/"+data["id"];
        $.get(href, function(response, status){
			$("#e-sform").attr("pid", data.id);
			$("#updated-val").html(moment(data.updatedAt).format('HH:mm DD-MM-YYYY'));
            $("#e-sname").val(data.supName);
            $("#e-sphone").val(data.supPhone);
            $("#e-address").val(data.address);
        })
        $("#e-sup-modal").modal("show");
    });

    $("#e-sform").on("submit", function (e) {
        e.preventDefault();
        let supplier = JSON.stringify({
          supName: $("#e-sname").val(),
          supPhone: $("#e-sphone").val(),
          address: $("#e-address").val(),
        });
        $.ajax({
            url: "/api/supplier/"+data["id"],
            method: "put",
            data: supplier,
            contentType: "application/json",
            success: function (response) {  
                table.ajax.reload(null, false) 
                $('#e-sup-modal form :input').val("");
                $("#e-sup-modal").modal("hide");
                $("#toast-content").html("Chỉnh sửa thành công: #"+response.data['id']+' - '+ response.data['supName'])
                toast.show()
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });

    

    $("#btnCreate").on("click", function(e){
        e.preventDefault();
        $("#c-sup-modal").modal("show");
    });

    $("#c-sform").on("submit", function (e) {
        e.preventDefault();
        let supplier = JSON.stringify({
          supName: $("#c-sname").val(),
          supPhone: $("#c-sphone").val(),
          address: $("#c-address").val(),
        });
        $.ajax({
            url: "/api/supplier",
            method: "post",
            data: supplier,
            contentType: "application/json",
            success: function (response) {  
                table.ajax.reload(null, false) 
                $('#c-sup-modal form :input').val("");
                $("#c-sup-modal").modal("hide");
                $("#toast-content").html("Tạo mới thành công: #"+response.data['id']+' - '+ response.data['supName'])
                toast.show()
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });
	
    $("#supplierTable tbody").on("click", ".btn-delete", function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        href = "/api/supplier/"+data["id"]+"";
        $("#yesBtn").attr("href", href);
        $("#yesBtn").attr("p-id", data["id"]);
        $("#confirmText").html("Bạn muốn xoá NCC này: \<strong\>" + data["supName"] + "\<\/strong\>?");
        $("#confirmModal").modal("show");
    });

    $("#yesBtn").on("click", function (e) {
        e.preventDefault();
        url = $(this).attr("href");
        id = $(this).attr("p-id");
        $.ajax({
            url: url,
            method: "delete",
            success: function (data) {  
                table.ajax.reload(null, false) 
                $("#confirmModal").modal("hide");
                $("#toast-content").html("Xoá thành công: #"+id)
                toast.show()
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });

    $("#btnClear").on("click", function (e) {
      e.preventDefault();
      table.ajax.reload(null, false)
      window.location="/supplier"
    });

    $("#multi_delete").on("click", function (e) {
        e.preventDefault();
        console.log("multi delete")
        let tb = $("#supplierTable").DataTable();
        let selectedRows = tb.row({page:'current'}).count()
        //selectedRows.push(table.row({selected: true}).data())
        console.log(selectedRows)
      });
    
  });
