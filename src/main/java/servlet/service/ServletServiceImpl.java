package servlet.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import servlet.ServletService;
import util.GetHttpUrl;



public class ServletServiceImpl implements ServletService {
	private static final Logger log = LoggerFactory.getLogger(GetHttpUrl.class);
	
	@Override
	public String getMain() throws Exception{
		String httpUrl = "/index.html";
		return httpUrl;
	}
	

	@Override
	public String getTime() throws Exception{
	    
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy년 MM월 dd일 kk:mm:ss");
		String nowDate = dayTime.format(new Date(time));
		
		return nowDate;
	}
	
}
