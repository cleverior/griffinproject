package com.sprd.systemui;

import android.app.AddonManager;
import android.content.Context;
import com.android.systemui.qs.QSTile;
import com.android.systemui.qs.QSTile.Host;
import com.android.systemui.qs.tiles.IntentTile;
import com.android.systemui.R;

public class SystemUIPluginUtils {
    static SystemUIPluginUtils sInstance;
    public static Context context;

    public SystemUIPluginUtils() {
    }

    public static SystemUIPluginUtils getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = (SystemUIPluginUtils) AddonManager.getDefault().getAddon(
                R.string.feature_display_data_lte_tile, SystemUIPluginUtils.class);
        return sInstance;
    }

    public static SystemUIPluginUtils getInstance(Context context) {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = (SystemUIPluginUtils) new AddonManager(context).getAddon(
                R.string.feature_display_data_lte_tile, SystemUIPluginUtils.class);
        return sInstance;
    }

    public QSTile<?> createDataTile(Host host) {
        return IntentTile.create(host, "");
    }

    public QSTile<?> createFourGTile(Host host) {
        return IntentTile.create(host, "");
    }
}
