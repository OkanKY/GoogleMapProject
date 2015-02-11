package com.ok.map.screen;

import java.text.NumberFormat;
import java.text.ParsePosition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ok.map.R;
import com.ok.map.database.MapDBHelper;
import com.ok.map.database.MapModel;

public class MapAddActivity extends Activity {
	private EditText txtMapName;
    private EditText txtCost;
	private Button btnRecord,btnBack,btnCalculator;
	private TextView textDuration,textDistance,textResult;
	private Boolean Brecord =false;
	private MapDBHelper dbHelper = new MapDBHelper(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmap);
	       textDistance=(TextView)findViewById(R.id.distance);
		   textDuration=(TextView)findViewById(R.id.duration);
		   textResult=(TextView)findViewById(R.id.sonuc);
	       txtMapName=(EditText)this.findViewById(R.id.uname);
	       txtCost=(EditText)this.findViewById(R.id.pwd);
	       btnRecord=(Button)this.findViewById(R.id.kaydetbtn);
	       btnBack=(Button)this.findViewById(R.id.geribtn);
	       btnCalculator=(Button)this.findViewById(R.id.hesaplabtn);
	       if(getIntent().getBooleanExtra("Record", false))
			{
	       txtCost.setText(""+getIntent().getDoubleExtra("cost", 1));
	       txtMapName.setText(""+getIntent().getStringExtra("name"));
			}
	       btnBack.setOnClickListener(new OnClickListener() {
	       	  @Override
	       	  public void onClick(View v) {
	       		Intent intent = new Intent(MapAddActivity.this,MapListActivity.class);
				startActivity(intent);
				finish();
	       	  }
	       	  });
	       btnCalculator.setOnClickListener(new OnClickListener() {
		       	  @Override
		       	  public void onClick(View v) {
		       			Calculator();
		       	  }
		       	  });
	      // btnRecord.setEnabled(!getIntent().getBooleanExtra("Record", true)); 
	       btnRecord.setOnClickListener(new OnClickListener() {
	  
	  @Override
	  public void onClick(View v) {
	   // TODO Auto-generated method stub
		  
		  dbRecord();
		}

	 });  
	       setText();
	}
	private void setText()
	{
		textDistance.setText("Toplam Uzaklýk "+getIntent().getStringExtra("Distance"));
		textDuration.setText("Tahmini Varýþ Süresi "+getIntent().getStringExtra("Duration"));
	}
	private void Calculator()
	{
        if(isNumeric(txtCost.getText().toString()))
        {
		textResult.setText("Toplam Tutar "+Double.parseDouble(getIntent().getStringExtra("Distance").replaceAll("[^\\d.]+|\\.(?!\\d)", ""))*Double.parseDouble(txtCost.getText().toString())/100+" TL");
        }
        else
        Toast.makeText(MapAddActivity.this,"Lütfen Sayýsal Deðer Giriniz" , Toast.LENGTH_LONG).show();	
	}
	private static boolean isNumeric(String str)
	{
		  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	private void dbRecord()
	{
		if(!getIntent().getBooleanExtra("Record", true)&&!Brecord)
		{
		    MapModel model=new MapModel();
		    try
		    {
			model.setName(txtMapName.getText().toString());
			model.setCost(Double.parseDouble(txtCost.getText().toString()));
			model.setStartLatitude(getIntent().getDoubleExtra("sLatitute", 1));
			model.setStartLongitude(getIntent().getDoubleExtra("sLongitude", 1));
			model.setTargetLatitude(getIntent().getDoubleExtra("dLatitude", 1));
			model.setTargetLongitude(getIntent().getDoubleExtra("dLogitude", 1));
			model.setDistance(getIntent().getStringExtra("Distance"));
			model.setDuration(getIntent().getStringExtra("Duration"));
			dbHelper.createAlarm(model);
   			Toast.makeText(MapAddActivity.this,"Kayýt Ýþlemi Tamamlandý" , Toast.LENGTH_LONG).show();
   			Brecord=true;
		    }
		    catch(Exception e)
		    {
	   	   Toast.makeText(MapAddActivity.this,"Hata Olustu"+e.toString() , Toast.LENGTH_LONG).show();
	
		    }
		}
		else
			Toast.makeText(MapAddActivity.this,"Kayýtlý Veri Tekrar Kayýt Edilemez" , Toast.LENGTH_LONG).show();
				
	}

}
