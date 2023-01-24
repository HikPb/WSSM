package com.project.wsms.collection;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orderdetails")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetail {
	
	@Id
	private String orderdetailId;
	private String orderId;
	private String productId;
	private String sku;
	private Integer quantity;
	private Integer currentSellPrice;
	private Integer importedPrice;
	private Integer totalWeight;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	private Integer discount;
	private Boolean isSell;
}
