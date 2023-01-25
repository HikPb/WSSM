package com.project.wsms.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cusId;
	@Column(name = "cus_name", nullable = false)
	private String cusName;
	@Column(name = "cus_phone", nullable = false)
	private String cusPhone;
	
	private List<Address> cusAddress;
	private Integer npCus; //number of purchases
	private Integer nsoCus; //number of successful orders
	
}
