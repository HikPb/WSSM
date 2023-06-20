const toast = new bootstrap.Toast($("#toast"));

$(document).ready(function () {
    $("#profile-form").on("submit", async function(e){
        e.preventDefault();
        await fetch("/api/employee/"+$(this).attr("wid"),{
            method: "PUT",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json",
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: JSON.stringify({
                fullname: $("#fullname").val(),
                phone: $("#phone").val()      
            }),
        })
        .then(response => {
            if(!response.ok) throw Error(response.statusText);
            return response.json();
        })
        .then(data =>{
            if(data.status=="ok") {
                $('#submit-btn').prop('disabled', 'disabled');
                $("#toast-content").html("Cập nhật thành công!");
            }else{
                $("#toast-content").html(data.message);
            }
            toast.show()
        })
        .catch(error => console.error(error));
    });

    $("#pw-form").on("submit", async function(e){
        e.preventDefault();
        await fetch("/api/employee/"+$(this).attr("wid")+"/changepassword",{
            method: "PUT",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json",
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: JSON.stringify({
                password: $("#p1").val(),
                newPassword: $("#p2").val()      
            }),
        })
        .then(response => {
            if(!response.ok) throw Error(response.statusText);
            return response.json();
        })
        .then(data =>{
            if(data.status=="ok") {
                $('#submit-btn2').prop('disabled', 'disabled');
                $("#toast-content").html("Cập nhật thành công!");
                $("#p1").val("");
                $("#p2").val("");
                $("#p3").val("");
                $("#message").html("");
            }else{
                $("#toast-content").html(data.message);
            }
            toast.show();
        })
        .catch(error => console.error(error));
    });
    
    $('#fullname, #phone').bind('keyup', function() {
        if($(this).val()!='') {
            $('#submit-btn').removeAttr('disabled');
        }else{
            $('#submit-btn').prop('disabled', 'disabled');
        }
    });

    // $('#p1').on('keyup', function(){
    //     if($("#p1").val()==""){
    //         $('#message').html('');
    //         $('#submit-btn2').prop('disabled', 'disabled');
    //     } else if ($('#p2').val() == $('#p3').val()) {
    //         $('#submit-btn2').removeAttr('disabled');
    //         $('#message').html('Xác nhận mật khẩu').css('color', 'green');
    //     } else {
    //         $('#submit-btn2').prop('disabled', 'disabled');
    //         $('#message').html('Chưa xác nhận mật khẩu').css('color', 'red');
    //     } 
    // })
    $('#p1, #p3, #p2').on('keyup', function () {
        if($("#p1").val()==""){
            $('#message').html('');
            $('#submit-btn2').prop('disabled', 'disabled');
        }
        if($('#p1').val()!="" && $('#p2').val()=="" && $('#p3').val()==""){
            $('#message').html('');
            $('#submit-btn2').prop('disabled', 'disabled');
        }
        if($('#p1').val()!="" && $('#p2').val()!="" && $('#p2').val() == $('#p3').val()) {
            $('#submit-btn2').removeAttr('disabled');
            $('#message').html('Xác nhận mật khẩu').css('color', 'green');
        } 
        if($('#p1').val()!="" && $('#p2').val() != $('#p3').val()) {
            $('#submit-btn2').prop('disabled', 'disabled');
            $('#message').html('Chưa xác nhận mật khẩu').css('color', 'red');
        } 
                
    });

    $("#av-btn").click(function(e){
        e.preventDefault();
        $("#av-input").click();
    });

    $("#av-input").on("change", function(){
        let upload_img = "";
        const reader = new FileReader();
        reader.addEventListener("load", ()=>{
            upload_img = reader.result;
        });
        reader.readAsDataURL(this.files[0]);
    })

});



function showhidePassword(element, icon) {
    var x = document.getElementById(element);
    var i = document.getElementById(icon)
    if (x.type === "password") {
        x.type = "text";
        i.classList.toggle("fa-eye");
        i.classList.toggle("fa-eye-slash");
    } else {
        x.type = "password";
        i.classList.toggle("fa-eye");
        i.classList.toggle("fa-eye-slash");
    }
}