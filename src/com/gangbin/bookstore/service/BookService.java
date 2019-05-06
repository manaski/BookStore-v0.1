package com.gangbin.bookstore.service;

 
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import com.gangbin.bookstore.dao.AccountDAO;
import com.gangbin.bookstore.dao.BookDAO;
import com.gangbin.bookstore.dao.TradeDAO;
import com.gangbin.bookstore.dao.TradeItemDAO;
import com.gangbin.bookstore.dao.UserDAO;
import com.gangbin.bookstore.daoImp.AccountDAOImpl;
import com.gangbin.bookstore.daoImp.BookDAOImpl;
import com.gangbin.bookstore.daoImp.TradeDAOImpl;
import com.gangbin.bookstore.daoImp.TradeItemDAOImpl;
import com.gangbin.bookstore.daoImp.UserDAOImpl;
import com.gangbin.bookstore.domain.Book;
import com.gangbin.bookstore.domain.ShoppingCart;
import com.gangbin.bookstore.domain.ShoppingCartItem;
import com.gangbin.bookstore.domain.Trade;
import com.gangbin.bookstore.domain.TradeItem;
import com.gangbin.bookstore.web.CriteriaBook;
import com.gangbin.bookstore.web.Page;

/**
 * 主要是和book类相关的各种操作，还有购物相关的操作
 * @author Ligangbin
 *
 */
public class BookService {
	private BookDAO bookDAO = new BookDAOImpl();
	public Page<Book> getPage(CriteriaBook criteriaBook)
	{
		Page<Book> page=bookDAO.getPage(criteriaBook);
		return page;
	}
	
	public Book getBook(int id)
	{
		Book page=bookDAO.getBook(id);
		return page;
	}
	
	public void removeItemFromShoppingCart(ShoppingCart sc, int id) {
		sc.removeItem(id);
	}
	
	public boolean addToCart(int id,ShoppingCart shoppingCart)
	{
		//根据ID去数据库查询图书对象
		Book book=bookDAO.getBook(id);
		
		if(book!=null)
		{
			shoppingCart.addBook(book);
			return true;
		}else{
			return false;
			
		}
	}

	public void clear(ShoppingCart shoppingCart) {
		 shoppingCart.clear();
	}

	public void updateItemQuantity(ShoppingCart sc, int id, int quantity) {
		 sc.updateItemQuantity(id, quantity);
		
	}

	private AccountDAO accountDAO = new AccountDAOImpl();
	private TradeDAO tradeDAO = new TradeDAOImpl();
	private UserDAO userDAO = new UserDAOImpl();
	private TradeItemDAO tradeItemDAO=new TradeItemDAOImpl();
	 
	
	public void cash(ShoppingCart shoppingCart, String username, String accountId) {
		
		//1. 更新 mybooks 数据表相关记录的 salesamount 和 storenumber
		  bookDAO.batchUpdateStoreNumberAndSalesAmount(shoppingCart.getItems());
	       
		  //  int d=10/0;         事务异常测试
		//2. 更新 account 数据表的 balance
	      int id=-1;
	      try {
			id=Integer.valueOf(accountId);
		} catch (NumberFormatException e) {
		}
	    accountDAO.updateBalance(id, shoppingCart.getTotalMoney());
		
		//3. 向 trade 数据表插入一条记录，交易记录比较简单，通过交易Id与具体的交易Item进行绑定
	    Trade trade=new Trade();
	    trade.setUserId(userDAO.getUser(username).getUserId());
	    trade.setTradeTime(new Timestamp(new java.util.Date().getTime()));
		tradeDAO.insert(trade);
		
		//4. 向 tradeitem 数据表插入 n 条记录
		//把购物车里的商品包装成Items集合，然后批量插入数据库中
		Collection<TradeItem> items = new ArrayList<>();
		for(ShoppingCartItem sci: shoppingCart.getItems()){
			TradeItem tradeItem = new TradeItem();
			tradeItem.setBookId(sci.getBook().getId());
			tradeItem.setQuantity(sci.getQuantity());
			tradeItem.setTradeId(trade.getTradeId());
			items.add(tradeItem);
		}
		
		tradeItemDAO.batchSave(items);
		//5. 清空购物车
	     shoppingCart.clear();
		
		
	}
}
