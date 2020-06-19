package com.example.order_app.access;

import java.sql.SQLException;
import java.util.List;

import com.example.order_app.dto.Item;

public interface ItemAccessor {
	// 商品を全件抽出する
	public List<Item> findAll();

	// 商品をitem_idで検索する
	public Item findById(final String itemId);

	// 商品を商品名(部分一致)で抽出する
	public List<Item> findByName(final String itemName);

	// 商品を追加する
	public void add(final Item item) throws SQLException;

	// 商品を更新する
	public int update(final Item item) throws SQLException;

	// 商品を削除する
	public void delete(final String itemId) throws SQLException;

}