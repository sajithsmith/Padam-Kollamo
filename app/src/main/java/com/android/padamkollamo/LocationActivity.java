package com.android.padamkollamo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity {
	String result;
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	
	
	protected LocationManager locationManager;
	String pid,pic,name,rew,xr,ur,user,mr,start,end;
	
	String webViewUrl;

	WebView webView;
	Bitmap bm = null;
	
	Button b;
	TextView tv;
	ImageView iv;
	ProgressDialog  pDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loca);
        tv =(TextView)findViewById(R.id.textView1);
        b =(Button)findViewById(R.id.button1);
        
        webView = (WebView)findViewById(R.id.webView1);
        
        webView.getSettings().setJavaScriptEnabled(true);

        // Other webview options
        webView.getSettings().setLoadWithOverviewMode(true);

        //webView.getSettings().setUseWideViewPort(true);

        //Other webview settings
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(true); 
        
        
        
Intent i = getIntent();
        
        // rew = i.getStringExtra("add");
		
		// getting product id (pid) from intent
        user= i.getStringExtra("user");
		pid = i.getStringExtra("pid");
		pic = i.getStringExtra("pic");
		name = i.getStringExtra("name");
		rew = i.getStringExtra("rew");
		
		Toast.makeText(getBaseContext(), rew, Toast.LENGTH_LONG).show();
        
       
       
       tv.setText(name);
        
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
        		LocationManager.GPS_PROVIDER, 
        		MINIMUM_TIME_BETWEEN_UPDATES, 
        		MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
        		new MyLocationListener()
        );
      
      b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				webViewUrl = "http://maps.google.com/maps?saddr="+start+"&daddr="+rew;
				
				webView.loadUrl(webViewUrl);

			    // Define Webview manage classes
			    startWebView(); 
			}
		});
        
      
        pDialog = new ProgressDialog(LocationActivity.this);
		pDialog.setMessage("Getting Location ...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
		      
		loadImage();
    }    

	

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			
			
			
			float lat = (float) (location.getLatitude());
			float lng = (float) (location.getLongitude());
			
			
		
			
			start = lat+","+lng;
			
			pDialog.dismiss();
			Toast.makeText(getBaseContext(), "my location :"+start, Toast.LENGTH_LONG).show();
			
			
		
			
		}

		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(LocationActivity.this, "Provider status changed",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(LocationActivity.this,
					"Provider disabled by the user. GPS turned off",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(LocationActivity.this,
					"Provider enabled by the user. GPS turned on",
					Toast.LENGTH_LONG).show();
		}

	}
	
	public static String getUserLocation(String lat, String lon) {
		System.out.println("service started");
        String userlocation = null;
        String readUserFeed = readUserLocationFeed(lat.trim() + ","+ lon.trim());
        try {
            JSONObject Strjson = new JSONObject(readUserFeed);
            JSONArray jsonArray = new JSONArray(Strjson.getString("results"));
            userlocation = jsonArray.getJSONObject(1)
                    .getString("formatted_address").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("User Location ", userlocation);
        return userlocation;
    }



      public static String readUserLocationFeed(String address) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng="+ address + "&sensor=false");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(LocationActivity.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
      
      private void startWebView() {



    	    // Create new webview Client to show progress dialog
    	    // Called When opening a url or click on link
    	    // You can create external class extends with WebViewClient 
    	    // Taking WebViewClient as inner class

    	    webView.setWebViewClient(new WebViewClient() {      
    	        ProgressDialog progressDialog;

    	        //If you will not use this method url links are open in new brower not in webview
    	        public boolean shouldOverrideUrlLoading(WebView view, String url) {              

    	            // Check if Url contains ExternalLinks string in url 
    	            // then open url in new browser
    	            // else all webview links will open in webview browser
    	        	if (url.startsWith("tel:")) { 
    	                Intent intent = new Intent(Intent.ACTION_DIAL,
    	                        Uri.parse(url)); 
    	                startActivity(intent); 
    	        }else if(url.startsWith("http:") || url.startsWith("https:")) {
    	            view.loadUrl(url);
    	        }
    	        return true;

    	                // Stay within this webview and load url
    	             // view.loadUrl(url); 
    	            //    return true;


    	        }



    	        //Show loader on url load
    	        public void onLoadResource (WebView view, String url) {

    	            // if url contains string androidexample
    	            // Then show progress  Dialog
    	            if (progressDialog == null && url.contains("any Text If you want") 
    	                    ) {

    	                // in standard case YourActivity.this
    	                progressDialog = new ProgressDialog(LocationActivity.this);
    	                progressDialog.setMessage("Loading...");
    	                progressDialog.show();
    	            }
    	        }

    	        // Called when all page resources loaded
    	        public void onPageFinished(WebView view, String url) {

    	            try{
    	                // Close progressDialog
    	                if (progressDialog.isShowing()) {
    	                    progressDialog.dismiss();
    	                    progressDialog = null;
    	                }
    	            }catch(Exception exception){
    	                exception.printStackTrace();
    	            }
    	        }

    	    }); 


    	    // You can create external class extends with WebChromeClient 
    	    // Taking WebViewClient as inner class
    	    // we will define openFileChooser for select file from camera or sdcard

    	    webView.setWebChromeClient(new WebChromeClient() {

    	        // openFileChooser for Android 3.0+
    	        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType){  

    	            // Update message
    	         //   mUploadMessage = uploadMsg;

    	            try{    

    	                // Create AndroidExampleFolder at sdcard

    	                File imageStorageDir = new File(
    	                                       Environment.getExternalStoragePublicDirectory(
    	                                       Environment.DIRECTORY_PICTURES)
    	                                       , "AndroidExampleFolder");

    	                if (!imageStorageDir.exists()) {
    	                    // Create AndroidExampleFolder at sdcard
    	                    imageStorageDir.mkdirs();
    	                }

    	           

    	              }
    	             catch(Exception e){
    	                 Toast.makeText(getBaseContext(), "Exception:"+e, 
    	                            Toast.LENGTH_LONG).show();
    	             }

    	        }

    	        // openFileChooser for Android < 3.0
    	        public void openFileChooser(ValueCallback<Uri> uploadMsg){
    	            openFileChooser(uploadMsg, "");
    	        }

    	        //openFileChooser for other Android versions
    	        public void openFileChooser(ValueCallback<Uri> uploadMsg, 
    	                                   String acceptType, 
    	                                   String capture) {

    	            openFileChooser(uploadMsg, acceptType);
    	        }



    	        // The webPage has 2 filechoosers and will send a 
    	        // console message informing what action to perform, 
    	        // taking a photo or updating the file

    	        public boolean onConsoleMessage(ConsoleMessage cm) {  

    	            onConsoleMessage(cm.message(), cm.lineNumber(), cm.sourceId());
    	            return true;
    	        }

    	        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
    	            //Log.d("androidruntime", "Show console messages, Used for debugging: " + message);

    	        }
    	    });   // End setWebChromeClient

    	}



    	// Return here when file selected from camera or from SDcard

    	@Override 
    	protected void onActivityResult(int requestCode, int resultCode,  
    	                                   Intent intent) { 

    	

    	     

    	 

    	}

    	// Open previous opened link from history on webview when back button pressed

    	@Override
    	// Detect when the back button is pressed
    	public void onBackPressed() {

    	    if(webView.canGoBack()) {

    	        webView.goBack();

    	    } else {
    	        // Let the system handle the back button
    	        super.onBackPressed();
    	    }
    	}
    	
    	public void loadImage() {
    		// TODO Auto-generated method stub
    int loader = R.drawable.loader;
            
            // Imageview to show
            ImageView image = (ImageView) findViewById(R.id.imageView1);
            
            // Image url
            String image_url = "http://192.168.43.73/movie_review/uploadedimages/"+pic;
            URL url;
    		try {
    			url = new URL(image_url);
    		
            
            // ImageLoader class instance
            try {
    			bm = BitmapFactory.decodeStream(url.openConnection().getInputStream());
    			image.setImageBitmap(bm);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		} catch (MalformedURLException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    	}
    
}