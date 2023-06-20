package com.project.wsms.dto;

import java.util.Date;
import java.util.Set;

import com.project.wsms.model.Export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ExportDto {
    private Integer id;
    private String note;
    private Integer status;
    private Integer wareId;
    private Integer empId;
    private Integer totalQty;
    private Integer totalMoney;
    private Date expectedAt;
    private Set<ExportItemDto> items;

    public Export convertToEntity(){
        Export newItem = new Export();
        newItem.setNote(this.note);
        newItem.setStatus(this.status);
        newItem.setTotalQty(this.totalQty);
        newItem.setTotalMoney(this.totalMoney);
        newItem.setExpected_at(this.expectedAt);
        return newItem;
    }

    public void convertToDto(Export item){
        this.id = item.getId();
        this.status = item.getStatus();
        this.note = item.getNote();
        this.totalQty = item.getTotalQty();
        this.totalMoney = item.getTotalMoney();
        this.expectedAt = item.getExpected_at();
    }
}
