package com.project.wsms.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "warehouses")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

	@JsonIgnore
	@OneToMany(mappedBy = "warehouse")
	private Set<Export> exports = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "warehouse")
	private Set<Import> imports = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "warehouse")
	private Set<CheckQty> checkqtys = new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "warehouse")
	private Set<Item> items = new HashSet<>();

	public Set<Item> getItems() {
		return this.items;
	}

	public void addImports(Import item) {
		this.imports.add(item);
	}
	public void addExports(Export item) {
		this.exports.add(item);
	}
	public void addCheckQtys(CheckQty item) {
		this.checkqtys.add(item);
	}
	public void addItem(Item item) {
		this.items.add(item);
	}
}
