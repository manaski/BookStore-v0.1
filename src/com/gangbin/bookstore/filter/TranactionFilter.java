package com.gangbin.bookstore.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gangbin.bookstore.db.JDBCUtils;
import com.gangbin.bookstore.web.ConnectionContext;

 

/**
 * Servlet Filter implementation class TranactionFilter
 */
@WebFilter("/*")
public class TranactionFilter implements Filter {

    /**
     * Default constructor. 
     */
    public TranactionFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		Connection connection = null;
		
		try {
			
			//1. 获取连接，还是用工具类获取连接
			connection = JDBCUtils.getConnection();
			
			//2. 开启事务
			connection.setAutoCommit(false);
			
			//3. 利用 ThreadLocal 把连接和当前线程绑定，调用set方法将connection绑定到当前线程
			ConnectionContext.getInstance().bind(connection);
			
			//4. 把请求转给目标 Servlet
			chain.doFilter(request, response);
			
			//5. 提交事务
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
			
			//6. 回滚事务 出现异常时回滚事务
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpServletRequest req = (HttpServletRequest) request;
			resp.sendRedirect(req.getContextPath() + "/error-3.jsp");
			
		} finally{
			//7. 解除绑定
			ConnectionContext.getInstance().remove();
			
			//8. 关闭连接
			JDBCUtils.release(connection);
			
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
