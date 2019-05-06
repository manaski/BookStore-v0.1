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
 * ��Ҫ�Ǻ�book����صĸ��ֲ��������й�����صĲ���
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
		//����IDȥ���ݿ��ѯͼ�����
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
		
		//1. ���� mybooks ���ݱ���ؼ�¼�� salesamount �� storenumber
		  bookDAO.batchUpdateStoreNumberAndSalesAmount(shoppingCart.getItems());
	       
		  //  int d=10/0;         �����쳣����
		//2. ���� account ���ݱ�� balance
	      int id=-1;
	      try {
			id=Integer.valueOf(accountId);
		} catch (NumberFormatException e) {
		}
	    accountDAO.updateBalance(id, shoppingCart.getTotalMoney());
		
		//3. �� trade ���ݱ����һ����¼�����׼�¼�Ƚϼ򵥣�ͨ������Id�����Ľ���Item���а�
	    Trade trade=new Trade();
	    trade.setUserId(userDAO.getUser(username).getUserId());
	    trade.setTradeTime(new Timestamp(new java.util.Date().getTime()));
		tradeDAO.insert(trade);
		
		//4. �� tradeitem ���ݱ���� n ����¼
		//�ѹ��ﳵ�����Ʒ��װ��Items���ϣ�Ȼ�������������ݿ���
		Collection<TradeItem> items = new ArrayList<>();
		for(ShoppingCartItem sci: shoppingCart.getItems()){
			TradeItem tradeItem = new TradeItem();
			tradeItem.setBookId(sci.getBook().getId());
			tradeItem.setQuantity(sci.getQuantity());
			tradeItem.setTradeId(trade.getTradeId());
			items.add(tradeItem);
		}
		
		tradeItemDAO.batchSave(items);
		//5. ��չ��ﳵ
	     shoppingCart.clear();
		
		
	}
}
