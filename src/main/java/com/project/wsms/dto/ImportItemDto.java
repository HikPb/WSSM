package com.project.wsms.dto;

import com.project.wsms.model.ImportItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportItemDto {
    private Integer id;
    private Integer pprice;
    private Integer qty;
    private String sku;
    private Integer itemId;

    public ImportItem convertToEntity(){
        ImportItem newImportItem = new ImportItem();
        newImportItem.setId(this.id);
        newImportItem.setPurcharsePrice(this.pprice);
        newImportItem.setSku(this.sku);
        newImportItem.setQty(this.qty);
        return newImportItem;
    }

    public void convertToDto(ImportItem item){
        this.id = item.getId();
        this.pprice = item.getPurcharsePrice();
        this.sku = item.getSku();
        this.qty = item.getQty();
    }
}
