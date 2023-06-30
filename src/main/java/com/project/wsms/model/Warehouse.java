package com.project.wsms.model;

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
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "warehouses")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Warehouse extends AuditModel {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "phone", nullable = false)
	private String phone;

	@Column(name = "address", nullable = true)
	private String address;

	@JsonBackReference(value = "warehouse-export")
	@JsonIgnore
	@OneToMany(mappedBy = "warehouse")
	private Set<Export> exports = new HashSet<>();

	@JsonBackReference(value = "warehouse-import")
	@JsonIgnore
	@OneToMany(mappedBy = "warehouse")
	private Set<Import> imports = new HashSet<>();

	@JsonBackReference(value = "warehouse-checkqty")
	@JsonIgnore
	@OneToMany(mappedBy = "warehouse")
	private Set<CheckQty> checkqtys = new HashSet<>();
	
	@JsonBackReference(value = "warehouse-item")
	@JsonIgnore
	@OneToMany(mappedBy = "warehouse")
	private Set<Item> items = new HashSet<>();

	@Transient
	@JsonIgnore
	@JsonBackReference(value = "warehouse-order")
	@OneToMany(mappedBy = "warehouse")
	private Set<Order> orders = new HashSet<>();

	public void addImport(Import item) {
		this.imports.add(item);
		item.setWarehouse(this);
	}
	public void addExport(Export item) {
		this.exports.add(item);
		item.setWarehouse(this);
	}
	public void addCheckQty(CheckQty item) {
		this.checkqtys.add(item);
		item.setWarehouse(this);
	}
	public void addItem(Item item) {
		this.items.add(item);
		item.setWarehouse(this);
	}
	public void addOrder(Order item) {
		this.orders.add(item);
		item.setWarehouse(this);
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
	public void removeItem(Integer id) {
		Item item = this.items.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst().orElse(null);
		if(item!=null) {
			this.items.remove(item);
		}
	}
	public void removeOrder(Integer id) {
		Order item = this.orders.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst().orElse(null);
		if(item!=null) {
			this.orders.remove(item);
		}
	}
	
}
