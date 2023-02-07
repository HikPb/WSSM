package com.project.wsms.model;

import java.util.Set;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer role;
	private String username;
	private String fullname;
	private String phone;
	
	@OneToMany(mappedBy="employee")
	private Set<Order> orders;
	
	public void addOrder(Order order) {
		this.orders.add(order);
	}

}
