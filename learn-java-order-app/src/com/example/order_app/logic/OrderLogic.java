package com.example.order_app.logic;

import java.sql.SQLException;
import java.util.List;

import com.example.order_app.constants.OrderStatus;
import com.example.order_app.dto.Order;
import com.example.order_app.dto.OrderForm;

public interface OrderLogic {
	// 受注をステータス指定で抽出する
	List<Order> findByStatus(final OrderStatus orderStatus);

	// 受注をOrder_idで検索する
	public OrderForm findById(final String orderId);

	// 受注を保存する
	void save(final OrderForm orderForm) throws SQLException;

	// 受注を削除する
	void delete(final String orderId) throws SQLException;
}
