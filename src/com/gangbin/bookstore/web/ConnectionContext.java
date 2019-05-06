package com.gangbin.bookstore.web;

import java.sql.Connection;

/**
 * ����ģʽ������һ��threadlocal����
 * @author Ligangbin
 *
 */
public class ConnectionContext {
	
	private ConnectionContext(){}
	//����ģʽ
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
