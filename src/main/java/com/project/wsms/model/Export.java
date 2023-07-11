package com.project.wsms.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
@Table(name = "export")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Export extends AuditModel{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ware_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference(value = "warehouse-export")
	@JsonIgnoreProperties(value = {
		"createdAt", "updatedAt", "hibernateLazyInitializer"})
	private Warehouse warehouse;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "emp_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference(value = "emp-export")
	@JsonIgnoreProperties(value = {
		"roles", "fullname", "password", "phone", "hibernateLazyInitializer"})
	private Employee employee;
	
	@Column(name = "note", nullable = true, columnDefinition="TEXT")
	private String note;
	@Column(name = "status", nullable = false)
	private Integer status;
	@Column(name = "total_qty", nullable = true)
	private Integer totalQty;
	@Column(name = "total_money", nullable = true)
	private Integer totalMoney;
	@Column(name = "expected_at", nullable = true)
	private Date expected_at;

	@OneToMany(mappedBy = "exportProduct")
	private Set<ExportItem> items = new HashSet<>();

	public void addItem(ExportItem item) {
		this.items.add(item);
		item.setExportProduct(this);
	}
	
	public void removeItem(Integer itemId) {
		ExportItem item = this.items.stream().filter(c -> Objects.equals(c.getId(), itemId)).findFirst().orElse(null);
		if(item!=null) {
			this.items.remove(item);
		}
	}
}
