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
 * 2018年9月12日
 */
public class SystemConfig {

    private String configname = "comparetable.ini";
    private String currerntDirPath = "";
    private int limit = 10000;

    private Properties properties = new Properties();
    private FileInputStream fis = null; // 读

    private String driver = "com.mysql.jdbc.Driver";

    private String url1 = "";
    private String database1 = "";
    private String user1 = "";
    private String password1 = "";
    private String tableName1 = "";
    private String idParamName1 = "";
    private int handleParamIndex1 = 0;
    private String splitString1 = "";
    private String idIndexParam1 = "";
    private String paramName1 = "";
    private String sql1 = "";
    sqlHelper sqlHelper1 = null;
    Map<String, String> reMap1 = new HashMap<String, String>();


    private String url2 = "url2";
    private String database2 = "database2";
    private String user2 = "user2";
    private String password2 = "password2";
    private String tableName2 = "";
    private String idParamName2 = "";
    private String idIndexParam2 = "";
    private int handleParamIndex2 = 0;
    private String splitString2 = "";
    private String paramName2 = "";
    private String sql2 = "";
    sqlHelper sqlHelper2 = null;
    Map<String, String> reMap2 = new HashMap<String, String>();

    //加载资源文件
    public SystemConfig() {
        File currerntDir = new File("");
        currerntDirPath = currerntDir.getAbsolutePath();
        String configpath = currerntDirPath + File.separator + configname;
        try {
            fis = new FileInputStream(configpath);
            properties.load(fis);
        } catch (Exception e) {
            System.out.println("配置根目录为：" + currerntDir);
            System.out.println("加载资源出错，请确保配置名称为comparetable.ini");
            e.printStackTrace();
        }
    }


    public void init() {
        System.out.println("开始加载1资源");
        url1 = properties.getProperty("url1");
        database1 = properties.getProperty("database1");
        user1 = properties.getProperty("user1");
        password1 = properties.getProperty("password1");
        tableName1 = properties.getProperty("tableName1");
        idParamName1 = properties.getProperty("idParamName1");
        idParamName1 = properties.getProperty("idParamName1");
        idIndexParam1 = properties.getProperty("idIndexParam1");

        sql1 = properties.getProperty("sql1");
        paramName1 = properties.getProperty("paramName1");
        handleParamIndex1 = Integer.parseInt(properties.getProperty("handleParamIndex1"));
        splitString1 = properties.getProperty("splitString1");


        sqlHelper1 = new sqlHelper();
        sqlHelper1.initConnetction(url1, database1, user1, password1);
        //查询总数量
        String sqlselectString1 = "select count(*) from " + tableName1;
        int count1 = getCount(sqlHelper1, sqlselectString1);
        System.out.println(tableName1 + "总共:" + count1);
        String startString1 = "0";
        int sum1 = 0;
        //按批次查询
        while (true) {
            System.out.println(sum1);

            //                String limitString = sql1 + " where " + idParamName1 + " >" + start + " limit " + limit + " order by " + idParamName1 + " asc";
            String limitString = sql1 + " where " + idIndexParam1 + " > " + startString1 +
                    " order by " + idIndexParam1 + " asc  limit " + limit;
            System.out.println(limitString);
            Map<String, String> tempreMap1 = getData(sqlHelper1, limitString, idParamName1, paramName1, handleParamIndex1, splitString1);

            startString1 = tempreMap1.getOrDefault("max", "-1");
            tempreMap1.remove("max");
            reMap1.putAll(tempreMap1);
            sum1 = sum1 + tempreMap1.size();
            if (tempreMap1.size() < limit)
                break;

        }


        sqlHelper1.closesqlHelper();
        System.out.println("加载1资源完毕");

        System.out.println("-----------------");

        //-----------------------神奇的分割线---------------------------------------------------------

        System.out.println("开始加载2资源");
        url2 = properties.getProperty("url2");
        database2 = properties.getProperty("database2");
        user2 = properties.getProperty("user2");
        password2 = properties.getProperty("password2");
        tableName2 = properties.getProperty("tableName2");
        idParamName2 = properties.getProperty("idParamName2");
        idIndexParam2 = properties.getProperty("idIndexParam2");
        handleParamIndex2 = Integer.parseInt(properties.getProperty("handleParamIndex2"));
        sql2 = properties.getProperty("sql2");
        paramName2 = properties.getProperty("paramName2");

        splitString2 = properties.getProperty("splitString2");
        //加载数据
        sqlHelper2 = new

                sqlHelper();
        sqlHelper2.initConnetction(url2, database2, user2, password2);
        String sqlselectString2 = "select count(*) from " + tableName2;

        int count2 = getCount(sqlHelper2, sqlselectString2);
        System.out.println(tableName2 + "总共:" + count2);
        String startString2 = "0";
        int sum2 = 0;
        //按批次查询
        while (true) {
            System.out.println(sum2);
            //                String limitString = sql1 + " where " + idParamName1 + " >" + start + " limit " + limit + " order by " + idParamName1 + " asc";
            String limitString = sql2 + " where " + idIndexParam2 + " > " + startString2 +
                    " order by " + idIndexParam2 + " asc  limit " + limit;
            System.out.println(limitString);
            Map<String, String> tempreMap2 = getData(sqlHelper2, limitString, idParamName2, paramName2, handleParamIndex2, splitString2);

            startString2 = tempreMap2.getOrDefault("max", "-1");
            tempreMap2.remove("max");

            reMap2.putAll(tempreMap2);
            sum2 = sum2 + tempreMap2.size();
            if (tempreMap2.size() < limit)
                break;

        }


        sqlHelper2.closesqlHelper();
        System.out.println("加载2资源完毕");
        System.out.println("----------------");


        //-----------------------神奇的分割线---------------------------------------------------------

        System.out.println("开始比对");
        //比对数据 得到相对于 re1的 增删改 数据集合
        Map<String, List<String>> differ1 = compareData(reMap1, reMap2);
        //输出到文件
        FileUtil.writeStringByBufferedWriter(differ1.get("add"), currerntDirPath + File.separator + tableName1 + ".add");
        FileUtil.writeStringByBufferedWriter(differ1.get("delete"), currerntDirPath + File.separator + tableName1 + ".delete");
        FileUtil.writeStringByBufferedWriter(differ1.get("update"), currerntDirPath + File.separator + tableName1 + ".update");

        System.out.println("比对结束");

    }


