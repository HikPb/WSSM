const toast = new bootstrap.Toast($("#toast"));
//const wareData = getAjaxResponse("/api/warehouse");
//const itemData = getAjaxResponse("/api/item");
//const customerData = getAjaxResponse("/api/customer");

const cartItemsEl = document.getElementById("order-items");
const searchbox = document.getElementById("c-searchbox");

const shipFeeEl = document.getElementById("c-shippingFee");
const discountEl = document.getElementById("c-discount");
const reMoneyEl = document.getElementById("c-receivedMoney");

const totalTEl = document.getElementById("total");
const totalDisTEl = document.getElementById("totalDiscount");
const paymentTEl = document.getElementById("payment");
const receivedTEl = document.getElementById("received");
const oweTEl = document.getElementById("owe");

const subTotalEl = document.getElementById("subTotal");
const codEl = document.getElementById("cod");

//let cart = JSON.parse(localStorage.getItem("CART")) || [];
var cart = [];
var customer;
var totalPrice = 0;
var totalItems = 0;
var totalDiscount = 0;
var totalWeight = 0;
var shippingFee = 0;
var owe = 0;
var discount = 0;
var receivedMoney = 0;
var revenue = 0;
var sales = 0;
var profit = 0;
updateCart();

// ADD TO CART
function addToCart(id) {
    // check if product already exist in cart
    searchbox.value = "";
    if (cart.some((item) => item.itemId === id)) {
        changeNumberOfUnits("plus", id);
    } else {
        const item = itemData.find((product) => product.id === id);
        const customItem = {
            itemId: item.id,
            sprice: item.retailPrice,
            sku: item.sku,
        };
        cart.push({
        ...customItem,
        qty: 1,
        discount: 0
        });
    }
    updateCart();
}

    // update cart
function updateCart() {
    renderCartItems();
    renderSubtotal();

// save cart to local storage
//localStorage.setItem("CART", JSON.stringify(cart));
}

    // calculate and render subtotal
function renderSubtotal() {
    totalPrice = 0,
    totalItems = 0;
    totalDiscount = 0;
    totalWeight = 0;
    sales = 0;
    revenue = 0;
    owe = 0;
    var totalCost = 0; //Tong von
    if(discountEl.value!="") discount = parseInt(discountEl.value.replace(/\,/g, ''),10);
    if(shipFeeEl.value!="") shippingFee = parseInt(shipFeeEl.value.replace(/\,/g, ''),10);
    if(reMoneyEl.value!="") receivedMoney = parseInt(reMoneyEl.value.replace(/\,/g, ''),10);

    cart.forEach((item) => {
        let itemDto = itemData.find( x => x.id === item.itemId);
        totalPrice += item.sprice * item.qty;
        totalItems += parseInt(item.qty,10);
        totalDiscount += parseInt(item.discount,10);
        totalWeight += itemDto.product.weight * item.qty;
        totalCost += itemDto.purcharsePrice * item.qty;
    });

    totalDiscount += discount;
    revenue = totalPrice + shippingFee - totalDiscount;
    sales = totalPrice + shippingFee;
    owe = totalPrice + shippingFee - totalDiscount - receivedMoney;
    profit = revenue - totalCost;

    totalTEl.textContent = `${numberWithCommas(totalPrice)}`;
    totalDisTEl.textContent = `${numberWithCommas(totalDiscount)}`;
    paymentTEl.textContent = `${numberWithCommas(totalPrice - totalDiscount)}`;
    receivedTEl.textContent = `${numberWithCommas(receivedMoney)}`;
    oweTEl.textContent = `${numberWithCommas(owe)}`;
    
    subTotalEl.textContent = `${numberWithCommas(revenue)}`;
    codEl.textContent = `${numberWithCommas(totalPrice + shippingFee - totalDiscount - receivedMoney)}`;
}

