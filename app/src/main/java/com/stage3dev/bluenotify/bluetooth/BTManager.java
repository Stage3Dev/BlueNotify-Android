package com.stage3dev.bluenotify.bluetooth;


import io.reactivex.Observable;

/**
 * Created by Kyle on 2/12/17.
 */
public interface BTManager {

    Observable<Integer> getBTRadioState();

    Observable<Integer> getConnectionState();

    boolean enableBluetoothRadio(boolean enable);

    void startScan();
}
