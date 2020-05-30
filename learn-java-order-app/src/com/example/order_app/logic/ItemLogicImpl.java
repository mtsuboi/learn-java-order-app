package com.example.order_app.logic;

import java.util.List;

import com.example.order_app.access.ItemAccessor;
import com.example.order_app.constants.FormMode;
import com.example.order_app.dto.Item;
import com.example.order_app.dto.ItemForm;

public class ItemLogicImpl implements ItemLogic {
	private ItemAccessor itemAccessor;

	public ItemAccessor getItem() {
		return itemAccessor;
	}

	public void setItem(ItemAccessor itemAccessor) {
		this.itemAccessor = itemAccessor;
	}

	@Override
	public List<Item> findAll() {
		// 商品を全件抽出する
		return itemAccessor.findAll();
	}

	@Override
	public Item findById(String itemId) {
		// 商品をitem_idで検索する
		return itemAccessor.findById(itemId);
	}

	@Override
	public void save(ItemForm itemForm) {
		Item item = new Item();
		item.setItemId(itemForm.getItemId());
		item.setItemName(itemForm.getItemName());
		item.setItemPrice(Integer.parseInt(itemForm.getItemPrice()));

		// 新規モードなら追加、編集モードなら更新
		if(FormMode.valueOf(itemForm.getMode()) == FormMode.NEW) {
			itemAccessor.add(item);
		} else {
			itemAccessor.update(item);
		}
	}

	@Override
	public void delete(String itemId) {
		// 商品を削除する
		itemAccessor.delete(itemId);
	}

}
