$(document).ready(function () {
    let table = $("#productTable").DataTable( {
        // processing: true,
        // serverSide: true,
        responsive: true,
        ajax: {
            url: "/products/api",
            dataSrc: '',
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: function (d) {
                    return JSON.stringify(d);
            }
            },
        columns: [
            {
                defaultContent: '',
                data: null,
                orderable: false,
                className: 'select-checkbox',
            },
            { 
                data: 'isSell', 
                orderable: false,
                render: function(data, type, row){
                if(data == true){
                    return `<label class="switch"><input class="issell" data="`+data +`" type="checkbox" checked><span class="slider round"></span></label>`
                }
                if(data == false){
                    return `<label class="switch"><input class="issell" data="`+data +`"type="checkbox"><span class="slider round"></span></label>`
                }
            }},
            { 
                data: 'productId',
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
                data: 'cateId', 
                className: 'td-data'
            },
            { 
                data: 'importPrice',
                className: 'td-data'
            },
            { 
                data: 'sellPrice', 
                className: 'td-data'
            },
            { 
                data: 'created_at',
                className: 'td-data',
                render: function(data, type, row){
                return moment(data).format('HH:mm DD-MM-YYYY')
            }
            },
            {
                defaultContent: '<button th:productId="${product.productId}" th:productName="${product.productName}" id="btnDelete title="Delete this product" class="fa-regular fa-trash-can icon-dark btn-delete"></button>'
            },
        ],
        paging: true, 
        pagingType: 'numbers',
        lengthMenu: [ [20, 30, 50, -1], [20, 30, 50, "All"] ],
        language: {
            "search": "_INPUT_",            
            "searchPlaceholder": "Search",
            "lengthMenu": "_MENU_/trang",
            "zeroRecords": "Không có sản phẩm nào!",
            "info": "Trang _PAGE_/_PAGES_",
            "infoEmpty": "Không có sản phẩm",
            "infoFiltered": "(filtered from _MAX_ total records)"
        },
        dom: '<"top"if>rt<"bottom"pl><"clear">',
        // buttons: [
        //     'excel', 'print'
        // ],
        search: {
            "addClass": 'form-control input-lg col-xs-12'
        },
        // columnDefs: [ {
        //     className: "td-data", 
        //     targets: [2,3,4] 
        // } ],
        select: {
            style:    'multi',
            selector: 'td:first-child'
        },
        order: [[ 8, 'desc' ]]
    });

    new $.fn.dataTable.Buttons( table, {
        buttons: [             
            {
            extend:    'print',
            text:      '<i class="fa fa-print"></i> In',
            titleAttr: 'Print',
            className: 'btn-tools',
            exportOptions: {
                columns: ':visible'
            }
            },  
            {
                extend:    'excel',
                text:      '<i class="fa-solid fa-file-export"></i><span>Xuất excel</span>',
                titleAttr: 'Excel',
                className: 'btn-tools',
                exportOptions: {
                    columns: ':visible'
                }
                },
        ]
    } );
    table.buttons().container().appendTo('#action-tools');

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
        // var data = table.row($(this).parents('tr')).data();
        data = table.row(this).data();
        href = "products/api/"+data["productId"];
        $.get(href, function(product, status){
            $("#id-val").val(product.productId);
            $("#name-val").val(product.productName);
            $("#barcode-val").val(product.barcode);
            $("#ip-val").val(product.importPrice);
            $("#sp-val").val(product.sellPrice);
            $("#cate-val").val(product.cateId);
            // $("#time-val").innerHTML += 'Hello';
        })
        $("#product-modal-edit").modal("show");
    });

    $("#productTable").on("click", 'tbody tr .issell', function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        // if(data["isSell"]=true){
        //     $(this).prop('checked', false); 
        // }else{
        //     $(this).prop('checked', true);
        // }
        url = "products/"+data["productId"]+"/issell/"+!data["isSell"];
        $.ajax({
            url: url,
            method: "POST",
            // data: {
            //     id: data["productId"],
            //     isSell: data["isSell"]
            // },
            success: function (data) {  
                table.ajax.reload(null, false);
        
            },  
            error: function (err) {  
                alert(err);  
            } 
        });

        
      });

    $("#btnCreate").on("click", function(e){
        e.preventDefault();
        $("#product-modal").modal("show");
        
    });

    $("#cate-create").autocomplete({  
        source: function (request, response) {  
            $.ajax({  
                url: "/api/category",  
                method: "get",  
                contentType: "application/json;charset=utf-8", 
                // data : {
                //     q : request.term
                // },   
                success: function (data) {  
                    console.log(data);
					response(data);  
                },  
                error: function (err) {  
                    alert(err);  
                }  
            });  
        }  
    });  


    // $('#productTable tbody').on('click', '.btn-edit', function(e) {
    //     e.preventDefault();
    //     href = $(this).attr("href");
    //     $.get(href, function(product, status){
    //         $("#id-val").val(product.productId);
    //         $("#name-val").val(product.productName);
    //         $("#barcode-val").val(product.barcode);
    //         $("#ip-val").val(product.importPrice);
    //         $("#sp-val").val(product.sellPrice);
    //         $("#cate-val").val(product.cateId);
    //         // $("#time-val").innerHTML += 'Hello';
    //     })
    //     $("#product-modal-edit").modal("show");
        
    //     });

    $("#productTable tbody").on("click", ".btn-delete", function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        href = "products/delete/"+data["productId"]+"";
        console.log(data);
        // link = $(this);
        // href = "products/delete/"+link.attr("productId")+"";
        $("#yesBtn").attr("href", href);
        $("#yesBtn").attr("productId", data["productId"]);

        $("#confirmText").html("Bạn muốn xoá sản phẩm này: \<strong\>" + data["productName"] + "\<\/strong\>?");
        $("#confirmModal").modal("show");
    });

    $("#yesBtn").on("click", function (e) {
        e.preventDefault();
        url = $(this).attr("href");
        id = $(this).attr("productId");
        $.ajax({
            url: url,
            method: "GET",
            success: function (data) {  
                window.location.href = "/products"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });

    $("#btnClear").on("click", function (e) {
      e.preventDefault();
      table.ajax.reload(null, false)
    //   window.location="/products"
    });
  });

  function changePageSize() {
    $("#searchForm").submit();
  }
