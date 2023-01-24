$(document).ready(function () {
    $('#pr-select').select2({
        placeholder: "Nhap ten san pham hoac barcode",
        minimumInputLength: 3,
        allowClear: true,
        ajax: {
            url: 'products/api/search/',
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
                        id: d['productId'],
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

    $("#pr-select2").keyup(function() {
		$.ajax({
			type: "GET",
			url: "products/api/search/",
			data: {
                keyword : $(this).val()
            },
			beforeSend: function() {
				$("#pr-select2").css("background", "#FFF");
			},
			success: function(data) {
                $.each(function(key,value){


                })
				$("#suggesstion-box").show();
				$("#suggesstion-box").html("Xin");
				$("#pr-select2").css("background", "#FFF");
			}
		});
	});

    //To select a country name
    function selectCountry(val) {
	$("#pr-select2").val(val);
	$("#suggesstion-box").hide();
    };

    // Select address
    $(".ad-select-0").select2({
        width: 'resolve',
        placeholder: "Chọn địa chỉ",
        allowClear: true
    });
    
    $(".ad-select-1").select2({
        width: 'resolve',
        placeholder: "Tỉnh thành phố",
        allowClear: true
    });
    
    $(".ad-select-2").select2({
        width: 'resolve',
        placeholder: "Quận huyện",
        allowClear: true
    });
    
    $(".ad-select-3").select2({
        width: 'resolve',
        placeholder: "Phuong xa",
        allowClear: true
    });

    $(".deli-select").select2({
        width: 'resolve',
        minimumResultsForSearch: Infinity,
        placeholder: "ĐVVC",
        allowClear: true
    });
    
    // Select warehouse
    $(".wh-select").select2({
        width: '80px',
        placeholder: "Kho mặc định",
        allowClear: true
    });
    
    // Bootstrap datepicker
    $('#date_1').datepicker({
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
    });

    $('#testbtn').on('click', function(e) {
        $('#test').append("<div th:replace="+"fragments/footer :: footer"+"></div>")
    });
});