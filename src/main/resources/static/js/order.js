$(document).ready(function () {
    $('#pr-select').select2({
        placeholder: "Nhap ten san pham hoac barcode",
        minimumInputLength: 3,
        allowClear: true,
        ajax: {
            url: 'products/api/search/',
            type: 'GET',
            dataType: 'json',
            data: function (params) {
                var query = {
                  keyword: params.term,
                }
                return query;
            },
            processResults: function (data, params) {
                // return {
                //     results: data
                // };
                console.log(data)
                return {
                    results: $.map(data, function (item) {
                        return {
                            id: item.id,
                            text: item.productName,
                            
                        };
                    })
                };
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
				$("#pr-select2").css("background", "#FFF url(LoaderIcon.gif) no-repeat 165px");
			},
			success: function(data) {
                console.log(data)
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
        placeholder: "Chọn địa chỉ",
        allowClear: true
    });
    
    $(".ad-select-1").select2({
        placeholder: "Tỉnh thành phố",
        allowClear: true
    });
    
    $(".ad-select-2").select2({
        placeholder: "Quận huyện",
        allowClear: true
    });
    
    $(".ad-select-3").select2({
        placeholder: "Phuong xa",
        allowClear: true
    });
    
    // Select warehouse
    $(".wh-select").select2({
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
});