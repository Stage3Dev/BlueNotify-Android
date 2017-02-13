package com.stage3dev.bluenotify.di;

import com.stage3dev.bluenotify.ConnectActivity;
import com.stage3dev.bluenotify.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Kyle on 2/11/17.
 */

@Singleton
@Component(modules = {AndroidModule.class, ConnectionModule.class})
public interface UserComponent {

    void inject(MainActivity mainActivity);

    void inject(ConnectActivity connectActivity);
}
