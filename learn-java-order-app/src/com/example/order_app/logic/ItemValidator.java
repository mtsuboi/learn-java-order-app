package com.example.order_app.logic;

import java.util.List;

import com.example.order_app.dto.ItemForm;

public interface ItemValidator {

	// 商品マスタフォームをを入力検証する
	List<String> validate(final ItemForm itemForm);

}
