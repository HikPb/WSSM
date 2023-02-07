package com.project.wsms.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	
	@Column(name = "address", nullable = true)
	private String address;
	
	@OneToMany(mappedBy="customer")
	private Set<Order> orders;
	
	private Integer npCus; //number of purchases
	private Integer nsoCus; //number of successful orders
	
	public void addOrder(Order order) {
		this.orders.add(order);
	}
}
