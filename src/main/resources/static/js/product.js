$(document).ready(function () {
    const toast = new bootstrap.Toast($("#toast"));
    const wareData = getAjaxResponse("/api/warehouse")
    const cateData = getAjaxResponse("/api/category")
    const supData = getAjaxResponse("/api/supplier")
    const table = $("#productTable").DataTable( {
        processing: true,
        responsive: true,
        ajax: {
            url: "/api/products",
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
                data: 'productName',
                className: 'td-data'
            },
            { 
                data: 'barcode', 
                className: 'td-data'
            },
            { 
                data: 'categories', 
                className: 'td-data',
                render: function(data, type, row){
					buttons='';
                    if(data){
                        data.forEach(c => {buttons += '<span class="badge badge-primary badge-pill m-r-5 m-b-5">' + c.cateName +' </span>'});
                    }
					return buttons;
				}
            },
            { 
                data: 'weight', 
                className: 'td-data'
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
        paging: true, 
        pagingType: 'numbers',
        lengthMenu: [ [20, 30, 50, -1], [20, 30, 50, "All"] ],
        language: {
            "search": "_INPUT_",            
            "searchPlaceholder": "Search",
            "lengthMenu": "_MENU_/trang",
            "zeroRecords": "Không có sản phẩm nào!",
            "info": "Trang _PAGE_/_PAGES_",
            "infoEmpty": "Không có sản phẩm",
            "infoFiltered": "(filtered from _MAX_ total records)"
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

    $('#productTable').on('click', 'tbody tr .td-data', function (e) {
        e.preventDefault();
        data = table.row(this).data();
        href = "api/products/"+data["id"];
        $.get(href, function(response, status){
			$("#pme-form").attr("action", "/api/products/"+data.id);
			$("#created-val").html(moment(data.createdAt).format('HH:mm DD-MM-YYYY'));
            $("#e-pname").val(data.productName);
            $("#e-barcode").val(data.barcode);
            //$("#ip-val").val(product.importPrice);
            //$("#sp-val").val(product.sellPrice);
            $("#e-weight").val(data.weight);
        })
        $("#product-modal-edit").modal("show");
    });

    $("#productTable").on("click", 'tbody tr .issell', function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        url = "/api/products/"+data["id"]+"/"+!data["isSell"];
        $.ajax({
            url: url,
            method: "POST",
            success: function (data) {  
                table.ajax.reload(null, false);
        
            },  
            error: function (err) {  
                alert(err);  
            } 
        });

        
    });
	
    $("#c-categories").select2({
        data: $.map(cateData, function(s) {
            return {
                text: s.cateName,
                id: s.id
            }
        }),
        placeholder: "Chọn danh mục sản phẩm",
        allowClear: true,
        tags: true,
        tokenSeparators: [','],
        width: '100%',
        height: '34px',
        //dropdownAutoWidth: true,
        dropdownParent: ".cate-c-group"
    });

	$("#c-supplier").select2({
        data: $.map(supData, function(s) {
            return {
                text: s.supName,
                id: s.id
            }
        }),
        placeholder: "Chọn đơn vị cung cấp",
        allowClear: true,
        width: '100%',
        height: '34px',
        //dropdownAutoWidth: true,
        dropdownParent: ".sup-c-group"
    });

    $("#c-warehouse").select2({
        data: $.map(wareData, function(s) {
            return {
                text: s.name,
                id: s.id
            }
        }),
        placeholder: "Chọn kho hàng",
        allowClear: true,
        width: '100%',
        height: '34px',
        //dropdownAutoWidth: true,
        dropdownParent: ".ware-c-group"
    });

    $("#c-categories").on("select2:select", function(e){
        console.log($("#c-categories").select2("data"));
    })
    
    $("#c-warehouse").on("select2:select" , function(e){
        let insert =    '<tr id="r'+e.params.data.id+'">' +
                            '<td class="cell-align" style="font-weight: 700; border-right: 1px solid #ccc;">'+e.params.data.text+'</td>'+
                            '<td class="cell-align">'+
                                '<label class="switch"><input class="status" type="checkbox" checked>'+
                                    '<span class="slider round"></span>'+
                                '</label>'+
                            '</td>'+
                            '<td class="cell-align"></td>'+
                            '<td class="cell-align"><input class="form-control ip-70 pprice" type="number"></td>'+
                            '<td class="cell-align"><input class="form-control ip-70 sprice" type="number"></td>'+
                            '<td class="cell-align"><input class="form-control ip-70 qty"  type="number"></td>'+
                        '</tr>'
        
        $("#c-tb").find('tbody').append(insert);
    });
      
    $("#c-warehouse").on("select2:unselect", function(e){
        //let dt = $("#wh").select2("data")
        //let dt= $(this).val()
        id="r"+e.params.data.id
        deleteRow(id)
    });
	
    $("#btnCreate").on("click", function(e){
        e.preventDefault();
        $("#product-modal").modal("show");
    });

    $("#c-pform").on("submit", function (e) {
        e.preventDefault();
        //ccate = $("#c-categories").select2('data');
        ccate = $("#c-categories").select2("data").map(c=>{
            return{
                id: c.id,
                cateName: c.text
            }
        })
        console.log(ccate)
        let product = JSON.stringify({
          barcode: $("#c-barcode").val(),
          productName: $("#c-pname").val(),
          weight: $("#c-weight").val(),
          categories: ccate
        });
        $.ajax({
            url: "/api/products/new",
            method: "post",
            data: product,
            contentType: "application/json",
            success: function (response) { 
                table.ajax.reload(null, false) 
                $('#product-modal form :input').val("");
                $("#product-modal").modal("hide");
                $("#toast-content").html("Tạo mới thành công: # "+response.data['id']+' - '+ response.data['productName'])
                toast.show()
                //window.location.href = "/products"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });
	
      
    $("#productTable tbody").on("click", ".btn-delete", function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        $("#yesBtn").attr("p-name", data['productName']);
        $("#yesBtn").attr("p-id", data['id']);
        $("#confirmText").html("Bạn muốn xoá sản phẩm này: \<strong\>" + data["productName"] + "\<\/strong\>?");
        $("#confirmModal").modal("show");
    });
    $("#yesBtn").on("click", function (e) {
        e.preventDefault();
        pname = $(this).attr("p-name");
        id = $(this).attr("p-id");
        $.ajax({
            url: "api/products/" + id,
            method: "delete",
            success: function (data) {  
                table.ajax.reload(null, false) 
                $("#confirmModal").modal("hide");
                $("#toast-content").html("Xóa thành công: #"+id+" - "+pname)
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
      //window.location="/products"
    });
    
});

function deleteRow(rid){   
    var row = document.getElementById(rid);
    row.parentNode.removeChild(row);
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