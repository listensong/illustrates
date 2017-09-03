package zms.song.illustrates.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;

import zms.song.illustrates.common.ActionType;

/**
 * Created by song on 2017/8/20.
 */

public abstract class ContainerActivity extends BaseActivity {
    protected final SparseArray<ActionType> mActivitySets = new SparseArray<>();

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void pushActivity(@IdRes int key, Class<?> clz) {
        mActivitySets.put(key, new ActionType(key, new Intent(this, clz)));

    }

    protected void initBasicView(View.OnClickListener listener) {
        for (int i = 0; i < mActivitySets.size(); i++) {
            View view = findViewById(mActivitySets.keyAt(i));
            mActivitySets.valueAt(i).setView(view);
            view.setOnClickListener(listener);
        }
    }

    public void onClicked(View view) {
        if (view == null) {
            return;
        }

        ActionType actionType = mActivitySets.get(view.getId());
        if (actionType != null) {
            startActivity(actionType.getIntent());
        }
    }
}
