package com.gangbin.bookstore.test;

import org.junit.Test;

import com.gangbin.bookstore.daoImp.UserDAOImpl;
import com.gangbin.bookstore.domain.User;

public class UserDaoTest {
	UserDAOImpl user=new UserDAOImpl();

	@Test
	public void testGetUser() {
		User users=user.getUser("Tom");
		System.out.println(users);
	}

}
