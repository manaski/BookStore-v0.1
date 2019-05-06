package com.gangbin.bookstore.test;

import org.junit.Test;

import com.gangbin.bookstore.daoImp.AccountDAOImpl;
import com.gangbin.bookstore.domain.Account;

public class AccountDAOTest {

	AccountDAOImpl account=new AccountDAOImpl();
	  
	@Test
	public void testGet() {
		 Account a=account.get(1);
		 System.out.println(a);
	}

	@Test
	public void testUpdateBalance() {
		
		 Account a=account.get(1);
		 System.out.println(a);
		 account.updateBalance(1, 100);
		  a=account.get(1);
		 System.out.println(a);
	}

}
