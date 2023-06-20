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

	$("#loginBtn").on("click", async function(e){
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
			console.log(response)
			return response.json();
		})
		.then(data =>{
			if(data.status=="ok"){
				// let redirectUrl = "/overview";
				// console.log(data)
				// fetch(redirectUrl, {
				// 	headers: {
				// 	  'Authorization': `Bearer ${data.data.token}`
				// 	}
				//   })
				//   .then(() => {
				// 	window.location.href = redirectUrl;
				//   })
				//   .catch((error) => {
				// 	console.error(error);
				//   });
				//document.cookie = "token="+data.data.token+";expires=Thu, 15 Jun 2023 23:00:00 UTC;"
				window.location="/overview";
			}else if(data.status=="failed"){
				window.location.href="/login"
			}
			//console.log(data);
		})
		.catch(error => console.log(error));
	})
})(jQuery);