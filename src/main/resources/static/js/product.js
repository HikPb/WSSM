const toast = new bootstrap.Toast($("#toast"));

$(document).ready(function () {
    const wareData = getAjaxResponse("/api/warehouse")
    //const cateData = getAjaxResponse("/api/category")
    const supData = getAjaxResponse("/api/supplier")
    const table = $("#productTable").DataTable( {
        processing: true,
        responsive: true,
        //serverSide: true,
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
                data: null,
                orderable: false,
                searchable: false,
                render: function(data, type, row){
                    if(user.roles.includes("ROLE_WAREHOUSE_ADMIN")){
                        return `<div>
                                    <button class="btn btn-default btn-xs btn-delete" data-toggle="tooltip" data-original-title="Delete"><i class="fa-solid fa-trash"></i></button>
                                </div>`
                    }
                    return "";
                }
            },
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
            "zeroRecords": "Không có sản phẩm nào!",
            "info": "Trang _PAGE_/_PAGES_",
            "infoEmpty": "Không có sản phẩm",
            "infoFiltered": "(lọc từ _MAX_ kết quả)"
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
        $("#editModalDiv").empty();
        $("#editModalDiv").append(editModalElem);

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
            $("#"+id).remove()
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
                },  
                error: function (err) {  
                    alert(err);  
                } 
            });
            
        });

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
	
    $("#btnCreate").on("click", function(e){
        e.preventDefault();
        $("#createModalDiv").empty();
        $("#createModalDiv").append(createModalElem);


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
            id="r"+e.params.data.id;
            $("#"+id).remove()
            //$("#c-tb").deleteRow(id);
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
                error: function (error) {  
    
                    alert(error.message);  
                } 
            });
            
        });
        
        $("#product-modal").modal("show");
    });

    $("#categoryBtn").on("click", function(e){
        e.preventDefault();
        $("#categoryModal").modal("show");
    });

    $("#newCateBtn").on("click", function(e){
        e.preventDefault();
        $("#newCateBtn").prop("disabled", true);
        $("#newCateDiv").css("display", "block");
        $(".cate-item-icon").css("display", "none");
    });

    $("#cancelCateBtn").on("click", function(e){
        e.preventDefault();
        $("#newCateDiv").css("display", "none");
        $(".cate-item-icon").css("display", "block");
        $("#newCateIp").val('');
        $("#newCateBtn").prop("disabled", false);
    });

    $("form[name='newCateForm']").on("submit", function(e){
        e.preventDefault();
        fetch("/api/category",{
            method: "POST",
            credentials: "same-origin",
            headers:{
                "Content-Type": "application/json",
            },
            referrerPolicy: "no-referrer",
            body: $("#newCateIp").val(),
        }).then(response =>{
            if(!response.ok) throw Error(response.statusText);
            return response.json();
        }).then(data =>{
            $("#newCateDiv").css("display", "none");
            $(".cate-item-icon").css("display", "block");
            $("#newCateIp").val('');
            loadListCategory();
            $("#newCateBtn").prop("disabled", false);
            $("#toast-content").html("Tạo mới thành công danh mục.")
            toast.show()
        }).catch(error => {console.error(error)});
    })
      
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
});

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

