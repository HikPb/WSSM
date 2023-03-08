const toast = new bootstrap.Toast($("#toast"));

$(document).ready(function () {
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
            "searchPlaceholder": "Tìm kiếm",
            "lengthMenu": "_MENU_/trang",
            "zeroRecords": "Không có sản phẩm nào!",
            "info": "Trang _PAGE_/_PAGES_",
            "infoEmpty": "Không có sản phẩm",
            "infoFiltered": "(lọc từ _MAX_ kết quả)"
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
        href = "/api/products/"+data["id"];
        $.get(href, function(response, status){
			$("#pme-form").attr("pid", data["id"]);
			$("#created-val").html(moment(data.createdAt).format('HH:mm DD-MM-YYYY'));
            $("#e-pname").val(data.productName);
            $("#e-barcode").val(data.barcode);
            $("#e-link").val(data.link);
            $("#e-note").val(data.note);
            selectedCate = data.categories.map(c=>{
                return c.id
            })
            $("#e-categories").val(selectedCate).trigger('change')
            selectedSup = data.suppliers.map(c=>{
                return c.id
            })
            $("#e-supplier").val(selectedSup).trigger('change')
            $("#e-weight").val(data.weight);
        })
        $.get("/api/item/product/"+data["id"], function(response){
            if(response.data.length>0){    
                listWh = response.data.map((w)=>{
                    return w?.warehouse?.id
                })
                //$("#e-warehouse").prop("disabled", true);
                $("#e-warehouse").val(listWh).trigger("change")
                var insert = "";
                var st = ""
                response.data.forEach(i=>{
                    if(i.active === true){
                        st = '<div class="form-check form-switch"><input class="form-check-input e-status" type="checkbox" onclick="setItemStatus('+ i?.id +')" role="switch" checked></div>'
                    }else {
                        st = '<div class="form-check form-switch"><input class="form-check-input e-status" type="checkbox" onclick="setItemStatus('+ i?.id +')" role="switch"></div>'
                    }
                    insert +=    '<tr class="tb-data" id="er'+ i.warehouse.id +'" wid="'+ i.warehouse.id +'" iid="'+ i?.id +'">' +
                                    '<td class="cell-align" style="font-weight: 700; border-right: 1px solid #ccc;">'+ i.warehouse.name +'</td>'+
                                    '<td class="cell-align">'+ st +
                                    '</td>'+
                                    //'<td class="cell-align"></td>'+
                                    '<td class="input-val cell-align"><input class="form-control ip-70 e-pprice" type="number" value="'+ i.purcharsePrice +'"></td>'+
                                    '<td class="input-val cell-align"><input class="form-control ip-70 e-sprice" type="number" value="'+ i.retailPrice +'"></td>'+
                                    '<td class="input-val cell-align"><input class="form-control ip-70 e-qty"  type="number" value="'+ i.qty +'"></td>'+
                                '</tr>'
                })
                $("#e-tb").find('tbody').empty()
                $("#e-tb").find('tbody').append(insert);
            }
        })
        $("#product-modal-edit").modal("show");
    });

    $("#pme-form").on("submit", function (e) {
        e.preventDefault();
        e_cate = $("#e-categories").select2("data").map(c=>{
            return{
                id: c.id,
                cateName: c.text
            }
        })
        e_sup = $("#e-supplier").select2("data").map(c=>{
            return{
                id: c.id,
                supName: c.text
            }
        })
        let items = [];
        $("#e-tb tr.tb-data").map(function (index, elem) {
            obj = {
                id: $(this).attr("iid"),
                active : $(this).find('.e-status').is(":checked"),
                wareId : $(this).attr("wid"),
                sku: "",
                pprice : $(this).find('.e-pprice').val(),
                sprice : $(this).find('.e-sprice').val(),
                qty : $(this).find('.e-qty').val()
            }
            items.push(obj)
        });
        let product = JSON.stringify({
          barcode: $("#e-barcode").val(),
          productName: $("#e-pname").val(),
          weight: $("#e-weight").val(),
          link: $("#e-link").val(),
          note: $("#e-note").val(),
          categories: e_cate,
          suppliers: e_sup,
          items: items
        });
        $.ajax({
            url: "/api/products/" + $("#pme-form").attr("pid"),
            method: "put",
            data: product,
            contentType: "application/json",
            success: function (response) { 
                table.ajax.reload(null, false) 
                $("#product-modal-edit").modal("hide");
                $("#toast-content").html("Chỉnh sửa thành công: # "+response.data['id']+' - '+ response.data['productName'])
                toast.show()
                //window.location.href = "/products"
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
        placeholder: {id: '',text: "Chọn đơn vị cung cấp"},
        allowClear: true,
        data: $.map(supData, function(s) {
            return {
                text: s.supName,
                id: s.id
            }
        }),
        tags: true,
        width: '100%',
        height: '34px',
        dropdownParent: ".sup-c-group"
    }).val('').trigger('change');

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
        dropdownParent: ".ware-c-group"
    });
    
    $("#c-warehouse").on("select2:select" , function(e){
        let insert =    '<tr class="tb-data" id="r'+ e.params.data.id +'" wid="'+ e.params.data.id +'">' +
                            '<td class="cell-align" style="font-weight: 700; border-right: 1px solid #ccc;">'+ e.params.data.text +'</td>'+
                            '<td class="cell-align">'+
                                '<div class="form-check form-switch"><input class="form-check-input status" type="checkbox" role="switch" checked></div>'+
                            '</td>'+
                            //'<td class="cell-align"></td>'+
                            '<td class="input-val cell-align"><input class="form-control ip-70 pprice" type="number"></td>'+
                            '<td class="input-val cell-align"><input class="form-control ip-70 sprice" type="number"></td>'+
                            '<td class="input-val cell-align"><input class="form-control ip-70 qty"  type="number"></td>'+
                        '</tr>'
        
        $("#c-tb").find('tbody').append(insert);
    });
      
    $("#c-warehouse").on("select2:unselect", function(e){
        //let dt = $("#wh").select2("data")
        //let dt= $(this).val()
        id="r"+e.params.data.id
        deleteRow(id)
    });
	
    $("#e-categories").select2({
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
        dropdownParent: ".cate-e-group"
    });

	$("#e-supplier").select2({
        placeholder: {id: '',text: "Chọn đơn vị cung cấp"},
        allowClear: true,
        data: $.map(supData, function(s) {
            return {
                text: s.supName,
                id: s.id
            }
        }),
        tags: true,
        width: '100%',
        height: '34px',
        dropdownParent: ".sup-e-group"
    }).val('').trigger('change');

    $("#e-warehouse").select2({
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
        dropdownParent: ".ware-e-group"
    });

    $("#e-warehouse").on("select2:select" , function(e){
        let insert = "";
        let pid = $("#pme-form").attr("pid");
        let wpItem = getAjaxResponse("/api/item/product/"+pid+"/warehouse/"+e.params.data.id);

        console.log(wpItem)
        if(wpItem!=null){
            if(wpItem?.active === true){
                st = '<div class="form-check form-switch"><input class="form-check-input e-status" type="checkbox" onclick="setItemStatus('+ wpItem?.id +')" role="switch" checked></div>'
            }else {
                st = '<div class="form-check form-switch"><input class="form-check-input e-status" type="checkbox" onclick="setItemStatus('+ wpItem?.id +')" role="switch"></div>'
            }
            insert =    '<tr class="tb-data" id="er'+ e.params.data.id +'" wid="'+ e.params.data.id +'" iid="'+ wpItem?.id +'">' +
                            '<td class="cell-align" style="font-weight: 700; border-right: 1px solid #ccc;">'+ e.params.data.text +'</td>'+
                            '<td class="cell-align">'+ st +
                            '</td>'+
                            //'<td class="cell-align"></td>'+
                            '<td class="input-val cell-align"><input class="form-control ip-70 e-pprice" type="number" value="'+ wpItem?.purcharsePrice +'"></td>'+
                            '<td class="input-val cell-align"><input class="form-control ip-70 e-sprice" type="number" value="'+ wpItem?.retailPrice +'"></td>'+
                            '<td class="input-val cell-align"><input class="form-control ip-70 e-qty"  type="number" value="'+ wpItem?.qty +'"></td>'+
                        '</tr>'
        }else{
            insert =    '<tr class="tb-data" id="er'+ e.params.data.id +'" wid="'+ e.params.data.id +'" iid="">' +
                            '<td class="cell-align" style="font-weight: 700; border-right: 1px solid #ccc;">'+ e.params.data.text +'</td>'+
                            '<td class="cell-align">'+
                                '<div class="form-check form-switch"><input class="form-check-input e-status" type="checkbox" role="switch" checked></div>'+
                            '</td>'+
                            //'<td class="cell-align"></td>'+
                            '<td class="input-val cell-align"><input class="form-control ip-70 e-pprice" type="number"></td>'+
                            '<td class="input-val cell-align"><input class="form-control ip-70 e-sprice" type="number"></td>'+
                            '<td class="input-val cell-align"><input class="form-control ip-70 e-qty"  type="number"></td>'+
                        '</tr>'
        }
        $("#e-tb").find('tbody').append(insert);
    });

    $("#e-warehouse").on("select2:unselect", function(e){
        id="er"+e.params.data.id
        deleteRow(id)
    });

    $("#btnCreate").on("click", function(e){
        e.preventDefault();
        $("#product-modal").modal("show");
    });

    $("#c-pform").on("submit", function (e) {
        e.preventDefault();
        c_cate = $("#c-categories").select2("data").map(c=>{
            return{
                id: c.id,
                cateName: c.text
            }
        })
        c_sup = $("#c-supplier").select2("data").map(c=>{
            return{
                id: c.id,
                supName: c.text
            }
        })
        let items = [];
        $("#c-tb tr.tb-data").map(function (index, elem) {
            obj = {
                active : $(this).find('.status').is(":checked"),
                wareId : $(this).attr("wid"),
                sku: "",
                pprice : $(this).find('.pprice').val(),
                sprice : $(this).find('.sprice').val(),
                qty : $(this).find('.qty').val()
            }
            items.push(obj)
        });
        let product = JSON.stringify({
          barcode: $("#c-barcode").val(),
          productName: $("#c-pname").val(),
          weight: $("#c-weight").val(),
          link: $("#c-link").val(),
          note: $("#c-note").val(),
          categories: c_cate,
          suppliers: c_sup,
          items: items
        });
        $.ajax({
            url: "/api/products/new",
            method: "post",
            data: product,
            contentType: "application/json",
            success: function (response) { 
                table.ajax.reload(null, false) 
                $('#product-modal form').trigger("reset")
                // $("#c-categories").select2().val("");
                // $("#c-supplier").select2().val([]).trigger("change");
                // $("#c-warehouse").select2().val("");
                $("#product-modal").modal("hide");
                $("#product-modal").find('form').trigger('reset');
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
            if(response.data!== null || response.data!==""){
                return response.data;
            }else{
                return []
            }

            
        } 
    }).responseJSON;
    return result.data;
}

function setItemStatus(d){
    url = "/api/item/"+d+"/status";
    $.ajax({
        url: url,
        method: "POST",
        success: function (response) {  
            $("#toast-content").html("Cập nhật trạng thái: #"+response.data['id']+' - '+ response.data.product['productName'])
            toast.show()
        },  
        error: function (err) {  
            alert(err);  
        } 
    });
}