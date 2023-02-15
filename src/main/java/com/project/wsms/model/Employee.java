package com.project.wsms.model;

import java.util.HashSet;
import java.util.Set;

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
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String role;
	private String username;
	private String fullname;
	private String phone;
	private String password;
	
	@OneToMany(mappedBy="employee")
	private Set<Order> orders  = new HashSet<>();
	
	@OneToMany(mappedBy="employee")
	private Set<Import> imports  = new HashSet<>();
	
	@OneToMany(mappedBy="employee")
	private Set<Export> exports  = new HashSet<>();
	
	@OneToMany(mappedBy="employee")
	private Set<CheckQty> checkqty = new HashSet<>();
	
	public void addOrder(Order order) {
		this.orders.add(order);
	}

	public void addImport(Import ip) {
		this.imports.add(ip);
	}

	public void addExport(Export export) {
		this.exports.add(export);
	}

	public void addCheckQty(CheckQty cq) {
		this.checkqty.add(cq);
	}

}
