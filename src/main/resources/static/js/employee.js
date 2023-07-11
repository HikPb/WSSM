const toast = new bootstrap.Toast($("#toast"));

$(document).ready(function () {
    showListEmployee();
    
    $("#e-form").on("submit", function (e) {
        e.preventDefault();
        let payload = JSON.stringify({
            username: $(this).attr("uname"),
            newPassword: $("#e-password").val(),
        });
        $.ajax({
            url: "/api/employee/admin-change",
            method: "put",
            data: payload,
            contentType: "application/json",
            success: function (response) {  
                showListEmployee()
                $("#toast-content").html("Chỉnh sửa thành công: #"+response.data['id']+' - '+ response.data['name'])
                toast.show()
            },  
            error: function (err) {  
                alert(err);  
            } 
        });    
    });

    $("#e2-form").on("submit", function (e) {
        e.preventDefault();
        let roles = [];
        if($("#sales_emp").is(':checked')==true){ roles.push("sales_employee") }
        if($("#sales_adm").is(':checked')==true){ roles.push("sales_admin") }
        if($("#ware_emp").is(':checked')==true){ roles.push("warehouse_employee") }
        if($("#ware_adm").is(':checked')==true){ roles.push("warehouse_admin") }
        if($("#deli_man").is(':checked')==true){ roles.push("delivery_man") }
        let payload = JSON.stringify({
            username: $(this).attr("uname"),
            roles: roles,
        });
        $.ajax({
            url: "/api/employee/admin-change-role",
            method: "put",
            data: payload,
            contentType: "application/json",
            success: function (response) {  
                showListEmployee()
                $("#toast-content").html("Chỉnh sửa thành công: #");
                toast.show()
            },  
            error: function (err) {  
                alert(err);  
            } 
        });    
    });

    $("#btn-create").on("click", function(e){
        e.preventDefault();
        $("#c-emp-modal").modal("show");
    });

    $("#c-form").on("submit", async function (e) {
        e.preventDefault();
        await fetch("/api/employee",{
            method: "POST",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json",
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: JSON.stringify({
                username: $("#c-username").val(),
                password: $("#c-password").val(),
                role: "ROLE_USER"
      
            }),
        })
        .then(response => {
            if(!response.ok) throw Error(response.statusText);
            return response.json();
        })
        .then(data =>{
            showListEmployee()
            $('#c-emp-modal form :input').val("");
            $("#c-emp-modal").modal("hide");
            $("#toast-content").html("Tạo thành công: #"+response.data['id']+' - '+ response.data['username'])
            toast.show()
        })
        .catch(error => console.error(error));
    });
        
    $("#ul-list").on("click", "li", function() {
        $("#ul-list li").removeClass("selected")
        if(!$(this).hasClass("selected")){
            showData($(this).attr("wid"))
            $(this).addClass("selected");
        }
    });

    $("#ul-list").on("click", ".btn-delete", function (e) {
        e.preventDefault();
        href = "/api/employee/" + $(this).attr("w-id");
        $("#yesBtn").attr("href", href);
        $("#yesBtn").attr("w-id", $(this).attr("w-id"));
        $("#confirmText").html("Bạn chắc chắn muốn xoá tài khoản: \<strong\>" + $(this).attr("wname") + "\<\/strong\> này?");
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
                showListEmployee()
                $("#confirmModal").modal("hide");
                $("#toast-content").html("Đã xóa: #"+id)
                toast.show()
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
    });
    
  });

  function showData(id){
    let url = "/api/employee/"+ id;
    let empData = getAjaxResponse(url)
    console.log(empData)
    $("#e-form").attr("uname", empData.username);
    $("#e2-form").attr("uname", empData.username);
    $("#e-fullname").val(empData.fullname);
    $("#e-phone").val(empData.phone);
    $("#sales_emp").prop('checked',false);
    $("#sales_adm").prop('checked',false);
    $("#ware_emp").prop('checked',false);
    $("#ware_adm").prop('checked',false);
    $("#deli_man").prop('checked',false);    
    $("#sales_emp").prop('disabled',false);
    $("#sales_adm").prop('disabled',false);
    $("#ware_emp").prop('disabled',false);
    $("#ware_adm").prop('disabled',false); 
    $("#deli_man").prop('disabled',false);
    $("#roleBtn").prop('disabled',false); 
    //$("#e-password").val(empData.username);
    if(empData.roles.includes("ROLE_ADMIN", 0)==true){
        $("#sales_emp").prop('disabled',true);
        $("#sales_adm").prop('disabled',true);
        $("#ware_emp").prop('disabled',true);
        $("#ware_adm").prop('disabled',true);
        $("#deli_man").prop('disabled',true);
        $("#roleBtn").prop('disabled',true); 
    }
    empData.roles.forEach(role =>{
        switch(role){
            case "ROLE_SALES_EMPLOYEE":
                $("#sales_emp").prop('checked',true);
                break;
            case "ROLE_SALES_ADMIN":
                $("#sales_adm").prop('checked',true);
                break;
            case "ROLE_WAREHOUSE_EMPLOYEE":
                $("#ware_emp").prop('checked',true);
                break;
            case "ROLE_WAREHOUSE_ADMIN":
                $("#ware_adm").prop('checked',true);
                break;
            case "ROLE_DELIVERY_MAN":
                $("#deli_man").prop('checked',true);
                break;
        }
    })
    
  }

function showListEmployee(){
    var lists = [];
    $.get("/api/employee", function(response){
        let items =""
        lists = $.map(response.data, dt=>{
            return{
                id: dt['id'],
                username: dt['username'],
                phone: dt['phone'],
                fullname: dt['fullname'],
                roles: dt['roles']
            }
        })
        if(lists.length>0){
            lists.sort((a,b) => (a.id>b.id)?1:-1);
            $(".media-list").empty();
            lists.forEach(w=>{
                if(w.fullname == null) w.fullname ="-- Chưa nhập --"
                if(w.phone == null) w.phone ="-- Chưa nhập --"
                if(w.roles.includes('ROLE_ADMIN',0)){
                    items +=    `<li class="media" style="cursor: pointer;" wid="${w.id}" wname="${w.username}">
                                <div class="m-body">
                                    <div class="media-heading">${w.username}
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <i class="fa-solid fa-user"></i>
                                            <span class="span-info" style="margin-left: 5px;">${w.fullname}</span>
                                        </div>
                                        <div class="col-md-6">
                                            <i class="fa-solid fa-phone"></i>
                                            <span class="span-info" style="margin-left: 5px;">${w.phone}</span>
                                        </div>
                                    </div>
                                </div>
                            </li>`
                }else{
                    items +=    `<li class="media" style="cursor: pointer;" wid="${w.id}" wname="${w.username}">
                                <div class="m-body">
                                    <div class="media-heading">${w.username} 
                                        <button class="btn btn-xs btn-delete" style="position: absolute;right:30px;" w-id="${w.id}" wname="${w.username}" data-toggle="tooltip" data-original-title="Delete">
                                            <i class="fa-solid fa-trash"></i>
                                        </button>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <i class="fa-solid fa-user"></i>
                                            <span class="span-info" style="margin-left: 5px;">${w.fullname}</span>
                                        </div>
                                        <div class="col-md-6">
                                            <i class="fa-solid fa-phone"></i>
                                            <span class="span-info" style="margin-left: 5px;">${w.phone}</span>
                                        </div>
                                    </div>
                                </div>
                            </li>`
                }
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

function search() {
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
