package com.trs.util;
/**
 * �������ݿ��һ��������
 */
import java.sql.*;


public class sqlHelper {
	//������Ҫ�ı���
	private  Connection ct=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;
	
	private String driver="com.mysql.jdbc.Driver";
	private String url="";
	private String database="";
	private String user="";
	private String password="";
	
	
	
	public void initConnetction(String url,String database,String user,String password) {
	
		
			try {
				Class.forName(driver);
				ct=DriverManager.getConnection(url+database,user,password);
			}catch (ClassNotFoundException e) {
				System.out.println("ȱ��mysql-connector-java jar��");
				
				
			}
			catch (SQLException e) {
				
				System.out.println("url:"+url+",database:"+database+",user:"+user+",password:"+password+"\t�������ݿ������������");
				
			}
	
	}
	
	//ִ�в�ѯ���з��ؼ���
	public  ResultSet excuteQuery(String sql,String parameters[]){
		
		try {
			ps=ct.prepareStatement(sql);
			
			if(parameters==null||parameters.equals("")){
				rs=ps.executeQuery();
			}else{
				for (int i = 0; i < parameters.length; i++) {
					ps.setObject(i+1, parameters[i]);
				}
				rs=ps.executeQuery();	
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			System.out.println("sql:"+sql+"\tִ�д�����������飡");
			
		}
		
		return rs;
		
	}
	
	//�޷��ؼ��ģ�ִ����ɾ��
	public  boolean excuteUpdate(String sql,String parameters[]){
		boolean b=false;
		try {
		
			ps=ct.prepareStatement(sql);
			
			if(parameters==null||parameters.equals("")){
				if(ps.executeUpdate()==1) b=true;
			}else{
				for (int i = 0; i < parameters.length; i++) {
					ps.setObject(i+1, parameters[i]);
				}
				if(ps.executeUpdate()==1) b=true;		 
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ʧ��");
			b=false;
			
		}finally {
			
		}
		
		return b;
		
	}
	



	
	
	//�ر���Դ�ķ���
	public  void closesqlHelper(){
		try {
			if(rs!=null){
			rs.close();
			rs=null;
		}
		if(ps!=null){
			ps.close();
			ps=null;
		}
		if(ct!=null){
			ct.close();
			ct=null;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
