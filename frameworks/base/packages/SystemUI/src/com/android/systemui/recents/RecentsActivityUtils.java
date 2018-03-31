package com.android.systemui.recents;

import android.app.ActivityManager;
import android.app.AddonManager;
import android.content.Context;
import android.widget.ImageButton;
import com.android.systemui.R;
import com.android.systemui.recents.views.RecentsView;

public class RecentsActivityUtils {
    static RecentsActivityUtils mInstance;
    public static RecentsActivityUtils getInstance() {
        if (mInstance == null) {
            mInstance = (RecentsActivityUtils) AddonManager.getDefault().
                    getAddon(R.string.feature_display_for_clearall_task,RecentsActivityUtils.class);
        }
        return mInstance;
    }
    public void init(Context context,RecentsView recentsview, ImageButton imagebutton) {
    }

    /* SPRD: Bug 475644 new feature of quick cleaning. @{ */
    public boolean isSupportClearAllTasks() {
        return false;
    }
    /* @} */
}
