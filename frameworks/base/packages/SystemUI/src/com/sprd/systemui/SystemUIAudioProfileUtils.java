package com.sprd.systemui;

import android.app.AddonManager;
import android.content.Context;
import android.content.Intent;
import com.android.systemui.qs.QSTile;
import com.android.systemui.qs.QSTile.Host;
import com.android.systemui.qs.tiles.IntentTile;
import com.android.systemui.R;

public class SystemUIAudioProfileUtils {
    static SystemUIAudioProfileUtils sInstance;

    public SystemUIAudioProfileUtils() {
    }

    public static SystemUIAudioProfileUtils getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = (SystemUIAudioProfileUtils) AddonManager.getDefault().getAddon(
                R.string.feature_audioprofile_systemui, SystemUIAudioProfileUtils.class);
        return sInstance;
    }

    public static SystemUIAudioProfileUtils getInstance(Context context) {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = (SystemUIAudioProfileUtils) new AddonManager(context).getAddon(
                R.string.feature_audioprofile_systemui, SystemUIAudioProfileUtils.class);
        return sInstance;
    }

    public boolean isSupportAudioProfileTile() {
        return false;
    }

    public QSTile<?> createAudioProfileTile(Host host, Context context) {
        return IntentTile.create(host, "");
    }

}
