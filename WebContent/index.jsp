<%@page import="com.gangbin.bookstore.domain.TestConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 
<%
 response.sendRedirect(request.getContextPath()+"/bookServlet?method=getBooks");
%>
 