function editInline(event, cateId){
    //var parent = event.target.parentElement.parentElement.parentElement;
    $("#newCateBtn").prop("disabled", true);
    $(".cate-item-icon").css("display", "none");
    var cate = cateData.find(c => c.id === cateId);
    var parent = event.target.closest(".cate-item");
    parent.innerHTML = `<form id="editCateForm" style="width: 100%;" cid="${cateId}">
                            <div class="col d-flex">
                                    <div class="mr-2" style="width: 100%;">
                                        <input class="form-control" type="text" id="editCateIp" placeholder="Danh mục" value="${cate.cateName}" required>
                                    </div>
                                    <div class="mx-2">
                                        <button class="btn btn-danger" type="button" onclick="cancelEditInline(event, ${cateId})">Hủy</button>
                                    </div>
                                    <div class="ml-2">
                                        <button class="btn btn-primary" form="editCateForm" type="submit"><i class="fa-solid fa-floppy-disk"></i></button>
                                    </div>
                            </div>
                        </form>`
    
    $("#editCateForm").on("submit", function(e){
        e.preventDefault();
        var data = {
            id : parseInt($(this).attr('cid')),
            cateName: $("#editCateIp").val()
        }
        fetch("/api/category",{
            method: "PUT",
            credentials: "same-origin",
            headers:{
                "Content-Type": "application/json",
            },
            referrerPolicy: "no-referrer",
            body: JSON.stringify(data),
        }).then(response =>{
            if(!response.ok) throw Error(response.statusText);
            return response.json();
        }).then(data =>{
            loadListCategory();
            $("#newCateBtn").prop("disabled", false);
            $("#toast-content").html("Chỉnh sửa thành công danh mục.")
            toast.show()
        }).catch(error => {console.error(error)});
    });
}

function cancelEditInline(event, cateId){
    $("#newCateBtn").prop("disabled", false);
    $(".cate-item-icon").css("display", "block");
    var cate = cateData.find(c => c.id === cateId);
    var parent = event.target.closest(".cate-item");
    parent.innerHTML = `<div class="d-flex flex-row">
                            <div class="cate-item-icon mx-2">
                                <span><i class="fa-solid fa-chevron-right"></i> </span>
                            </div>
                            <span>${cate.cateName}</span>
                        </div>
                        <div class="cate-item-icon">
                            <span class="mx-2" onclick="editInline(event, ${cateId})"><i class="fa-solid fa-pen-to-square"></i></i></span>
                            <span onclick="deleteCategory(event, ${cateId})"><i class="fa-solid fa-trash"></i></span>
                        </div>`
}

function deleteCategory(event, cateId){
    $("#newCateBtn").prop("disabled", true);
    $(".cate-item-icon").css("display", "none");
    var parent = event.target.closest(".cate-item");
    var div = event.target.closest(".cate-item-icon");
    div.remove();
    parent.innerHTML += `<div class="d-flex align-items-center confirm-delete">
                            <div class="mr-2" style="width: 100%;">
                                <span>Xác nhận xóa danh mục này?</span>
                            </div>
                            <div class="mx-2">
                                <button class="btn btn-outline-default" type="button" onclick="cancelDeleteCategory(event, ${cateId})">Hủy</button>
                            </div>
                            <div class="ml-2">
                                <button class="btn btn-danger" type="button" onclick="confirmDeleteCategory(${cateId})">Xóa</i></button>
                            </div>
                        </div>`
}

function cancelDeleteCategory(event, cateId){
    $("#newCateBtn").prop("disabled", false);
    $(".cate-item-icon").css("display", "block");
    var parent = event.target.closest(".cate-item");
    var div = event.target.closest(".confirm-delete");
    div.remove();
    parent.innerHTML += `<div class="cate-item-icon">
                            <span class="mx-2" onclick="editInline(event, ${cateId})"><i class="fa-solid fa-pen-to-square"></i></i></span>
                            <span onclick="deleteCategory(event, ${cateId})"><i class="fa-solid fa-trash"></i></span>
                        </div>`
}


function confirmDeleteCategory(cateId){
    fetch("/api/category/"+cateId,{
        method: "DELETE",
    }).then(response =>{
        if(!response.ok) throw Error(response.statusText);
        return response.json();
    }).then(data =>{
        loadListCategory();
        $("#newCateBtn").prop("disabled", false);
        $("#toast-content").html("Đã xóa")
        toast.show()
    }).catch(error => {console.error(error)});
}

