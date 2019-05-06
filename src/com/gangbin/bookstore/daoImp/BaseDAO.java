package com.gangbin.bookstore.daoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.gangbin.bookstore.dao.Dao;
import com.gangbin.bookstore.db.JDBCUtils;
import com.gangbin.bookstore.utils.ReflectionUtils;
import com.gangbin.bookstore.web.ConnectionContext;
 
/*
 * 对顶层DAO接口的具体实现，如何和数据库进行交互，这里类型还没有定T，主要是
 * 借助QueryRunner的一些方法实现的
 */
public class BaseDAO<T> implements Dao<T> {

	private QueryRunner queryRunner=new QueryRunner();
	private Class<T> clazz;
	
	//class主要是获得泛型的参数类型
	public BaseDAO() {
		clazz = ReflectionUtils.getSuperGenericType(getClass());
	}
	//和update的区别是这个方法会返回一个ID值
	@Override
	public long insert(String sql, Object... args) {
	    long id=0;
		Connection connection=null;
		ResultSet resultSet=null;
		PreparedStatement preparedStatement=null;
		try {
			connection= ConnectionContext.getInstance().get();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		 //设置占位符？
			if(args!=null)
		    {
		    	for(int i=0;i<args.length;i++)
		    	{
		    		preparedStatement.setObject(i+1, args[i]);
		    	}
		    }
			//采用statement的方式执行操作
			preparedStatement.executeUpdate();
			//得到产生的主键值
		    resultSet=preparedStatement.getGeneratedKeys();
		    if(resultSet.next())
		    {
		    	id=resultSet.getLong(1);
		    }
		} catch (Exception e) {
			 e.printStackTrace();
		} 
		return id;
	}

	@Override
	public void update(String sql, Object... args) {
            Connection connection = null;
		try {
			connection= ConnectionContext.getInstance().get();
			
			//采用queryRunner的方式执行操作
			queryRunner.update(connection, sql, args);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

	@Override
	public T query(String sql, Object... args) {
             Connection connection = null;
		
		try {
			connection= ConnectionContext.getInstance().get();
			//获得一个对象，这里采用了反射的机制，传入类型参数，查到的结果以这个类型返回
			return queryRunner.query(connection, sql, new BeanHandler<>(clazz), args);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}

	@Override
	public List<T> queryForList(String sql, Object... args) {
           Connection connection = null;
		
		try {
			connection= ConnectionContext.getInstance().get();
			//获得一个list结果
			return queryRunner.query(connection, sql, new BeanListHandler<>(clazz), args);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	@Override
	public <V> V getSingleVal(String sql, Object... args) {
         Connection connection = null;
		
		try {
			connection= ConnectionContext.getInstance().get();
			//获得某一个值
			return (V)queryRunner.query(connection, sql, new ScalarHandler(), args);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}

	@Override
	public void batch(String sql, Object[]... params) {
       Connection connection = null;
		
		try {
			connection= ConnectionContext.getInstance().get();
			queryRunner.batch(connection, sql, params);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	

}
