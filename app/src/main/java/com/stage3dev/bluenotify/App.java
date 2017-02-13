package com.stage3dev.bluenotify;

import android.app.Application;

import com.stage3dev.bluenotify.di.AndroidModule;
import com.stage3dev.bluenotify.di.ConnectionModule;
import com.stage3dev.bluenotify.di.DaggerUserComponent;
import com.stage3dev.bluenotify.di.UserComponent;

/**
 * Created by Kyle on 2/11/17.
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private UserComponent userComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        userComponent = DaggerUserComponent.builder()
                .androidModule(new AndroidModule(this))
                .connectionModule(new ConnectionModule())
                .build();

    }

    public UserComponent getUserComponent() {
        return userComponent;
    }
}
