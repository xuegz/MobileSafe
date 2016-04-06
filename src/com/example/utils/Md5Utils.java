package com.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Md5Utils {
	/**
	 * md5加密
	 * @param text
	 * @return
	 */
	public static String encode(String text){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(text.getBytes());
			StringBuilder sb  =new StringBuilder();
			for(byte b : result){
				int number = b&0xff; // 加盐 +1  ;
				String hex = Integer.toHexString(number);
				if(hex.length()==1){
					sb.append("0"+hex);
				}else{
					sb.append(hex);
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			//can't reach
			return "";
		}
	}
    /**
     * ��ȡ���ļ���MD5(����������)
     * @param sourceDir
     * @return
     * ��������
     */
	public static String getFileMd5(String sourceDir) {
		
		File file = new File(sourceDir);
		
		try {
			FileInputStream fis = new FileInputStream(file);
			
			byte[] buffer =  new byte[1024];
			
			int len = -1;
			//��ȡ������ժҪ
			MessageDigest messageDigest = MessageDigest.getInstance("md5");
			
			while((len = fis.read(buffer))!= -1){
				
				messageDigest.update(buffer, 0, len);
				
			}
			byte[] result = messageDigest.digest();
			
			StringBuffer sb = new StringBuffer();
			
			for(byte b : result){
				int number = b&0xff; // ���� +1 ;
				String hex = Integer.toHexString(number);
				if(hex.length()==1){
					sb.append("0"+hex);
				}else{
					sb.append(hex);
				}
			}
			return sb.toString();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
}
