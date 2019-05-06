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
 * �Զ���DAO�ӿڵľ���ʵ�֣���κ����ݿ���н������������ͻ�û�ж�T����Ҫ��
 * ����QueryRunner��һЩ����ʵ�ֵ�
 */
public class BaseDAO<T> implements Dao<T> {

	private QueryRunner queryRunner=new QueryRunner();
	private Class<T> clazz;
	
	//class��Ҫ�ǻ�÷��͵Ĳ�������
	public BaseDAO() {
		clazz = ReflectionUtils.getSuperGenericType(getClass());
	}
	//��update����������������᷵��һ��IDֵ
	@Override
	public long insert(String sql, Object... args) {
	    long id=0;
		Connection connection=null;
		ResultSet resultSet=null;
		PreparedStatement preparedStatement=null;
		try {
			connection= ConnectionContext.getInstance().get();
			preparedStatement = (PreparedStatement) connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		 //����ռλ����
			if(args!=null)
		    {
		    	for(int i=0;i<args.length;i++)
		    	{
		    		preparedStatement.setObject(i+1, args[i]);
		    	}
		    }
			//����statement�ķ�ʽִ�в���
			preparedStatement.executeUpdate();
			//�õ�����������ֵ
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
			
			//����queryRunner�ķ�ʽִ�в���
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
			//���һ��������������˷���Ļ��ƣ��������Ͳ������鵽�Ľ����������ͷ���
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
			//���һ��list���
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
			//���ĳһ��ֵ
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
