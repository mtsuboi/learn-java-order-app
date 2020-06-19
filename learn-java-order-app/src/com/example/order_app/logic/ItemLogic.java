package com.example.order_app.logic;

import java.sql.SQLException;
import java.util.List;

import com.example.order_app.dto.Item;
import com.example.order_app.dto.ItemForm;

public interface ItemLogic {
	// 商品を全件抽出する
	List<Item> findAll();

	// 商品をitem_idで検索する
	Item findById(final String itemId);

	// 商品を商品名(部分一致)で検索する
	List<Item> findByName(final String itemName);

	// 商品を追加する
	void save(final ItemForm itemForm) throws SQLException;

	// 商品を削除する
	void delete(final String itemId) throws SQLException;

}
