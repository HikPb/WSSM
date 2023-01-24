package com.project.wsms.collection;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.message.StringFormattedMessage;
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
@Document(collection = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

	@Id
	private String orderId;
	private String cusId;
//	private String sessionId;
//	private String token;
	private String employeeId;
	
	private Integer status;
	private List<String> listOrderDetailId;
	private String deliveryUnitId;
	
	private Integer totalOrderWeight;
	private Integer shippingFee; //tien ship
	private Integer totalDiscount; // tong tien giam gia
	private Integer receivedMoney; // tien da thanh toan truoc, dat coc vv
	private Integer owe;	// tien khach con nợ
	private Integer total;
	private Integer revenue; // doanh thu
	private Integer sales; // doanh so
	
	private String receiverName;
	private String receiverPhone;
	private Address orderAddress;
	
	private String internalNote;
	private String printedNote;
	
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
}
