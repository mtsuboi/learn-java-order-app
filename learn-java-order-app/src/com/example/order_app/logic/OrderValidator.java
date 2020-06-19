package com.example.order_app.logic;

import java.util.List;

import com.example.order_app.dto.OrderForm;

public interface OrderValidator {

	// 受注フォームを入力検証する
	List<String> validate(OrderForm orderForm);
}