// render cart items
function renderCartItems() {
    cartItemsEl.innerHTML = ""; // clear cart element
    if(cart.length>0){
        cart.forEach((item) => {
            let itemDto = itemData.find(x => x.id === item.itemId);
            cartItemsEl.innerHTML += 
                `<div class="cart-item media">
                    <div style="display:flex; align-item:center;">
                        <div class="media-img">
                            <img src="/img/product.jpg" width="50" height="50" >
                        </div>
                        <div class="media-body">
                            <div class="media-heading" style="margin-bottom:0px;">
                                <span class="badge badge-info m-r-5 m-b-5">${itemDto.product.barcode}</span>
                                <span class="font-14">${itemDto.product.productName}</span>
                                <span style="position: absolute; right: 22px;"><i class="fa-regular fa-circle-xmark" style="color: #ff0000;" onclick="removeItemFromCart(${item.itemId})"></i></span>
                            </div>
                            <div style="display:flex; align-items:center;>
                                <span><i class="fa-solid fa-weight-scale"></i> <span style="color:#0af;">${itemDto.product.weight} g</span></span>
                                <div style="display:inline-block; right:25px; position:absolute;">
                                    <input type="number" style="border:none; text-align:right; width: 60px; background-color: #eaeaea;" value="${item.qty}" min="1" max="9999" onchange="changeItemQty(this,${item.itemId})">
                                    <span> x </span>
                                    <input type="text" style="border:none; text-align:right; background-color: #eaeaea;" value="${numberWithCommas(item.sprice)}đ" size="10" disabled>                        
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="display:flex; align-items:center;">
                        <div style="margin-left: 64px;">
                            <span>Giảm giá: </span>
                            <input class="" type="text" style="border:none; text-align:right; width: 85px; background-color: #eaeaea;" value="${item.discount}" min="0" max="999999999" onchange="changeItemDiscount(this,${item.itemId})">
                            <span> đ</span>
                        </div>
                        <div style="display:inline-block; right:25px; position:absolute;">
                            <span>
                                Tổng: 
                                <span style="color:#0af;">
                                ${numberWithCommas(item.sprice * item.qty - item.discount)} đ
                                </span>
                            </span>                    
                        </div>
                    </div>
                </div>`;
        });
    }else{
        cartItemsEl.innerHTML = 
            `<div id="virtualize-list">
                <div class="order-empty-text">Giỏ hàng trống</div>
            </div>`
    }
}

// remove item from cart
function removeItemFromCart(id) {
    cart = cart.filter((item) => item.itemId !== id);
    updateCart();
}

    // change number of units for an item
function changeNumberOfUnits(action, id) {
    let itemDto = itemData.find(x => x.id === id);
    cart = cart.map((item) => {
        let qty = item.qty;
        if (item.itemId === id) {
            if (action === "minus" && item.qty > 1) {
                item.qty--;
            } else if (action === "plus" && qty < itemDto.qty) {
                item.qty++;
            }
        }
        return {
        ...item,
        };
    });
    console.log(cart)
    updateCart();
}

function changeItemQty(elm, id) {
    let newQty = elm.value;
    cart = cart.map((item)=>{
        if(item.itemId === id){
            item.qty = newQty;
        }
        return{
            ...item
        }
    });

    updateCart();
}

function changeItemDiscount(elm, id) {
    let newDiscount = elm.value;
    cart = cart.map((item)=>{
        if(item.itemId === id){
            item.discount = newDiscount;
        }
        return{
            ...item
        }
    });

    updateCart();
}

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

