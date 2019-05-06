package com.gangbin.bookstore.daoImp;

import java.util.LinkedHashSet;
import java.util.Set;

import com.gangbin.bookstore.dao.TradeDAO;
import com.gangbin.bookstore.domain.Trade;

 

public class TradeDAOImpl extends BaseDAO<Trade> implements TradeDAO {

	@Override
	public void insert(Trade trade) {
		String sql = "INSERT INTO trade (userid, tradetime) VALUES " +
				"(?, ?)";
		//这个方法会返回一个Id，这里直接插入到Trade对象属性
		long tradeId = insert(sql, trade.getUserId(), trade.getTradeTime());
		trade.setTradeId((int)tradeId);
	}

	@Override
	public Set<Trade> getTradesWithUserId(Integer userId) {
		String sql = "SELECT tradeId, userId, tradeTime FROM trade " +
				"WHERE userId = ? ORDER BY tradeTime DESC";
		return new LinkedHashSet(queryForList(sql, userId));
	}

}
