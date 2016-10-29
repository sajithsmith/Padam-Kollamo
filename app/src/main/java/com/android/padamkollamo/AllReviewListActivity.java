package com.android.padamkollamo;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class   AllReviewListActivity extends ListActivity {

	
	
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// url to get all products list
	private static String url_all_products = "http://192.168.43.73/movie_review/get_all_reviews.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "reviews";
	private static final String TAG_PID = "id";
	private static final String TAG_NAME = "fname";
	private static final String TAG_PIC = "poster";
	private static final String TAG_REW = "review";
	private static final String TAG_XR = "ex_rating";
	private static final String TAG_UR = "us_rating";
	private static final String TAG_COUNT = "count";


	// products JSONArray
	JSONArray products = null;
	String user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_products);
		
		Intent in =  getIntent();
		user= in.getStringExtra("uid");
		Log.d("uid",user);

		// Hashmap for ListView
		productsList = new ArrayList<HashMap<String, String>>();

		// Loading products in Background Thread
		new LoadAllProducts().execute();

		// Get listview
		ListView lv = getListView();

		// on seleting single product
		// launching Edit Product Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String pid = ((TextView) view.findViewById(R.id.pid)).getText()
						.toString();
				String fname = ((TextView) view.findViewById(R.id.name)).getText()
						.toString();
				String pic = ((TextView) view.findViewById(R.id.pic)).getText()
						.toString();
				String rew = ((TextView) view.findViewById(R.id.rew)).getText()
						.toString();
				String xr = ((TextView) view.findViewById(R.id.ex_review)).getText()
						.toString();
				String ur  = ((TextView) view.findViewById(R.id.us_review)).getText()
						.toString();
				String count  = ((TextView) view.findViewById(R.id.rat_count)).getText()
						.toString();
				

				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						AndroidLoadImageFromURLActivity.class);
				// sending pid to next activity
				//in.putExtra("user", user);
				in.putExtra("pid", pid);
				in.putExtra("pic", pic);
				in.putExtra("name", fname);
				in.putExtra("rew", rew);
				in.putExtra("xr", xr);
				in.putExtra("ur", ur);
				in.putExtra("count", count);
				
				
				// starting new activity and expecting some response back
				startActivityForResult(in, 100);
			}
		});

	}

	// Response from Edit Product Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// if result code 100 is received 
			// means user edited/deleted product
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}

	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllProducts extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AllReviewListActivity.this);
			pDialog.setMessage("Loading Data. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
			
			// Check your log cat for JSON reponse
			//Log.d("All Products: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					products = json.getJSONArray(TAG_PRODUCTS);

					// looping through All Products
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_PID);
						String fname = c.getString(TAG_NAME);
						String review = c.getString(TAG_REW);
						String poster = c.getString(TAG_PIC);
						String xr = c.getString(TAG_XR);
						String ur = c.getString(TAG_UR);
						String count = c.getString(TAG_COUNT);
					

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_PID, id);
						map.put(TAG_NAME, fname);
						map.put(TAG_PIC, poster);
						map.put(TAG_REW, review);
						map.put(TAG_XR, xr);
						map.put(TAG_UR, ur);
						map.put(TAG_COUNT,count);
						

						// adding HashList to ArrayList
						productsList.add(map);
					}
				} else {
					// no products found
					// Launch Add New product Activity
					
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
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							AllReviewListActivity.this, productsList,
							R.layout.list_item, new String[] { TAG_PID,
									TAG_NAME,TAG_PIC,TAG_REW,TAG_XR,TAG_UR,TAG_COUNT},
							new int[] { R.id.pid, R.id.name,R.id.pic,R.id.rew,R.id.ex_review,R.id.us_review,R.id.rat_count});
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}