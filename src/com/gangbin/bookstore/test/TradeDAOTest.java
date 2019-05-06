package com.gangbin.bookstore.test;

import java.sql.Timestamp;
import java.util.Set;

import org.junit.Test;

import com.gangbin.bookstore.dao.TradeDAO;
import com.gangbin.bookstore.daoImp.TradeDAOImpl;
import com.gangbin.bookstore.domain.Trade;

 
public class TradeDAOTest {

	private TradeDAO tradeDAO = new TradeDAOImpl();
	@Test
	public void testInsertTrade() {
		Trade trade = new Trade();
		trade.setUserId(3);
		trade.setTradeTime(new Timestamp(new java.util.Date().getTime()));
		
		tradeDAO.insert(trade);
	}

	@Test
	public void testGetTradesWithUserId() {
		Set<Trade> trades = tradeDAO.getTradesWithUserId(1);
		System.out.println(trades);
	}

}
