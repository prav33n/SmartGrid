package context.sg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.util.Log;
/* author : praveen jelish 
 * function to send HTTP get,post and put request to the devices*/
public class Httpclient {
	
	String result;
	
	public void httpclient(final JSONObject send, final String URL, final String method){
		 Thread t = new Thread(new Runnable() {
	    public void run() { 
			try {
				StringEntity se;
				//Log.e("http string",URL+"//"+send.toString());
				HttpParams httpParameters = new BasicHttpParams();   //set connection parameters
				HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);
				HttpConnectionParams.setSoTimeout(httpParameters, 60000);
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpResponse response= null;
				//HttpClient httpclient = new DefaultHttpClient();
				//HttpPost httppost = new HttpPost("http://"+URL+"/control/huescript.php");¨
				if(method.equals("post")){
					HttpPost httppost = new HttpPost(URL);	
					httppost.setHeader("Content-type", "application/json");
					se = new StringEntity(send.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					httppost.setEntity(se);
					response = httpclient.execute(httppost); 
					
				}
				else if(method.equals("put")){
					HttpPut put = new HttpPut(URL);
					 put.setHeader("Content-type", "application/json");
					se = new StringEntity(send.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					 put.setEntity(se);
					response = httpclient.execute(put); 
				}
				else if(method.equals("get")){
					HttpGet get = new HttpGet(URL);
					response = httpclient.execute(get); 
				}
				
				
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				streamtostring(is);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("http error","caught");
			}
			}
	        
		 });
		 t.start();
	}
	
	public void httpclient(final JSONObject send, final String URL){
		 Thread t = new Thread(new Runnable() {
	           public void run() { 
			try {
				StringEntity se;
				//Log.e("http string",URL+"//"+send.toString());
				HttpParams httpParameters = new BasicHttpParams();   //set connection parameters
				HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);
				HttpConnectionParams.setSoTimeout(httpParameters, 60000);
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpResponse response;	
				//HttpClient httpclient = new DefaultHttpClient();
				//HttpPost httppost = new HttpPost("http://"+URL+"/control/huescript.php");¨
				HttpPost httppost = new HttpPost(URL);	
				httppost.setHeader("Content-type", "application/json");
				se = new StringEntity(send.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				httppost.setEntity(se);
				response = httpclient.execute(httppost); 
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				streamtostring(is);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("http error","caught");
			}
			}
	        
		 });
		 t.start();
	}
	
	
	
	public void httpput(final JSONObject send, final String URL){
		 Thread t = new Thread(new Runnable() {
	           public void run() { 
			try {
				StringEntity se;
				Log.e("http string","//"+send.toString());
				HttpParams httpParameters = new BasicHttpParams();   //set connection parameters
				HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);
				HttpConnectionParams.setSoTimeout(httpParameters, 60000);
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpResponse response;	
				//HttpClient httpclient = new DefaultHttpClient();
				//HttpPost httppost = new HttpPost("http://"+URL+"/control/huescript.php");¨
				HttpPut put = new HttpPut(URL);
				 put.setHeader("Content-type", "application/json");
				 put.setHeader("Connection","Keep-Alive");
				se = new StringEntity(send.toString());
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				put.setEntity(se);
				httpclient.execute(put);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("http error","caught");
			}
			}
	        
		 });
		 t.start();
	}
	
	
	public void httpget(final JSONObject send, final String URL){
		
		 Thread t = new Thread(new Runnable() {
	           public void run() { 
			try {
				StringEntity se;
				Log.e("http string","//"+send.toString());
				HttpParams httpParameters = new BasicHttpParams();   //set connection parameters
				HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);
				HttpConnectionParams.setSoTimeout(httpParameters, 60000);
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpResponse response;	
				//HttpClient httpclient = new DefaultHttpClient();
				//HttpPost httppost = new HttpPost("http://"+URL+"/control/huescript.php");¨
				HttpGet get = new HttpGet(URL);
				response = httpclient.execute(get); 
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				streamtostring(is);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("http error","caught");
			}
			}
	        
		 });
		 t.start();
	}
	
	
	
	public String streamtostring(InputStream is){
    	//convert response to string
		try{
			int length;
			if(is.available()>0)
				length = is.available();
			else 
				length = 1;
			BufferedReader reader = new BufferedReader(new InputStreamReader(is), length);
			StringBuilder sb = new StringBuilder();
			String line = null;
			  while ((line = reader.readLine()) != null) 
		        {
		            sb.append(line);
		        }
			String result=sb.toString();
			is.close();
			reader = null;
			sb = null;
			Log.e("Received data",""+result);
			this.result = result;
			return result;
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
			e.printStackTrace();
			return null;
		}
    	
    }
}
