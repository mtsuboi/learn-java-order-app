package com.example.order_app.access;

import java.sql.SQLException;
import java.util.List;

import com.example.order_app.dto.OrderDetail;

public interface OrderDetailAccessor {
	// 受注明細をOrder_idで検索する
	public List<OrderDetail> findById(final String orderId);

	// 受注明細を追加する
	public void add(final OrderDetail orderDetail) throws SQLException;

	// 受注明細を更新する
	//public int update(final Order order);

	// 受注明細をOrderIdごと削除する
	public void deleteByOrderId(final String orderId) throws SQLException;
}
