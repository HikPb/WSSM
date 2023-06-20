package com.project.wsms.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
// @JsonIdentityInfo(
//         generator = ObjectIdGenerators.PropertyGenerator.class,
//         property = "id")
public class Product extends AuditModel {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "product_name", nullable = false, length = 50)
	private String productName;
	@Column(name = "barcode", nullable = false, length = 8)
	private String barcode;
	
	@Column(name = "weight", nullable = false)
	private Float weight;
	@Column(name = "total_import", nullable = true)
	private Integer tImport;
	@Column(name = "total_sale", nullable = true)
	private Integer tSale;
	@Column(name = "total_inventory", nullable = true)
	private Integer tInventory;
	
	//@JsonIgnore
	@JsonBackReference(value = "product-item")
	@OneToMany(mappedBy="product")
	private Set<Item> items = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "product_category", 
		joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false), 
		inverseJoinColumns = @JoinColumn(name = "cate_id", referencedColumnName = "id", nullable = false))
	private Set<Category> categories = new HashSet<>();
		
	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "product_supplier", 
		joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false), 
		inverseJoinColumns = @JoinColumn(name = "sup_id", referencedColumnName = "id", nullable = false))
	@JsonIgnoreProperties(value = {
		"createdAt", "updatedAt", "hibernateLazyInitializer"}) 	
	private Set<Supplier> suppliers = new HashSet<>();

	@Column(name = "note", nullable = true, columnDefinition="TEXT")
	private String note;

	@Column(name = "link", nullable = true, columnDefinition="TEXT")
	private String link;

	public void addItem(Item item) {
		this.items.add(item);
	}
	
	public void addCategory(Category category) {
		this.categories.add(category);
		category.getProducts().add(this);
	}
	
	public void removeCategory(Integer cateId) {
		Category category = this.categories.stream().filter(c -> Objects.equals(c.getId(), cateId)).findFirst().orElse(null);
		if(category!=null) {
			this.categories.remove(category);
			category.getProducts().remove(this);
		}
	}
	
	public void resetCategories() {
		this.categories.forEach(c->
			c.getProducts().remove(this)
		);
		this.categories.clear();
	}

	public void addSupplier(Supplier sup) {
		this.suppliers.add(sup);
		sup.getProducts().add(this);
	}
	
	public void removeSupplier(Integer supId) {
		Supplier sup = this.suppliers.stream().filter(c -> Objects.equals(c.getId(), supId)).findFirst().orElse(null);
		if(sup!=null) {
			this.suppliers.remove(sup);
			sup.getProducts().remove(this);
		}
	}
	
	public void resetSuppliers() {
		this.suppliers.forEach(c->
			c.getProducts().remove(this)
		);
		this.suppliers.clear();
	}
	
}