    /**
     * 得到表的总数据
     *
     * @param sqlHelper12
     * @param sqlselectString1
     * @return
     */
    private int getCount(sqlHelper sqlHelper, String sqlselectString1) {
        int count = 0;
        ResultSet rs = sqlHelper.excuteQuery(sqlselectString1, null);
        try {
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("执行 " + sqlselectString1 + " 有误");
            e.printStackTrace();
        }

        return count;
    }


    /**
     * 比对数据 生成针对 re1 的增删改 数据
     *
     * @param re12
     * @param re22
     * @return
     */
    private Map<String, List<String>> compareData(Map<String, String> re1, Map<String, String> re2) {

        Map<String, List<String>> differ = new HashMap<String, List<String>>();
        List<String> add = new ArrayList<String>();
        List<String> delete = new ArrayList<String>();
        List<String> update = new ArrayList<String>();
        for (String id : re1.keySet()) {
            //re1有 re2没有  delete
            if (!re2.containsKey(id)) {
                delete.add(id + "," + re1.get(id));
            }
            //re1  re2都有   update
            else {
                String t1 = re1.get(id);
                String t2 = re2.get(id);
                if (!t1.equals(t2)) {
                    update.add(t1 + "\n" + t2 + "\n");
                }
            }

        }
        for (String id : re2.keySet()) {
            //re2有 re1没有  add
            if (!re1.containsKey(id)) {
                add.add(id+","+re1.get(id));
            }


        }

        differ.put("add", add);
        differ.put("delete", delete);
        differ.put("update", update);

        return differ;
    }


    /**
     * 得到数据
     *
     * @param sqlHelper
     * @param sql         待执行语句
     * @param idParamName 主键字段名
     * @param paramName   字段数组名
     * @param tparamName  字段字符串
     * @return
     */
    private Map<String, String> getData(sqlHelper sqlHelper, String sql, String idParamName, String tparamName, int handleParamIndex, String splitString) {
        Map<String, String> map = new HashMap<String, String>();

        ResultSet rs = sqlHelper.excuteQuery(sql, null);
        try {
            while (rs.next()) {
                //待读取的字段名称
                String[] paramName = tparamName.split(",");
                if (paramName.length > 0) {
                    String reString = "";
                    //主键
                    String idString = rs.getString(idParamName).trim();
                    //属性
                    for (int i = 0; i < paramName.length; i++) {
                        String param = paramName[i];
                        String tempsss = rs.getString(param);
                        String sss = "";
                        if (tempsss != null)
                            sss = rs.getString(param).trim();
                        //对比的字段序号
                        if (i == handleParamIndex - 1) {
                            //splitString 拆分符号
                            String[] _sss = sss.split(splitString);
                            if (_sss.length > 0) {
                                //对结果进行排序
                                Arrays.sort(_sss);
                                sss = "";
                                for (String string : _sss) {
                                    sss = sss + string + ";";
                                }
                                sss = sss.substring(0, sss.length() - 1);

                            }
                        }
                        reString = reString + sss + ",";

                    }

                    //idString=MD5Util.md5Encode(reString);
                    //最大id号码

                    map.put("max", rs.getString("id"));
                    map.put(idString, reString);
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("无法取出参数：" + tparamName + "\t请检查参数");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }

}
