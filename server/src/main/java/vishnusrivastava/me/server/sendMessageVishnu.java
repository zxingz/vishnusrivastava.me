package vishnusrivastava.me.server;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class sendMessageVishnu {

	public static int send(String text){
		HttpClient httpClient = HttpClientBuilder.create().build();
		try {

		    HttpPost request = new HttpPost("https://graph.facebook.com/v2.6/me/messages?access_token=EAAGnebMRB0oBAEnv567IoqtNAQZA4RQGTDfnZA78k6xV1Tpis8yJDL40wZCZBqqq1oclAdyah5jD7QhdhOeOofzIFIszQATcdLv1xkygG8zMOoHZB9yX7XITHjx0yLwOhnG6QJZCwplBDlSaC71Hw4Ikr8v7GzEXVZCBTZAAb3OO5QZDZD");
		    StringEntity params =new StringEntity("{\"recipient\":{\"id\":\"1396212283787127\"},\"message\":{\"text\":\""+text+"\"}}");
		    request.addHeader("content-type", "application/json");
		    request.setEntity(params);
		    HttpResponse response = httpClient.execute(request);

		    //handle response here...
		    if (response.getStatusLine().getStatusCode() == 200){
		    	return 0;
		    }
		    return -1;

		}catch (Exception ex) {
			return -1;
		    //handle exception here

		} 
	}
	
	
	public static void main(String args[]){
		System.out.println(sendMessageVishnu.send("hello"));
	}
}
