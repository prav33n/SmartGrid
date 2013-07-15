package context.sg;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Bgservice extends Service{
	JSONObject jsonObjSend = new JSONObject();
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		try{
		jsonObjSend.put("key","Mute");
		jsonObjSend.put("key","Mute");}
		catch (Exception e){
			Log.e("exception", "caught");
		}
		return null;
	
	}

	
	
	public void httpclient(JSONObject send){
		String tvip = "192.168.1.100";
	
		try {
			StringEntity se;
			se = new StringEntity(send.toString());
			HttpParams httpParameters = new BasicHttpParams();   //set connection parameters
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
			HttpConnectionParams.setSoTimeout(httpParameters, 10000);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpResponse response;	
			//HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://192.168.1.100/test.php");
			httppost.setHeader("Content-type", "application/json");
			httppost.setHeader("Accept", "application/json");
			httppost.setEntity(se);
			response = httpclient.execute(httppost);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("http error","caught");
		}
		
	}
	
}
