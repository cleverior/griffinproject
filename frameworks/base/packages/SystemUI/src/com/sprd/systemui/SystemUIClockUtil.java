package com.sprd.systemui;

import com.android.systemui.R;
import android.app.AddonManager;
import android.content.Context;

public class SystemUIClockUtil {
    static SystemUIClockUtil sInstance;
    public static Context context;

    public static SystemUIClockUtil getInstance() {
        if (sInstance != null) {
            return sInstance;
                    }
        sInstance = (SystemUIClockUtil)AddonManager.getDefault().getAddon(
                R.string.feature_clock_addon, SystemUIClockUtil.class);
        return sInstance;
    }

    public boolean isAllDay(boolean is24) {
        return false;
    }
}
