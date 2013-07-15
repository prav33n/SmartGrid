package context.sg;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, CallDetectService.class);
        startService(intent);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
        
    }
    
    public void showinterface(View V){
    	ImageButton bt = (ImageButton)V;
    	Intent i;
    	if(bt.getTag().toString().equalsIgnoreCase("television"))
    		i = new Intent(this.getApplicationContext(),Television.class);
    	else if(bt.getTag().toString().equalsIgnoreCase("lightnew"))
    		i = new Intent(this.getApplicationContext(),Lightnew.class);
    	else 
    		i = new Intent(this.getApplicationContext(),MainActivity.class);
        startActivity(i);  
    }
    
}
