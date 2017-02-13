package com.stage3dev.bluenotify;

import android.os.Bundle;

import com.trello.rxlifecycle2.components.RxActivity;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;
import co.lujun.lmbluetoothsdk.BluetoothController;

public class ConnectActivity extends RxActivity {

    @Inject
    BluetoothController bluetoothController;

    @BindString(R.string.sdp_uuid)
    String sdpUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ((App) getApplication()).getUserComponent().inject(this);
        ButterKnife.bind(this);

    }


    private void scanForDevices() {

    }
}
