package com.gangbin.bookstore.service;

import com.gangbin.bookstore.dao.AccountDAO;
import com.gangbin.bookstore.daoImp.AccountDAOImpl;
import com.gangbin.bookstore.domain.Account;

/**
 * ��Ҫ�ǻ���˻��Ĳ���
 * @author Ligangbin
 *
 */
public class AccountService {
	
	private AccountDAO accountDAO = new AccountDAOImpl();
	
	public Account getAccount(int accountId){
		return accountDAO.get(accountId);
	}
	
}
