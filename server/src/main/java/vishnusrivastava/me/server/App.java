package vishnusrivastava.me.server;

import java.io.BufferedReader;
/**
 * Hello world!
 *
 */
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App 
{
	public static void main(String[] args) throws Exception {
	    HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
	    server.createContext("/fbhook", new pageHandler("fbhook"));
	    server.createContext("/", new pageHandler("main"));
	    server.createContext("/resources", new pageHandler("resources"));
	    server.setExecutor(null);
	    server.start();
	  }
}
