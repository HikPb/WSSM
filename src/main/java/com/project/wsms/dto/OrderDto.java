package com.project.wsms.dto;

import java.util.Set;

import com.project.wsms.model.Category;
import com.project.wsms.model.Order;
import com.project.wsms.model.Supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class OrderDto {
    private Integer id;
    private Integer status;

    private String internalNote;
    private String printNote;

    private String deliveryUnitId;
    
    private String address;
    private String receiverName;
    private String receiverPhone;

    private Integer totalWeight;
	private Integer shippingFee; //tien ship
	private Integer totalDiscount; // tong tien giam gia
	private Integer receivedMoney; // tien da thanh toan truoc, dat coc vv
	private Integer owe;	// tien khach con nợ
	private Integer total;
	private Integer revenue; // doanh thu
	private Integer sales;

    private Integer emId;
    private Integer cusId;
    private Set<OrderItemDto> items;

    public Order convertToEntity(){
        Order newItem = new Order();
        newItem.setId(this.id);
        newItem.setStatus(this.status);
        newItem.setPrintedNote(this.printNote);
        newItem.setInternalNote(this.internalNote);
        newItem.setDeliveryUnitId(this.deliveryUnitId);
        newItem.setAddress(this.address);
        newItem.setReceiverName(this.receiverName);
        newItem.setReceiverPhone(this.receiverPhone);
        newItem.setTotalWeight(this.totalWeight);
        newItem.setShippingFee(this.shippingFee);
        newItem.setTotalDiscount(this.totalDiscount);
        newItem.setReceivedMoney(this.receivedMoney);
        newItem.setOwe(this.owe);
        newItem.setTotal(this.total);
        newItem.setRevenue(this.revenue);
        newItem.setSales(this.sales);
        return newItem;
    }

    public void convertToDto(Order item){
        this.id = item.getId();
        this.status = item.getStatus();
        this.printNote = item.getPrintedNote();
        this.internalNote = item.getInternalNote();
        this.deliveryUnitId = item.getDeliveryUnitId();
        this.address = item.getAddress();
        this.receiverName = item.getReceiverName();
        this.receiverPhone = item.getReceiverPhone();
        this.totalWeight = item.getTotalWeight();
        this.shippingFee = item.getShippingFee();
        this.totalDiscount = item.getTotalDiscount();
        this.receivedMoney = item.getReceivedMoney();
        this.owe = item.getOwe();
        this.total = item.getTotal();
        this.revenue = item.getRevenue();
        this.sales = item.getSales();
    }
}
