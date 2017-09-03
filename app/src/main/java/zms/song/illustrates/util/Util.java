package zms.song.illustrates.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;

import zms.song.illustrates.R;

/**
 * Created by song on 9/3/2017.
 */

public class Util {
    public static void pushFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = fragment.getClass().getSimpleName() + "_" + fragment.hashCode();
        fragmentTransaction.add(R.id.container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }
}
