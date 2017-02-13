package com.stage3dev.bluenotify.di;

import android.content.Context;

import com.stage3dev.bluenotify.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kyle on 1/26/17.
 */

@Module
public class AndroidModule {

    private final App app;

    public AndroidModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app;
    }
}
