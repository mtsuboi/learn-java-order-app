package com.example.order_app.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.example.order_app.constants.FormCommand;
import com.example.order_app.constants.FormMode;
import com.example.order_app.constants.OrderStatus;
import com.example.order_app.dto.OrderForm;
import com.example.order_app.dto.OrderFormDetail;
import com.example.order_app.logic.OrderLogic;
import com.example.order_app.logic.OrderValidator;

public class OrderEntryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String orderId = req.getParameter("order_id");

		if(orderId == null || orderId.isEmpty()) {
			// order_idパラメータなしの場合は新規モード
			req.setAttribute("mode", FormMode.NEW);
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime now = LocalDateTime.now();
			req.setAttribute("order_date", dateFormat.format(now));
			req.setAttribute("order_status", OrderStatus.ORDER);
			// 明細の1行目だけ空行を用意しておく
			OrderFormDetail orderFormDetail = new OrderFormDetail();
			orderFormDetail.setOrderDetailNo("1");
			List<OrderFormDetail> detailList = new ArrayList<OrderFormDetail>();
			detailList.add(orderFormDetail);
			req.setAttribute("list", detailList);

			RequestDispatcher dispatcher = req.getRequestDispatcher("order_entry.jsp");
			dispatcher.forward(req, resp);
		} else {
			// order_idパラメータありの場合はDBから該当商品を検索
			WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			OrderLogic logic = (OrderLogic) context.getBean("aopOrderLogic");
			OrderForm orderForm = logic.findById(orderId);

			if(orderForm.getOrderId() == null || orderForm.getOrderId().isEmpty()) {
				// 検索できなかった場合はエラー
				req.setAttribute("message", "指定された受注は存在しません。別のユーザーに削除された可能性があります。");
				RequestDispatcher dispatcher = req.getRequestDispatcher("error.jsp");
				dispatcher.forward(req, resp);
			} else {
				// 検索できた場合は更新モード＆検索結果セット
				req.setAttribute("mode", FormMode.UPDATE);
				req.setAttribute("order_id", orderForm.getOrderId());
				req.setAttribute("order_status", orderForm.getOrderStatus());
				req.setAttribute("order_date", orderForm.getOrderDate());
				req.setAttribute("ship_date", orderForm.getShipDate());
				req.setAttribute("customer_name", orderForm.getCustomerName());
				req.setAttribute("customer_zipcode", orderForm.getCustomerZipcode());
				req.setAttribute("customer_address", orderForm.getCustomerAddress());
				req.setAttribute("list", orderForm.getOrderDetailList());
				RequestDispatcher dispatcher = req.getRequestDispatcher("order_entry.jsp");
				dispatcher.forward(req, resp);
			}
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		OrderLogic logic = (OrderLogic) context.getBean("aopOrderLogic");

		FormCommand command = FormCommand.valueOf(req.getParameter("command"));
		switch(command) {
		case SAVE:
			OrderForm orderForm = new OrderForm();

			// パラメータから入力値を取得（ヘッダ部）
			orderForm.setMode(req.getParameter("mode"));
			orderForm.setOrderId(req.getParameter("order_id"));
			orderForm.setOrderStatus(OrderStatus.getByCode(req.getParameter("order_status_code")));
			orderForm.setOrderDate(req.getParameter("order_date"));
			orderForm.setCustomerName(req.getParameter("customer_name"));
			orderForm.setCustomerZipcode(req.getParameter("customer_zipcode"));
			orderForm.setCustomerAddress(req.getParameter("customer_address"));

			// パラメータから入力値を取得（明細部）
			String[] itemIdArray = req.getParameterValues("item_id");
			String[] itemNameArray = req.getParameterValues("item_name");
			String[] itemPriceArray = req.getParameterValues("item_price");
			String[] orderQuantityArray = req.getParameterValues("order_quantity");
			String[] orderDetailAmountArray = req.getParameterValues("order_detail_amount");

			List<OrderFormDetail> detailList = new ArrayList<OrderFormDetail>();
			for(int index = 0; index < itemIdArray.length; index++) {
				OrderFormDetail orderFormDetail = new OrderFormDetail();
				orderFormDetail.setOrderDetailNo(Integer.toString(index + 1));
				orderFormDetail.setItemId(itemIdArray[index]);
				orderFormDetail.setItemName(itemNameArray[index]);
				orderFormDetail.setItemPrice(itemPriceArray[index]);
				orderFormDetail.setOrderQuantity(orderQuantityArray[index]);
				orderFormDetail.setOrderDetailAmount(orderDetailAmountArray[index]);
				detailList.add(orderFormDetail);
			}
			orderForm.setOrderDetailList(detailList);

			// 入力検証
			OrderValidator orderValidator = (OrderValidator) context.getBean("orderValidator");
			List<String> errors = orderValidator.validate(orderForm);

			if(errors == null || errors.isEmpty()) {
				// エラーが無ければDBに保存して商品一覧画面に戻る
				try {
					logic.save(orderForm);
				} catch (SQLException e) {
					// 何もしない
				}

				resp.sendRedirect("/order_list?order_status=" + req.getParameter("order_status_code"));
			} else {
				// エラーがあったらエラーメッセージをフォームに表示
				req.setAttribute("errors", errors);

				// フォームに入力値を戻す
				req.setAttribute("mode", orderForm.getMode());
				req.setAttribute("order_id", orderForm.getOrderId());
				req.setAttribute("order_status", orderForm.getOrderStatus());
				req.setAttribute("order_date", orderForm.getOrderDate());
				req.setAttribute("customer_name", orderForm.getCustomerName());
				req.setAttribute("customer_zipcode", orderForm.getCustomerZipcode());
				req.setAttribute("customer_address", orderForm.getCustomerAddress());
				req.setAttribute("list", orderForm.getOrderDetailList());

				RequestDispatcher dispatcher = req.getRequestDispatcher("order_entry.jsp");
				dispatcher.forward(req, resp);
			}
			break;
		case ORDER:
			try {
				logic.changeStatus(req.getParameter("order_id"), OrderStatus.ORDER);
			} catch (SQLException e) {
				// 何もしない
			}
			resp.sendRedirect("/order_list?order_status=" + req.getParameter("order_status_code"));
			break;
		case SHIPPING:
			try {
				logic.changeStatus(req.getParameter("order_id"), OrderStatus.SHIPPING);
			} catch (SQLException e) {
				// 何もしない
			}
			resp.sendRedirect("/order_list?order_status=" + req.getParameter("order_status_code"));
			break;
		case SHIPPED:
			try {
				logic.changeStatus(req.getParameter("order_id"), OrderStatus.SHIPPED);
			} catch (SQLException e) {
				// 何もしない
			}
			resp.sendRedirect("/order_list?order_status=" + req.getParameter("order_status_code"));
			break;
		case DELETE:
			try {
				logic.delete(req.getParameter("order_id"));
			} catch (SQLException e) {
				// 何もしない
			}
			resp.sendRedirect("/order_list?order_status=" + req.getParameter("order_status_code"));
			break;
		case GOBACK:
			resp.sendRedirect("/order_list?order_status=" + req.getParameter("order_status_code"));
			break;
		default:
			break;
		}

	}

}
