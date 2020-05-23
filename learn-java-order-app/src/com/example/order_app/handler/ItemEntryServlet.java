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
import com.example.order_app.dto.ItemForm;
import com.example.order_app.logic.ItemLogic;

public class ItemEntryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String itemId = req.getParameter("item_id");
		if(itemId == null || itemId.isEmpty()) {
			// item_idパラメータなしの場合は新規モード
			req.setAttribute("mode", "new");
		} else {
			// item_idパラメータありの場合はDBから該当商品を検索
			WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			ItemLogic logic = (ItemLogic) context.getBean("aopItemLogic");
			Item item = logic.findById(itemId);

			if(item.getItemId().isEmpty()) {
				// 検索できなかった場合は新規モード
				req.setAttribute("mode", "new");
			} else {
				// 検索できた場合は更新モード＆検索結果セット
				req.setAttribute("mode", "update");
				req.setAttribute("item_id", item.getItemId());
				req.setAttribute("item_name", item.getItemName());
				req.setAttribute("item_price", item.getItemPrice());
			}
		}

		RequestDispatcher dispatcher = req.getRequestDispatcher("item_entry.jsp");
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		ItemLogic logic = (ItemLogic) context.getBean("aopItemLogic");

		// 入力検証
		ItemForm itemForm = new ItemForm();
		itemForm.setMode(req.getParameter("mode"));
		itemForm.setItemId(req.getParameter("item_id"));
		itemForm.setItemName(req.getParameter("item_name"));
		itemForm.setItemPrice(req.getParameter("item_price"));

		List<String> errors = logic.validate(itemForm);

		if(errors == null || errors.isEmpty()) {
			// エラーが無ければDBに保存して商品一覧画面に戻る
			logic.save(itemForm);

			resp.sendRedirect("/item_list");
		} else {
			// エラーがあったらエラーメッセージをフォームに表示
			req.setAttribute("errors", errors);

			// フォームに入力値を戻す
			req.setAttribute("mode", itemForm.getMode());
			req.setAttribute("item_id", itemForm.getItemId());
			req.setAttribute("item_name", itemForm.getItemName());
			req.setAttribute("item_price", itemForm.getItemPrice());

			RequestDispatcher dispatcher = req.getRequestDispatcher("item_entry.jsp");
			dispatcher.forward(req, resp);
		}

	}

}
