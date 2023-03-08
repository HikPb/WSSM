package com.project.wsms.model;

import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends AuditModel{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cus_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Employee employee;
	
	private Integer status;
	
	private String deliveryUnitId;
	
	private Integer totalWeight;
	private Integer shippingFee; //tien ship
	private Integer totalDiscount; // tong tien giam gia
	private Integer receivedMoney; // tien da thanh toan truoc, dat coc vv
	private Integer owe;	// tien khach con nợ
	private Integer total;
	private Integer revenue; // doanh thu
	private Integer sales; // doanh so
	
	private String receiverName;
	private String receiverPhone;
	@Column(name = "address", nullable = true)
	private String address;

	private String internalNote;
	private String printedNote;	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ware_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Warehouse warehouse;
	
	@OneToMany(mappedBy="order")
	private Set<OrderItem> orderItems;
	
	public void addOrderItem(OrderItem item) {
		this.orderItems.add(item);
	}
	
	public void removeOrderItem(Integer itemId) {
		OrderItem item = this.orderItems.stream().filter(c -> Objects.equals(c.getId(), itemId)).findFirst().orElse(null);
		if(item!=null) {
			this.orderItems.remove(item);
		}
	}
	
}
