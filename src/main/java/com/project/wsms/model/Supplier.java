package com.project.wsms.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	@JsonIgnore
	@OneToMany(mappedBy="supplier")
	private List<Item> items;
	
	/*
	 * @JsonManagedReference public List<Item> getItems () { return this.items; }
	 */
	
	public void addItem(Item item) {
		this.items.add(item);
	}

}