async function selectCustomer(id){
    //let customer;
    await fetch("/api/customer/"+id,{
        method: "GET",
        credentials: "same-origin",
        headers: {
            "Content-Type": "application/json",
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
    })
    .then(response => {
        if(!response.ok) throw Error(response.statusText);
        return response.json();
    })
    .then(data =>{
        if(data.status=="ok") {
            customer = data.data;
        }else{
            $("#toast-content").html(data.message);
            toast.show();
        }
    })
    .catch(error => console.error(error));
    let dob = "Chưa có";
    $("#c-cusName").val(customer.name);
    $("#c-cusPhone").val(customer.phone);
    if(customer.dob!="" && customer.dob!=null){
        dob = moment(customer.dob).format('DD-MM-YYYY');
        $("#c-cusDob").val(moment(customer.dob).format('YYYY-MM-DD'))
    }
    $("#c-receiverName").val(customer.name);
    $("#c-receiverPhone").val(customer.phone);
    $("#c-receiverAddress").val(customer.address);
    let cusInfoElem = 
    `
    <div class="customer-info-container">
        <div class="close-box" onclick="closeCustomerInfo()"><i class="fa-regular fa-circle-xmark" style="color: #ff0000;"></i></div>
        <div class="customer-profile">
            <div class="customer-profile-info">
                <span><i class="fa-solid fa-user"></i>
                    ${customer.name}</span>
                <span><i class="fa-solid fa-phone"></i>
                    ${customer.phone}</span>
            </div>
            <div class="customer-profile-dob">
                <span>${dob}</span>
            </div>
        </div>
        
        <div class="customer-info-history">
            <div>
                <span>
                    Tổng: <span>${numberWithCommas(customer.tmoney)}</span> đ
                    </soan>
            </div>
            <div>
                <span>
                    Thành công:
                    <span>${customer.nsoCus}</span>/
                    <span>${customer.npCus}</span>
                </span>
            </div>
        </div>
    </div>
    `
    $("#c-cusInfo").empty();
    $("#c-cusInfo").append(cusInfoElem);
}

function closeCustomerInfo(){
    //e.preventDefault();
    customer = null;
    $("#c-cusName").val('');
    $("#c-cusPhone").val('');
    $("#c-cusDob").val('');
    $("#c-receiverName").val('');
    $("#c-receiverPhone").val('');
    $("#c-receiverAddress").val('');
    $("#c-cusInfo").empty();
    $("#c-cusInfo").append(
        `<div style="position: relative; padding: 6px; text-align: center; background-color: rgba(240, 245, 255, .4); border: 1px solid #adc6ff;">
            <i class="fa-solid fa-exclamation"></i>
            <span>Chưa có thông tin</span>
        </div> `);
}

function loadDraftData(){
    let dt = JSON.parse(localStorage.getItem("order-draft-data"));
    $("#c-warehouse").val(dt.wareId).trigger('change');
    $("#c-internalNote").val(dt.internalNote);
    $("#c-printNote").val(dt.printedNote);
    $("#c-shippingFee").val(dt.shippingFee);
    $("#c-discount").val(dt.totalDiscount);
    $("#c-receivedMoney").val(dt.receivedMoney);

    cart.splice(0,cart.length);
    
    if(dt.items.length>0){
        dt.items.forEach(i =>{
            let obj = {
                itemId: i.itemId,
                sprice: i.sprice,
                sku: i.sku,
                qty: i.qty,
                discount: i.discount
            }
            cart.push(obj)
        })
    }
    updateCart();

    if(dt.cusId!=null){
        selectCustomer(dt.cusId);
    }else{
        $("#c-cusName").val(dt.cusName);
        $("#c-cusPhone").val(dt.cusPhone);
        if(dt.cusDob!=""){
            $("#c-cusDob").val(moment(dt.cusDob).format('YYYY-MM-DD'));
        }
        $("#c-receiverName").val(dt.receiverName);
        $("#c-receiverPhone").val(dt.receiverPhone);
        $("#c-receiverAddress").val(dt.address);
    }
    $("#draftModal").modal('hide');
}

function storeDraftData(event) {
    event.preventDefault();
    localStorage.removeItem("order-draft-data"); 
    if(!($("#c-internalNote").val() == "" &&
    $("#c-printNote").val() == "" &&
    $("#c-receiverAddress").val() == "" &&
    $("#c-receiverName").val() == "" &&
    $("#c-receiverPhone").val() == "" &&
    $("#c-cusName").val() == "" &&
    $("#c-cusPhone").val() == "" &&
    $("#c-cusDob").val() == "" &&
    cart.length == 0)
    ){
        let orderItemDto = [];
        if(cart.length!==0){
            cart.map(it=>{
                let obj = {
                    itemId: it.itemId,
                    sprice: it.sprice,
                    qty: it.qty,
                    sku: it.sku,
                    discount: it.discount
                    
                }
                orderItemDto.push(obj);
            })
        }
        let cusId = null;
        let cusDob = "";
        if(customer!=null) cusId = customer.id;
        if($("#c-cusDob").val()!=""){
            cusDob = moment($("#c-cusDob").val()).format("YYYY-MM-DD");
        }
        let data = JSON.stringify({
            status: 1,
            internalNote: $("#c-internalNote").val(),
            printNote: $("#c-printNote").val(),
            deliveryUnitId: "",
            address: $("#c-receiverAddress").val(),
            receiverName: $("#c-receiverName").val(),
            receiverPhone: $("#c-receiverPhone").val(),
            cusName: $("#c-cusName").val(),
            cusPhone: $("#c-cusPhone").val(),
            cusDob: cusDob,
            discount: discount,
            totalWeight: totalWeight,
            shippingFee: shippingFee,
            totalDiscount: totalDiscount,
            receivedMoney: receivedMoney,
            owe: owe,
            total: totalItems,
            revenue: revenue,
            sales: sales,
            profit: profit,
            cusId: cusId,
            empId: user.id,
            wareId: $("#c-warehouse").val(),
            items: orderItemDto,
        });
        localStorage.setItem("order-draft-data", data);
    }

}
$(window).on( 'load', (event) =>{
    let draftData = JSON.parse(localStorage.getItem("order-draft-data"));
    if(draftData != {} && draftData != null){
        $('#draftModal').modal('show');
    }else{
        cart = [];
        customer = null;
    }
});



$(document).ready(function () {
    window.addEventListener("pagehide",storeDraftData ,false);
    $("#c-warehouse").select2({
        data: $.map(wareData, function(s) {
            return {
                text: s.name,
                id: s.id
            }
        }),
        placeholder: "Chọn kho hàng",
        minimumResultsForSearch: Infinity,
        width: '100%',
        height: '34px',

        dropdownParent: ".ware-c-group"
    });
    //$("#c-warehouse").select2("val",null);

    $("#c-warehouse").on("change", debounce(searchItem,100));
    $("#c-searchbox").on("keyup", debounce(searchItem,500));
    $("#c-checkbox").on("click", debounce(searchItem,100));

    $("#c-cusName").on("keyup click", debounce(function(e) {
        e.preventDefault()
        $("#c-receiverName").val(this.value);
        if(this.value.length>0) searchCustomerByName(this.value);
    },300));

    $("#c-cusPhone").on("keyup click", debounce(function(e) {
        e.preventDefault()
        $("#c-receiverPhone").val(this.value);
        if(this.value.length>2){
            searchCustomerByPhone(this.value);
        }
    },300));

    $(".deli-select").select2({
        width: 'resolve',
        minimumResultsForSearch: Infinity,
        placeholder: "ĐVVC",
        allowClear: true
    });


    $("#c-form").on("submit", async function(e){
        e.preventDefault();
        if(customer == null){
            await fetch("/api/customer",{
                method: "POST",
                credentials: "same-origin",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    name: $("#c-cusName").val(),
                    phone: $("#c-cusPhone").val(),
                    address: $("#c-receiverAddress").val(),
                    dob: $("#c-cusDob").val()
                }),
            })
            .then(response => {
                if(!response.ok) throw Error(response.statusText);
                return response.json();
            })
            .then(data =>{
                if(data.status=="ok") {
                    customer = data.data;
                    // $("#toast-content").html("Chỉnh sửa thành công: # "+data.message);
                    // toast.show();
                }
            })
            .catch(error => console.error(error));
        }
        if(customer!=null){
            if(customer.name.localeCompare($("#cusName").val()) != 0 ||
            customer.phone.localeCompare($("#cusPhone").val()) != 0 ||
            customer.dob.localeCompare($("#cusDob").val()) != 0){
                await fetch("/api/customer/"+customer.id,{
                    method: "PUT",
                    credentials: "same-origin",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                        name: $("#c-cusName").val(),
                        phone: $("#c-cusPhone").val(),
                        address: customer.address,
                        dob: $("#c-cusDob").val()
                    }),
                })
                .then(response => {
                    if(!response.ok) throw Error(response.statusText);
                    return response.json();
                })
                .then(data =>{
                    if(data.status=="ok") {
                        customer = data.data;
                    }
                })
                .catch(error => console.error(error));
            }
            let orderItemDto = [];
            if(cart.length!==0){
                cart.map(it=>{
                    let obj = {
                        itemId: it.itemId,
                        sprice: it.sprice,
                        qty: it.qty,
                        sku: it.sku,
                        discount: it.discount
                    }
                    orderItemDto.push(obj);
                })
            }
            
            await fetch("/api/order",{
                method: "POST",
                credentials: "same-origin",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    status: 1,
                    internalNote: $("#c-internalNote").val(),
                    printNote: $("#c-printNote").val(),
                    deliveryUnitId: "",
                    address: $("#c-receiverAddress").val(),
                    receiverName: $("#c-receiverName").val(),
                    receiverPhone: $("#c-receiverPhone").val(),
                    discount: discount,
                    totalWeight: totalWeight,
                    shippingFee: shippingFee,
                    totalDiscount: totalDiscount,
                    receivedMoney: receivedMoney,
                    owe: owe,
                    total: totalItems,
                    revenue: revenue,
                    sales: sales,
                    profit: profit,
                    empId: user.id,
                    cusId: customer.id,
                    wareId: $("#c-warehouse").val(),
                    items: orderItemDto,
                }),
            })
            .then(response => {
                if(!response.ok) throw Error(response.statusText);
                return response.json();
            })
            .then(data =>{
                if(data.status=="ok") {
                    window.removeEventListener("pagehide", storeDraftData);
                    localStorage.removeItem("order-draft-data"); 
                    window.location.href="/order";
                    // $("#toast-content").html("Chỉnh sửa thành công: # "+data.message);
                    // toast.show();
                }
            })
            .catch(error => console.error(error));
        }

    })
});


