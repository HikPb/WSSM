package com.project.wsms.dto;

import com.project.wsms.model.CqItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CqItemDto {
    private Integer id;
    private Integer itemId;
    private Integer bfQty;
    private Integer crQty;
    private String sku;

    public CqItem convertToEntity(){
        CqItem newCqItem = new CqItem();
        newCqItem.setId(this.id);
        newCqItem.setBf_qty(this.bfQty);
        newCqItem.setSku(this.sku);
        newCqItem.setCr_qty(this.crQty);
        return newCqItem;
    }

    public void convertToDto(CqItem item){
        this.id = item.getId();
        this.bfQty = item.getBf_qty();
        this.crQty = item.getCr_qty();
        this.sku = item.getSku();
    }
}
