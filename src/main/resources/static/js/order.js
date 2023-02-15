const toast = new bootstrap.Toast($("#toast"));
$(document).ready(function () {
    $('#c-searchbox').select2({
        placeholder: "Nhập tên sản phẩm hoặc barcode",
        minimumInputLength: 1,
        width: "100%",
        ajax: {
            url: '/api/products/search',
            type: 'GET',
            dataType: 'json',
            dropdownParent: $('#test1'),
            data: function (params) {
                var query = {
                  keyword: params.term,
                }
                return query;
            },
            processResults: function (data, params) {
                var rs= [];
                data['data'].forEach( d =>{
                    var rsObj = {
                        id: d['id'],
                        text: d['productName']
                      }
                    rs.push(rsObj)
                })
                return {
                    results: rs
                };
                
                // return {
                //     results: $.map(data.data, function (item) {
                //         return {
                //             id: item.productId,
                //             text: item.productName,
                            
                //         };
                //     })
                // };
            }
        }
    });

    // $("#pr-select2").keyup(function() {
	// 	$.ajax({
	// 		type: "GET",
	// 		url: "/api/products/search",
	// 		data: {
    //             keyword : $(this).val()
    //         },
	// 		beforeSend: function() {
	// 			$("#pr-select2").css("background", "#FFF");
	// 		},
	// 		success: function(data) {
    //             $.each(function(key,value){


    //             })
	// 			$("#suggesstion-box").show();
	// 			$("#suggesstion-box").html("Xin");
	// 			$("#pr-select2").css("background", "#FFF");
	// 		}
	// 	});
	// });

    //To select a country name
    function selectCountry(val) {
	$("#pr-select2").val(val);
	$("#suggesstion-box").hide();
    };


    $(".deli-select").select2({
        width: 'resolve',
        minimumResultsForSearch: Infinity,
        placeholder: "ĐVVC",
        allowClear: true
    });
    
    // Select warehouse
    $(".wh-select").select2({
        width: '100%',
        placeholder: "Kho mặc định",
        allowClear: true
    });
    
    // Bootstrap datepicker
    /*$('#date_1').datepicker({
        todayBtn: "linked",
        dateFormat: 'dd-mm-yy',
        keyboardNavigation: false,
        forceParse: false,
        calendarWeeks: true,
        //autoclose: true
    });
    
    $('#date_2').datepicker({
        dateFormat: 'dd-mm-yy',
        calendarWeeks: true,
    });*/

    $('#testbtn').on('click', function(e) {
        $('#test').append("<div th:replace="+"fragments/footer :: footer"+"></div>")
    });
});