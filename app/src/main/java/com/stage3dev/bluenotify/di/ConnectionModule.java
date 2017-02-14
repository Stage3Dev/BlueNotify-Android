package com.stage3dev.bluenotify.di;

import android.content.Context;

import com.stage3dev.bluenotify.R;
import com.stage3dev.bluenotify.bluetooth.BTManager;
import com.stage3dev.bluenotify.bluetooth.BTManagerImpl;

import java.util.UUID;

import javax.inject.Singleton;

import co.lujun.lmbluetoothsdk.BluetoothController;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kyle on 2/11/17.
 */

@Module
public class ConnectionModule {

    private static final String TAG = ConnectionModule.class.getSimpleName();

    @Provides
    @Singleton
    BluetoothController provideBluetoothController(Context ctx) {
        BluetoothController controller = BluetoothController.getInstance().build(ctx);
//        controller.setAppUuid(UUID.fromString(ctx.getString(R.string.sdp_uuid)));
        return controller;
    }

    @Provides
    @Singleton
    BTManager provideBTManager(BluetoothController btc) {
        return new BTManagerImpl(btc);
    }

}
