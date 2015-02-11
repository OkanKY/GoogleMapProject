package com.ok.map.screen;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ok.map.R;
import com.ok.map.database.MapDBHelper;
import com.ok.map.database.MapModel;



public class MapListActivity extends Activity implements OnClickListener {
	Button enter,exit;
 	private List<MapModel> mapList;
 	private ArrayAdapter<MapModel> adapter;
 	private ListView listView1;
 	private RadioButton rdLocation;
 	private MapDBHelper dbHelper = new MapDBHelper(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.maplist);
	    rdLocation = (RadioButton) findViewById(R.id.rbLocation);
	    listView1 = (ListView) findViewById(R.id.listView1);
	    listView1.setOnItemClickListener(new OnItemClickListener() {
	    	  @Override
	    	  public void onItemClick(AdapterView<?> parent, View view,
	    	    int pos, long id) {
	    		  if (isNetworkAvailable() == true) {
	    		    Intent intent = new Intent(MapListActivity.this,MapActivity.class);
	    		    intent.putExtra("Record",true);
	    		    intent.putExtra("Location",rdLocation.isChecked());
	    		    intent.putExtra("name",mapList.get(pos).getName());
	    		    intent.putExtra("Distance",mapList.get(pos).getDistance());
		       		intent.putExtra("Duration",mapList.get(pos).getDuration());
		       		intent.putExtra("sLatitute",mapList.get(pos).getStartLatitude());
		       		intent.putExtra("sLongitude",mapList.get(pos).getStartLongitude());
		       		intent.putExtra("dLatitude",mapList.get(pos).getTargetLatitude());
		       		intent.putExtra("dLogitude",mapList.get(pos).getTargetLongitude());
		       		intent.putExtra("cost",mapList.get(pos).getCost());
	    			startActivity(intent);
	    			finish();
	    		  }
	    		  else
	       Toast.makeText(MapListActivity.this, "Ýnternette Baðlý Deðil", Toast.LENGTH_LONG).show();
 
	    	
	    	  }
	    	});
	    listView1.setLongClickable(true);
	  
	    listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){ 
            @Override 
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) 
           { 
                deleteAlarm(mapList.get(pos).getId());
				return true;
           } 
      });
	    RefreshAdapter();
	    enter = (Button) findViewById(R.id.enter);
	    exit=(Button)this.findViewById(R.id.exitmap);
	    enter.setOnClickListener(this);
	    exit.setOnClickListener(this);
	   
	    
	}
	public void deleteAlarm(long id) {
		final long alarmId = id;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Secilen Kaydý Silmek Ýstiyormusunuz")
		.setTitle("Kayýt Silme?")
		.setCancelable(true)
		.setNegativeButton("Hayýr", null)
		.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dbHelper.deleteAlarm(alarmId);
				RefreshAdapter();
			}
		
		}).show();
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager
	            .getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	@Override
	public void onClick(View v) { 
	    if (v.getId() == R.id.enter) {
  		  if (isNetworkAvailable() == true) {
	    	Intent intent = new Intent(MapListActivity.this,MapActivity.class);
	    	intent.putExtra("Location",rdLocation.isChecked());
	    	intent.putExtra("Record",false);
			startActivity(intent);
			finish();
  		  }
  		  else
  		  Toast.makeText(MapListActivity.this, "Ýnternette Baðlý Deðil", Toast.LENGTH_LONG).show();
	    	
	    	}
	    if (v.getId() == R.id.exitmap) {
	    	finish();
	    	}
	
	    
	}
	

	private void RefreshAdapter()
	{   
		if(dbHelper.getAlarms()!=null)
		{
		mapList=dbHelper.getAlarms();

		}
		else
		{
		Toast.makeText(MapListActivity.this,"Hiç Kayýt Yok" , Toast.LENGTH_LONG).show();
		mapList=new ArrayList<MapModel>() ;  	
		}
		adapter = new ArrayAdapter<MapModel>(this,
                android.R.layout.simple_list_item_1, mapList);
		listView1.setAdapter(adapter);

		
	}


	
}
