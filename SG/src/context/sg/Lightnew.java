package context.sg;

import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Lightnew extends Activity implements SeekBar.OnSeekBarChangeListener{
	JSONObject jsonObjSend, jsonobj;
	Httpclient ht;
	String hueip;
	String url;
	int lights;
	JSONArray jArray;
	Thread t;
	@Override
	    public void onCreate(Bundle savedInstanceState)  {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.light);
	        //url ="http://"+hueip+"/control/huescript.php";
	        url ="http://www.meethue.com/api/nupnp";
	        ht = new Httpclient();	    
	        ht.httpclient(jsonObjSend, url, "get");
	        try {
				Thread.sleep(2000);
				jArray = new JSONArray(ht.result);
				hueip= jArray.getJSONObject(0).getString("internalipaddress"); /* Get the lamp IP using the HUE UPNP services*/
				ht.result="";
				url="http://"+hueip+"/api/sgdeveloper/lights";
				ht.httpclient(jsonObjSend, url, "get");
				Thread.sleep(1500);
				jsonobj = new JSONObject(ht.result);
				Log.e("huetest",""+jsonobj.length());
				lights = jsonobj.length();  /*get the number of lights attached to the HUE bridge */
				if(lights==0)
					lights = 6;
				Log.e("hue data",""+lights+"//"+hueip);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Toast.makeText(getApplicationContext(), "hue light not found",Toast.LENGTH_LONG).show();
			}
			
		    
	        ToggleButton status = (ToggleButton)findViewById(R.id.status);
	        SeekBar bar = (SeekBar)findViewById(R.id.lumins); // make seekbar object
	    	bar.setOnSeekBarChangeListener(this);
	        RadioGroup preset = (RadioGroup)findViewById(R.id.presets);
	        
	        preset.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){ /* preset control block*/
				@Override
				public void onCheckedChanged(RadioGroup group, int id) {
					// TODO Auto-generated method stub
					RadioButton btn = (RadioButton)group.findViewById(id);
					Log.e("selected",""+id + btn.getText());
					jsonObjSend = new JSONObject();
					JSONObject jsonpreset = new JSONObject();
					try {
							jsonObjSend.put("device","light");
							
							if(btn.getText().equals("Movies")){
								jsonpreset.put("on",true);
								jsonpreset.put("bri",50);
								jsonpreset.put("hue",20000);
								
							}
							else if(btn.getText().equals("Games")){
								jsonpreset.put("on",true);
								jsonpreset.put("bri",150);
								jsonpreset.put("hue",40000);
							}
							else if(btn.getText().equals("Ambient")){
								jsonpreset.put("on",true);
								jsonpreset.put("bri",250);
								jsonpreset.put("hue",60000);
							}
							//jsonObjSend.put("preset",jsonpreset);
							for(int i =1;i <= lights; i++){
							url="http://"+hueip+"/api/sgdeveloper/lights/"+i+"/state"; 
							ht.httpclient(jsonpreset,url,"put"); }
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}});
	        
	        status.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {

	        	@Override
				public void onCheckedChanged(CompoundButton btn, boolean status) {
					// TODO Auto-generated method stub
	        	
	        		 jsonObjSend = new JSONObject();
	        		Log.e("compound","button");
	        		try {
	        			//jsonObjSend.put("device","light");
	        			if(status)
	        				jsonObjSend.put("on",true);	     
	        			else 
	        				jsonObjSend.put("on",false);	  
						
						ht.httpclient(jsonObjSend,url);
						for(int i =1;i <= lights; i++){
							url="http://"+hueip+"/api/sgdeveloper/lights/"+i+"/state"; 
							ht.httpclient(jsonObjSend,url,"put"); }						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        	});
	        
	        
	    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		 jsonObjSend = new JSONObject();
		//Log.e("Lumins",""+progress);
		try {
			//jsonObjSend.put("device","light");
			jsonObjSend.put("bri",(progress*2));
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		for(int i =1;i <= lights; i++){
			url="http://"+hueip+"/api/sgdeveloper/lights/"+i+"/state"; 
			ht.httpclient(jsonObjSend,url,"put"); }		
		
	}
	
	public void setcolor(View V)
	{
					jsonObjSend = new JSONObject();
					try {
				 	//jsonObjSend.put("device","light");
					Button bt = (Button)V;
					if(bt.getTag().toString().equals("red")){
							jsonObjSend.put("hue",0);
							}
					else if(bt.getTag().toString().equals("blue")){
						jsonObjSend.put("hue",46920);
						}
					else if(bt.getTag().toString().equals("green")){
						jsonObjSend.put("hue",25500);
						}
					else if(bt.getTag().toString().equals("yellow")){
						jsonObjSend.put("hue",20000);
						}
				else if(bt.getTag().toString().equals("sky")){
						jsonObjSend.put("hue",40000);
						}
							
					for(int i =1;i <= lights; i++){
						url="http://"+hueip+"/api/sgdeveloper/lights/"+i+"/state"; 
						ht.httpclient(jsonObjSend,url,"put"); }		
				
	 	} 
	 	catch (JSONException e) {
	// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	public String getlights(){
		url = "http://"+hueip+"/api/sgdeveloper/lights/";
		ht.httpclient(jsonObjSend, url, "get");
		return ht.result;
	}
	
	
	public void playcolorloop(View v){
		//hue and saturation random generations
		
		t = new Thread(new Runnable() {
	     public void run() { 
			try {
				StringEntity se;
				for(int i=0;i<300;i++){
					 try {
							 for(int j =1;j <= lights; j++){
									jsonObjSend = new JSONObject();
									Random r = new Random();
									int sat = r.nextInt(255 - 1 + 1) + 1;
									int hue = r.nextInt(65535 - 1 + 1) + 1;
									int bri = r.nextInt(150 - 1 + 1) + 1;
									if(i%3 == 0)
									 jsonObjSend.put("bri",150);
									else if(i%4 == 0){
										jsonObjSend.put("bri",100);}
									else if(i%5 == 0){
										jsonObjSend.put("on",true);
										jsonObjSend.put("bri",20);
									}
									else{
										jsonObjSend.put("on",true);
										jsonObjSend.put("bri",bri);}
									 jsonObjSend.put("sat",sat);
									 jsonObjSend.put("hue",hue);
									 
									 jsonObjSend.put("transitiontime",1);
									url="http://"+hueip+"/api/sgdeveloper/lights/"+j+"/state";
									HttpParams httpParameters = new BasicHttpParams();   //set connection parameters
									HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
									HttpConnectionParams.setSoTimeout(httpParameters, 10000);
									HttpClient httpclient = new DefaultHttpClient(httpParameters);
									HttpResponse response;	
									//HttpClient httpclient = new DefaultHttpClient();
									//HttpPost httppost = new HttpPost("http://"+URL+"/control/huescript.php");¨
									HttpPut put = new HttpPut(url);
									put.setHeader("Content-type", "application/json");
									put.setHeader("Connection","Keep-Alive");
									se = new StringEntity(jsonObjSend.toString());
									se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
									put.setEntity(se);
									httpclient.execute(put);
									 }		
							 Thread.sleep(1000);				
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}
				//Log.e("http string","//"+send.toString());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("http error","caught");
			}
			}
	        
		 });
		 t.start();
		
	}
	
	public void stopcolorloop(View v){
		
	}
}