package zms.song.illustrates.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by song on 2017/8/20.
 */

public class IllustratesApplication extends Application {
    private static final String LOG_TAG = "Illustrates";

    private static IllustratesApplication mIllustratesApplication;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            if (activity != null) {
                Log.d(LOG_TAG, activity.getClass().getSimpleName() + " onCreate");
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            if (activity != null) {
                Log.d(LOG_TAG, activity.getClass().getSimpleName() + " onDestroy");
            }
        }
    };

    public static IllustratesApplication getIllustratesApplication() {
        return mIllustratesApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mIllustratesApplication = this;
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        mHandler.removeCallbacksAndMessages(null);
        unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }
}
