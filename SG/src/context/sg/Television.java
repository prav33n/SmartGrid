package context.sg;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class Television extends Activity implements SeekBar.OnSeekBarChangeListener{
	
		JSONObject jsonObjSend,jsonhue;
		Httpclient ht;
		final String tvip = "192.168.1.111"; /* IP of the Smart TV retrieved from Remote Data base */
		final String hueip = "192.168.1.110"; /* IP of the Hue Lamp retrieved from Remote Data base */
		String url;
		@Override
		    public void onCreate(Bundle savedInstanceState)  {
		        super.onCreate(savedInstanceState);
		        setContentView(R.layout.television);
		        jsonObjSend = new JSONObject();
		        ht = new Httpclient(); 
		        ToggleButton status = (ToggleButton)findViewById(R.id.tvstatus);
			    SeekBar bar = (SeekBar)findViewById(R.id.tvvolume); 
			    bar.setOnSeekBarChangeListener(this);
			    url = "http://"+tvip+":1925/1/input/key";			    
			    status.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
		        	@Override
					public void onCheckedChanged(CompoundButton btn, boolean status) {
						// TODO Auto-generated method stub
		        		 
		        		Log.e("compound","button");
		        		try {
		        			jsonObjSend = new JSONObject();
		        			jsonObjSend.put("key","Standby");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ht.httpclient(jsonObjSend,url,"post");	
					}
		        	});
			    
			    
			    
		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			 jsonObjSend = new JSONObject();
			 try {
				jsonObjSend.put("muted",false); 
				jsonObjSend.put("current",Math.ceil(arg1*0.6));
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
			url ="http://"+tvip+":1925/1/audio/volume";
			ht.httpclient(jsonObjSend,url,"post");	
			
		}
		
		public void buttonclick(View v){  /* Remote Buttons block*/
			url = "http://"+tvip+":1925/1/input/key";	
			Button bt = (Button)v;
			jsonObjSend = new JSONObject();
			try {
				jsonObjSend.put("key",bt.getTag().toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ht.httpclient(jsonObjSend,url,"post");	
			
		}
		
		public void ambilight(View v){ /*ambilight control blosk*/
			Button bt = (Button)v; 
			jsonObjSend = new JSONObject();
			url="http://"+tvip+":1925/1/ambilight/mode";
			try {
				jsonObjSend.put("current","manual");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ht.httpclient(jsonObjSend,url,"post");	
			url="http://"+tvip+":1925/1/ambilight/cached";
			jsonObjSend = new JSONObject();
			

			try {
				if(bt.getTag().toString().equals("red")){
					jsonObjSend.put("r",255);
					jsonObjSend.put("g",0);
					jsonObjSend.put("b",0);
					
				}
					
				else if(bt.getTag().toString().equals("blue")){
					jsonObjSend.put("r",0);
					jsonObjSend.put("g",0);
					jsonObjSend.put("b",255);
				
				}
					else if(bt.getTag().toString().equals("green")){
						jsonObjSend.put("r",0);
						jsonObjSend.put("g",255);
						jsonObjSend.put("b",0);
						
					}
						else if(bt.getTag().toString().equals("yellow")){
							jsonObjSend.put("r",255);
							jsonObjSend.put("g",255);
							jsonObjSend.put("b",0);
							
						}
							
							else if(bt.getTag().toString().equals("skyblue")){
								jsonObjSend.put("r",0);
								jsonObjSend.put("g",255);
								jsonObjSend.put("b",255);
						
							}

				ht.httpclient(jsonObjSend,url,"post");	
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
			
		}
		
		
}