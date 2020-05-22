package com.example.order_app.access;

import java.util.List;

import com.example.order_app.dto.Item;

public interface ItemAccessor {
	// 商品を全件抽出する
	public List<Item> findAll();

	// 商品をitem_idで検索する
	public Item findById(final String itemId);

	// 商品を追加する
	public void add(final Item item);

	// 商品を更新する
	public int update(final Item item);

	// 商品を削除する
	public void delete(final String itemId);

}