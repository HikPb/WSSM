package com.project.wsms.dto;

import com.project.wsms.model.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Integer id;
    private Integer wareId;
    private Integer pprice;
    private Integer sprice;
    private Integer qty;
    private String sku;
    private boolean active;

    public Item convertToEntity(){
        Item newItem = new Item();
        newItem.setActive(this.active);
        newItem.setId(this.id);
        newItem.setPurcharsePrice(this.pprice);
        newItem.setRetailPrice(this.sprice);
        newItem.setSku(this.sku);
        newItem.setQty(this.qty);
        return newItem;
    }

    public void convertToDto(Item item){
        this.id = item.getId();
        this.active = item.isActive();
        this.pprice = item.getPurcharsePrice();
        this.sprice = item.getRetailPrice();
        this.sku = item.getSku();
        this.qty = item.getQty();
    }
}
