package com.appkey.example;

import com.appkey.sdk.AppKeyChecker;
import com.appkey.sdk.AppKeyCheckerCallback;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

/**
 * MainActivity of the AppKey Example project
 * 
 * The purpose of this project is to provide an AppKey integration example.  This simple activity
 * simulates the hub activity of your application.  The check itself is patterned after the Google
 * License Verification Library (LVL), but less complicated and thus simpler to implement.
 *  
 * @author jimvitek
 */
public class MainActivity extends Activity {

    private TextView mTextViewAppKeyCheckResult=null;
    private ImageView mImageViewKeylock;

    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 

        mImageViewKeylock=(ImageView)findViewById(R.id.imageViewKeylock);
        mTextViewAppKeyCheckResult=(TextView)findViewById(R.id.textViewAppKeyCheckResult);
    }
    
    @Override
    protected void onResume() { 
        super.onResume();
        mTextViewAppKeyCheckResult.setText("Check in progress");
        mImageViewKeylock.setImageResource(R.drawable.appkey_squarekey_gray);

        /* 
         * AppKey check from .onResume() to ensure it is performed each time the app is used.
         * The checker will automatically discard excess calls, so don't worry if it is called 
         * several times per session.
         */
        AppKeyChecker akChecker = new AppKeyChecker(MainActivity.this, "7");  //TODO: Replace 7 with appId from AppKey.com
        akChecker.checkAccess(new AppKeyCallback()); 
    }

    class AppKeyCallback implements AppKeyCheckerCallback {

        @Override
        public void allow() { 
            /*
             * Insert code to enable premium functionality below
             */
            mTextViewAppKeyCheckResult.setText("App Unlocked");
            mImageViewKeylock.setImageResource(R.drawable.appkey_squarekey_green);
        }

        @Override
        public void dontAllow(int reason) { 
            /*
            *
            * Insert code to disable premium functionality below
            * 
            */
            mTextViewAppKeyCheckResult.setText("App Locked");
            mImageViewKeylock.setImageResource(R.drawable.appkey_squarekey_red);

            /*
             * Prompt the user to purchase a paid upgrade, or install AppKey for premium functionality.
             * The AppKeyWizard is provided as a simple way to prompt the user with AlertDialog
             * based on how far they are through the installation process.  Unfortunately, android does
             * not permit automatic installation of AppWidgets even if the user wants it.  The AppKeyWizard
             * provides contextual help for users who do not know how to install a widget.
             * 
             * Note, remember to customize the strings in appkeywizard.xml to fit your app.  These messages 
             * are often the users first exposure to AppKey, and the more contextual they are to your app, 
             * the more likely the user will buy an upgrade or install AppKey.
             * 
             * Optional: Include a Uri to the premium/paid version of this app if you have one
             */
            new AppKeyWizard(MainActivity.this,reason,null);
            //new AppKeyWizard(MainActivity.this,reason,Uri.parse("market://details?id=com.appkey.widget"));  //TODO: Put your premium app in the Uri
        }
    }
}
