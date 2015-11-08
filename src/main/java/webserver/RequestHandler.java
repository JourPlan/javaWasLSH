package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.GetHttpUrl;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	// 파일 요청이 없을 경우의 기본 파일
	private static final String DEFAULT_FILE_PATH = "/index.html";
	  
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		//try안에 () 문법은 jdk1.7버전 이후 부터 생겼다. stream과 같은 자원을 얻어온다음 다 사용한 경우 반드시 close를 해야하는데 아래 구문에서는
		//close부분이 없다. 없는이유가 inputStream과 outputStream 클래스에서 Closeable인터페이스를 implements하고있기 때문에 자동으로 close를 해준다. 
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			
			//일반적으로 헤더는 라인단위로 구성이 되있기때문에 라인단위로 데이터를 읽어야한다.
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			byte[] httpBody = GetHttpUrl.getHttpBf(br);
			if(httpBody == null){ //404에러일때
				DataOutputStream dos = new DataOutputStream(out);
				response404Header(dos, "/errorPage/error404.html");
				
			}else{
				DataOutputStream dos = new DataOutputStream(out);
				response200Header(dos, httpBody.length);
				responseBody(dos, httpBody);
			}
			
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void response403Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 403 Forbidden \r\n");
			dos.writeBytes("Location: /errorPage/error403.html\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private void response404Header(DataOutputStream dos, String location) {
		try {
			dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Connection: close\r\n");
            dos.writeBytes("\r\n");
            dos.writeBytes("<h1>404 Not Found</h1>");
//            dos.writeBytes("Location: /errorPage/error404.html\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private void response500Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 500 Internal Server Error \r\n");
			dos.writeBytes("Location: /errorPage/error500.html\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private void response302Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /errorPage/error500.html\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
