package android.util;

import android.os.SystemClock;

/**
 * util for check time.
 * @hide
 */
public class CheckTime {
    private static final long ACTION_TIMEOUT = 200;

    private static final String TAG = "CheckTime";

    public static long getTime() {
        return SystemClock.elapsedRealtime();
    }

    /**
     * Check the action running duration, follow the ACTION_TIMEOUT default duration.
     * @return true if check failed.
     */
    public static boolean checkTime(long start, String action) {
        return checkTime(start, action, ACTION_TIMEOUT);
    }

    /**
     * Check the action running duration.
     * @return true if duration longer than timeout.
     */
    public static boolean checkTime(long start, String action, long timeout) {
        return checkTime(start, action, timeout, true);
    }


    public static boolean checkTime(long start, String action, long timeout, boolean app) {
        long now = getTime();
        long cost = now - start;
        boolean failed = cost > timeout;
        if (failed) {
            // If we are taking a long time, log about it.
            if (app) {
                Log.w(TAG, "App running slow: Executing " + action + " took " + cost + "ms");
            } else {
                Slog.w(TAG, "System running slow: Executing " + action + " took " + cost + "ms");
            }
        }

        return failed;
    }

    /**
     * @hide
     */
    public static class System {
        public System() {
            throw new RuntimeException("You should never call new CheckTime.System().");
        }

        public static boolean checkTime(long start, String action) {
            return CheckTime.checkTime(start, action, CheckTime.ACTION_TIMEOUT, false);
        }

        public static boolean checkTime(long start, String action, long timeout) {
            return CheckTime.checkTime(start, action, timeout, false);
        }

    }
}
