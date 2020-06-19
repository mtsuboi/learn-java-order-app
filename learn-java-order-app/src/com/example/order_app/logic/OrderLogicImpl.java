package com.example.order_app.logic;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.order_app.access.OrderAccessor;
import com.example.order_app.access.OrderDetailAccessor;
import com.example.order_app.constants.FormMode;
import com.example.order_app.constants.OrderStatus;
import com.example.order_app.dto.Order;
import com.example.order_app.dto.OrderDetail;
import com.example.order_app.dto.OrderForm;
import com.example.order_app.dto.OrderFormDetail;

public class OrderLogicImpl implements OrderLogic {
	private OrderAccessor orderAccessor;
	private OrderDetailAccessor orderDetailAccessor;

	public OrderAccessor getOrder() {
		return orderAccessor;
	}

	public void setOrder(OrderAccessor orderAccessor) {
		this.orderAccessor = orderAccessor;
	}

	public OrderDetailAccessor getOrderDetail() {
		return orderDetailAccessor;
	}

	public void setOrderDetail(OrderDetailAccessor orderDetailAccessor) {
		this.orderDetailAccessor = orderDetailAccessor;
	}

	@Override
	public List<Order> findByStatus(OrderStatus orderStatus) {
		// 指定されたステータスの受注を抽出する
		return orderAccessor.findByStatus(orderStatus);
	}

	@Override
	public OrderForm findById(String orderId) {
		OrderForm orderForm = new OrderForm();

		// 受注をOrder_idで検索する
		Order order = orderAccessor.findById(orderId);

		// 検索できなかった場合は終了
		if(order.getOrderId() == null || order.getOrderId().trim().isBlank()) {
			return orderForm;
		}

		orderForm.setOrderId(order.getOrderId());
		orderForm.setOrderStatus(order.getOrderStatus().getCode());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if(order.getOrderDate() != null) {
			orderForm.setOrderDate(dateFormat.format(order.getOrderDate()));
		}
		if(order.getShipDate() != null) {
			orderForm.setShipDate(dateFormat.format(order.getShipDate()));
		}
		orderForm.setCustomerName(order.getCustomerName());
		orderForm.setCustomerZipcode(order.getCustomerZipcode());
		orderForm.setCustomerAddress(order.getCustomerAddress());
		orderForm.setCustomerTel(order.getCustomerTel());
		orderForm.setOrderAmount(Integer.toString(order.getOrderAmount()));

		// 受注明細をOrder_idで検索する
		List<OrderDetail> detailList = orderDetailAccessor.findById(orderId);

		List<OrderFormDetail> orderFormDetailList = new ArrayList<OrderFormDetail>();
		for(OrderDetail detail : detailList) {
			OrderFormDetail orderDetailForm = new OrderFormDetail();
			orderDetailForm.setOrderDetailNo(Integer.toString(detail.getOrderDetailNo()));
			orderDetailForm.setItemId(detail.getItemId());
			orderDetailForm.setItemName(detail.getItemName());
			orderDetailForm.setItemPrice(Integer.toString(detail.getItemPrice()));
			orderDetailForm.setOrderQuantity(Integer.toString(detail.getOrderQuantity()));
			orderDetailForm.setOrderDetailAmount(Integer.toString(detail.getOrderDetailAmount()));
			orderFormDetailList.add(orderDetailForm);
		}
		orderForm.setOrderDetailList(orderFormDetailList);

		return orderForm;
	}

	@Override
	public void save(OrderForm orderForm) throws SQLException {
		// 受注を保存する
		// 新規モードなら受注IDを採番、更新モードなら取得する
		String orderId;
		if(FormMode.valueOf(orderForm.getMode()) == FormMode.NEW) {
			orderId = orderAccessor.numberingOrderId();
		} else {
			orderId = orderForm.getOrderId();
		}

		// 受注明細を追加しながら、受注金額を集計する
		int orderAmount = 0;
		for(OrderFormDetail detail : orderForm.getOrderDetailList()) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrderId(orderId);
			orderDetail.setOrderDetailNo(Integer.parseInt(detail.getOrderDetailNo()));
			orderDetail.setItemId(detail.getItemId());
			orderDetail.setItemName(detail.getItemName());
			orderDetail.setItemPrice(Integer.parseInt(detail.getItemPrice()));
			orderDetail.setOrderQuantity(Integer.parseInt(detail.getOrderQuantity()));
			orderDetail.setOrderDetailAmount(Integer.parseInt(detail.getOrderDetailAmount()));
			orderDetailAccessor.add(orderDetail);

			orderAmount = orderAmount + Integer.parseInt(detail.getOrderDetailAmount());
		}

		// 受注を追加する
		Order order = new Order();
		order.setOrderId(orderId);
		order.setOrderStatus(OrderStatus.ORDER);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date orderDate = dateFormat.parse(orderForm.getOrderDate());
			order.setOrderDate(orderDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		order.setCustomerName(orderForm.getCustomerName());
		order.setCustomerZipcode(orderForm.getCustomerZipcode());
		order.setCustomerAddress(orderForm.getCustomerAddress());
		order.setOrderAmount(orderAmount);
		orderAccessor.add(order);

	}

	@Override
	public void delete(String orderId) throws SQLException {
		// 受注明細を削除する
		orderDetailAccessor.deleteByOrderId(orderId);
		// 受注を削除する
		orderAccessor.delete(orderId);
	}

}
