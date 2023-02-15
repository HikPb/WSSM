package com.project.wsms.dto;

import java.util.Set;

import com.project.wsms.model.Category;
import com.project.wsms.model.Product;
import com.project.wsms.model.Supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ProductDto {
    private Integer id;
    private String productName;
    private String barcode;
	private Float weight;
    private String link;
    private String note;
    private Integer supId;
    private Set<ItemDto> items;
    private Set<Category> categories;
    private Set<Supplier> suppliers;

    public Product convertToEntity(){
        Product newItem = new Product();
        newItem.setBarcode(this.barcode);
        newItem.setId(this.id);
        newItem.setProductName(this.productName);
        newItem.setWeight(this.weight);
        newItem.setLink(this.link);
        newItem.setNote(this.note);
        return newItem;
    }

    public void convertToDto(Product item){
        this.id = item.getId();
        this.productName = item.getProductName();
        this.barcode = item.getBarcode();
        this.weight = item.getWeight();
        this.link = item.getLink();
        this.note = item.getNote();
    }
}
