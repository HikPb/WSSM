package com.project.wsms.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "cus_name", nullable = false, length = 50)
	private String name;
	@Column(name = "cus_phone", nullable = false, length = 15)
	private String phone;
	@Column(name = "dob", nullable = true)
	private Date dob;
	@Column(name = "address", nullable = true)
	private String address;
	@Column(name = "npcus", nullable = true)
	private Integer npCus = 0; //number of purchases
	@Column(name = "nsocus", nullable = true)
	private Integer nsoCus = 0; //number of successful orders
	@Column(name = "nrocus", nullable = true)
	private Integer nroCus = 0; //number of refuned orders
	@Column(name = "total_money", nullable = true)
	private Integer tmoney = 0;
	@Column(name = "total_owe", nullable = true)
	private Integer towe = 0;
	
	@JsonBackReference(value = "customer-order")
	@JsonIgnore
	@OneToMany(mappedBy="customer")
	private Set<Order> orders = new HashSet<>();
	
	public void addOrder(Order order) {
		this.orders.add(order);
		order.setCustomer(this);
	}

	public void removeOrder(Integer id) {
		Order item = this.orders.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst().orElse(null);
		if(item!=null) {
			this.orders.remove(item);
		}
	}


}
