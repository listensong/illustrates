package zms.song.illustrates.base;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 *
 * Created by song on 2017/8/20.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected String LOG_TAG;
    protected Context mContext;
    protected FragmentManager mFragmentManager;
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        LOG_TAG = this.getClass().getSimpleName();
        mFragmentManager = getFragmentManager();
    }

    protected void setStatusBarVisibility(Window window, boolean visible) {
        if (window == null) {
            return;
        }

        if (visible) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
