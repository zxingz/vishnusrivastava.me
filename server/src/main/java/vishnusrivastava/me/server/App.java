package vishnusrivastava.me.server;

import java.io.ByteArrayOutputStream;
/**
 * Hello world!
 *
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App 
{
	public static void main(String[] args) throws Exception {
	    HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
	    server.createContext("/fbhook", new MyHandler());
	    server.setExecutor(null); // creates a default executor
	    server.start();
	  }

	  static class MyHandler implements HttpHandler {
	    @SuppressWarnings("restriction")
		public void handle(HttpExchange t) throws IOException {
	      System.out.println(t.getRequestURI().getQuery());
	      String query[] = t.getRequestURI().getQuery().split("&");
	      byte [] response = query[1].split("=")[1].getBytes();
	      t.sendResponseHeaders(200, response.length);
	      OutputStream os = t.getResponseBody();
	      os.write(response);
	      os.close();
	    }
	  }
}