function loadListCategory(){
    var li_items="";
    cateData = getAjaxResponse("/api/category");
    $("#cateNum").text(cateData.length);
    $("#ul-list").empty();
    cateData.forEach(cate =>{
        li_items += 
        `<li class="list-group-item cate-item" catename="${cate.cateName}">
            <div class="d-flex flex-row">
                <div class="cate-item-icon mx-2">
                    <span><i class="fa-solid fa-chevron-right"></i> </span>
                </div>
                <span>${cate.cateName}</span>
            </div>
            <div class="cate-item-icon">
                <span class="mx-2" onclick="editInline(event, ${cate.id})"><i class="fa-solid fa-pen-to-square"></i></i></span>
                <span onclick="deleteCategory(event, ${cate.id})"><i class="fa-solid fa-trash"></i></span>
            </div>
        </li>`
    });
    $("#ul-list").append(li_items);
}

function searchCategory() {
    var filter, ul, li, i, txtValue;
    filter = document.getElementById("cate-search").value.toUpperCase();
    ul = document.getElementById("ul-list");
    li = ul.getElementsByTagName("li");
    for (i = 0; i < li.length; i++) {
        txtValue = li[i].getAttribute("catename");
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
    }
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

const createModalElem =
`<div class="modal fade" role="dialog" id="product-modal">
<div class="modal-dialog modal-xl" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <div class="modal-title" id="pmTitle">
                <h3>Thông tin sản phẩm</h3>
            </div>
            <button type="submit" class="btn btn-primary" style="position: absolute; right: 40px;" form="c-pform"><span>Lưu</span></button>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        
        <form action="#" enctype="multipart/form-data" method="post" id="c-pform">
            <div class="modal-body">
                <div class="steps-container">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <span class="form-title">Mã sản phẩm <span
                                        style="color: rgb(245, 34, 45); margin-right: 4px;">*</span></span>
                                <input placeholder="Mã sản phẩm, barcode" class="form-control" type="text" 
                                        oninvalid="this.setCustomValidity('Hãy nhập đầy đủ thông tin bắt buộc')"
                                         oninput="setCustomValidity('')" id="c-barcode" autocomplete="off" required>
                            </div>

                            <div class="form-group">
                                <span class="form-title">Tên sản phẩm <span
                                        style="color: rgb(245, 34, 45); margin-right: 4px;">*</span></span>
                                <input placeholder="Tên sản phẩm" class="form-control" type="text" id="c-pname" 
                                        oninvalid="this.setCustomValidity('Hãy nhập đầy đủ thông tin bắt buộcbuộc')"
                                         oninput="setCustomValidity('')" autocomplete="off" required>
                            </div>

                            <div class="form-group row">
                                <span class="col-sm-4 form-title">Khối lượng (gram):</span>
                                <div class="col-sm-8">
                                    <input class="form-control" role="combobox" type="number" id="c-weight" 
                                        oninvalid="this.setCustomValidity('Hãy nhập đầy đủ thông tin bắt buộcbuộc')"
                                         oninput="setCustomValidity('')" autocomplete="off" required>
                                </div>
                            </div>

                            <div class="form-group ware-c-group">
                                <span class="form-title">Kho hàng:</span>
                                <select class="form-control" id="c-warehouse" role="combobox" multiple></select>
                            </div>

                            <div class="form-group">
                                <div>
                                    <div class="form-title">Link nhập hàng:</div>
                                    <div>
                                        <input placeholder="Enter để thêm link sản phẩm" class="form-control"
                                            type="text" autocomplete="off" id="c-link">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group cate-c-group">
                                <span class="form-title">Danh mục:</span>
                                <select class="form-control" id="c-categories" multiple></select>
                            </div>

                            <div class="form-group sup-c-group">
                                <span class="form-title">Nhà cung cấp:</span>
                                <select id="c-supplier" class="form-control" multiple></select>
                            </div>

                            <div class="form-group">
                                <span class="form-title">Ghi chú:</span>
                                <textarea rows="4" placeholder="Ghi chú SP" autocomplete="off" id="c-note"
                                    class="form-control"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="table-container">
                    <table class="table table-hover table-responsive-xl" id="c-tb">
                        <thead class="thead-light">
                            <tr>
                                <th style="border-right: 1px solid #ccc;">Kho hàng</th>
                                <th>Trạng thái</th>
                                <!-- <th>Hình ảnh</th> -->
                                <th>Giá nhập</th>
                                <th>Giá bán</th>
                                <th>Số lượng</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>

    </div>
</div>
</div>`

const editModalElem = 
`<div class="modal fade" role="dialog" id="product-modal-edit">
<div class="modal-dialog modal-xl" role="document">
    <div class="modal-content">
        <div class="modal-header">
            <div class="modal-title">
                <h3>Thông tin sản phẩm</h3>
            </div>
            <button type="submit" class="btn btn-primary" style="position: absolute; right: 40px;" form="pme-form"><span>Lưu</span></button>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <form id="pme-form" enctype="multipart/form-data" method="put">
            <div style="display: flex; flex-direction: row-reverse;background-color: #fff; padding: 12px; align-items: center;">
                <div style="position: absolute; left: 15px">
                     <span>Cập nhật: </span>
                     <span  id="created-val"></span>
                </div>
            </div>
            <div class="modal-body">
                <div class="steps-container">
                    <div class="row">
                        <div class="col-md-6">

                            <div class="form-group">
                                <span class="form-title">Mã sản phẩm <span
                                        style="color: rgb(245, 34, 45); margin-right: 4px;">*</span></span>
                                <input placeholder="Mã sản phẩm, barcode" class="form-control" type="text" 
                                        oninvalid="this.setCustomValidity('Hãy nhập đầy đủ thông tin bắt buộc')"
                                         oninput="setCustomValidity('')" id="e-barcode" autocomplete="off" required>
                            </div>

                            <div class="form-group">
                                <span class="form-title">Tên sản phẩm <span
                                        style="color: rgb(245, 34, 45); margin-right: 4px;">*</span></span>
                                <input placeholder="Tên sản phẩm" class="form-control" type="text" id="e-pname" 
                                        oninvalid="this.setCustomValidity('Hãy nhập đầy đủ thông tin bắt buộcbuộc')"
                                         oninput="setCustomValidity('')" autocomplete="off" required>
                            </div>

                            <div class="form-group row">
                                <span class="col-sm-4 form-title">Khối lượng (gram):</span>
                                <div class="col-sm-8">
                                    <input class="form-control" role="combobox" type="number" id="e-weight" 
                                        oninvalid="this.setCustomValidity('Hãy nhập đầy đủ thông tin bắt buộcbuộc')"
                                         oninput="setCustomValidity('')" autocomplete="off" required>
                                </div>
                            </div>

                            <div class="form-group ware-e-group">
                                <span class="form-title">Kho hàng:</span>
                                <select class="form-control" id="e-warehouse" role="combobox" multiple></select>
                            </div>

                            <div class="form-group">
                                <div>
                                    <div class="form-title">Link nhập hàng:</div>
                                    <div>
                                        <input placeholder="Enter để thêm link sản phẩm" class="form-control"
                                            type="text" value="" autocomplete="off" id="e-link">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">

                            <div class="form-group cate-e-group">
                                <span class="form-title">Danh mục:</span>
                                <select class="form-control" id="e-categories" multiple></select>
                            </div>

                            <div class="form-group sup-e-group">
                                <span class="form-title">Nhà cung cấp:</span>
                                <select id="e-supplier" class="form-control" multiple></select>
                            </div>

                            <div class="form-group">
                                <span class="form-title">Ghi chú:</span>
                                <textarea rows="4" placeholder="Ghi chú SP" autocomplete="off" id="e-note"
                                    class="form-control"></textarea>
                            </div>
                        </div>
                    </div>                    
                </div>
                <div class="table-container">
                    <table class="table table-hover table-responsive-xl" id="e-tb">
                        <thead class="thead-light">
                            <tr>
                                <th style="border-right: 1px solid #ccc;">Kho hàng</th>
                                <th>Trạng thái</th>
                                <!-- <th>Hình ảnh</th> -->
                                <th>Giá nhập</th>
                                <th>Giá bán</th>
                                <th>Số lượng</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </form>
    </div>
</div>
</div>`