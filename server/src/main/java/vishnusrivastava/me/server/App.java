package vishnusrivastava.me.server;

import java.io.BufferedReader;
/**
 * Hello world!
 *
 */
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App 
{
	public static void main(String[] args) throws Exception {
				       
		SocketIONamespace chatnamespace = null;
		
		//chat server
	    Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);        
        final SocketIOServer chatServer = new SocketIOServer(config);
        final SocketIONamespace chat2namespace = chatServer.addNamespace("/chat2");
        chatnamespace = chat2namespace;
        
        
		//main server
	    HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
	    server.createContext("/fbhook", new pageHandler("fbhook",chatnamespace));
	    server.createContext("/", new pageHandler("main",chatnamespace));
	    server.createContext("/resources", new pageHandler("resources",chatnamespace));
	    server.setExecutor(null);
	    server.start();
	    
	    
	  
        chat2namespace.addEventListener("message", ChatObject.class, new DataListener<ChatObject>() {
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackRequest) {
                // broadcast messages to all clients
                chat2namespace.getBroadcastOperations().sendEvent("message", data);
                sendMessageVishnu.send("@"+data.getUserName()+" : "+data.getMessage());
            }
        });

        chatServer.start();
        Thread.sleep(Integer.MAX_VALUE);
        chatServer.stop();
	    

	    
	    
        
	  }
}
