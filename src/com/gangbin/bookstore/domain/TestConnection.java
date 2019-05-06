package com.gangbin.bookstore.domain;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TestConnection {
	
	public void getCon() throws SQLException
	{
		ComboPooledDataSource dataSource =null;
		dataSource= new ComboPooledDataSource();
		Connection connection=dataSource.getConnection();
		System.out.println(connection);
	}

}
