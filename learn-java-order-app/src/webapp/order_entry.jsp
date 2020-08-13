<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/common.css">

<title>受注登録</title>
</head>
<body>
	<%@ include file="navbar.jsp"%>

	<main role="main" class="container-fluid" style="max-width: 1000px;">
		<h1>受注登録</h1>
		<%@ include file="error_message.jsp"%>
		<form name="order_form" action="order_entry" method="post">
			<input type="hidden" name="mode" id="mode" value="${mode}">
			<input type="hidden" name="command" id="command">
			<div class="form-row">
				<div class="form-group col-sm-4 col-lg-2">
					<label for="order_id">受注ID : </label>
					<input type="text" class="form-control" name="order_id" id="order_id" value="${order_id}"
						readonly="readonly" style="background-color : lightgray;"
						<c:if test="${mode == 'NEW'}"> placeholder="登録後に自動採番されます。"</c:if>>
				</div>
				<div class="form-group col-sm-4 col-lg-3">
					<label for="order_date">受注日: </label>
					<input type="date" class="form-control" name="order_date" id="order_date" value="${order_date}">
				</div>
				<div class="form-group col-sm-4 col-lg-3">
					<c:if test="${order_status == 'SHIPPED'}">
						<label for="ship_date">出荷日: </label>
						<input type="date" class="form-control" name="ship_date" id="ship_date" value="${ship_date}">
					</c:if>
				</div>
				<div class="form-group col-sm-4 col-lg-3">
					<label for="order_status">ステータス: </label>
					<input type="text" class="form-control" name="order_status" id="order_status" value="${order_status.name}" disabled="disabled">
					<input type="hidden" name="order_status_code" id="order_status_code" value="${order_status.code}">
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-sm-8 col-lg-5">
					<label for="customer_name">顧客名: </label>
					<input type="text" class="form-control" name="customer_name" id="customer_name" value="${customer_name}">
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-sm-4 col-lg-2">
					<label for="customer_zipcode">郵便番号: </label>
					<input type="text" class="form-control" name="customer_zipcode" id="customer_zipcode" value="${customer_zipcode}">
				</div>
				<div class="form-group col-sm-12 col-lg-10">
					<label for="customer_address">住所: </label>
					<input type="text" class="form-control" name="customer_address" id="customer_address" value="${customer_address}">
				</div>
			</div>
			<div class="form-row">
				<div class="form-group col-6">
					<a class="btn btn-primary" role="button" onclick="add_row();" style="color: white !important;">
						<svg class="bi bi-plus-circle" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
		 						<path fill-rule="evenodd" d="M8 3.5a.5.5 0 0 1 .5.5v4a.5.5 0 0 1-.5.5H4a.5.5 0 0 1 0-1h3.5V4a.5.5 0 0 1 .5-.5z"/>
		 						<path fill-rule="evenodd" d="M7.5 8a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1H8.5V12a.5.5 0 0 1-1 0V8z"/>
								<path fill-rule="evenodd" d="M8 15A7 7 0 1 0 8 1a7 7 0 0 0 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
						</svg>
						<span style="vertical-align: middle;">行追加</span>
					</a>
				</div>
				<div class="form-group col-6 text-right">

				</div>
			</div>
			<div class="table-responsive">
				<table class="table table-striped" id="order_detail">
					<thead>
						<tr>
							<th>No.</th>
							<th>商品ID</th>
							<th>商品名</th>
							<th>単価</th>
							<th>数量</th>
							<th>金額</th>
							<th>
								<svg class="bi bi-trash" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
									<path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
									<path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
								</svg>
							</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="detail" items="${list}">
							<tr>
								<td id="line_no"><c:out value="${detail.orderDetailNo}" /></td>
								<td>
									<div class="row">
										<input type="text" name="item_id" id="item_id" value="<c:out value="${detail.itemId}" />" class="form-control" style="width: 80px;" onblur="find_item(this);">
										<button type="button" class="btn btn-info btn-sm" data-toggle="modal" data-target="#item_search_dialog" data-whatever="${detail.orderDetailNo}">
											<svg class="bi bi-search" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
											  <path fill-rule="evenodd" d="M10.442 10.442a1 1 0 0 1 1.415 0l3.85 3.85a1 1 0 0 1-1.414 1.415l-3.85-3.85a1 1 0 0 1 0-1.415z"/>
											  <path fill-rule="evenodd" d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zM13 6.5a6.5 6.5 0 1 1-13 0 6.5 6.5 0 0 1 13 0z"/>
											</svg>
										</button>
									</div>
								</td>
								<td><input type="text" name="item_name" id="item_name" value="<c:out value="${detail.itemName}" />" class="form-control" style="width: 300px;" readonly="readonly"></td>
								<td><input type="text" name="item_price" id="item_price" value="<c:out value="${detail.itemPrice}" />" class="form-control" style="width: 80px;text-align: right;" readonly="readonly"></td>
								<td><input type="number" name="order_quantity" id="order_quantity" value="<c:out value="${detail.orderQuantity}" />" class="form-control" style="width: 60px;text-align: right;" onblur="calc_detail_amount(this);"></td>
								<td><input type="text" name="order_detail_amount" id="order_detail_amount" value="<c:out value="${detail.orderDetailAmount}" />" class="form-control" style="width: 100px;text-align: right;" readonly="readonly"></td>
								<td>
									<a class="trash" onclick="delete_row(this);">
										<svg class="bi bi-trash" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
											<path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
											<path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4L4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
										</svg>
									</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="form-row">
				<div class="form-group col-4">
					<button type="submit" class="btn btn-primary" onclick="order_form.command.value='SAVE'">登録</button>
					<button type="submit" class="btn btn-secondary" onclick="order_form.command.value='GOBACK'">戻る</button>
				</div>
				<div class="form-group col-6">
					<c:if test="${mode == 'UPDATE'}">
						<c:if test="${order_status == 'ORDER'}">
							<a class="btn btn-info" href="delivery_note_print?order_id=${order_id}" target="_blank" role="button">出荷指示書印刷</a>
							<button type="submit" class="btn btn-success" onclick="order_form.command.value='SHIPPING'">出荷作業開始</button>
						</c:if>
						<c:if test="${order_status == 'SHIPPING'}">
							<a class="btn btn-info" href="delivery_note_print?order_id=${order_id}" target="_blank" role="button">出荷指示書印刷</a>
							<button type="submit" class="btn btn-success" onclick="order_form.command.value='SHIPPED'">出荷完了</button>
							<button type="submit" class="btn btn-secondary" onclick="order_form.command.value='ORDER'">受注に戻す</button>
						</c:if>
						<c:if test="${order_status == 'SHIPPED'}">
							<button type="submit" class="btn btn-secondary" onclick="order_form.command.value='SHIPPING'">出荷作業中に戻す</button>
						</c:if>
					</c:if>
				</div>
				<div class="form-group col-2 text-right">
					<c:if test="${mode == 'UPDATE' and order_status == 'ORDER'}">
						<button type="submit" class="btn btn-secondary" onclick="order_form.command.value='DELETE'">削除</button>
					</c:if>
				</div>
			</div>
		</form>
	</main>

	<!-- 検索ダイアログ -->
	<div class="modal fade" id="item_search_dialog" tabindex="-1" role="dialog" aria-labelledby="item_search_label" aria-hidden="true">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="item_search_label">商品検索</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<div class="row">
							<label for="serach_item_name" class="col-form-label">検索キーワード(商品名部分一致):</label>
							<input type="text" class="form-control col-11" id="serach_item_name">
							<button type="button" class="btn btn-info btn-sm" onclick="item_search();">
								<svg class="bi bi-search" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
									<path fill-rule="evenodd" d="M10.442 10.442a1 1 0 0 1 1.415 0l3.85 3.85a1 1 0 0 1-1.414 1.415l-3.85-3.85a1 1 0 0 1 0-1.415z"/>
									<path fill-rule="evenodd" d="M6.5 12a5.5 5.5 0 1 0 0-11 5.5 5.5 0 0 0 0 11zM13 6.5a6.5 6.5 0 1 1-13 0 6.5 6.5 0 0 1 13 0z"/>
								</svg>
							</button>
						</div>
						<div class="table-responsive">
							<table class="table table-hover" id="item_search_result">
								<thead>
									<tr>
										<th>商品ID</th>
										<th>商品名</th>
										<th>単価</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="item_select_btn" onclick="item_select();">選択</button>
					<button type="button" class="btn btn-secondary" data-dismiss="modal">閉じる</button>
				</div>
				<input type="hidden" id="target_row_no">
			</div>
		</div>
	</div>

	<!-- jQueryとBootstrap -->
	<script src="js/jquery-3.5.1.min.js"></script>
	<script src="js/bootstrap.bundle.min.js"></script>

	<!-- 画面制御Javascript -->
	<script type="text/javascript">
		// イベントリスナー
		$(function() {
			// 商品検索ダイアログ オープン
			$('#item_search_dialog').on('show.bs.modal', function (event) {
				var $button = $(event.relatedTarget);
				var row_no = $button.closest('tr').index() + 1;
				var $modal = $(this);
				$modal.find('.modal-title').text('商品検索(明細' + row_no + '行目)');
				// どの行で検索ダイアログを押したか控えておく
				$('#target_row_no').val(row_no);
				// 検索キーワードをクリア
				$('#serach_item_name').val('');
				// 選択ボタンを無効化しておく
				$('#item_select_btn').prop('disabled',true);
				// テーブルをクリア（メッセージ表示）
				$('#item_search_result tbody').html('<tr class="message"><td colspan="3">ここに検索結果が表示されます。</td></tr>');
			});

			// 商品検索ダイアログ 行選択
			$('#item_search_result').on('click', 'tr', function (event) {
				// メッセージでない（検索結果の商品行である）場合のみ
				if(!$(this).hasClass('message')) {
					// 選択した行のクラスに"selected"を追加
					$(this).parent().find('.selected').removeClass('selected');
					$(this).addClass('selected');
					// 選択ボタンを有効化
					if($('#item_select_btn').prop('disabled')) {
						$('#item_select_btn').prop('disabled',false);
					}
				}
			});
		});

		// 商品検索（検索実行）
		function item_search() {
			var url = "item_find?item_name=" + $('#serach_item_name').val();
			var $targetTbody = $('#item_search_result tbody');
			// テーブルをクリアする
			$targetTbody.html('');
			$.getJSON(url, function(json){
				if(Object.keys(json).length > 0) {
					// 検索結果が1件以上あればテーブルに表示
					for(var item in json) {
						var trHtml = "<tr>";
						trHtml = trHtml + "<td>" + json[item].itemId + "</td>";
						trHtml = trHtml + "<td>" + json[item].itemName + "</td>";
						trHtml = trHtml + "<td>" + json[item].itemPrice + "</td>";
						trHtml = trHtml + "</tr>";
						$targetTbody.append(trHtml);
					}
				} else {
					// 検索結果が無い場合はその旨メッセージ表示
					$('#item_search_result tbody').html('<tr class="message"><td colspan="3">該当データはありません。</td></tr>');
				}
			});
		}

		// 商品検索（選択ボタン）
		function item_select() {
			var row_idx = parseInt($('#target_row_no').val()) - 1;
			var $targetTr = $('#order_detail tbody tr').eq(row_idx);
			var $selectedTr = $('#item_search_result .selected');
			// 検索結果から選択した行の各項目を受注明細の該当行の各項目へ転記する
			$targetTr.find('#item_id').val($selectedTr.find('td').eq(0).text());
			$targetTr.find('#item_name').val($selectedTr.find('td').eq(1).text());
			$targetTr.find('#item_price').val($selectedTr.find('td').eq(2).text());
			// 検索ダイアログを閉じる
			$('#item_search_dialog').modal('hide');
		}

		// 行追加
		function add_row() {
			var $clonedTr = $('#order_detail tbody tr:last').clone();
			var line_no = $('#order_detail tbody tr:last').index();
			$clonedTr.find('#line_no').text(line_no+2);
			$clonedTr.find('button').attr('data-whatever', line_no+2);
			$clonedTr.find('input').val('');
			$('#order_detail tbody').append($clonedTr);
		}

		// 行削除
		function delete_row(obj) {
			var $targetTr = $(obj).closest('tr');
			if(confirm('行 「No.' + $targetTr.find("#line_no").text() + '」を削除します。よろしいですか？')) {
				if($targetTr.parent().children().length == 1) {
					$targetTr.find('input').val('');
				} else {
					$targetTr.remove();
					$('#order_detail tbody tr').each(function(line_no) {
						$(this).find('#line_no').text(line_no + 1);
						$(this).find('button').attr('data-whatever', line_no + 1);
					});
				}
			}
		}

		// 商品情報取得
		function find_item(obj) {
			var $targetTr = $(obj).closest('tr');
			var url = "item_find?item_id=" + $targetTr.find('#item_id').val();
			$.getJSON(url, function(json){
				$targetTr.find('#item_name').val(json.itemName);
				$targetTr.find('#item_price').val(json.itemPrice);
			});
		}

		// 明細金額計算
		function calc_detail_amount(obj) {
			var $targetTr = $(obj).closest('tr');
			var itemPrice = $targetTr.find('#item_price').val();
			var orderQuantity = $targetTr.find('#order_quantity').val();
			$targetTr.find('#order_detail_amount').val(itemPrice * orderQuantity);
		}
	</script>

</body>
</html>