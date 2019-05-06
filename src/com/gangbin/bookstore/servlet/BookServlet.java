package com.gangbin.bookstore.servlet;

 
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.gangbin.bookstore.domain.Book;
import com.gangbin.bookstore.domain.ShoppingCart;
import com.gangbin.bookstore.domain.ShoppingCartItem;
import com.gangbin.bookstore.domain.User;
import com.gangbin.bookstore.service.AccountService;
import com.gangbin.bookstore.service.BookService;
import com.gangbin.bookstore.service.UserService;
import com.gangbin.bookstore.web.BookStoreWebUtils;
import com.gangbin.bookstore.web.CriteriaBook;
import com.gangbin.bookstore.web.Page;
import com.google.gson.Gson;
 

 
@WebServlet("/bookServlet")
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    BookService bookService=new BookService();
   
    public BookServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 doPost(request, response);
	}

 
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	       String methodName=request.getParameter("method");
	       Method method;
	       //根据函数的名字来获得函数，并进行调用，这里也是反射的机制
	       try {
	        method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
	        method.invoke(this, request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}

	}
	/*
	 * 下面这个方法中，用到了gson的方法，调用这个方法之后会回传给gson一个map数组，然后gson通过
	 * 这个map数组得到最新的状态值，对页面进行更新，这个功能要以来一个导入的新包。
	 */
	public void updateItemQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idString=request.getParameter("id");
		String quantityValstr=request.getParameter("quantity");
		//上面这两个参数是从请求参数中获得的，需要更新的对象和新的值
		int id=-1;
		int quantity=-1;
		try {
			id=Integer.valueOf(idString);
			quantity=Integer.valueOf(quantityValstr);
		} catch (Exception e) {
			System.out.println("异常");
		}
		ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
		if(id > 0 && quantity > 0)
			bookService.updateItemQuantity(sc, id, quantity);
		
		//5. 传回 JSON 数据: bookNumber:xx, totalMoney
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("bookNumber", sc.getBookNumber());
		result.put("totalMoney", sc.getTotalMoney());
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(result);
		response.setContentType("text/javascript");
		response.getWriter().print(jsonStr);

	}
	private UserService userService = new UserService();
	private AccountService accountService=new AccountService();
	public void cash(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. 简单验证: 验证表单域的值是否符合基本的规范: 是否为空, 是否可以转为 int 类型, 是否是一个 email. 不需要进行查询
				//数据库或调用任何的业务方法.
				String username = request.getParameter("username");
				String accountId = request.getParameter("accountId");
				
				StringBuffer errors = validateFormField(username, accountId);
				
				//表单验证通过。 
				if(errors.toString().equals("")){
					errors = validateUser(username, accountId);
					
					//用户名和账号验证通过
					if(errors.toString().equals("")){
						errors = validateBookStoreNumber(request);
						
						//库存验证通过
						if(errors.toString().equals("")){
							errors = validateBalance(request, accountId);
						}
					}
				}
				//验证不通过进入错误界面
				if(!errors.toString().equals("")){
					request.setAttribute("errors", errors);
					request.getRequestDispatcher("/WEB-INF/pages/cash.jsp").forward(request, response);
					return;
				}
				
				//验证通过执行具体的逻辑操作
				bookService.cash(BookStoreWebUtils.getShoppingCart(request), username, accountId); 
				response.sendRedirect(request.getContextPath() + "/success.jsp");
		
		
	}
	
	   //验证表单域是否符合基本的规则: 是否为空. 
		public StringBuffer validateFormField(String username, String accountId){
			StringBuffer errors = new StringBuffer("");
			
			if(username == null || username.trim().equals("")){
				errors.append("用户名不能为空<br>");
			}
			
			if(accountId == null || accountId.trim().equals("")){
				errors.append("账号不能为空");			
			}
			
			return errors;
		}
		
		//校验用户信息
		public StringBuffer validateUser(String username,String  accountId)
		{
			StringBuffer error=new StringBuffer("");
			boolean flag=false;
			User user=userService.getUserByUserName(username);
			int account=-1;
			if(user!=null)
			{
		     account=user.getAccountId();
		     if(accountId.trim().equals(""+account))
		    	 flag=true;
			}
			if(!flag)
				error.append("账号姓名不匹配");
			 return error;
		}
		
		
		
		public StringBuffer validateBalance(HttpServletRequest request, String accountId)
		{
			
			StringBuffer error=new StringBuffer("");
			ShoppingCart shoppingCart= BookStoreWebUtils.getShoppingCart(request);
			float total=shoppingCart.getTotalMoney();
			int id=-1;
			try {
				id=Integer.valueOf(accountId);
			} catch (NumberFormatException e) {	 
			}
			
			float balance=accountService.getAccount(id).getBalance();
			if(balance<total)
				error.append("余额不足");
			 return error;
		}
		//校验库存是够足够
		public StringBuffer validateBookStoreNumber(HttpServletRequest request)
		{
			StringBuffer error=new StringBuffer("");
			ShoppingCart shoppingCart= BookStoreWebUtils.getShoppingCart(request);
			for(ShoppingCartItem sc:shoppingCart.getItems())
			{
				Book book=sc.getBook();
				int quantity=sc.getQuantity();
				int bookstore=book.getStoreNumber();
				if(bookstore<quantity)
					error.append(book.getTitle()+" "+"库存不足<br>");
			}

			return error;
		}
		
	public void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   //获取商品ID
		String idString=request.getParameter("id");
		boolean flag=false;
	    int id=0;
			  try {
				id= Integer.valueOf(idString);
			} catch (NumberFormatException e) {
			}
	    //调用服务层方法添加进购物车
	    if(id>0)
	    {
	    	//获取购物车对象
		     ShoppingCart shoppingCart=BookStoreWebUtils.getShoppingCart(request);
		     //添加购物车内
	    	 flag=bookService.addToCart(id,shoppingCart);
	    }
	    if(flag)
	    {
	    	getBooks(request, response);
	    	return;
	    }
	    response.sendRedirect(request.getContextPath()+"/error-2.jsp");
	    
	   
	
	}
	public void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idString=request.getParameter("id");
		int id=-1;
		try {
			id=Integer.valueOf(idString);
		} catch (NumberFormatException e) {}
		HttpSession session=request.getSession();
		ShoppingCart shoppingCart=(ShoppingCart) session.getAttribute("ShoppingCart");
		bookService.removeItemFromShoppingCart(shoppingCart, id);
		if(shoppingCart.isEmpty())
		{
			 request.getRequestDispatcher("/WEB-INF/pages/emptycart.jsp").forward(request, response);	
			 return;
		}
	   
		request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
	}
	protected void clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("clear方法");
		ShoppingCart shoppingCart=BookStoreWebUtils.getShoppingCart(request);
		bookService.clear(shoppingCart);
		System.out.println("清空");
		request.getRequestDispatcher("/WEB-INF/pages/emptycart.jsp").forward(request, response);
	}
	protected void forwardPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		request.getRequestDispatcher("/WEB-INF/pages/" + page + ".jsp").forward(request, response);
	}
	protected void userPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("users.jsp").forward(request, response);
	}
	protected void getBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String idString=request.getParameter("id");
		 int id=-1;
		 try {
			id=Integer.valueOf(idString);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 Book book=null;
		 if(id>0)
		 {
			 book=bookService.getBook(id);
		 }else
		 {
			 response.sendRedirect(request.getContextPath()+"/error-1.jsp");
		 }
		 if(book==null)
			 response.sendRedirect(request.getContextPath()+"/error-2.jsp");
		  request.setAttribute("book", book);
	       try {
			request.getRequestDispatcher("/WEB-INF/pages/book.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	protected void getBooks(HttpServletRequest request,HttpServletResponse response)
	{
		   String pageNostr=request.getParameter("pageNo");
	       String minPricestr=request.getParameter("minPrice");
	       String maxPricestr=request.getParameter("maxPrice");
	       //从请求中得到参数
	       int pageNo=1;
	       int minPrice=0;
	       int maxPrice=Integer.MAX_VALUE;
	       //得到三个参数用来构造查询对象
	       try {
	    	   pageNo=Integer.valueOf(pageNostr);
		} catch (Exception e) {
			System.out.println("异常1");
			
		}
	       try {
	    	   minPrice=Integer.valueOf(minPricestr);
	    	   
		} catch (Exception e) {
			System.out.println("异常2");
		
		}
	       try {
	    	   maxPrice=Integer.valueOf(maxPricestr);
		} catch (Exception e) {
			System.out.println("异常3");
			
		}
	      CriteriaBook criteriaBook=new CriteriaBook(minPrice, maxPrice, pageNo);
	       //得到查询的page结果
	       Page<Book> page=bookService.getPage(criteriaBook);
	       request.setAttribute("bookpage", page);
	       try {
			request.getRequestDispatcher("/WEB-INF/pages/books.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
