package com.gangbin.bookstore.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.gangbin.bookstore.exception.DBException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * JDBC �Ĺ�����
 *
 */
public class JDBCUtils {

	private static ComboPooledDataSource dataSource = null;
	static{
		dataSource = new ComboPooledDataSource();
	}
	
	//��ȡ���ݿ�����
	public static Connection getConnection(){  
		try {
			Connection connection= dataSource.getConnection();
		//	System.out.println(connection);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("���ݿ����Ӵ���!");
		}
	}
 
	//�ر����ݿ�����
	public static void release(Connection connection) {
		try {
			if(connection != null){
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("���ݿ����Ӵ���!");
		}
	}
	
	//�ر����ݿ�����
	public static void release(ResultSet rs, Statement statement) {
		try {
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("���ݿ����Ӵ���!");
		}
		
		try {
			if(statement != null){
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("���ݿ����Ӵ���!");
		}
	}
	
}