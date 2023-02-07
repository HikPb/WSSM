$(document).ready(function () {
    let table = $("#productTable").DataTable( {
        // processing: true,
        // serverSide: true,
        responsive: true,
        ajax: {
            url: "/api/products",
            dataSrc: '',
            type: "GET",
            dataType: "json",
            contentType: "application/json",
            dataSrc: 'data'
            },
        columns: [
            {
                defaultContent: '',
                data: null,
                orderable: false,
                className: 'select-checkbox',
            },
            /*{ 
                data: 'isSell', 
                orderable: false,
                render: function(data, type, row){
	                if(data == true){
	                    return `<label class="switch"><input class="issell" data="`+data +`" type="checkbox" checked><span class="slider round"></span></label>`
	                }
	                if(data == false){
	                    return `<label class="switch"><input class="issell" data="`+data +`"type="checkbox"><span class="slider round"></span></label>`
	                }
            	}
            },*/
            { 
                data: 'id',
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
                data: 'categories', 
                className: 'td-data',
                render: function(data, type, row){
					buttons='';
					data.forEach(c => {buttons += '<span class="badge badge-primary">' + c.cateName +' </span>'});
					return buttons;
				}
            },
            { 
                data: 'weight', 
                className: 'td-data'
            },
            { 
                data: 'createdAt',
                className: 'td-data',
                render: function(data, type, row){
                	return moment(data).format('HH:mm DD-MM-YYYY')
            	}
            },
            { 
                data: 'updatedAt',
                className: 'td-data',
                render: function(data, type, row){
                	return moment(data).format('HH:mm DD-MM-YYYY')
            	}
            },
            {
                defaultContent: '<button th:p-id="${product.id}" th:productName="${product.productName}" id="btnDelete title="Delete this product" class="fa-regular fa-trash-can icon-dark btn-delete"></button>'
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
                columns: [2,3,4,5,6,7]
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
        data = table.row(this).data();
        href = "api/products/"+data["id"];
        $.get(href, function(response, status){
			$("#pme-form").attr("action", "/api/products/"+data.id);
			$("#created-val").html(moment(data.createdAt).format('HH:mm DD-MM-YYYY'));
            $("#e-pname").val(data.productName);
            $("#e-barcode").val(data.barcode);
            //$("#ip-val").val(product.importPrice);
            //$("#sp-val").val(product.sellPrice);
            $("#e-weight").val(data.weight);
        })
        $("#product-modal-edit").modal("show");
    });

    $("#productTable").on("click", 'tbody tr .issell', function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        url = "/api/products/"+data["id"]+"/"+!data["isSell"];
        $.ajax({
            url: url,
            method: "POST",
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

    /*$("#cate-create").on("input", function(){
		$.get('/api/category/search?key=' + $(this).val(), function(response) {
			list ='';
			if(response.data && response.data !=""){
				response.data.forEach((d)=>{list += '<li>' + d.cateName + '</li>'});
	     		$("#c-result").html('<ul>' + list + '</ul>');
     		}
     	})
	});*/
	
	$.widget("ui.autocomplete", $.ui.autocomplete, {
    options : $.extend({}, this.options, {
        multiselect: false
    }),
    _create: function(){
        this._super();

        var self = this,
            o = self.options;

        if (o.multiselect) {
            self.selectedItems = [];           
            self.multiselect = $("<div></div>")
                .addClass("ui-autocomplete-multiselect ui-state-default ui-widget")
                .css("width", "100%")
                .insertBefore(self.element)
                .append(self.element)
                .bind("click.autocomplete", function(){
                    self.element.focus();
                });
            
            var fontSize = parseInt(self.element.css("fontSize"), 10);
            function autoSize(e){
                var $this = $(this);
                $this.width(1).width(this.scrollWidth+fontSize-1);
            };

            var kc = $.ui.keyCode;
            self.element.bind({
                "keydown.autocomplete": function(e){
                    if ((this.value === "") && (e.keyCode == kc.BACKSPACE)) {
                        var prev = self.element.prev();
                        delete self.selectedItems[prev.text()];
                        prev.remove();
                    }
                },
                "focus.autocomplete blur.autocomplete": function(){
                    self.multiselect.toggleClass("ui-state-active");
                },
                "keypress.autocomplete change.autocomplete focus.autocomplete blur.autocomplete": autoSize
            }).trigger("change");

            o.select = function(e, ui) {
                $("<div></div>")
                    .addClass("ui-autocomplete-multiselect-item")
                    //.text(ui.item.label)
                    .append(
						$("<span></span>").text(ui.item.label)
					)
                    .append(
                        $("<span></span>")
                            .addClass("ui-icon ui-icon-close")
                            .click(function(){
                                var item = $(this).parent();
                                self.selectedItems = self.selectedItems.filter((i) => i.label !== item.text());
                                item.remove();
                            })
                    )
                    .insertBefore(self.element);
                
                self.selectedItems.push(ui.item);
                //console.log(self.selectedItems);
                self._value("");
                return false;
            }
        }

        return this;
    }
	});
	
	$("#c-categories").autocomplete({
	    source: function(request, response) {
			let lists = [];
			let matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
			$.ajax({
				url: '/api/category/search?key='+request.term,
			    type: "GET",
			    success: function(data){
					lists = $.map(data.data, function(c) {
	             		return {
			                 label: c.cateName,
			                 value: c.id
			             }
					})
					response($.grep( lists, function(item){
              			return matcher.test(item.label);
          			}))
				}
			});
	    },
	    multiselect: true,
    	minLength: 0,
    	delay: 200,
    	select: function(event, ui){
			console.log(ui.item.value)
			return false
		}
  	}).focus(function () {
    	$(this).autocomplete("search");
	});
	
	$("#c-categories").autocomplete("option", "appendTo", ".cate-c-group");

	$("#c-supplier").autocomplete({
	    source: function(request, response) {
			let lists = [];
			let matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
			$.ajax({
				url: '/api/supplier/search?key='+request.term,
			    type: "GET",
			    success: function(data){
					lists = $.map(data.data, function(s) {
	             		return {
			                 label: s.supName,
			                 value: s.id
			             }
					})
					response($.grep( lists, function(item){
              			return matcher.test(item.label);
          			}))
				}
			});
	    },
    	minLength: 0,
    	delay: 200,
    	select: function(e, ui){
			$( "#c-supplier" ).val(ui.item.label);
			return false;
		}
  	}).focus(function () {
    	$(this).autocomplete("search","");
	});
	
	$("#c-supplier").autocomplete("option", "appendTo", ".sup-c-group");
	
	$.ajax({
		url: '/api/warehouse',
	    type: "GET",
	    success: function(data){
			let lists = $.map(data.data, function(s) {
         		return {
	                 label: s.name,
	                 value: s.id
	             }
			})
			console.log(lists)
			$("#c-warehouse").autocomplete({
			    source: function( request, response ) {
				    let matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
				    response( $.grep( lists, function( item ){
				        return matcher.test( item.label );
				    }));
				},
				autoFocus: true,
				minLength: 0,
		    	multiselect: true,
		    	appendTo: ".ware-c.group",
		    	/*select: function( event, ui ) {  

		            lists = jQuery.grep(lists, function(element) {
		            	return element.value != ui.item.value;
		            });   
		            $('#c-warehouse').autocomplete('option', 'source', lists);          
		            return false;        

            	} */
		  	}).focus(function(){
    				$(this).autocomplete("search");
    			});
    		}
	});
	
	//$("#c-warehouse").autocomplete("option", "appendTo", ".ware-c-group");
	
	$("#c-pform").on("submit", function (e) {
        e.preventDefault();
        let ccate = $("#c-categories").autocomplete("instance").selectedItems;
        console.log(ccate)
        let product = JSON.stringify({
	      barcode: $("#c-barcode").val(),
	      productName: $("#c-pname").val(),
	      weight: $("#c-weight").val(),
	      categories: ccate.map(function(item){return{id: item.value, cateName: item.label}})
	    });
        $.ajax({
            url: "/api/products/new",
            method: "post",
            data: product,
            contentType: "application/json",
            success: function (data) {  
				window.location.href = "/products"
            },  
            error: function (err) {  
                alert(err);  
            } 
        });
        
      });
      
    $("#productTable tbody").on("click", ".btn-delete", function (e) {
        e.preventDefault();
        data = table.row($(this).parents('tr')).data();
        href = "/api/products/"+data["id"]+"";
        $("#yesBtn").attr("href", href);
        $("#yesBtn").attr("p-id", data["id"]);

        $("#confirmText").html("Bạn muốn xoá sản phẩm này: \<strong\>" + data["productName"] + "\<\/strong\>?");
        $("#confirmModal").modal("show");
    });

    $("#yesBtn").on("click", function (e) {
        e.preventDefault();
        url = $(this).attr("href");
        id = $(this).attr("p-id");
        $.ajax({
            url: url,
            method: "delete",
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
