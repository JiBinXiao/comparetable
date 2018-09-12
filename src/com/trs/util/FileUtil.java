package com.trs.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class FileUtil {


	/**
	 * 将list集合写入文件
	 * 
	 * @param datas
	 * @param filename
	 */
	public static void writeStringByBufferedWriter(List<String> datas,String filename) {
		 try {
	            File file = new File(filename);
	            if (!file.exists()) {
	                file.createNewFile();
	            }else {
	            	file.delete();
	            }
	             // 一次写入的文件大小小于10M时， bufferedWriter并不能显著降低时间,而且此时BufferedOutputStream仍是占优的
	            FileWriter fWriter = new FileWriter(file,true);
	            BufferedWriter bufferedWriter = new BufferedWriter(fWriter);
	            long begin = System.currentTimeMillis();
	            for (String string : datas) {
	            	bufferedWriter.write(string+"\n");
				}
	            bufferedWriter.flush();
	            bufferedWriter.close();
	          //  System.out.println("BufferedWriter 执行耗时: " + (System.currentTimeMillis() - begin));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	
	
}