function debounce(func, wait, immediate) {
    var timeout;
    return function() {
        var context = this, args = arguments;
        var later = function() {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        var callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
};

function getAjaxResponse( url ){
    let result= jQuery.ajax({
        url: url,
        type: 'get',
        async:false,
        contentType: "application/json",
        dataType: 'json',
        success:function(response){
            if(response.data!== null || response.data!==""){
                return response.data;
            }else{
                return []
            }         
        } 
    }).responseJSON;
    return result.data;
}

function searchItem(){
    let cb = $("#c-checkbox").is(":checked");
    let wh = $("#c-warehouse").select2("data")[0].id;
    let key = $("#c-searchbox").val();
    let searchResult=[];
    let li = "";
    $("#c-searchbox").parent().find('.search-items').remove();
    $("#c-searchbox").parent().append('<div class="search-items" id="listresult"></div>');
    if(key.length>0){
        if(cb==true){
            searchResult = itemData.filter(function(s){
                return s.active == true && s.qty>0 && 
                (s.product.productName.includes(key) || s.product.barcode.includes(key)) &&
                s.warehouse.id == wh;
            })
        }else{
            searchResult = itemData.filter(function(s){
                return s.active == true && 
                (s.product.productName.includes(key) || s.product.barcode.includes(key)) &&
                s.warehouse.id == wh;
            })
        }
        if(searchResult.length>0){
            $("#listresult").empty();
            searchResult.forEach(rs=>{
                $("#listresult").append($(`<div class="s-item media" onclick="addToCart(${rs.id})">
                            <div class="media-img">
                                <img src="/img/product.jpg" width="50" height="50" >
                            </div>
                            <div class="media-body">
                                <div class="media-heading" style="margin-bottom:0px;">
                                    <span class="badge badge-info m-r-5 m-b-5">${rs.product.barcode}</span>
                                    <span class="font-14">${rs.product.productName}</span>
                                </div>
                                <div>
                                <span><i class="fa-solid fa-weight-scale"></i> <span style="color:#0af;">${rs.product.weight} g</span></span>
                                <span style="color:#0af; right:15px; position:absolute;">${numberWithCommas(rs.retailPrice)}đ</span>
                                </div>
                                <div class="font-13">
                                    <span class="m-r-20"><i class="fa-solid fa-warehouse m-r-5"></i><strong>Kho hàng: </strong>${rs.warehouse.name}</span>
                                    <span class="m-r-20"><strong>Tồn kho: </strong>${rs.qty}</span>
                                </div>
                            </div>
                        </div>`));
            })
            //$("#listresult").append(li);
        }else{
            $("#listresult").empty();
            $("#listresult").append($(`<div class="s-item media">
                                        Không tìm thấy kết quả nào
                                    </div>)`));
        }
    }
}

async function searchCustomerByName(key){
    let searchResult=[];
    let li = "";
    $("#c-cusName").parent().find('.cus-search-items').remove();
    $("#c-cusName").parent().append('<div class="cus-search-items" id="cus-n-result"></div>');
    
    if(key.length>0){
        await fetch("/api/customer/searchname?name="+key,{
            method: "GET",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json",
                // 'Content-Type': 'application/x-www-form-urlencoded',
            },
        })
        .then(response => {
            if(!response.ok) throw Error(response.statusText);
            return response.json();
        })
        .then(data =>{
            if(data.status=="ok") {
                searchResult = data.data;
            }else{
                $("#toast-content").html(data.message);
                toast.show();
            }
        })
        .catch(error => console.error(error));
        if(searchResult.length>0){
            $("#cus-n-result").empty();
            searchResult.forEach(rs=>{
                $("#cus-n-result").append($(    `<div class="cus-item media" onclick="selectCustomer(${rs.id})">
                                                    <div class="media-body">
                                                        <div style="margin-bottom:0px;">
                                                            <div>
                                                                <i class="fa-solid fa-address-book"></i>
                                                                <span class="font-14">${rs.name}</span>
                                                            </div>
                                                            <div>
                                                                <i class="fa-solid fa-phone"></i>
                                                                <span class="font-14">${rs.phone}</span>
                                                            </div>
                                                        </div>
                                                        <div>
                                                            <span class="font-14">Số lần mua thành công: ${rs.nsoCus}</span>
                                                        </div>
                                                    </div>
                                                </div>`));
            })
            //$("#listresult").append(li);
        }else{
            $("#cus-n-result").empty();
            // $("#cus-n-result").append($(`<div class="s-item media">
            //                             Không tìm thấy kết quả nào
            //                         </div>)`));
        }
    }
}

async function searchCustomerByPhone(key){
    let searchResult=[];
    let li = "";
    $("#c-cusPhone").parent().find('.cus-search-items').remove();
    $("#c-cusPhone").parent().append('<div class="cus-search-items" id="cus-p-result"></div>');
    
    if(key.length>2){
        await fetch("/api/customer/search?phone="+key,{
            method: "GET",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/json",
            },
        })
        .then(response => {
            if(!response.ok) throw Error(response.statusText);
            return response.json();
        })
        .then(data =>{
            if(data.status=="ok") {
                searchResult = data.data;
            }else{
                $("#toast-content").html(data.message);
                toast.show();
            }
        })
        .catch(error => console.error(error));
        if(searchResult.length>0){
            $("#cus-p-result").empty();
            searchResult.forEach(rs=>{
                $("#cus-p-result").append($(    `<div class="cus-item media" onclick="selectCustomer(${rs.id})">
                                                    <div class="media-body">
                                                        <div style="margin-bottom:0px;">
                                                            <div>
                                                                <i class="fa-solid fa-address-book"></i>
                                                                <span class="font-14">${rs.name}</span>
                                                            </div>
                                                            <div>
                                                                <i class="fa-solid fa-phone"></i>
                                                                <span class="font-14">${rs.phone}</span>
                                                            </div>
                                                        </div>
                                                        <div>
                                                            <span class="font-14">Số lần mua thành công: ${rs.nsoCus}</span>
                                                        </div>
                                                    </div>
                                                </div>`));
            })
        }else{
            $("#cus-p-result").empty();
        }
    }
}

// cart array

function closeAllLists(elmnt, searchItem, searchbox) {
    var x = document.getElementsByClassName(searchItem);
    var inp = document.getElementById(searchbox);
    for (var i = 0; i < x.length; i++) {
    if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
    }
    }
}

document.addEventListener("click", function (e) {
    closeAllLists(e.target, "search-items", "c-searchbox");
    closeAllLists(e.target, "cus-search-items", "c-cusName");
});