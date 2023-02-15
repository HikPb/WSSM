package com.project.wsms.dto;

import com.project.wsms.model.ExportItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportItemDto {
    private Integer id;
    private Integer itemId;
    private Integer sprice;
    private Integer qty;
    private String sku;

    public ExportItem convertToEntity(){
        ExportItem newExportItem = new ExportItem();
        newExportItem.setId(this.id);
        newExportItem.setRetailPrice(this.sprice);
        newExportItem.setSku(this.sku);
        newExportItem.setQty(this.qty);
        return newExportItem;
    }

    public void convertToDto(ExportItem item){
        this.id = item.getId();
        this.sprice = item.getRetailPrice();
        this.sku = item.getSku();
        this.qty = item.getQty();
    }
}
