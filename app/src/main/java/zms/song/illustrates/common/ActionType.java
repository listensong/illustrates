package zms.song.illustrates.common;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by song on 2017/8/20.
 */

public class ActionType {
    private static final String LOG_TAG = ActionType.class.getSimpleName();

    private int mResId;
    private View mView;
    private Intent mIntent;

    public ActionType() {

    }

    public ActionType(@IdRes int resId, @NonNull Intent intent) {
        this.mResId =  resId;
        this.mIntent = intent;
    }

    public ActionType(@IdRes int resId, @NonNull View view, @NonNull Intent intent) {
        this.mResId =  resId;
        this.mView = view;
        this.mIntent = intent;
    }

    public void setView(@NonNull View view) {
        this.mView = view;
    }

    public int getResId() {
        return mResId;
    }

    public View getView() {
        return mView;
    }

    public Intent getIntent() {
        return mIntent;
    }
}
