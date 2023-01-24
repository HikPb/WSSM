package com.project.wsms.collection;

import lombok.Data;

@Data
public class Address {
	private String fulladdress;
	private String others;
	private String city;
	private String district;
	private String wards;
	
	public void makeFullAddress() {
		String fulladdress= this.others +", "+ this.wards +", "+ this.district +", "+ this.city;
		this.setFulladdress(fulladdress);
	}
}