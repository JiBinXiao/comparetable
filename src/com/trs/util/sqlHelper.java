package com.trs.util;
/**
 * 操作数据库的一个工具类
 */
import java.sql.*;


public class sqlHelper {
	//定义需要的变量
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
				System.out.println("缺少mysql-connector-java jar包");
				
				
			}
			catch (SQLException e) {
				
				System.out.println("url:"+url+",database:"+database+",user:"+user+",password:"+password+"\t连接数据库参数有误，请检查");
				
			}
	
	}
	
	//执行查询，有返回集的
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
			
			System.out.println("sql:"+sql+"\t执行此语句有误，请检查！");
			
		}
		
		return rs;
		
	}
	
	//无返回集的，执行增删改
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
			System.out.println("失败");
			b=false;
			
		}finally {
			
		}
		
		return b;
		
	}
	



	
	
	//关闭资源的方法
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
