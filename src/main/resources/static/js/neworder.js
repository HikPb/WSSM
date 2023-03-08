const toast = new bootstrap.Toast($("#toast"));
//const wareData = getAjaxResponse("/api/warehouse");
//const itemData = getAjaxResponse("/api/item");
//const customerData = getAjaxResponse("/api/customer");

const cartItemsEl = document.getElementById("order-items");
const searchbox = document.getElementById("c-searchbox");

const shipFeeEl = document.getElementById("shippingFee");
const discountEl = document.getElementById("discount");
const reMoneyEl = document.getElementById("receivedMoney");

const totalTEl = document.getElementById("total");
const totalDisTEl = document.getElementById("totalDiscount");
const paymentTEl = document.getElementById("payment");
const receivedTEl = document.getElementById("received");
const oweTEl = document.getElementById("owe");

const subTotalEl = document.getElementById("subTotal");
const codEl = document.getElementById("cod");

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
    let totalPrice = 0,
        totalItems = 0;
        totalDiscount = 0;
        
    discount = parseInt(discountEl.value.replace(/\,/g, ''),10);
    shippingFee = parseInt(shipFeeEl.value.replace(/\,/g, ''),10);
    receivedMoney = parseInt(reMoneyEl.value.replace(/\,/g, ''),10);

    cart.forEach((item) => {
        totalPrice += item.sprice * item.qty;
        totalItems += item.qty;
        totalDiscount += parseInt(item.discount,10);
    });

    totalPrice += shippingFee;
    totalDiscount += discount;

    totalTEl.textContent = `${numberWithCommas(totalPrice)}`;
    totalDisTEl.textContent = `${numberWithCommas(totalDiscount)}`;
    paymentTEl.textContent = `${numberWithCommas(totalPrice - totalDiscount)}`;
    receivedTEl.textContent = `${numberWithCommas(receivedMoney)}`;
    oweTEl.textContent = `${numberWithCommas(totalPrice - totalDiscount - receivedMoney)}`;
    
    subTotalEl.textContent = `${numberWithCommas(totalPrice)}`;
    codEl.textContent = `${numberWithCommas(totalPrice - totalDiscount - receivedMoney)}`;
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
                                <button class="hide-btn btn-close" onclick="removeItemFromCart(${item.itemId})"></button>
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

$(document).ready(function () {
    $("#c-warehouse").select2({
        data: $.map(wareData, function(s) {
            return {
                text: s.name,
                id: s.id
            }
        }),
        placeholder: "Chọn kho hàng",
        width: '100%',
        height: '34px',
        dropdownParent: ".ware-c-group"
    });
    $("#c-warehouse").select2("val",null);

    $("#c-warehouse").on("change", debounce(searchItem,100));
    $("#c-searchbox").on("keyup", debounce(searchItem,500));
    $("#c-checkbox").on("click", debounce(searchItem,100));


    $(".deli-select").select2({
        width: 'resolve',
        minimumResultsForSearch: Infinity,
        placeholder: "ĐVVC",
        allowClear: true
    });

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

// cart array
//let cart = JSON.parse(localStorage.getItem("CART")) || [];
let cart = [];
updateCart();



function closeAllLists(elmnt) {
    var x = document.getElementsByClassName("search-items");
    var inp = document.getElementById("c-searchbox");
    for (var i = 0; i < x.length; i++) {
    if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
    }
    }
}

document.addEventListener("click", function (e) {
    closeAllLists(e.target);
});
