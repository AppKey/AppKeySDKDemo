package com.appkey.example;

import com.appkey.sdk.AppKeyChecker;
import com.appkey.sdk.AppKeyCheckerCallback;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

/**
 * AppKey SDK Demo Integration
 * 
 * The purpose of this project is to provide an open source example of an AppKey integration example.  This simple activity
 * simulates the main activity of your application.  The AppKey check is automatically refreshed onResume(). The result returns
 * via callback with methods patterned after the Google LVL, but less complicated and simpler to implement.
 *  
 * @author jimvitek
 */
public class MainActivity extends Activity { 

    private TextView mTextViewAppKeyStatus;
    private ImageView mImageViewKeylock;
    private Button mButtonPromptUser;
    private AppKeyChecker mAppKeyChecker; 

    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 

        mImageViewKeylock = (ImageView)findViewById(R.id.imageViewKeylock);
        mTextViewAppKeyStatus = (TextView)findViewById(R.id.textViewAppKeyCheckResult);
        mButtonPromptUser = (Button)findViewById(R.id.buttonPromptUser);
        mButtonPromptUser.setOnClickListener(ButtonPromptUserListener);

        /* AppKeyChecker - Check whether or not AppKey is enabled
         * 
         * Patterned after Google's LVL - http://developer.android.com/google/play/licensing/adding-licensing.html#impl-lc
         * 
         * @param context a Context
         * @param appId AppKey.com assigned AppID of the calling application
         * @param analyticsEnabled if true, and if the calling app has INTERNET permission, the SDK
         *        will send user interaction events along the installation funnel for the purpose of
         *        measuring & optimizing conversion. Events are tracked via Google Analytics using
         *        an anonymous UDID.
         *        
         * a 4th argument overload is available for white-label integrations
         * @param unlockerPackageName packageName of the white-labeled unlocker app
        */
        mAppKeyChecker = new AppKeyChecker(MainActivity.this, "7", true);  //TODO: Replace the 7 with your appId from AppKey.com
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mTextViewAppKeyStatus.setText("Check in progress");
        mImageViewKeylock.setImageResource(R.drawable.appkey_squarekey_gray);

        /* 
         * AppKey check from .onResume() to ensure it is performed each time the user
         * opens or switches to the app.
         */
        mAppKeyChecker.checkAccess(new AppKeyCallback()); 
    }

    class AppKeyCallback implements AppKeyCheckerCallback {

        @Override
        public void allow() { 
            /*
             * Insert code to enable premium content here
             */
        	
        	MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
		            mTextViewAppKeyStatus.setText("App Unlocked");
		            mImageViewKeylock.setImageResource(R.drawable.appkey_squarekey_green);
				}
        	});
        }

        @Override
        public void dontAllow(int reason) { 
            /*
            * Insert code to disable premium content here
            */
        	
        	MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
		            mTextViewAppKeyStatus.setText("App Locked");
		            mImageViewKeylock.setImageResource(R.drawable.appkey_squarekey_red);
				}
        	});
        }
    }
    
    View.OnClickListener ButtonPromptUserListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

		    /**
		     * Prompt the user to install AppKey
		     * 
		     * @param activity an Activity
		     * @param benefit Description of what will be unlocked for AppKey users
		     */
			mAppKeyChecker.promptUser(MainActivity.this, "[your awesome feature]");
		}
	};
}
