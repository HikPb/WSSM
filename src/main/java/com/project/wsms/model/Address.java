package com.project.wsms.model;

import lombok.Data;

@Entity
@Builder
@Setter(value = AccessLevel.PUBLIC)
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "products")
public class Address {
	private String fulladdress;
	private String others;
	private String city;
	private String district;
	private String wards;
	
	public void fullAddress() {
		String fulladdress= this.others +", "+ this.wards +", "+ this.district +", "+ this.city;
		this.setFulladdress(fulladdress);
	}
}