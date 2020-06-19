package com.example.order_app.dto;

public class OrderFormDetail {
	private String orderDetailNo;
	private String itemId;
	private String itemName;
	private String itemPrice;
	private String orderQuantity;
	private String orderDetailAmount;

	public String getOrderDetailNo() {
		return orderDetailNo;
	}
	public void setOrderDetailNo(String orderDetailNo) {
		this.orderDetailNo = orderDetailNo;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(String orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public String getOrderDetailAmount() {
		return orderDetailAmount;
	}
	public void setOrderDetailAmount(String orderDetailAmount) {
		this.orderDetailAmount = orderDetailAmount;
	}

}
