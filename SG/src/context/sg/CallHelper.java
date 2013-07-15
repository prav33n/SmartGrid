package context.sg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.StrictMode;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Helper class to detect incoming and outgoing calls.
 * @author Moskvichev Andrey V.
 *
 */
public class CallHelper {
Httpclient ht = new Httpclient();
String tvurl = "http://192.168.1.111:1925/1/input/key";	//TODO: get from device database or remotedb IP address of the Smart TV system. 
	/**
	 * Listener to detect incoming calls. 
	 */
	private class CallStateListener extends PhoneStateListener {
		JSONObject jsonObjSend = new JSONObject();
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			
			case TelephonyManager.CALL_STATE_IDLE:
				if(start!= 0){  /* check if call in initiated previously and listen for end state*/
					Log.e("start",""+start);
					try {
						jsonObjSend.put("key","Mute");
						ht.httpclient(jsonObjSend, tvurl, "post"); /*send mute signal when call ends */
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				break;
			
			case TelephonyManager.CALL_STATE_RINGING:
				// called when someone is ringing to this phone
				/*Toast.makeText(ctx,
						"Incoming: "+incomingNumber, 
						Toast.LENGTH_LONG).show();*/
				try {
					jsonObjSend.put("key","Mute");
					ht.httpclient(jsonObjSend, tvurl, "post"); /*send mute signal when incomming call */
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				start =1;
				break;
			}
		}
	}
	
	/**
	 * Broadcast receiver to detect the outgoing calls.
	 */
	public class OutgoingReceiver extends BroadcastReceiver {
	    public OutgoingReceiver() {
	    }

	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	JSONObject jsonObjSend = new JSONObject();
	        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
	        try {
				jsonObjSend.put("key","Mute");
				ht.httpclient(jsonObjSend, tvurl, "post");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   start =1;
	    }
  
	}
	private int start = 0;
	private Context ctx;
	private TelephonyManager tm;
	private CallStateListener callStateListener;
	
	private OutgoingReceiver outgoingReceiver;

	public CallHelper(Context ctx) {
		this.ctx = ctx;
		
		callStateListener = new CallStateListener();
		outgoingReceiver = new OutgoingReceiver();
	}
	
	/**
	 * Start calls detection.
	 */
	public void start() {
		tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		ctx.registerReceiver(outgoingReceiver, intentFilter);
	}
	
	/**
	 * Stop calls detection.
	 */
	public void stop() {
		tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
		ctx.unregisterReceiver(outgoingReceiver);
	}
	
	
	/* author : Praveen Jelish 
	 * Function to send control to television : updated to single class*/
	public void httpclient(final JSONObject send){
		final String tvip = "192.168.1.104";
		
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
				HttpPost httppost = new HttpPost("http://"+tvip+"/control/mobilescript.php");
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
		//	sb.append(reader.readLine() + "\n");
		//	String line="0";
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
			return result;
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
			e.printStackTrace();
			return null;
		}
    	
    }
}
