package com.stage3dev.bluenotify;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.stage3dev.bluenotify.bluetooth.BTManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.lmbluetoothsdk.base.State;

import static com.trello.rxlifecycle2.android.RxLifecycleAndroid.bindActivity;

public class MainActivity extends RxAppCompatActivity {

    @BindView(R.id.bluetooth_switch)
    Switch btSwitch;

    @BindView(R.id.connection_status)
    TextView tvConnectionStatus;


    @Inject
    BTManager btManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App) getApplicationContext()).getUserComponent().inject(this);
        ButterKnife.bind(this);

        btManager.getBTRadioState()
                .compose(bindActivity(lifecycle()))
                .subscribe(integer -> {
                    btSwitch.setChecked(integer == BluetoothAdapter.STATE_ON);
                    btSwitch.setEnabled(integer == BluetoothAdapter.STATE_ON ||
                            integer == BluetoothAdapter.STATE_OFF);
                });

        btManager.getConnectionState()
                .compose(bindActivity(lifecycle()))
                .subscribe(integer -> {
                    String message;
                    switch (integer) {
                        case State.STATE_CONNECTED:
                            message = "Connected";
                            break;
                        case State.STATE_CONNECTING:
                            message = "Connecting";
                            break;
                        case State.STATE_DISCONNECTED:
                            message = "Disconnecting";
                            break;
                        default:
                            message = "Disconnected";
                            tvConnectionStatus.setText(message);
                    }
                });

        //Todo unsubscribe using lifecycle
        RxCompoundButton.checkedChanges(btSwitch)
                .skip(1)
                .subscribe(aBoolean -> {
                    btManager.enableBluetoothRadio(aBoolean);
                });


    }
}
