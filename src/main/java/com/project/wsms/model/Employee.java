package com.project.wsms.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
 	@JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false), 
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false))
  	private Set<Role> roles = new HashSet<>();
	
	@NotBlank
  	@Size(max = 20)
	private String username;
	private String fullname;
	private String phone;
	@NotBlank
  	@Size(max = 120)
	private String password;

	@JsonBackReference(value = "emp-mess-sender")
	@JsonIgnore
	@OneToMany(mappedBy="sender")
	private Set<Message> sentMess  = new HashSet<>();

	@JsonBackReference(value = "emp-mess-recipient")
	@JsonIgnore
	@OneToMany(mappedBy="recipient")
	private Set<Message> receivedMess  = new HashSet<>();
	
	@JsonBackReference(value = "emp-order")
	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private Set<Order> orders = new HashSet<>();
	
	@JsonBackReference(value = "emp-import")
	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private Set<Import> imports  = new HashSet<>();
	
	@JsonBackReference(value = "emp-export")
	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private Set<Export> exports  = new HashSet<>();
	
	@JsonBackReference(value = "emp-checkqty")
	@JsonIgnore
	@OneToMany(mappedBy="employee")
	private Set<CheckQty> checkqtys = new HashSet<>();
	
	public void addSentMess(Message mess) {
		this.sentMess.add(mess);
		mess.setSender(this);
	}

	public void addReceivedMess(Message mess) {
		this.receivedMess.add(mess);
		mess.setRecipient(this);
	}

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
