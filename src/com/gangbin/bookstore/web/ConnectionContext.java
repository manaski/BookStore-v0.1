package com.gangbin.bookstore.web;

import java.sql.Connection;

/**
 * 单例模式，返回一个threadlocal对象
 * @author Ligangbin
 *
 */
public class ConnectionContext {
	
	private ConnectionContext(){}
	//单例模式
	private static ConnectionContext instance = new ConnectionContext();
	
	public static ConnectionContext getInstance() {
		return instance;
	}

	private ThreadLocal<Connection> connectionThreadLocal = 
			new ThreadLocal<>();
			
	public void bind(Connection connection){
		connectionThreadLocal.set(connection);
	}
	
	public Connection get(){
		return connectionThreadLocal.get();
	}
	
	public void remove(){
		connectionThreadLocal.remove();
	}
	
}
