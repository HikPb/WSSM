package com.project.wsms.dto;

import com.project.wsms.model.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Integer id;
    private Integer itemId;
    private Integer sprice;
    private Integer qty;
    private String sku;
    private Integer discount;

    public OrderItem convertToEntity(){
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setDiscount(this.discount);
        newOrderItem.setId(this.id);
        newOrderItem.setPrice(this.sprice);
        newOrderItem.setSku(this.sku);
        newOrderItem.setQty(this.qty);
        return newOrderItem;
    }

    public void convertToDto(OrderItem item){
        this.id = item.getId();
        this.discount = item.getDiscount();
        this.sprice = item.getPrice();
        this.sku = item.getSku();
        this.qty = item.getQty();
    }
}
