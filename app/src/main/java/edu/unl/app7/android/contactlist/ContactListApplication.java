package edu.unl.app7.android.contactlist;

import android.app.Application;

import com.firebase.client.Firebase;


public class ContactListApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // The Firebase library must be initialized once with an Android context
        Firebase.setAndroidContext(getApplicationContext());
        // Enabling disk persistence allows our app to also keep all of its state even after an app restart
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
