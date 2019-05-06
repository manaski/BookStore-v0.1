package com.gangbin.bookstore.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gangbin.bookstore.domain.User;
import com.gangbin.bookstore.service.UserService;

@WebServlet("/userServlet")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 //获得username参数
		String name=request.getParameter("username");
		//查询交易ID集合
		UserService userService=new UserService();
		User user=userService.getUserWithTrades(name);
		if(user==null)
		{
			response.sendRedirect(request.getContextPath()+"/error-1.jsp");
			return;
		}
			
		 
		request.setAttribute("user", user);
		System.out.println("设置好了属性");
		request.getRequestDispatcher("/WEB-INF/pages/trades.jsp").forward(request, response);
		
		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
