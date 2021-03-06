package com.stage3dev.bluenotify.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import javax.inject.Inject;

import co.lujun.lmbluetoothsdk.BluetoothController;
import co.lujun.lmbluetoothsdk.base.BluetoothListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;


/**
 * Created by Kyle on 2/12/17.
 */
public class BTManagerImpl implements BTManager, BluetoothListener {

    private static final String TAG = BTManagerImpl.class.getSimpleName();

    private BluetoothController bluetoothController;
    private BehaviorSubject<Integer> btRadioSubjec;
    private BehaviorSubject<Integer> btConnectionSubject;

    @Inject
    public BTManagerImpl(BluetoothController bluetoothController) {
        this.bluetoothController = bluetoothController;
        this.bluetoothController.setBluetoothListener(this);

        btRadioSubjec = BehaviorSubject.createDefault(bluetoothController.getBluetoothState());
        btConnectionSubject = BehaviorSubject.createDefault(bluetoothController.getConnectionState());
    }

    @Override
    public Observable<Integer> getBTRadioState() {
        return btRadioSubjec.subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Integer> getConnectionState() {
        return btConnectionSubject.subscribeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void startScan() {
        Log.d(TAG, "startScan() called");
        bluetoothController.startScan();
    }

    @Override
    public void onReadData(BluetoothDevice device, byte[] data) {
        Log.d(TAG, "onReadData() called with: device = [" + device + "], data = [" + data + "]");

    }

    @Override
    public void onActionStateChanged(int preState, int state) {
        Log.d(TAG, "onActionStateChanged() called with: preState = [" + preState + "], state = [" + state + "]");
        btRadioSubjec.onNext(state);
    }

    @Override
    public void onActionDiscoveryStateChanged(String discoveryState) {
        Log.d(TAG, "onActionDiscoveryStateChanged() called with: discoveryState = [" + discoveryState + "]");
    }

    @Override
    public void onActionScanModeChanged(int preScanMode, int scanMode) {
        Log.d(TAG, "onActionScanModeChanged() called with: preScanMode = [" + preScanMode + "], scanMode = [" + scanMode + "]");
    }

    @Override
    public void onBluetoothServiceStateChanged(int state) {
        Log.d(TAG, "onBluetoothServiceStateChanged() called with: state = [" + state + "]");
        btConnectionSubject.onNext(state);
    }

    @Override
    public void onActionDeviceFound(BluetoothDevice device, short rssi) {
        Log.d(TAG, "onActionDeviceFound() called with: device = [" + device + "], rssi = [" + rssi + "]");

    }

    public boolean enableBluetoothRadio(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }
}
