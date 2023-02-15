package com.project.wsms.dto;

import java.util.Date;
import java.util.Set;

import com.project.wsms.model.CheckQty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CheckQtyDto {
    private Integer id;
    private String note;
    private Date checkqtyAt;
    private Integer status;
    private Integer wareId;
    private Integer empId;
    private Set<CqItemDto> items;

    public CheckQty convertToEntity(){
        CheckQty newItem = new CheckQty();
        newItem.setNote(this.note);
        newItem.setStatus(this.status);
        newItem.setCheckqtyAt(this.checkqtyAt);
        return newItem;
    }

    public void convertToDto(CheckQty item){
        this.id = item.getId();
        this.status = item.getStatus();
        this.note = item.getNote();
        this.checkqtyAt = item.getCheckqtyAt();
    }
}
