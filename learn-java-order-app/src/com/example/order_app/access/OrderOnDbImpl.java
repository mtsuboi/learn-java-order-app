package com.example.order_app.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.example.order_app.constants.OrderStatus;
import com.example.order_app.dto.Order;

public class OrderOnDbImpl implements OrderAccessor {
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
	public List<Order> findByStatus(OrderStatus orderStatus) {
		// 受注をステータス指定で抽出する
		String sql = "SELECT order_id, order_status, order_date, ship_date, customer_name, customer_zipcode, customer_address, order_amount FROM orders WHERE order_status = ? ORDER BY order_id";
		List<Order> list = new ArrayList<Order>();

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, orderStatus.getCode());
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next()) {
					Order order = new Order();
					order.setOrderId(rs.getString("order_id"));
					order.setOrderStatus(OrderStatus.getByCode(rs.getString("order_status")));
					order.setOrderDate(rs.getDate("order_date"));
					order.setShipDate(rs.getDate("ship_date"));
					order.setCustomerName(rs.getString("customer_name"));
					order.setCustomerZipcode(rs.getString("customer_zipcode"));
					order.setCustomerAddress(rs.getString("customer_address"));
					order.setOrderAmount(rs.getInt("order_amount"));
					list.add(order);
				}
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Order findById(String orderId) {
		// 受注をOrder_idで検索する
		String sql = "SELECT order_id, order_status, order_date, ship_date, customer_name, customer_zipcode, customer_address, order_amount FROM orders WHERE order_id = ?";
		Order order = new Order();

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, orderId);
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next()) {
					order.setOrderId(rs.getString("order_id"));
					order.setOrderStatus(OrderStatus.getByCode(rs.getString("order_status")));
					order.setOrderDate(rs.getDate("order_date"));
					order.setShipDate(rs.getDate("ship_date"));
					order.setCustomerName(rs.getString("customer_name"));
					order.setCustomerZipcode(rs.getString("customer_zipcode"));
					order.setCustomerAddress(rs.getString("customer_address"));
					order.setOrderAmount(rs.getInt("order_amount"));
				}
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public String numberingOrderId() throws SQLException {
		// Order_idを採番する
		String sql = "SELECT MAX(order_id) AS order_id from orders";
		int orderIdInt = 0;
		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next() && rs.getString("order_id") != null) {
					orderIdInt = Integer.parseInt(rs.getString("order_id"));
				}
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw e;
		}
		return String.format("%06d", orderIdInt + 1);
	}

	@Override
	public void add(Order order) throws SQLException {
		// 受注を追加する
		String sql = "INSERT INTO orders (order_id, order_status, order_date, ship_date, customer_name, customer_zipcode, customer_address, order_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, order.getOrderId());
			stmt.setString(2, order.getOrderStatus().getCode());
			if(order.getOrderDate() == null) {
				stmt.setDate(3, null);
			} else {
				stmt.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
			}
			if(order.getShipDate() == null) {
				stmt.setDate(4, null);
			} else {
				stmt.setDate(4, new java.sql.Date(order.getShipDate().getTime()));
			}
			stmt.setString(5, order.getCustomerName());
			stmt.setString(6, order.getCustomerZipcode());
			stmt.setString(7, order.getCustomerAddress());
			stmt.setInt(8, order.getOrderAmount());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public int update(String orderId, OrderStatus orderStatus, Date shipDate)  throws SQLException {
		// 受注ステータスと出荷日を更新する
		String sql = "UPDATE orders SET order_status = ?, ship_date = ? WHERE order_id = ?";

		int count = 0;
		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, orderStatus.getCode());
			if(shipDate == null) {
				stmt.setNull(2, java.sql.Types.DATE);
			} else {
				stmt.setDate(2, new java.sql.Date(shipDate.getTime()));
			}
			stmt.setString(3, orderId);
			count = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			throw e;
		}
		return count;
	}

	@Override
	public void delete(String orderId) throws SQLException {
		// 受注を削除する
		String sql = "DELETE FROM orders WHERE order_id = ?";

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
