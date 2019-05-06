package com.gangbin.bookstore.db;

import java.sql.Connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class Main {
	
	 public static void main(String[] args) {
		 Connection connection=null; 
			try {
				ComboPooledDataSource dataSource = new ComboPooledDataSource();
				dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
				dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/nogizaka46?serverTimezone=GMT");
				dataSource.setUser("root");
				dataSource.setPassword("nogizaka46");
				connection = (Connection) dataSource.getConnection();
				System.out.println(connection);
			} catch (Exception e) {
				 e.printStackTrace();
			} 
		
	}

}
