package com.stage3dev.bluenotify;

import android.app.Activity;
import android.os.Looper;
import android.os.PowerManager;

import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

/**
 * Add this to your application's src/debug/... sources
 */
public class DebugUtils {

    /**
     * Show the activity over the lockscreen and wake up the device. If you launched the app manually
     * both of these conditions are already true. If you deployed from the IDE, however, this will
     * save you from hundreds of power button presses and pattern swiping per day!
     */
    /**
     * Show the activity over the lockscreen and wake up the device. If you launched the app manually
     * both of these conditions are already true. If you deployed from the IDE, however, this will
     * save you from hundreds of power button presses and pattern swiping per day!
     */
    public static void riseAndShine(Activity activity) {

        if (Looper.getMainLooper().getThread() != Thread.currentThread()) {
            activity.runOnUiThread(() -> riseAndShine(activity));
            return;
        }

        activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);

        PowerManager power = (PowerManager) activity.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock lock =
                power.newWakeLock(FLAG_KEEP_SCREEN_ON | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "wakeup!");
        lock.acquire();
        lock.release();
    }
}
