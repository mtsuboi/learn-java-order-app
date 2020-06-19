package com.example.order_app.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.order_app.access.ItemAccessor;
import com.example.order_app.constants.FormMode;
import com.example.order_app.dto.Item;
import com.example.order_app.dto.OrderForm;
import com.example.order_app.dto.OrderFormDetail;

public class OrderValidatorImpl implements OrderValidator {
	private ItemAccessor itemAccessor;

	public ItemAccessor getItem() {
		return itemAccessor;
	}

	public void setItem(ItemAccessor itemAccessor) {
		this.itemAccessor = itemAccessor;
	}

	@Override
	public List<String> validate(OrderForm orderForm) {
		// 受注フォームをを入力検証する
		List<String> errors = new ArrayList<String>();

		// ヘッダ部のチェック

		// 受注ID: 必須、半角6桁(数字)　※更新モード時のみ念のためチェック
		if(FormMode.valueOf(orderForm.getMode()) == FormMode.UPDATE) {
			String orderId = orderForm.getOrderId();
			if(orderId == null || orderId.trim().isBlank()) {
				errors.add("受注IDは必須です。");
			} else if (!orderId.matches("[0-9]{6}")) {
				errors.add("受注IDは半角6桁（数字）で入力してください。");
			}
		}

		// 受注日: 必須、日付チェック
		String orderDate = orderForm.getOrderDate();
		if(orderDate == null || orderDate.trim().isBlank()) {
			errors.add("受注日は必須です。");
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false); // 日付を厳密にチェックする
			try {
				dateFormat.parse(orderDate);
			} catch(ParseException e) {
				errors.add("受注日は正しい日付を入力してください。");
			}
		}

		// 顧客名: 必須チェック
		String customerName = orderForm.getCustomerName();
		if(customerName == null || customerName.trim().isBlank()) {
			errors.add("顧客名は必須です。");
		}

		// 郵便番号: 必須チェック
		String customerZipcode = orderForm.getCustomerZipcode();
		if(customerZipcode == null || customerZipcode.trim().isBlank()) {
			errors.add("郵便番号は必須です。");
		}

		// 住所: 必須チェック
		String customerAddress = orderForm.getCustomerAddress();
		if(customerAddress == null || customerAddress.trim().isBlank()) {
			errors.add("住所は必須です。");
		}

		// 明細部のチェック
		for(OrderFormDetail detail : orderForm.getOrderDetailList()) {
			// 明細番号取得
			String orderDetailNo = detail.getOrderDetailNo();
			// エラーメッセージの先頭に付ける文言
			String errPreFix = "明細No." + orderDetailNo + "： ";

			// 商品ID： 必須、半角英数字、存在チェック
			String itemId = detail.getItemId();
			if(itemId == null || itemId.trim().isBlank()) {
				errors.add(errPreFix + "商品IDは必須です。");
			} else if(!itemId.matches("[A-Z0-9]{5}")) {
				errors.add(errPreFix + "商品IDは半角5桁(英大文字または数字)で入力してください。");
			} else {
				Item item = itemAccessor.findById(itemId);
				String findItemId = item.getItemId();
				if(findItemId == null) {
					errors.add(errPreFix + "商品ID「" + itemId + "」は商品マスタに存在しません。");
				}
			}

			// 商品名： 必須
			String itemName = detail.getItemName();
			if(itemName == null || itemName.trim().isBlank()) {
				errors.add(errPreFix + "商品名は必須です。");
			}

			// 単価: 必須、数値
			String itemPrice = detail.getItemPrice();
			if(itemPrice == null || itemPrice.trim().isBlank()) {
				errors.add(errPreFix + "単価は必須です。");
			} else {
				try {
					Integer.parseInt(itemPrice);
				} catch(NumberFormatException e) {
					errors.add(errPreFix + "単価は数字で入力してください。");
				}
			}

			// 数量: 必須、数値
			String orderQuantity = detail.getOrderQuantity();
			if(orderQuantity == null || orderQuantity.trim().isBlank()) {
				errors.add(errPreFix + "数量は必須です。");
			} else {
				try {
					Integer.parseInt(orderQuantity);
				} catch(NumberFormatException e) {
					errors.add(errPreFix + "数量は数字で入力してください。");
				}
			}

			// 金額: 必須、数値
			String orderDetailAmount = detail.getOrderDetailAmount();
			if(orderDetailAmount == null || orderDetailAmount.trim().isBlank()) {
				errors.add(errPreFix + "金額は必須です。");
			} else {
				try {
					Integer.parseInt(orderDetailAmount);
				} catch(NumberFormatException e) {
					errors.add(errPreFix + "金額は数字で入力してください。");
				}
			}

		}

		return errors;
	}

}
