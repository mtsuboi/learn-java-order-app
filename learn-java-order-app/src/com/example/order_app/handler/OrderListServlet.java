package com.example.order_app.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.example.order_app.constants.OrderStatus;
import com.example.order_app.dto.Order;
import com.example.order_app.logic.OrderLogic;

public class OrderListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		OrderLogic logic = (OrderLogic) context.getBean("aopOrderLogic");

		// ステータスが指定されていたら指定の受注を抽出してlistへ入れる(既定はステータス：受注)
		String orderStatusCode = req.getParameter("order_status");
		OrderStatus orderStatus = OrderStatus.ORDER;
		if(orderStatusCode != null && !orderStatusCode.isEmpty()) {
			orderStatus = OrderStatus.getByCode(orderStatusCode);
		}
		List<Order> list = logic.findByStatus(orderStatus);
		req.setAttribute("list", list);

		RequestDispatcher dispatcher = req.getRequestDispatcher("order_list.jsp");
		dispatcher.forward(req, resp);
	}

}
