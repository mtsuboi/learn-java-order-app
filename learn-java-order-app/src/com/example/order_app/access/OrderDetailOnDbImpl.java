package com.example.order_app.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.example.order_app.dto.OrderDetail;

public class OrderDetailOnDbImpl implements OrderDetailAccessor {
	private DataSource ds;

	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
	}

	private Connection getConnection() throws SQLException {
		return getDs().getConnection();
	}

	@Override
	public List<OrderDetail> findById(String orderId) {
		// 受注明細をorder_idで抽出する
		String sql = "SELECT order_id, order_detail_no, item_id, item_name, item_price, order_quantity, order_detail_amount FROM order_detail WHERE order_id = ?";
		List<OrderDetail> list = new ArrayList<OrderDetail>();

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, orderId);
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next()) {
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setOrderId(rs.getString("order_id"));
					orderDetail.setOrderDetailNo(rs.getInt("order_detail_no"));
					orderDetail.setItemId(rs.getString("item_id"));
					orderDetail.setItemName(rs.getString("item_name"));
					orderDetail.setItemPrice(rs.getInt("item_price"));
					orderDetail.setOrderQuantity(rs.getInt("order_quantity"));
					orderDetail.setOrderDetailAmount(rs.getInt("order_detail_amount"));
					list.add(orderDetail);
				}
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void add(OrderDetail orderDetail) throws SQLException {
		// 受注明細を追加する
		String sql = "INSERT INTO order_detail (order_id, order_detail_no, item_id, item_name, item_price, order_quantity, order_detail_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, orderDetail.getOrderId());
			stmt.setInt(2, orderDetail.getOrderDetailNo());
			stmt.setString(3, orderDetail.getItemId());
			stmt.setString(4, orderDetail.getItemName());
			stmt.setInt(5, orderDetail.getItemPrice());
			stmt.setInt(6, orderDetail.getOrderQuantity());
			stmt.setInt(7, orderDetail.getOrderDetailAmount());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public void deleteByOrderId(String orderId) throws SQLException {
		// 受注明細を削除する
		String sql = "DELETE FROM order_detail WHERE order_id = ?";

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, orderId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw e;
		}

	}

}
