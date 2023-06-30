(function($) {

	"use strict";

	var fullHeight = function() {

		$('.js-fullheight').css('height', $(window).height());
		$(window).resize(function(){
			$('.js-fullheight').css('height', $(window).height());
		});

	};
	fullHeight();

	$(".toggle-password").click(function() {

	  $(this).toggleClass("fa-eye fa-eye-slash");
	  var input = $($(this).attr("toggle"));
	  if (input.attr("type") == "password") {
	    input.attr("type", "text");
	  } else {
	    input.attr("type", "password");
	  }
	});

	$("#login-form").on("submit", async function(e){
		e.preventDefault();
		await fetch ("/login", {
			method: "POST",
			redirect: 'follow',
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json',
				'credentials': 'same-origin',
			  },
			  body: JSON.stringify({username: $("input[name='username']").val(), password: $("input[name='password']").val()})
		})
		.then(response =>{
			if(!response.ok) {
				throw Error(response.statusText);
			}
			return response.json();
		})
		.then(data =>{
			if(data.status == "OK"){
				window.location.href ="/overview";
			} else if(data.status == "USERNAME_NOT_FOUND"){
				$("#caution-text").text('Tên đăng nhập không chính xác');
				$("#caution").css("display", "block");
				setTimeout(()=>{
					$("#caution").css("display", "none");
				},3000);
			} else if(data.status == "BAD_CREDENTIALS"){
				$("#caution-text").text('Mật khẩu không chính xác');
				$("#caution").css("display", "block");
				setTimeout(()=>{
					$("#caution").css("display", "none");
				},3000);
			}
		})
		.catch(error => console.log(error));
	});

	function validate() {
		if (document.f.username.value == "" && document.f.password.value == "") {
			alert("Username and password are required");
			document.f.username.focus();
			return false;
		}
		if (document.f.username.value == "") {
			alert("Username is required");
			document.f.username.focus();
			return false;
		}
		if (document.f.password.value == "") {
		alert("Password is required");
		document.f.password.focus();
			return false;
		}
	}

})(jQuery);