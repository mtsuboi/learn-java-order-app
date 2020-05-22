package com.example.order_app.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.example.order_app.dto.Item;

public class ItemOnDbImpl implements ItemAccessor {
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
	public List<Item> findAll() {
		// 商品を全件抽出する
		String sql = "SELECT item_id, item_name, item_price FROM items";
		List<Item> list = new ArrayList<Item>();

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			try(ResultSet rs = stmt.executeQuery()) {
				while(rs.next()) {
					Item item = new Item();
					item.setItemId(rs.getString("item_id"));
					item.setItemName(rs.getString("item_name"));
					item.setItemPrice(rs.getInt("item_price"));
					list.add(item);
				}
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public Item findById(String itemId) {
		// 商品をitem_idで検索する
		String sql = "SELECT item_id, item_name, item_price FROM items WHERE item_id = ?";
		Item item = new Item();

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, itemId);
			try(ResultSet rs = stmt.executeQuery()) {
				if(rs.next()) {
					item.setItemId(rs.getString("item_id"));
					item.setItemName(rs.getString("item_name"));
					item.setItemPrice(rs.getInt("item_price"));
				}
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public void add(Item item) {
		// 商品を追加する
		String sql = "INSERT INTO items (item_id, item_name, item_price) VALUES (?, ?, ?)";

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, item.getItemId());
			stmt.setString(2, item.getItemName());
			stmt.setInt(3, item.getItemPrice());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	@Override
	public int update(Item item) {
		// 商品を更新する
		String sql = "UPDATE items SET item_name = ?, item_price = ? WHERE item_id = ?";

		int count = 0;
		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, item.getItemName());
			stmt.setInt(2, item.getItemPrice());
			stmt.setString(3, item.getItemId());
			count = stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public void delete(String itemId) {
		// 商品を削除する
		String sql = "DELETE FROM items WHERE item_id = ?";

		try(Connection con = getConnection();
				PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, itemId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

}
