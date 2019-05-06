package com.gangbin.bookstore.daoImp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gangbin.bookstore.dao.BookDAO;
import com.gangbin.bookstore.domain.Book;
import com.gangbin.bookstore.domain.ShoppingCartItem;
import com.gangbin.bookstore.web.CriteriaBook;
import com.gangbin.bookstore.web.Page;
 
/*
 * 这里继承了BookDAO的一些方法，对其进行实现，并且继承了BaseDAO层，里面已经对一些基础的数据库交互方法进行了实现
 */
public class BookDAOImpl extends BaseDAO<Book> implements BookDAO {
	@Override
	public Book getBook(int id) {
		String sql = "SELECT id, author, title, price, publishingDate, " +
				"salesAmount, storeNumber, remark FROM mybooks WHERE id = ?";
		return query(sql, id);
	}

	//3. 将下面两个方法的结果封装到一个page对象中返回
	@Override
	public Page<Book> getPage(CriteriaBook cb) {
		Page<Book> page = new Page<>(cb.getPageNo());
		
		page.setTotalItemNumber(getTotalBookNumber(cb));
		//校验 pageNo 的合法性
		cb.setPageNo(page.getPageNo());
		//返回一个list
		page.setList(getPageList(cb, 3));
		
		return page;
	}

	//1. 得到书本的总数
	@Override
	public long getTotalBookNumber(CriteriaBook cb) {
		String sql = "SELECT count(id) FROM mybooks WHERE price >= ? AND price <= ?";
		return getSingleVal(sql, cb.getMinPrice(), cb.getMaxPrice()); 
	}

	//2. 利用请求的页数返回指定范围内的记录
	/**
	 * MySQL 分页使用 LIMIT, 其中 fromIndex 从 0 开始。 
	 */
	@Override
	public List<Book> getPageList(CriteriaBook cb, int pageSize) {
		String sql = "SELECT id, author, title, price, publishingDate, " +
				"salesAmount, storeNumber, remark FROM mybooks " +
				"WHERE price >= ? AND price <= ? " +
				"LIMIT ?, ?";
		
		return queryForList(sql, cb.getMinPrice(), cb.getMaxPrice(), 
				(cb.getPageNo() - 1) * pageSize, pageSize);
	}

	@Override
	public int getStoreNumber(Integer id) {
		String sql = "SELECT storeNumber FROM mybooks WHERE id = ?";
		return getSingleVal(sql, id);
	}

	@Override
	public void batchUpdateStoreNumberAndSalesAmount(
			Collection<ShoppingCartItem> items) {
		String sql = "UPDATE mybooks SET salesAmount = salesAmount + ?, " +
				"storeNumber = storeNumber - ? " +
				"WHERE id = ?";
		Object [][] params = null;
		params = new Object[items.size()][3];
		List<ShoppingCartItem> scis = new ArrayList<>(items);
		for(int i = 0; i < items.size(); i++){
			params[i][0] = scis.get(i).getQuantity();
			params[i][1] = scis.get(i).getQuantity();
			params[i][2] = scis.get(i).getBook().getId();
		}
		batch(sql, params);
	}

}
