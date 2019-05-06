package com.gangbin.bookstore.test;

 

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import com.gangbin.bookstore.daoImp.BookDAOImpl;
import com.gangbin.bookstore.db.JDBCUtils;
import com.gangbin.bookstore.domain.Book;
 

 

public class BaseDAOTest {
	
	@Test
	public void test() throws SQLException {
		Connection connection=null; 
		connection=JDBCUtils.getConnection();
	}
	private BookDAOImpl bookDAOImpl = new BookDAOImpl();
	
	@Test
	public void  TimeTest() throws ParseException {
		Timestamp timeStamep = new Timestamp(new java.util.Date().getTime());
		System.out.println(timeStamep);
	}
	@Test
	public void testInsert() {
		String sql = "INSERT INTO trade (userid, tradetime) VALUES(? ,?)";
		long id = bookDAOImpl.insert(sql, 1, new Timestamp(new java.util.Date().getTime()));
		System.out.println(id); 
	}

	@Test
	public void testUpdate() {
		String sql = "UPDATE mybooks SET salesamount = ? WHERE id = ?";
		bookDAOImpl.update(sql, 10, 1);
	}

	@Test
	public void testQuery() {
		String sql = "SELECT id, author, title, price, publishingDate, " +
				"salesAmount, storeNumber, remark FROM mybooks WHERE id = ?";
		
		Book book = bookDAOImpl.query(sql, 4);
		System.out.println(book);
	}

	@Test
	public void testQueryForList() {
		String sql = "SELECT id, author, title, price, publishingDate, " +
				"salesAmount, storeNumber, remark FROM mybooks WHERE id < ?";
		
		List<Book> books = bookDAOImpl.queryForList(sql, 4);
		System.out.println(books);
	}

	@Test
	public void testGetSingleVal() {
		String sql = "SELECT count(id) FROM mybooks";
		
		long count = bookDAOImpl.getSingleVal(sql);
		System.out.println(count); 
	}

	@Test
	public void testBatch() {
		String sql = "UPDATE mybooks SET salesAmount = ?, storeNumber = ? " +
				"WHERE id = ?";
		
		bookDAOImpl.batch(sql, new Object[]{1, 1, 1}, new Object[]{2, 2, 2}, new Object[]{3, 3, 3}); 
	}

}
