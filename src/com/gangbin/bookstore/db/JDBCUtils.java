package com.gangbin.bookstore.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.gangbin.bookstore.exception.DBException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * JDBC 的工具类
 *
 */
public class JDBCUtils {

	private static ComboPooledDataSource dataSource = null;
	static{
		dataSource = new ComboPooledDataSource();
	}
	
	//获取数据库连接
	public static Connection getConnection(){  
		try {
			Connection connection= dataSource.getConnection();
		//	System.out.println(connection);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("数据库连接错误!");
		}
	}
 
	//关闭数据库连接
	public static void release(Connection connection) {
		try {
			if(connection != null){
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("数据库连接错误!");
		}
	}
	
	//关闭数据库连接
	public static void release(ResultSet rs, Statement statement) {
		try {
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("数据库连接错误!");
		}
		
		try {
			if(statement != null){
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("数据库连接错误!");
		}
	}
	
}
