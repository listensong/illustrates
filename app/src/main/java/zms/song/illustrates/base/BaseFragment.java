package zms.song.illustrates.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by song on 2017/8/20.
 */

public abstract class BaseFragment extends Fragment {
    protected View mMainView;
    protected Context mContext;
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = IllustratesApplication.getIllustratesApplication();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacksAndMessages(null);
    }
}
