package com.ok.map.screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ok.map.R;
import com.ok.map.parser.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapActivity extends FragmentActivity implements LocationListener {
   private String currentDistance;
   private String currentDuration;
   private Boolean complete =false;
   private LatLng curOrigin,curDest;
   private LatLng endPoint;
// Creating MarkerOptions
	MarkerOptions endOptions = new MarkerOptions();
	GoogleMap map;
	ArrayList<LatLng> markerPoints;
	private Button btnCalculator,btnBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnCalculator=(Button)this.findViewById(R.id.hesapla);
		btnBack=(Button)this.findViewById(R.id.geri);
		btnCalculator.setOnClickListener(new OnClickListener() {
	       	  @Override
	       	  public void onClick(View v) {
	       		if(complete)
	       		{
	       		Intent intent = new Intent(MapActivity.this,MapAddActivity.class);
	       		if(getIntent().getBooleanExtra("Record", false))
	       		{	     		
	       		intent.putExtra("name",getIntent().getStringExtra("name"));
	       		intent.putExtra("cost",getIntent().getDoubleExtra("cost", 1));
	       		}
	       		intent.putExtra("Distance",getCurrentDistance());
	       		intent.putExtra("Duration",getCurrentDuration());
	       		intent.putExtra("Location",getIntent().getBooleanExtra("Location", false));
	       		intent.putExtra("Record",getIntent().getBooleanExtra("Record", false));
	       		intent.putExtra("sLatitute",getCurOrigin().latitude);
	       		intent.putExtra("sLongitude",getCurOrigin().longitude);
	       		intent.putExtra("dLatitude",getCurDest().latitude);
	       		intent.putExtra("dLogitude",getCurDest().longitude);
				startActivity(intent);
				finish();
	       		  }
	       		  else
	       			Toast.makeText(MapActivity.this,"Lütfen Secim Ýþlemlerini Tamamlayýnýz" , Toast.LENGTH_LONG).show();
	       	  }
	       	  });
	       btnBack.setOnClickListener(new OnClickListener() {
	  
	  @Override
	  public void onClick(View v) {
	   // TODO Auto-generated method stub
			Intent intent = new Intent(MapActivity.this,MapListActivity.class);
			startActivity(intent);
			finish();

		}
	 });  
		
		// Initializing 
		markerPoints = new ArrayList<LatLng>();
		
		// Getting reference to SupportMapFragment of the activity_main
		SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
		
		// Getting Map for the SupportMapFragment
		map = fm.getMap();
		
		// Enable MyLocation Button in the Map
		//map.setMyLocationEnabled(true);	

		 // Getting LocationManager object from System Service LOCATION_SERVICE
       LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
       
       // Creating a criteria object to retrieve provider
       Criteria criteria = new Criteria();

       // Getting the name of the best provider
       String provider = locationManager.getBestProvider(criteria, true);

       // Getting Current Location
        final Location location = locationManager.getLastKnownLocation(provider);
        if(getIntent().getBooleanExtra("Record", false)&&!getIntent().getBooleanExtra("Location", true) )
		{
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(getIntent().getDoubleExtra("sLatitute", 1),getIntent().getDoubleExtra("sLongitude", 1))));	
		}
        else
         map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

        // Zoom in the Google Map
    	map.animateCamera(CameraUpdateFactory.zoomTo(8));	

	  if(!getIntent().getBooleanExtra("Record", false))
	  {	// Setting onclick event listener for the map
		map.setMyLocationEnabled(true);
		map.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng point) {
				if(getIntent().getBooleanExtra("Location", false) && location!=null)
				{
				//for movetion location	
			    endPoint=point;
				setChangePointMap(point,new LatLng(location.getLatitude(),location.getLongitude()));
				}
				else
			    setClickMap(point);
					
			}
		});
	}
	else
	{   
		//Not Location use record data
		if(!getIntent().getBooleanExtra("Location", false))
		{

			setChangePointMap(new LatLng(getIntent().getDoubleExtra("dLatitude", 1),getIntent().getDoubleExtra("dLogitude", 1)),
			        new LatLng(getIntent().getDoubleExtra("sLatitute", 1),getIntent().getDoubleExtra("sLongitude", 1)));
		}
		//Use Current Location
		else if(location!=null)
		{
			
			onLocationChanged(location);
			locationManager.requestLocationUpdates(provider, 20000, 0, this);
		}
	}	
 }
	private void setClickMap(LatLng point)
	{
		// Already two locations				
		if( markerPoints.size()>1){
			markerPoints.clear();
			map.clear();
			complete=false;
		}
		
		// Adding new item to the ArrayList
		markerPoints.add(point);				
		
		// Creating MarkerOptions
		MarkerOptions options = new MarkerOptions();
		
		// Setting the position of the marker
		options.position(point);
		
		/** 
		 * For the start location, the color of marker is GREEN and
		 * for the end location, the color of marker is RED.
		 */
		if(markerPoints.size()==1){
			complete=false;
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		}else if(markerPoints.size()==2){
			options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		}
					
		
		// Add new marker to the Google Map Android API V2
		map.addMarker(options);
		
		// Checks, whether start and end locations are captured
		if(markerPoints.size() >= 2){					
			runService(markerPoints.get(0),markerPoints.get(1));
		}
	}
	private void setChangePointMap(LatLng endPoint,LatLng startPoint)
	{
		map.clear();
		MarkerOptions endOptions = new MarkerOptions();
		// Setting the position of the marker
		endOptions.position(endPoint);
		// * for the end location, the color of marker is RED.
		endOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		// Add new marker to the Google Map Android API V2
		// Creating MarkerOptions
		MarkerOptions startOptions = new MarkerOptions();
	    startOptions.position(startPoint);
		// * For the start location, the color of marker is GREEN and
		startOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
	    // Add new marker to the Google Map Android API V2
		map.addMarker(startOptions);
		map.addMarker(endOptions);
		//  whether start and end locations are captured				
		runService(startPoint,endPoint);
			
	}
	private void runService(LatLng origin,LatLng dest)
	{
		if (isNetworkAvailable() == true) {
		setCurOrigin(origin);
		setCurDest(dest);
		// Getting URL to the Google Directions API
		String url = getDirectionsUrl(origin, dest);				
		
		DownloadTask downloadTask = new DownloadTask();
		// Start downloading json data from Google Directions API
		downloadTask.execute(url);
		}
		 else
		  Toast.makeText(MapActivity.this, "Ýnternette Baðlý Deðil", Toast.LENGTH_LONG).show();
	    
	}
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager
	            .getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	private String getDirectionsUrl(LatLng origin,LatLng dest){
					
		// Origin of route
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		
		// Destination of route
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		
					
		// Sensor enabled
		String sensor = "sensor=false";			
					
		// Building the parameters to the web service
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
					
		// Output format
		String output = "json";
		
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		
		
		return url;
	}
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

	
	
	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String>{			
				
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {
				
			// For storing data from web service
			String data = "";
					
			try{
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}
		
		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			
			
			ParserTask parserTask = new ParserTask();
			
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
				
		}		
	}
	
	/** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
    	
    	// Parsing the data in non-ui thread    	
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
			
			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           
            
            try{
            	jObject = new JSONObject(jsonData[0]);
            	DirectionsJSONParser parser = new DirectionsJSONParser();
            	
            	// Starts parsing data
            	routes = parser.parse(jObject);    
            }catch(Exception e){
            	e.printStackTrace();
            }
            return routes;
		}
		
		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();
			String distance = "";
			String duration = "";
			
			
			
			if(result.size()<1){
				Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
				return;
			}
				
			
			// Traversing through all the routes
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				
				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);
				
				// Fetching all the points in i-th route
				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);	
					
					if(j==0){	// Get distance from the list
						distance = (String)point.get("distance");						
						continue;
					}else if(j==1){ // Get duration from the list
						duration = (String)point.get("duration");
						continue;
					}
					
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	
					
					points.add(position);						
				}
				
				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);	
				
			}
			
			
			// Drawing polyline in the Google Map for the i-th route
			map.addPolyline(lineOptions);	
			setCurrentDistance(distance);
			setCurrentDuration(duration);
           complete=true;
		}			
    }   
    public String getCurrentDistance() {
		return currentDistance;
	}

	public void setCurrentDistance(String currentDistance) {
		this.currentDistance = currentDistance;
	}

	public String getCurrentDuration() {
		return currentDuration;
	}

	public void setCurrentDuration(String currentDuration) {
		this.currentDuration = currentDuration;
	}

	public LatLng getCurOrigin() {
		return curOrigin;
	}

	public void setCurOrigin(LatLng curOrigin) {
		this.curOrigin = curOrigin;
	}

	public LatLng getCurDest() {
		return curDest;
	}

	public void setCurDest(LatLng curDest) {
		this.curDest = curDest;
	}

    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menumap, menu);
		return true;
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		// Getting latitude of the current location
		if(getIntent().getBooleanExtra("Location", false))
		{		double latitude = location.getLatitude();
				
				// Getting longitude of the current location
				double longitude = location.getLongitude();		
				
				// Creating a LatLng object for the current location
				LatLng latLng = new LatLng(latitude, longitude);

				
				// Showing the current location in Google Map
				map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
				
				// Zoom in the Google Map
				map.animateCamera(CameraUpdateFactory.zoomTo(8));	
				  if(getIntent().getBooleanExtra("Record", false))
				  {
					  setChangePointMap(new LatLng(getIntent().getDoubleExtra("dLatitude", 1.0),getIntent().getDoubleExtra("dLogitude", 2.0)),
								latLng);
				  }
				  else if(complete)
					  setChangePointMap(endPoint,latLng);
					  
				
		}
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}	
}