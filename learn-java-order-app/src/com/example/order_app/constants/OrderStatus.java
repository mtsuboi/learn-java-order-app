package com.example.order_app.constants;

public enum OrderStatus {
	ORDER("1","受注"),
	SHIPPING("2","出荷作業中"),
	SHIPPED("3","出荷済"),
	CANCEL("9","キャンセル");

	private final String code;
	private final String name;

	private OrderStatus(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}

	public static OrderStatus getByCode(String code) {
		for (OrderStatus orderStatus : OrderStatus.values()) {
		      if (orderStatus.code.equals(code)){
		        return orderStatus;
		      }
		}
		throw new IllegalArgumentException();
	}
}
