const toast = new bootstrap.Toast($("#toast"));

$(document).ready(function () {
    $("#profile-form").on("submit", function(){
        alert("Submit");
    })
    $('#fullname, #phone').bind('keyup', function() {
        if($(this).val()!='') {
            $('#submit-btn').removeAttr('disabled')
        }else{
            $('#submit-btn').addAttr('disable')
        }
    });

    $('#p3, #p2').on('keyup', function () {
        if ($('#p2').val() == $('#p3').val()) {
            $('#message').html('Xác nhận mật khẩu').css('color', 'green');
        } else 
            $('#message').html('Chưa xác nhận mật khẩu').css('color', 'red');
        });

});

// function allFilled() {
//     var filled = true;
//     $('form input').each(function() {
//         if($(this).val() == '') filled = false;
//     });
//     return filled;
// }

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