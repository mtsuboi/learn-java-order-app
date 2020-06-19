package com.example.order_app.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.example.order_app.dto.Item;
import com.example.order_app.logic.ItemLogic;
import com.google.gson.Gson;

public class ItemFindServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 商品を検索してJSONで返す
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		ItemLogic logic = (ItemLogic) context.getBean("aopItemLogic");

		// JSONを使う準備
		Gson gson = new Gson();
		String jsonString = null;

		String itemId = req.getParameter("item_id");
		String itemName = req.getParameter("item_name");
		// 商品IDが指定されていたら商品IDで検索
		if(itemId != null) {
			Item item = logic.findById(itemId);
			jsonString = gson.toJson(item);
		// 商品名が指定されていたら商品名で検索
		} else if(itemName != null) {
			List<Item> list = logic.findByName(itemName);
			jsonString = gson.toJson(list);
		}

		resp.setContentType("application/json");
		resp.setHeader("Cache-Control", "nocache");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter out = resp.getWriter();
		out.print(jsonString);
		out.flush();
	}

}
