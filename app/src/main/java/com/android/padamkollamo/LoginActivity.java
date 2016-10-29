package com.android.padamkollamo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoginActivity extends Activity {



    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FacebookException e;

    boolean loggedIn;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.login_activity);



        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);

        loggedIn = isFacebookLoggedIn();

        if(loggedIn==true)
        {
            Intent in = new Intent(getBaseContext(),UserMain.class);
            startActivity(in);
            finish();

        }





        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                String id1 = loginResult.getAccessToken().getUserId();

                Intent in = new Intent(getApplicationContext(),UserMain.class);
                in.putExtra("id",id1);
                startActivity(in);



                // Toast.makeText(getBaseContext(), loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
                // Toast.makeText(getBaseContext(), loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
               Toast.makeText(getApplicationContext(),"Login attempt canceled.",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(),"Login attempt failed.",Toast.LENGTH_LONG).show();

            }




        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }
}