package com.example.order_app.handler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.example.order_app.access.OrderAccessor;
import com.example.order_app.access.OrderDetailAccessor;
import com.example.order_app.constants.OrderStatus;
import com.example.order_app.dto.Order;
import com.example.order_app.dto.OrderDetail;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class DeliveryNotePrintServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private JasperPrint getReport(ServletContext servletContext, JasperReport jasperReport, Order order) {
		// Bean取得
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		OrderDetailAccessor orderDetailAccessor = (OrderDetailAccessor) context.getBean("aopOrderDetailDao");

		// パラメータ作成
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		parameters.put("PublishDate", LocalDateTime.now().format(formatter));
		parameters.put("OrderId",order.getOrderId());
		parameters.put("CustomerName", order.getCustomerName());
		parameters.put("OrderAmount", order.getOrderAmount());
		String customerZipcode = order.getCustomerZipcode();
		parameters.put("CustomerZipcode", "〒" + customerZipcode.substring(0, 3) + "-" + customerZipcode.substring(3));
		parameters.put("CustomerAddress", order.getCustomerAddress());
		File stampFile = new File(servletContext.getRealPath("/report/stamp.png"));
		parameters.put("StampPath", stampFile.getPath());

		// 受注明細をOrder_idで検索する
		List<OrderDetail> detailList = orderDetailAccessor.findById(order.getOrderId());

		// リストをデータソースとして設定
		JRBeanCollectionDataSource reportDatasource = new JRBeanCollectionDataSource(detailList);

		// 帳票を作成
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, reportDatasource);
			return jasperPrint;
		} catch (JRException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Bean取得
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
		OrderAccessor orderAccessor = (OrderAccessor) context.getBean("aopOrderDao");

		try {
			// 帳票レイアウトをコンパイル
			ServletContext servletContext = this.getServletConfig().getServletContext();
			File jrxmlFile = new File(servletContext.getRealPath("/report/DeliveryNote.jrxml"));
			JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getPath());

			// パラメータチェック
			String orderStatusCode = req.getParameter("order_status");
			String orderId = req.getParameter("order_id");

			JasperPrint jasperPrint = null;
			if(orderStatusCode == null || orderStatusCode.isEmpty()) {
				if(orderId == null || orderId.isEmpty()) {
					// パラメータがない場合はエラー
					req.setAttribute("message", "パラメータが指定されていません。");
					RequestDispatcher dispatcher = req.getRequestDispatcher("error.jsp");
					dispatcher.forward(req, resp);
					return;
				} else {
					// DBから該当受注を検索
					Order order = orderAccessor.findById(orderId);
					if(order.getOrderId() == null || order.getOrderId().isEmpty()) {
						// 検索できなかった場合はエラー
						req.setAttribute("message", "印刷対象のデータは存在しません。");
						RequestDispatcher dispatcher = req.getRequestDispatcher("error.jsp");
						dispatcher.forward(req, resp);
						return;
					} else {
						jasperPrint = this.getReport(servletContext, jasperReport, order);
					}
				}
			} else {
				// DBから該当受注を検索
				OrderStatus orderStatus = OrderStatus.getByCode(orderStatusCode);
				List<Order> orderList = orderAccessor.findByStatus(orderStatus);
				if(orderList.isEmpty()) {
					// 検索できなかった場合はエラー
					req.setAttribute("message", "印刷対象のデータは存在しません。");
					RequestDispatcher dispatcher = req.getRequestDispatcher("error.jsp");
					dispatcher.forward(req, resp);
					return;
				} else {
					boolean isFirstPage = true;
					for(Order order : orderList) {
						if(isFirstPage) {
							jasperPrint = this.getReport(servletContext, jasperReport, order);
							isFirstPage = false;
						} else {
							List<JRPrintPage> pages = this.getReport(servletContext, jasperReport, order).getPages();
							for(JRPrintPage page : pages) {
								jasperPrint.addPage(page);
							}
						}
					}
				}
			}

			// PDF出力
			byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
			resp.setContentType("application/pdf");
			resp.setContentLength(bytes.length);

			ServletOutputStream out = resp.getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
		} catch (JRException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return;
		}
	}
}
