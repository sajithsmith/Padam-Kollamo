package com.android.padamkollamo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class first extends Activity{
	
	String add = "Ariesplex SL Cinemas, Kunnumpuram Road, Near Ayurveda College, Thiruvananthapuram, Kerala 695001";
	
	
	protected void onCreate(Bundle sis)
	
	{
		super.onCreate(sis);
		setContentView(R.layout.main);
		
		Intent in = new Intent(getApplicationContext(),LocationActivity.class);
		
		in.putExtra("add", add);
		startActivity(in);
	}

}
