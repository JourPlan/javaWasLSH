package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import servlet.ServletService;
import servlet.service.ServletServiceImpl;

public class GetHttpUrl {
	
	private static final Logger log = LoggerFactory.getLogger(GetHttpUrl.class);
	// 파일 요청이 없을 경우의 기본 파일
	private static final String DEFAULT_FILE_PATH = "/index.html";
	
	
	public static byte[] getHttpBf(BufferedReader br) throws Exception {
		ServletServiceImpl servletServiceImpl = new ServletServiceImpl();
		byte[] httpBody = null;
		String httpUrl = "";
		try {
			String line = br.readLine();
			String[] splited = line.split(" ");
			httpUrl = splited[1];
			if(httpUrl.equals("/")){
				httpUrl = DEFAULT_FILE_PATH;
				httpBody = Files.readAllBytes(new File("./webapp" + httpUrl).toPath());
			}else{
				if(httpUrl.startsWith("/main")){
					
					httpUrl = servletServiceImpl.getMain();
					httpBody = Files.readAllBytes(new File("./webapp" + httpUrl).toPath());
				}else if(httpUrl.startsWith("/time")){
					String nowDate = servletServiceImpl.getTime();
					httpBody = nowDate.getBytes();
				}else{
					File file = new File("./webapp" + httpUrl);
					if(!file.exists()){
						httpBody = null;
					}else{
						httpBody = Files.readAllBytes(new File("./webapp" + httpUrl).toPath());
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return httpBody;
	}
	 
}
