package com.example.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
	
	public static String readFromStream(InputStream in) throws IOException{
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		int len=0;
		byte[] bys=new byte[1024];
		while((len=in.read(bys))!=-1){
			bos.write(bys, 0, len);
		}
		String result=bos.toString();
		in.close();
		bos.close();
		return result;
	}
}
