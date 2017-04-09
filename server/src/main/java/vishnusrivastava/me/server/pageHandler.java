package vishnusrivastava.me.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;

import com.corundumstudio.socketio.SocketIONamespace;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class pageHandler implements HttpHandler  {
	
	public String page = "/";	
	public SocketIONamespace chatnamespace;
	File file = new File(System.getProperty("user.dir")+"\\resources\\default.html");
	pageHandler(String page, SocketIONamespace chatnamespace){
		this.page = page;
		this.chatnamespace = chatnamespace;
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
		    		  file = new File(System.getProperty("user.dir")+"\\resources\\challenge.txt");
		    		  file.setWritable(true);
		    		  BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		    		  writer.write(data.get("hub.challenge"));
		    		  writer.close();
		    		  System.out.println("verified !");
		    		  }
		    	  }
		      else{
		    	  try {		    		 
		    		  String mymessage = getMyMessage(req);
		    		System.out.println(mymessage);
		    		 ChatObject user_message = new ChatObject();
		    		 user_message.setMessage(mymessage);
		    		 user_message.setUserName("Vishnu");
		    		 System.out.println(this.chatnamespace);
		    		 if (this.chatnamespace != null){
		    			chatnamespace.getBroadcastOperations().sendEvent("message", user_message);
		    		 }
					//sendMessageVishnu.send(getMyMessage(req));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    	  }
		      req.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
		      }
		
		else if ("resources".equals(page)){
			if (req.getRequestURI().getQuery() == null){
				req.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
			}
			else{
				String[] demand = req.getRequestURI().getQuery().split("=");
				if ("img".equals(demand[0])){
					file = new File(System.getProperty("user.dir")+"\\resources\\"+demand[1]);
				}
				else if ("css".equals(demand[0])){
					System.out.println(System.getProperty("user.dir")+"\\resources\\css\\"+demand[1]);
					file = new File(System.getProperty("user.dir")+"\\resources\\css\\"+demand[1]);
				}
			}			
		}
		else if ("main".equals(page)){
			req.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
			file = new File(System.getProperty("user.dir")+"\\resources\\index.html");
		}
		req.sendResponseHeaders(200, file.length());
		OutputStream os = req.getResponseBody();
		Files.copy(file.toPath(), os);
		os.close();
	}

}
