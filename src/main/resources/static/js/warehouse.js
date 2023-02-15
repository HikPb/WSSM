const toast = new bootstrap.Toast($("#toast"));

$(document).ready(function () {
    showListWarehouse();
    
    $("#e-wform").on("submit", function (e) {
        e.preventDefault();
        let customer = JSON.stringify({
            name: $("#e-wname").val(),
            phone: $("#e-wphone").val(),
            address: $("#e-waddress").val(),
        });
        $.ajax({
            url: "/api/warehouse/"+$(this).attr("wid"),
            method: "put",
            data: customer,
            contentType: "application/json",
            success: function (response) {  
                showListWarehouse()
                $("#toast-content").html("Chỉnh sửa thành công: #"+response.data['id']+' - '+ response.data['name'])
                toast.show()
            },  
            error: function (err) {  
                alert(err);  
            } 
        });    
    });

    $("#btn-create").on("click", function(e){
        e.preventDefault();
        $("#c-ware-modal").modal("show");
    });

    $("#c-form").on("submit", function (e) {
        e.preventDefault();
        let customer = JSON.stringify({
          name: $("#c-wname").val(),
          phone: $("#c-wphone").val(),
          address: $("#c-address").val(),
        });
        $.ajax({
            url: "/api/warehouse",
            method: "post",
            data: customer,
            contentType: "application/json",
            success: function (response) {  
                showListWarehouse()
                $('#c-ware-modal form :input').val("");
                $("#c-ware-modal").modal("hide");
                $("#toast-content").html("Tạo thành công: #"+response.data['id']+' - '+ response.data['name'])
                toast.show()
                //window.location.href = "/warehouse"
            },  
            error: function (err) {  
                alert(err);  
            } 
        })
    });
        
    $("#ul-list").on("click", "li", function() {
        $("#ul-list li").removeClass("selected")
        if(!$(this).hasClass("selected")){
            showWareData($(this).attr("wid"))
            $(this).addClass("selected");
        }
    });

    $("#ul-list").on("click", ".btn-delete", function (e) {
        e.preventDefault();
        href = "/api/warehouse/" + $(this).attr("w-id");
        $("#yesBtn").attr("href", href);
        $("#yesBtn").attr("w-id", $(this).attr("w-id"));
        $("#confirmText").html("Bạn chắc chắn muốn xoá kho hàng: \<strong\>" + $(this).attr("wname") + "\<\/strong\> này?");
        $("#confirmModal").modal("show");
    });

    $("#yesBtn").on("click", function (e) {
        e.preventDefault();
        url = $(this).attr("href");
        id = $(this).attr("w-id");
        $.ajax({
            url: url,
            method: "delete",
            success: function (data) {  
                showListWarehouse()
                $("#confirmModal").modal("hide");
                $("#toast-content").html("Đã xóa: #"+id)
                toast.show()
                //window.location.href = "/warehouse"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
    });

    // $("#whpTable").on("click", "tr .status", function (e) {
    //     e.preventDefault();
    //     data = $("#whpTable").DataTable().row($(this).parents('tr')).data();
    //     url = "/api/item/"+data["id"]+"/status";
    //     $.ajax({
    //         url: url,
    //         method: "POST",
    //         success: function (response) {  
    //             $("#toast-content").html("Cập nhật thành công: #"+response.data['id']+' - '+ response.data.product['productName'])
    //             toast.show()
    //         },  
    //         error: function (err) {  
    //             alert(err);  
    //         } 
    //     });

        
    //   });
    
  });

  function showWareData(wareId){
    let url = "/api/warehouse/"+ wareId;
    let url2 = "/api/item/warehouse/"+ wareId;
    let wareData = getAjaxResponse(url)
    let wareItem = getAjaxResponse(url2)
    $("#e-wform").attr("wid", wareId)
    $("#e-wname").val(wareData.name);
    $("#e-wphone").val(wareData.phone);
    $("#e-waddress").val(wareData.address);
    $("#whpTable").DataTable( {
        processing: true,
        responsive: true,
        destroy: true,
        data: wareItem,
        dataSrc: function(data){
            if(data.data == null){
                return [];
            } else {
                return data.data;
            }
        },
        columns: [
            {
                data: null,
                orderable: false,
                render: function(data, type, row){
	                if(data.active == true){
                        return `<div class="form-check form-switch"><input class="form-check-input status" type="checkbox" onclick="setItemStatus(`+data.id+`)" role="switch" checked></div>`
	                }
	                if(data.active == false){
                        return `<div class="form-check form-switch"><input class="form-check-input status" type="checkbox" onclick="setItemStatus(`+data.id+`)" role="switch"></div>`
	                }
            	}
            },
            { 
                data: 'product.id',
                className: 'td-data'
            },
            { 
                data: 'product.productName',
                className: 'td-data'
            },
            { 
                data: 'sku', 
                className: 'td-data',
            },
            { 
                data: 'qty', 
                className: 'td-data',
            },
            { 
                data: 'purcharsePrice', 
                className: 'td-data'
            },
            { 
                data: 'retailPrice',
                className: 'td-data',
            }
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
        dom: '<"top"if>rt<"bottom"pl><"clear">',
        search: {
            "addClass": 'form-control input-lg col-xs-12'
        },
        order: [[ 1, 'desc' ]]
    });
    
  }

function showListWarehouse(){
    var warehouses = [];
    $.get("/api/warehouse", function(response){
        let items =""
        warehouses = $.map(response.data, dt=>{
            return{
                id: dt['id'],
                name: dt['name'],
                phone: dt['phone'],
                address: dt['address']
            }
        })
        if(warehouses.length>0){
            $(".media-list").empty();
            warehouses.forEach(w=>{
                items +=    '<li class="media" style="cursor: pointer;" wid="'+w.id+'" wname="'+w.name+'">' +
                                '<div class="media-body">' +
                                    '<h6 class="media-heading">'+w.name+'</h6>' +
                                    '<button class="btn btn-xs btn-delete" style="position: absolute;right:30px;" w-id="'+w.id+'" wname="'+w.name+'" data-toggle="tooltip" data-original-title="Delete"><i class="fa-solid fa-trash"></i></button>' +
                                    '<div class="warehouse-phone">' +
                                        '<i class="fa-solid fa-phone"></i>' +
                                        '<span class="span-info">'+w.phone+'</span>' +
                                    '</div>' +
                                    '<div class="warehouse-address ">' +
                                        '<i class="fa-solid fa-location-dot"></i>' +
                                        '<span class="span-info">'+w.address+'</span>' +
                                    '</div>' +
                                '</div>' +
                            '</li>'
            })
            $(".media-list").append(items);
        }
    })
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

function searchWh() {
    var filter, ul, li, i, txtValue;
    filter = document.getElementById("wh-search").value.toUpperCase();
    ul = document.getElementById("ul-list");
    li = ul.getElementsByTagName("li");
    for (i = 0; i < li.length; i++) {
        txtValue = li[i].getAttribute("wname");
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
    }
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