package com.example.order_app.logging;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

public class Logger implements MethodBeforeAdvice, AfterReturningAdvice {

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		System.out.println("After: " + method.getName() + "[" + target + "]");
	}

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		System.out.println("Before: " + method.getName() + "[" + target + "]");
	}

}
