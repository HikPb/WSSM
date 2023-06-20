package com.project.wsms.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Getter
@Setter
@AllArgsConstructor 
@NoArgsConstructor
@Table(name = "employee")
//@JsonIgnoreProperties({ "password" })

public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	private String username;
	private String fullname;
	private String phone;
	private String password;
	
	//@JsonBackReference(value = "emp-order")
	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private Set<Order> orders = new HashSet<>();
	
	//@JsonBackReference(value = "emp-import")
	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private Set<Import> imports  = new HashSet<>();
	
	//@JsonBackReference(value = "emp-export")
	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private Set<Export> exports  = new HashSet<>();
	
	//@JsonBackReference(value = "emp-checkqty")
	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private Set<CheckQty> checkqtys = new HashSet<>();
	
	public void addOrder(Order order) {
		this.orders.add(order);
		order.setEmployee(this);
	}

	public void addImport(Import ip) {
		this.imports.add(ip);
		ip.setEmployee(this);
	}

	public void addExport(Export export) {
		this.exports.add(export);
		export.setEmployee(this);
	}

	public void addCheckQty(CheckQty cq) {
		this.checkqtys.add(cq);
		cq.setEmployee(this);
	}

	public void removeImport(Integer id) {
		Import item = this.imports.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst().orElse(null);
		if(item!=null) {
			this.imports.remove(item);
		}
	}
	public void removeExport(Integer id) {
		Export item = this.exports.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst().orElse(null);
		if(item!=null) {
			this.exports.remove(item);
		}
	}
	public void removeCheckQty(Integer id) {
		CheckQty item = this.checkqtys.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst().orElse(null);
		if(item!=null) {
			this.checkqtys.remove(item);
		}
	}
	public void removeOrder(Integer id) {
		Order item = this.orders.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst().orElse(null);
		if(item!=null) {
			this.orders.remove(item);
		}
	}

}
