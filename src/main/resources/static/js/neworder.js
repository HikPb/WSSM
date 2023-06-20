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
            inventory: item.qty,
            sku: item.sku,
            weight: item.product?.weight,
            barcode: item.product?.barcode,
            productName: item.product?.productName
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
    revenue = 0;
    owe = 0;
    if(discountEl.value!="") discount = parseInt(discountEl.value.replace(/\,/g, ''),10);
    if(shipFeeEl.value!="") shippingFee = parseInt(shipFeeEl.value.replace(/\,/g, ''),10);
    if(reMoneyEl.value!="") receivedMoney = parseInt(reMoneyEl.value.replace(/\,/g, ''),10);

    cart.forEach((item) => {
        totalPrice += item.sprice * item.qty;
        totalItems += item.qty;
        totalDiscount += parseInt(item.discount,10);
        totalWeight += item.weight;
    });

    totalDiscount += discount;
    revenue = totalPrice + shippingFee - totalDiscount;
    owe = totalPrice + shippingFee - totalDiscount - receivedMoney;

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
            cartItemsEl.innerHTML += 
                `<div class="cart-item media">
                    <div style="display:flex; align-item:center;">
                        <div class="media-img">
                            <img src="/img/product.jpg" width="50" height="50" >
                        </div>
                        <div class="media-body">
                            <div class="media-heading" style="margin-bottom:0px;">
                                <span class="badge badge-info m-r-5 m-b-5">${item.barcode}</span>
                                <span class="font-14">${item.productName}</span>
                                <span style="position: absolute; right: 22px;"><i class="fa-regular fa-circle-xmark" style="color: #ff0000;" onclick="removeItemFromCart(${item.itemId})"></i></span>
                            </div>
                            <div style="display:flex; align-items:center;>
                                <span><i class="fa-solid fa-weight-scale"></i> <span style="color:#0af;">${item.weight} g</span></span>
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
    cart = cart.map((item) => {
        let qty = item.qty;
        if (item.itemId === id) {
            if (action === "minus" && qty > 1) {
                qty--;
            } else if (action === "plus" && qty < item.inventory) {
                qty++;
            }
        }
        return {
        ...item,
        qty,
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

    $("#c-cusName").val(customer.name);
    $("#c-cusPhone").val(customer.phone);
    $("#c-cusDob").val(moment(customer.dob).format('YYYY-MM-DD'))
    $("#c-receiverName").val(customer.name);
    $("#c-receiverPhone").val(customer.phone);
    $("#c-receiverAddress").val(customer.address);
}

$(document).ready(function () {
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
    $("#c-warehouse").select2("val",null);

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


    $("#c-submit-btn").on("click", async function(e){
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
                    revenue: totalPrice,
                    sales: totalPrice,
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
