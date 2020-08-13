package com.example.order_app.access;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.example.order_app.constants.OrderStatus;
import com.example.order_app.dto.Order;

public interface OrderAccessor {
	// 受注をステータス指定で抽出する
	public List<Order> findByStatus(final OrderStatus orderStatus);

	// 受注をOrder_idで検索する
	public Order findById(final String orderId);

	// Order_idを採番する
	public String numberingOrderId() throws SQLException;

	// 受注を追加する
	public void add(final Order order) throws SQLException;

	// 受注ステータスと出荷日を更新する
	public int update(final String orderId, final OrderStatus orderStatus, final Date shipDate) throws SQLException;

	// 受注を削除する
	public void delete(final String orderId) throws SQLException;

}
