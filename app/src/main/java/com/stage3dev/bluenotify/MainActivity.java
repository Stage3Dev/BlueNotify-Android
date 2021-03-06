package com.stage3dev.bluenotify;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.stage3dev.bluenotify.bluetooth.BTManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.lmbluetoothsdk.base.State;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.trello.rxlifecycle2.android.RxLifecycleAndroid.bindActivity;

public class MainActivity extends RxAppCompatActivity {

    private static final int COARSE_LOCATION = 143;

    @BindView(R.id.bluetooth_switch)
    Switch btSwitch;

    @BindView(R.id.connection_status)
    TextView tvConnectionStatus;

    @BindView(R.id.bt_connect_client)
    Button connectButton;

    @Inject
    BTManager btManager;

    private int connectedState = State.STATE_DISCONNECTED;

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
                    this.connectedState = integer;
                    String message;
                    switch (integer) {
                        case State.STATE_CONNECTED:
                            message = "Connected";
                            connectButton.setText("Disconnect Client");
                            connectButton.setEnabled(true);
                            break;
                        case State.STATE_CONNECTING:
                            message = "Connecting";
                            connectButton.setEnabled(false);
                            break;
                        case State.STATE_DISCONNECTED:
                            message = "Disconnecting";
                            connectButton.setEnabled(false);
                            break;
                        default:
                            message = "Disconnected";
                            tvConnectionStatus.setText(message);
                            connectButton.setText("Connect Client");
                            connectButton.setEnabled(true);
                    }
                });

        //Todo unsubscribe using lifecycle
        RxCompoundButton.checkedChanges(btSwitch)
                .skip(1)
                .subscribe(aBoolean -> {
                    btManager.enableBluetoothRadio(aBoolean);
                });

        RxView.clicks(connectButton).subscribe(aVoid -> {

                btManager.startScan();
        });

    }

    @AfterPermissionGranted(COARSE_LOCATION)
    void startScan() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            btManager.startScan();
        } else {
            EasyPermissions.requestPermissions(this, "We need this permision to scan bluetooth",
                    COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Wake the device and show our activity
        if (BuildConfig.DEBUG) {
            // Calling this from your launcher activity is enough, but I needed a good example spot ;)
            DebugUtils.riseAndShine(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
