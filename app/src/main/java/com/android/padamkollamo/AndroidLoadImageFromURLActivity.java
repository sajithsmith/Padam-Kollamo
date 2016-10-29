package com.android.padamkollamo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AndroidLoadImageFromURLActivity extends Activity {
	
	float ratings[];
	String pid,pic,name,rew,xr,ur,user,mr,count;
	TextView t1,t2;
	float xrt,urt,art,mrt,nr;
	Bitmap b = null;
	private static final String url = "http://192.168.43.73/movie_review/check_rating.php";
	
	RatingBar r1,r2;
	Button b1,b2,b3,b4;
	TextView us;
	WebView webView;
	int success;
	private static final String TAG_SUCCESS = "success";
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_review);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
        
        
       
        
       
        Intent i = getIntent();
        
        
		
		// getting product id (pid) from intent
       // user= i.getStringExtra("user");
		pid = i.getStringExtra("pid");
		pic = i.getStringExtra("pic");
		name = i.getStringExtra("name");
		rew = i.getStringExtra("rew");
		xr = i.getStringExtra("xr");
		ur = i.getStringExtra("ur");
		count = i.getStringExtra("count");
		//Toast.makeText(getBaseContext(), ur, Toast.LENGTH_LONG).show();
		
		xrt = Float.parseFloat(xr);
		art = Float.parseFloat(ur);
		
		
		webView =  (WebView)findViewById(R.id.webView1);
		t1 = (TextView)findViewById(R.id.title);
		t2 = (TextView)findViewById(R.id.review);
		
		us = (TextView)findViewById(R.id.uid);
		r1 = (RatingBar)findViewById(R.id.ratingBar1);
		r2 = (RatingBar)findViewById(R.id.ratingBar2);
		
		b1 = (Button)findViewById(R.id.button1);
		b2 = (Button)findViewById(R.id.button2);
		b3 = (Button)findViewById(R.id.button3);
		b4 = (Button)findViewById(R.id.button4);
		
		//Toast.makeText(getApplicationContext(), count, Toast.LENGTH_LONG).show();
		
		us.setText(user);
		
		new check().execute();
		
		b4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(getApplicationContext(),read.class);
				in.putExtra("fn", t2.getText().toString());
				startActivity(in);
				
			}
		});
		
		 
		
		 b1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//showRatingAlert();
					
					final Dialog dialog = new Dialog(AndroidLoadImageFromURLActivity.this);
					dialog.setContentView(R.layout.custom);
					dialog.setTitle("Your Rating . . .");

					// set the custom dialog components - text, image and button
					final RatingBar rat = (RatingBar) dialog.findViewById(R.id.ratingBar1);
					rat.setNumStars(5);
					Button b1 = (Button) dialog.findViewById(R.id.button1);
					

					Button b2 = (Button) dialog.findViewById(R.id.button2);
					// if button is clicked, close the custom dialog
					b1.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(getBaseContext(), String.valueOf(rat.getRating()), Toast.LENGTH_LONG).show();
						
							
							float val = rat.getRating();
							 
							 if(count.equals("0"))
							 {
							 	nr = val;
							 	new CountIncrement().execute();
							 	
							 }
							else 
							{
								int tempcount= Integer.parseInt(count);
								nr = ((art * tempcount + val))/(tempcount +1);
							 	new CountIncrement().execute();

							 	
							}
							 
							 BufferedReader in;
							 
							 String rating = String.valueOf(nr);
								
								try
								{
							 DefaultHttpClient httpclient = new DefaultHttpClient();
							 HttpGet httpget = new HttpGet("http://192.168.43.73/movie_review/user_rating.php?fid="+pid+"&uid="+user+"&rat="+rating);
							 HttpResponse response = httpclient.execute(httpget);
							 in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							 StringBuffer sb = new StringBuffer("");
							 String line = "";
							 String NL = System.getProperty("line.separator");
							 while ((line = in.readLine()) != null) {                    
							     sb.append(line + NL);
							 }
							 in.close(); 
							 String result = sb.toString();
							 Log.v("My Response :: ", result);
							 //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
								}
							 catch(Exception exe)
							 {
							 	Log.v("EXCEPTION :: ", exe.toString());
							 }
						
						}
					});

					
					b2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					dialog.show();
						
						}

				
			});
		 
		 b2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showEditAlert();
				}

				
			});
		 
		 b3.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent in = new Intent(getApplicationContext(),Comments.class);
					in.putExtra("pid", pid);
					startActivity(in);
				}

				
			});
		 
		 r1.setStepSize((float)0.25);
		 r2.setStepSize((float)0.25);
		
		
		r1.setRating(xrt);
		r2.setRating(art);
		
		t1.setText(name);
		t2.setText(rew);
		
