package com.example.order_app.dbmanager;

import java.sql.SQLException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class DbManager implements MethodInterceptor {
	private PlatformTransactionManager txManager;

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object result = null;

		// メソッド名がfindで始まる場合はトランザクションの対象としない
		if(invocation.getMethod().getName().startsWith("find")) {
			// ターゲット処理実行
			result = invocation.proceed();
			return result;
		}

		// トランザクション開始
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus txStatus = txManager.getTransaction(def);
		System.out.println("Transaction Start: " + invocation.getMethod().getName() + "[" + invocation.getThis() + "]");

		try {
			// ターゲット処理実行
			result = invocation.proceed();
			// コミット
			txManager.commit(txStatus);
			System.out.println("Transaction Commit: " + invocation.getMethod().getName() + "[" + invocation.getThis() + "]");

		} catch (SQLException e) {
			// SQLExceptionでロールバック
			txManager.rollback(txStatus);
			System.out.println("Transaction Rollback: " + invocation.getMethod().getName() + "[" + invocation.getThis() + "]");
		}

		return result;
	}

}
