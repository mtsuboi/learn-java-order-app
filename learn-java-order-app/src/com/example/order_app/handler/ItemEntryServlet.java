package com.example.order_app.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.example.order_app.constants.FormMode;
import com.example.order_app.dto.Item;
import com.example.order_app.dto.ItemForm;
import com.example.order_app.logic.ItemLogic;
import com.example.order_app.logic.ItemValidator;

public class ItemEntryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String itemId = req.getParameter("item_id");
		if(itemId == null || itemId.isEmpty()) {
			// item_idパラメータなしの場合は新規モード
			req.setAttribute("mode", FormMode.NEW);
			RequestDispatcher dispatcher = req.getRequestDispatcher("item_entry.jsp");
			dispatcher.forward(req, resp);
		} else {
			// item_idパラメータありの場合はDBから該当商品を検索
			WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
			ItemLogic logic = (ItemLogic) context.getBean("aopItemLogic");
			Item item = logic.findById(itemId);

			if(item.getItemId() == null || item.getItemId().isEmpty()) {
				// 検索できなかった場合はエラー
				req.setAttribute("message", "指定された商品は存在しません。別のユーザーに削除された可能性があります。");
				RequestDispatcher dispatcher = req.getRequestDispatcher("error.jsp");
				dispatcher.forward(req, resp);
			} else {
				// 検索できた場合は更新モード＆検索結果セット
				req.setAttribute("mode", FormMode.UPDATE);
				req.setAttribute("item_id", item.getItemId());
				req.setAttribute("item_name", item.getItemName());
				req.setAttribute("item_price", item.getItemPrice());
				RequestDispatcher dispatcher = req.getRequestDispatcher("item_entry.jsp");
				dispatcher.forward(req, resp);
			}
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		ItemLogic logic = (ItemLogic) context.getBean("aopItemLogic");
		ItemValidator itemValidator = (ItemValidator) context.getBean("itemValidator");
		// 入力検証
		ItemForm itemForm = new ItemForm();

		itemForm.setMode(req.getParameter("mode"));
		itemForm.setItemId(req.getParameter("item_id"));
		itemForm.setItemName(req.getParameter("item_name"));
		itemForm.setItemPrice(req.getParameter("item_price"));

		List<String> errors = itemValidator.validate(itemForm);

		if(errors == null || errors.isEmpty()) {
			// エラーが無ければDBに保存して商品一覧画面に戻る
			try {
				logic.save(itemForm);
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}

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
