package com.project.wsms.dto;

import java.util.Date;
import java.util.Set;

import com.project.wsms.model.Import;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class ImportDto {
    private Integer id;
    private String note;
    private Integer status;
    private Integer supId;
    private Integer wareId;
    private Integer empId;
    private Integer totalQty;
    private Integer totalMoney;
    private Integer shipFee;
    private Date expectedAt;
    private Set<ImportItemDto> items;

    public Import convertToEntity(){
        Import newItem = new Import();
        newItem.setNote(this.note);
        newItem.setStatus(this.status);
        newItem.setExpected_at(this.expectedAt);
        newItem.setTotalQty(this.totalQty);
        newItem.setTotalMoney(this.totalMoney);
        newItem.setShipFee(this.shipFee);
        return newItem;
    }

    public void convertToDto(Import item){
        this.id = item.getId();
        this.status = item.getStatus();
        this.note = item.getNote();
        this.expectedAt = item.getExpected_at();
        this.totalQty = item.getTotalQty();
        this.totalMoney = item.getTotalMoney();
        this.shipFee = item.getShipFee();
    }
}
