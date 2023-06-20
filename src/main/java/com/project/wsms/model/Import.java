package com.project.wsms.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "import")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Import extends AuditModel{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ware_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference(value = "warehouse-import")
	@JsonIgnoreProperties(value = {
		"createdAt", "updatedAt", "phone", "address", "hibernateLazyInitializer"}) 
	private Warehouse warehouse;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sup_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference(value = "supplier-import")
	@JsonIgnoreProperties(value = {
		"createdAt", "updatedAt", "supPhone", "address", "hibernateLazyInitializer"}) 
	private Supplier supplier;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference(value = "emp-import")
	@JsonIgnoreProperties(value = {
		"role", "fullname", "phone", "hibernateLazyInitializer"})
	private Employee employee;

	@OneToMany(mappedBy = "importProduct")
	private Set<ImportItem> items = new HashSet<>();
	
	@Column(name = "note", nullable = true, columnDefinition="TEXT")
	private String note;
	@Column(name = "status", nullable = false)
	private Integer status;
	@Column(name = "total_qty", nullable = true)
	private Integer totalQty;
	@Column(name = "total_money", nullable = true)
	private Integer totalMoney;
	@Column(name = "ship_fee", nullable = true)
	private Integer shipFee;
	
	@Column(name = "expected_at", nullable = true)
	private Date expected_at;

	public void addItem(ImportItem item) {
		this.items.add(item);
		item.setImportProduct(this);
	}
	
	public void removeItem(Integer itemId) {
		ImportItem item = this.items.stream().filter(c -> Objects.equals(c.getId(), itemId)).findFirst().orElse(null);
		if(item!=null) {
			this.items.remove(item);
		}
	}
}