r1.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});

r2.setOnTouchListener(new OnTouchListener() {
	
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
});
		
        
        // Loader image - will be shown before loading image
        loadImage();
        displayWebView();
    }

	protected void showEditAlert() {
		// TODO Auto-generated method stub
		
		 AlertDialog.Builder ab =  new AlertDialog.Builder(AndroidLoadImageFromURLActivity.this);
         ab.setTitle("Enter Your Comment");
        // ab.setMessage("Please enter your opinion"); 
         final EditText input =  new EditText(AndroidLoadImageFromURLActivity.this);
         LinearLayout.LayoutParams lp =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
         input.setLayoutParams(lp);
         ab.setView(input);
       
         //ab.setIcon(R.drawable.icon);
         ab.setPositiveButton("POST", new DialogInterface.OnClickListener() {
 			
 			@Override
 			public void onClick(DialogInterface dialog, int which) {
 				// TODO Auto-generated method stub
 String pass = input.getText().toString();
 
	BufferedReader in;
 	
 	try
 	{
 	DefaultHttpClient httpclient = new DefaultHttpClient();
 	String u = "http://192.168.43.73/movie_review/user_comments.php?fname="+pass+"&uname="+us.getText().toString()+"&fid="+pid;
    URI uri = new URI(u.replace(" ", "%20"));
    String nu = uri.toString();
    Log.d("uri",nu);
 	HttpGet httpget = new HttpGet(nu);
    HttpResponse response = httpclient.execute(httpget);
    in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    StringBuffer sb = new StringBuffer("");
    String line = "";
    String NL = System.getProperty("line.separator");
    while ((line = in.readLine()) != null) {                    
        sb.append(line + NL);
    }
    in.close();
    String result = sb.toString();
    Log.v("My Response :: ", result);
    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
 	}
    catch(Exception exe)
    {
    	Log.v("EXCEPTION :: ", exe.toString());
    }
 
 			}
 		}); 
         ab.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
 			
 			@Override
 			public void onClick(DialogInterface dialog, int which) {
 				// TODO Auto-generated method stub
 				dialog.cancel();
 			}
 		});
         ab.show();
		
	}

	

	public void displayWebView() {
	// TODO Auto-generated method stub
	
	//Get webview 
    webView = (WebView) findViewById(R.id.webView1);

    // Define url that will open in webview 
    String webViewUrl = "http://192.168.43.73/movie_review/getandsettext/test.php?fname="+t2.getText().toString();



    // Javascript inabled on webview  
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

    //Load url in webview
    webView.loadUrl(webViewUrl);

    // Define Webview manage classes
    startWebView(); 

	
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
                progressDialog = new ProgressDialog(AndroidLoadImageFromURLActivity.this);
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
    
    
    }
); }

	private void loadImage() {
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
			b = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			image.setImageBitmap(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	class check extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AndroidLoadImageFromURLActivity.this);
			pDialog.setMessage("Loading product details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					
					try {
						// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("fid", pid));
						params.add(new BasicNameValuePair("uid", user));
						// getting product details by making HTTP request
						// Note that product details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url, "GET", params);

						// check your log for json response
						Log.d("Single Product Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}


		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
			if(success==1)
			{
				b1.setVisibility(View.GONE);
			}
		}
	}
	
	class CountIncrement extends AsyncTask<String, String, String> {
		
		private static final String counturl = "http://192.168.43.73/movie_review/countincrement.php";

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AndroidLoadImageFromURLActivity.this);
			pDialog.setMessage("Count Operation");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
		
			
			

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("pid", pid));
	
			
			
			

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(counturl,
					"POST", params);
			
			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(), LoginActivity.class);
					startActivity(i);
					
					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
			//zToast.makeText(getBaseContext(), "Sucess(count)", Toast.LENGTH_LONG).show();
		}

	}
	
	
	
	
}