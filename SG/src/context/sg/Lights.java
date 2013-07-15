package context.sg;

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
import android.widget.ToggleButton;

public class Lights extends Activity implements SeekBar.OnSeekBarChangeListener{
	JSONObject jsonObjSend;
	Httpclient ht;
	final String hueip = "192.168.1.104";
	String url;
	@Override
	    public void onCreate(Bundle savedInstanceState)  {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.light);
	        url ="http://"+hueip+"/control/huescript.php";
	        ht = new Httpclient();	        
	        ToggleButton status = (ToggleButton)findViewById(R.id.status);
	       
	    	        SeekBar bar = (SeekBar)findViewById(R.id.lumins); // make seekbar object
	    	        bar.setOnSeekBarChangeListener(this);
	        RadioGroup preset = (RadioGroup)findViewById(R.id.presets);
	        
	        preset.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
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
								jsonpreset.put("status",true);
								jsonpreset.put("bri",50);
								jsonpreset.put("hue",20000);
								
							}
							else if(btn.getText().equals("Games")){
								jsonpreset.put("status",true);
								jsonpreset.put("bri",150);
								jsonpreset.put("hue",40000);
							}
							else if(btn.getText().equals("Ambient")){
								jsonpreset.put("status",true);
								jsonpreset.put("bri",250);
								jsonpreset.put("hue",60000);
							}
							jsonObjSend.put("preset",jsonpreset);
							ht.httpclient(jsonObjSend,url);
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
	        			jsonObjSend.put("device","light");
						jsonObjSend.put("status",status);
						ht.httpclient(jsonObjSend,url);
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
		Log.e("Lumins",""+progress);
		try {
			jsonObjSend.put("device","light");
			jsonObjSend.put("lumins",(progress*2));
			ht.httpclient(jsonObjSend,url);		} catch (JSONException e) {
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
	/*	Log.e("Lumins",""+seekBar.getProgress());
		try {
			jsonObjSend.put("device","light");
			jsonObjSend.put("lumins",""+seekBar.getProgress());
			ht.httpclient(jsonObjSend,"url");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	public void setcolor(View V)
	{
					jsonObjSend = new JSONObject();
					try {
				 	jsonObjSend.put("device","light");
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
							
					ht.httpclient(jsonObjSend,url);		
				
	 	} 
	 	catch (JSONException e) {
	// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

}