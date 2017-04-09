package vishnusrivastava.me.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class pageHandler implements HttpHandler  {
	
	public String page = "/";	
	pageHandler(String page){
		this.page = page;
	}
	
	// get message from me
	public static String getMyMessage(HttpExchange req) throws Exception{
		InputStreamReader isr =  new InputStreamReader(req.getRequestBody(),"utf-8");
		BufferedReader br = new BufferedReader(isr);
		int character;
		StringBuilder buf = new StringBuilder(512);
		while ((character = br.read()) != -1) {
		    buf.append((char) character);
		}
		br.close();
		isr.close();
		String message = new String(buf);
		message = message.substring(message.indexOf("\"text\":")+8);
		message = message.substring(0, message.indexOf("\""));
		return message;
	}

	public void handle(HttpExchange req) throws IOException {
		
		
		byte [] response = "Nothing here ...".getBytes();
		
		if ("fbhook".equals(page)){
			String query = req.getRequestURI().getQuery();
			HashMap <String, String> data = new HashMap<String, String>();
			
		      if (query != null){
		    	  String vals[] = req.getRequestURI().getQuery().split("&");
		    	  for(int i = 0; i <vals.length; i++){
		    		  System.out.println(i +" "+vals[i].split("=")[1]+ " "+ vals[i].split("=")[0]);
		    		  data.put(vals[i].split("=")[0], vals[i].split("=")[1]);	 
		    	  }
		    	  if (data.containsKey("hub.verify_token") && data.get("hub.verify_token").equals("hello_ji")){
		    		  response = data.get("hub.challenge").getBytes();
		    		  System.out.println("verified !");
		    		  }
		    	  }
		      else{
		    	  try {
					sendMessageVishnu.send(getMyMessage(req));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	  }
		      }
		
		else if ("resources".equals(page)){
			String[] demand = req.getRequestURI().getQuery().split("=");
			if ("img".equals(demand)){
				
			}
			response = req.getRequestURI().getQuery().getBytes();
		}
		req.sendResponseHeaders(200, response.length);
		OutputStream os = req.getResponseBody();
		os.write(response);
		os.close();
	}

}
