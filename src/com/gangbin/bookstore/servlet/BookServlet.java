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
	       //���ݺ�������������ú����������е��ã�����Ҳ�Ƿ���Ļ���
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
	 * ������������У��õ���gson�ķ����������������֮���ش���gsonһ��map���飬Ȼ��gsonͨ��
	 * ���map����õ����µ�״ֵ̬����ҳ����и��£��������Ҫ����һ��������°���
	 */
	public void updateItemQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idString=request.getParameter("id");
		String quantityValstr=request.getParameter("quantity");
		//���������������Ǵ���������л�õģ���Ҫ���µĶ�����µ�ֵ
		int id=-1;
		int quantity=-1;
		try {
			id=Integer.valueOf(idString);
			quantity=Integer.valueOf(quantityValstr);
		} catch (Exception e) {
			System.out.println("�쳣");
		}
		ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
		if(id > 0 && quantity > 0)
			bookService.updateItemQuantity(sc, id, quantity);
		
		//5. ���� JSON ����: bookNumber:xx, totalMoney
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
		//1. ����֤: ��֤�����ֵ�Ƿ���ϻ����Ĺ淶: �Ƿ�Ϊ��, �Ƿ����תΪ int ����, �Ƿ���һ�� email. ����Ҫ���в�ѯ
				//���ݿ������κε�ҵ�񷽷�.
				String username = request.getParameter("username");
				String accountId = request.getParameter("accountId");
				
				StringBuffer errors = validateFormField(username, accountId);
				
				//����֤ͨ���� 
				if(errors.toString().equals("")){
					errors = validateUser(username, accountId);
					
					//�û������˺���֤ͨ��
					if(errors.toString().equals("")){
						errors = validateBookStoreNumber(request);
						
						//�����֤ͨ��
						if(errors.toString().equals("")){
							errors = validateBalance(request, accountId);
						}
					}
				}
				//��֤��ͨ������������
				if(!errors.toString().equals("")){
					request.setAttribute("errors", errors);
					request.getRequestDispatcher("/WEB-INF/pages/cash.jsp").forward(request, response);
					return;
				}
				
				//��֤ͨ��ִ�о�����߼�����
				bookService.cash(BookStoreWebUtils.getShoppingCart(request), username, accountId); 
				response.sendRedirect(request.getContextPath() + "/success.jsp");
		
		
	}
	
	   //��֤�����Ƿ���ϻ����Ĺ���: �Ƿ�Ϊ��. 
		public StringBuffer validateFormField(String username, String accountId){
			StringBuffer errors = new StringBuffer("");
			
			if(username == null || username.trim().equals("")){
				errors.append("�û�������Ϊ��<br>");
			}
			
			if(accountId == null || accountId.trim().equals("")){
				errors.append("�˺Ų���Ϊ��");			
			}
			
			return errors;
		}
		
		//У���û���Ϣ
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
				error.append("�˺�������ƥ��");
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
				error.append("����");
			 return error;
		}
		//У�����ǹ��㹻
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
					error.append(book.getTitle()+" "+"��治��<br>");
			}

			return error;
		}
		
	public void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   //��ȡ��ƷID
		String idString=request.getParameter("id");
		boolean flag=false;
	    int id=0;
			  try {
				id= Integer.valueOf(idString);
			} catch (NumberFormatException e) {
			}
	    //���÷���㷽����ӽ����ﳵ
	    if(id>0)
	    {
	    	//��ȡ���ﳵ����
		     ShoppingCart shoppingCart=BookStoreWebUtils.getShoppingCart(request);
		     //��ӹ��ﳵ��
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
		System.out.println("clear����");
		ShoppingCart shoppingCart=BookStoreWebUtils.getShoppingCart(request);
		bookService.clear(shoppingCart);
		System.out.println("���");
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
	       //�������еõ�����
	       int pageNo=1;
	       int minPrice=0;
	       int maxPrice=Integer.MAX_VALUE;
	       //�õ������������������ѯ����
	       try {
	    	   pageNo=Integer.valueOf(pageNostr);
		} catch (Exception e) {
			System.out.println("�쳣1");
			
		}
	       try {
	    	   minPrice=Integer.valueOf(minPricestr);
	    	   
		} catch (Exception e) {
			System.out.println("�쳣2");
		
		}
	       try {
	    	   maxPrice=Integer.valueOf(maxPricestr);
		} catch (Exception e) {
			System.out.println("�쳣3");
			
		}
	      CriteriaBook criteriaBook=new CriteriaBook(minPrice, maxPrice, pageNo);
	       //�õ���ѯ��page���
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
