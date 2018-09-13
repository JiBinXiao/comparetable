/**
 * 
 */
package com.trs.util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;



/**
 * @author xjb
 * 2018��9��12��
 */
public class SystemConfig {
	
	private  String configname = "comparetable.ini";
	private String currerntDirPath="";
	private int limit=10000;
	
	private  Properties properties =new Properties(); 
	private  FileInputStream fis = null; // ��
	
	private  String driver="com.mysql.jdbc.Driver";
	
	private  String url1="";
	private String database1="";
	private String user1="";
	private String password1="";
	private String tableName1="";
	private String idParamName1="";
	private int handleParamIndex1=0;
	private String splitString1="";
	
	private String paramName1="";
	private String sql1="";
	sqlHelper sqlHelper1=null;
	Map<String ,String> reMap1=new HashMap<String,String>();
	
	
	private String url2="url2";
	private String database2="database2";
	private String user2="user2";
	private String password2="password2";
	private String tableName2="";
	private String idParamName2="";
	private int handleParamIndex2=0;
	private String splitString2="";
	private String paramName2="";
	private String sql2="";
	sqlHelper sqlHelper2=null;
	Map<String ,String> reMap2=new HashMap<String,String>();
	
	//������Դ�ļ�
	public SystemConfig() {
		File currerntDir=new File("");
		currerntDirPath = currerntDir.getAbsolutePath();
		String configpath = currerntDirPath + File.separator + configname;
		try {
			fis = new FileInputStream(configpath);
			properties.load(fis);
		} catch (Exception e) {
			System.out.println("���ø�Ŀ¼Ϊ��"+currerntDir);
			System.out.println("������Դ������ȷ����������Ϊcomparetable.ini");
			e.printStackTrace();
		}
	}
	
	
	public  void  init() {
		System.out.println("��ʼ����1��Դ");
		url1 = properties.getProperty("url1");
		database1 = properties.getProperty("database1");
		user1 = properties.getProperty("user1");
		password1 = properties.getProperty("password1");
		tableName1 = properties.getProperty("tableName1");
		idParamName1 = properties.getProperty("idParamName1");
		
		sql1 = properties.getProperty("sql1");
		paramName1 = properties.getProperty("paramName1");
		handleParamIndex1 = Integer.parseInt(properties.getProperty("handleParamIndex1"));
		splitString1 = properties.getProperty("splitString1");
		
		
		
		sqlHelper1= new sqlHelper();
		sqlHelper1.initConnetction(url1, database1, user1, password1);
		//��ѯ������
		String sqlselectString1="select count(*) from "+tableName1;
		int count1 = getCount(sqlHelper1,sqlselectString1);
		System.out.println(tableName1+"�ܹ�:"+count1);
		
		//�����β�ѯ
		if(count1 > limit) {
			for (int i = 0; i < count1; i = i+limit) {
				System.out.println(i);
				int start = i;
				String limitString=sql1+" limit "+start+","+limit;
				Map<String, String> tempreMap1 = getData(sqlHelper1,limitString,idParamName1,paramName1,handleParamIndex1,splitString1);
				reMap1.putAll(tempreMap1);
			}
		}else {
			
			Map<String, String> tempreMap1 = getData(sqlHelper1,sql1,idParamName1,paramName1,handleParamIndex1,splitString1);
			reMap1.putAll(tempreMap1);
		}

		sqlHelper1.closesqlHelper();
		System.out.println("����1��Դ���");
		
		System.out.println("-----------------");
		
		//-----------------------����ķָ���---------------------------------------------------------
		
		System.out.println("��ʼ����2��Դ");
		url2 = properties.getProperty("url2");
		database2 = properties.getProperty("database2");
		user2 = properties.getProperty("user2");
		password2 = properties.getProperty("password2");
		tableName2 = properties.getProperty("tableName2");
		idParamName2 = properties.getProperty("idParamName2");
		handleParamIndex2 = Integer.parseInt(properties.getProperty("handleParamIndex2"));
		sql2 = properties.getProperty("sql2");
		paramName2 = properties.getProperty("paramName2");
		splitString2 = properties.getProperty("splitString2");
		//��������
		sqlHelper2= new sqlHelper();
		sqlHelper2.initConnetction(url2, database2, user2, password2);
		String sqlselectString2="select count(*) from "+tableName2;
		
		int count2 = getCount(sqlHelper2,sqlselectString2);
		System.out.println(tableName2+"�ܹ�:"+count2);
		//�����β�ѯ
		if(count2 > limit) {
			for (int i = 0; i < count2; i = i+limit) {
				System.out.println(i);
				int start = i;
				String limitString=sql2+" limit "+start+","+limit;
				Map<String, String> tempreMap2 = getData(sqlHelper2,limitString,idParamName2,paramName2,handleParamIndex2,splitString2);
				reMap2.putAll(tempreMap2);
			}
		}else {
			Map<String, String> tempreMap2 = getData(sqlHelper2,sql2,idParamName2,paramName2,handleParamIndex2,splitString2);
			reMap2.putAll(tempreMap2);
		}
		
		
		sqlHelper2.closesqlHelper();
		System.out.println("����2��Դ���");
		System.out.println("----------------");
		
		
		//-----------------------����ķָ���---------------------------------------------------------
		
		System.out.println("��ʼ�ȶ�");
		//�ȶ����� �õ������ re1�� ��ɾ�� ���ݼ���
		Map<String, List<String>> differ1=compareData(reMap1,reMap2);
		//������ļ�
		FileUtil.writeStringByBufferedWriter(differ1.get("add"), currerntDirPath+File.separator+tableName1+".add");
		FileUtil.writeStringByBufferedWriter(differ1.get("delete"), currerntDirPath+File.separator+tableName1+".delete");
		FileUtil.writeStringByBufferedWriter(differ1.get("update"), currerntDirPath+File.separator+tableName1+".update");
		
		System.out.println("�ȶԽ���");
		
	}
	
	
	
	


