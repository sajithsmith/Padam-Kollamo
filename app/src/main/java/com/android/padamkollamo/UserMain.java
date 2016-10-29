package com.android.padamkollamo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserMain extends Activity{
	
	Button bt1,bt2,b1;
	String user;
	
	protected void onCreate(Bundle sis)
	{
		super.onCreate(sis);
		setContentView(R.layout.user_main);
		
		Intent in =  getIntent();
		user= in.getStringExtra("id");
		
		bt1 = (Button)findViewById(R.id.b1);
		bt2 = (Button)findViewById(R.id.b2);
		b1 = (Button)findViewById(R.id.button1);
		
		
		
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getBaseContext(),LoginActivity.class);
				startActivity(in);
				finish();
			}
		});
		bt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(getApplicationContext(),AllNewReleases.class);
				in.putExtra("uid", user);
				startActivity(in);
				
			}
		});
		
bt2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent in = new Intent(getApplicationContext(),AllReviewListActivity.class);
				in.putExtra("uid", user);
				startActivity(in);
				
			}
		});
	}

}
