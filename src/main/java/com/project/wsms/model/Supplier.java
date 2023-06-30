package com.project.wsms.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "suppliers")
public class Supplier extends AuditModel {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "sup_name", nullable = false)
	private String supName;
	@Column(name = "sup_phone", nullable = false)
	private String supPhone;
	@Column(name = "sup_address", nullable = true)
	private String address;

	@ManyToMany(fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			},
			mappedBy = "suppliers")
	@JsonIgnore
	private Set<Product> products = new HashSet<>();

	@JsonBackReference(value = "supplier-import")
	@JsonIgnore
	@OneToMany(mappedBy="supplier")
	private Set<Import> importItems = new HashSet<>();

	public void addImportItem(Import item) {
		this.importItems.add(item);
		item.setSupplier(this);
	}

	public void removeItem(Integer itemId) {
		Import item = this.importItems.stream().filter(c -> Objects.equals(c.getId(), itemId)).findFirst().orElse(null);
		if(item!=null) {
			this.importItems.remove(item);
		}
	}
}
