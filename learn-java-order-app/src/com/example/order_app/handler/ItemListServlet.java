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

import com.example.order_app.dto.Item;
import com.example.order_app.logic.ItemLogic;

public class ItemListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		ItemLogic logic = (ItemLogic) context.getBean("aopItemLogic");

		// 商品を全件抽出してlistへ入れる
		List<Item> list = logic.findAll();
		req.setAttribute("list", list);

		RequestDispatcher dispatcher = req.getRequestDispatcher("item_list.jsp");
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		ItemLogic logic = (ItemLogic) context.getBean("aopItemLogic");

		String[] parsedCommand = req.getParameter("command").split(" ");

		if(parsedCommand[0] == "delete") {
			// 商品を削除する
//			String itemId = req.getParameter("item_id");
			String itemId = parsedCommand[1];
			logic.delete(itemId);
		}

		resp.sendRedirect("/item_list");

	}


}
