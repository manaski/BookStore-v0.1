package com.gangbin.bookstore.test;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.gangbin.bookstore.db.JDBCUtils;
 
public class TestConnect {

	@Test
	public void test() throws SQLException {
		Connection connection=null; 
//		try {
//			ComboPooledDataSource dataSource = new ComboPooledDataSource();
//			dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
//			dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/nogizaka46?serverTimezone=GMT");
//			dataSource.setUser("root");
//			dataSource.setPassword("nogizaka46");
//			connection = (Connection) dataSource.getConnection();
//			System.out.println(connection);
//		} catch (Exception e) {
//			 e.printStackTrace();
//		}finally{
//			 
//		}
		connection=JDBCUtils.getConnection();
		
		 
	 
	}

}
