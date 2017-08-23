package zms.song.illustrates.base;

import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by song on 2017/8/21.
 */

public abstract class PermissionBaseActivity extends AppCompatActivity {

    private Set<String> mPermsAll = new HashSet();
    private Set<String> mPermsNeedful = new HashSet();

    protected String TAG;
    protected Context mContext;
    protected boolean mNeedCheckPerm = false;
    protected static final int PERMISSION_REQUEST_CODE = 10086;
    protected FragmentManager mFragmentManager;
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected Toolbar mToolbar;


    public interface IPermissionsCallback {
        void onPermissionsResult(@NonNull String[] perms, @NonNull int[] result);
    }
    protected IPermissionsCallback mPermissionsCallback;

    public interface IPermissionCallback {
        void onPermissionResult(@NonNull String perm, int result);
    }
    protected final HashMap<String, IPermissionCallback> mPermmissonMapHandler = new HashMap<>();
    protected IPermissionCallback addPermissionHolder(@NonNull String key, IPermissionCallback permissionCallback) {
        mPermmissonMapHandler.put(key, permissionCallback);
        return permissionCallback;
    }
    protected IPermissionCallback removePermissionHolder(@NonNull String key) {
        return mPermmissonMapHandler.remove(key);
    }

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        TAG = this.getClass().getSimpleName();
        mFragmentManager = getFragmentManager();
        mNeedCheckPerm = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    protected void pushPermsData(String... perms) {
        if (!mNeedCheckPerm) {
            return;
        }

        if (perms == null) {
            return;
        }
        Collections.addAll(mPermsAll, perms);
    }

    protected void updatePermInfo() {
        if (!mNeedCheckPerm) {
            return;
        }
        mPermsNeedful.clear();
        for (String perm: mPermsAll) {
            if (ActivityCompat.checkSelfPermission(mContext, perm) != PackageManager.PERMISSION_GRANTED) {
                mPermsNeedful.add(perm);
            }
        }
    }

    protected boolean checkPermission(@NonNull String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(mContext, perm) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    protected boolean checkPermState() {
        updatePermInfo();
        if (mNeedCheckPerm) {
            return mPermsNeedful.size() > 0;
        } else {
            return false;
        }
    }

    protected void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = new String[mPermsNeedful.size()];
            mPermsNeedful.toArray(perms);
            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
        }
    }

    protected void requestPermissions(String... perms) {
        if (perms == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
        }
    }

    protected void requestPermissions(IPermissionCallback permissionCallback) {
        if (permissionCallback == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = new String[mPermsNeedful.size()];
            mPermsNeedful.toArray(perms);
            for (String perm: perms) {
                addPermissionHolder(perm, permissionCallback);
            }
            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
        } else {
            String[] perms = new String[mPermsNeedful.size()];
            mPermsNeedful.toArray(perms);
            for (String perm: perms) {
                permissionCallback.onPermissionResult(perm, PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    protected void requestPermission(String perm, IPermissionCallback permissionCallback) {
        if (perm == null) {
            return;
        }

        if (permissionCallback == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addPermissionHolder(perm, permissionCallback);
            ActivityCompat.requestPermissions(this, new String[]{perm}, PERMISSION_REQUEST_CODE);
        } else {
            permissionCallback.onPermissionResult(perm, PackageManager.PERMISSION_GRANTED);
        }
    }

    protected void requestPermissions(IPermissionCallback permissionCallback, String... perms) {
        if (perms == null) {
            return;
        }

        if (permissionCallback == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String perm: perms) {
                addPermissionHolder(perm, permissionCallback);
            }
            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
        } else {
            for (String perm: perms) {
                permissionCallback.onPermissionResult(perm, PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    protected void requestPermissions(IPermissionsCallback permissionsCallback, String... perms) {
        if (perms == null) {
            return;
        }

        if (permissionsCallback == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionsCallback = permissionsCallback;
            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
        } else {
            int[] grantResults = new int[perms.length];
            for (int i = 0; i < grantResults.length; i ++) {
                grantResults[i] = PackageManager.PERMISSION_GRANTED;
            }
            permissionsCallback.onPermissionsResult(perms, grantResults);
        }
    }


    private IPermissionCallback mPermissionHandle;
    protected void permissionRequestHandle(IPermissionCallback permissionCallback, String perm) {
        if (perm == null) {
            return;
        }

        if (permissionCallback == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionHandle = permissionCallback;
            ActivityCompat.requestPermissions(this, new String[]{perm}, PERMISSION_REQUEST_CODE);
        } else {
            permissionCallback.onPermissionResult(perm, PackageManager.PERMISSION_GRANTED);
        }
    }

    private IPermissionsCallback mPermissionsHandle;
    protected void permissionsRequestHandle(IPermissionsCallback permissionsCallback, String... perms) {
        if (perms == null) {
            return;
        }

        if (permissionsCallback == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionsHandle = permissionsCallback;
            ActivityCompat.requestPermissions(this, perms, PERMISSION_REQUEST_CODE);
        } else {
            int[] grantResults = new int[perms.length];
            for (int i = 0; i < grantResults.length; i ++) {
                grantResults[i] = PackageManager.PERMISSION_GRANTED;
            }
            permissionsCallback.onPermissionsResult(perms, grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            updatePermInfo();
            permissionResultHandler(permissions, grantResults);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void permissionResultHandler(@NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionHandle != null) {
            mPermissionHandle.onPermissionResult(permissions[0], grantResults[0]);
            mPermissionHandle = null;
        }

        if (mPermissionsHandle != null) {
            mPermissionsHandle.onPermissionsResult(permissions, grantResults);
            mPermissionsHandle = null;
        }

        if (mPermissionsCallback != null) {
            mPermissionsCallback.onPermissionsResult(permissions, grantResults);
            mPermissionsCallback = null;
        }

        if (mPermmissonMapHandler.size() > 0){
            for (int i = 0; i < permissions.length; i++) {
                IPermissionCallback callback = removePermissionHolder(permissions[i]);
                if (callback != null) {
                    callback.onPermissionResult(permissions[i], grantResults[i]);
                }
            }
        }
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        updatePermInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
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
