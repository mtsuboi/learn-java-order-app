package com.example.order_app.logic;

import java.util.ArrayList;
import java.util.List;

import com.example.order_app.access.ItemAccessor;
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
	public List<String> validate(ItemForm itemForm) {
		// 商品マスタフォームをを入力検証する
		List<String> errors = new ArrayList<String>();

		// 商品ID: 必須、半角5桁(英大文字または数字)、重複チェック
		String itemId = itemForm.getItemId();
		if(itemId == null || itemId.trim().isEmpty()) {
			errors.add("商品IDは必須です。");
		} else if(!itemId.matches("[A-Z0-9]{5}")) {
			errors.add("商品IDは半角5桁(英大文字または数字)で入力してください。");
		} else {
			Item item = itemAccessor.findById(itemId);
			String findItemId = item.getItemId();
			if(findItemId != null) {
				errors.add("商品ID[" + itemId + "]は既に登録済みです。");
			}
		}

		// 商品名: 必須
		String itemName = itemForm.getItemName();
		if(itemName == null || itemName.trim().isEmpty()) {
			errors.add("商品名は必須です。");
		}

		// 単価: 必須、数値
		String itemPrice = itemForm.getItemPrice();
		if(itemPrice == null || itemPrice.trim().isEmpty()) {
			errors.add("単価は必須です。");
		} else {
			try {
				Integer.parseInt(itemPrice);
			} catch(NumberFormatException e) {
				errors.add("単価は数値で入力してください。");
			}
		}

		return errors;
	}

	@Override
	public void add(Item item) {
			// 商品を追加する
			itemAccessor.add(item);
	}

	@Override
	public void delete(String itemId) {
		// 商品を削除する
		itemAccessor.delete(itemId);
	}

}