	/**
	 * �õ����������
	 * @param sqlHelper12
	 * @param sqlselectString1
	 * @return
	 */
	private int getCount(sqlHelper sqlHelper, String sqlselectString1) {
		int count=0;
		ResultSet rs= sqlHelper.excuteQuery(sqlselectString1, null);
		try {
			while(rs.next()) {
				count=rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ִ�� "+sqlselectString1+" ����");
			e.printStackTrace();
		}
		
		return count;
	}


	/**
	 * �ȶ����� ������� re1 ����ɾ�� ����
	 * @param re12
	 * @param re22
	 * @return
	 */
	private Map<String,List<String>> compareData(Map<String ,String> re1, Map<String ,String> re2) {
	
		Map<String,List<String>> differ=new HashMap<String, List<String>>();
		List<String>  add=new ArrayList<String>();
		List<String>  delete=new ArrayList<String>();
		List<String>  update=new ArrayList<String>();
		for (String id : re1.keySet()) {
			//re1�� re2û��  delete
			if( !re2.containsKey(id) ) {
				delete.add(re1.get(id));
			}
			//re1  re2����   update
			else {
			
				String t1=re1.get(id);
				String t2=re2.get(id);
				
				if( !t1.equals(t2)) {
					
					update.add(t1+"\n"+t2+"\n");
				}
					
			}
				
		}
		
		for (String id : re2.keySet()) {
			//re2�� re1û��  add
			if( !re1.containsKey(id) ) {
				add.add(re2.get(id));
			}
		
				
		}
		
		differ.put("add", add);
		differ.put("delete", delete);
		differ.put("update", update);
		
		return differ;
	}


	/**
	 * �õ�����
	 * @param sqlHelper 
	 * @param sql  ��ִ�����
	 * @param idParamName	�����ֶ���
	 * @param paramName	�ֶ�������
	 * @param tparamName �ֶ��ַ���
	 * @return
	 */
	private Map<String ,String> getData(sqlHelper sqlHelper,String sql,String idParamName,String tparamName,int handleParamIndex,String splitString){
		Map<String ,String>  map=new HashMap<String, String>();
		
		ResultSet rs= sqlHelper.excuteQuery(sql, null);
		try {
			while(rs.next()) {
				String[] paramName = tparamName.split(",");
				if(paramName.length>0) {
					String reString="";
					//����
					String idString=rs.getString(idParamName).trim();
					//����
					for (int i = 0; i < paramName.length; i++) {
						String param = paramName[i];
						String tempsss=rs.getString(param);
						String sss="";
						if(tempsss!=null)
							sss=rs.getString(param).trim();
						
						if(i == handleParamIndex-1 ) {
							
							String[] _sss=sss.split(splitString);
							if(_sss.length>0) {
								//�Խ����������
								Collections.sort(Arrays.asList(_sss));
								sss="";
								for (String string : _sss) {
									sss=sss+string+";";
								}
								sss=sss.substring(0, sss.length()-1);
								
							}
						}
						reString =reString + sss +",";
						
					}
					
					//idString=MD5Util.md5Encode(reString);
					
					map.put(idString, reString);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("�޷�ȡ��������"+tparamName+"\t�������");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
}
