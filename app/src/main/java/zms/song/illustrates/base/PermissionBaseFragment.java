package zms.song.illustrates.base;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by song on 2017/8/21.
 */

public abstract class PermissionBaseFragment extends Fragment {
    protected View mMainView;

    private Set<String> mPermsAll = new HashSet();
    private Set<String> mPermsNeedful = new HashSet();

    protected String TAG;
    protected Context mContext;
    protected boolean mNeedCheckPerm = false;
    protected static final int PERMISSION_REQUEST_CODE = 110;
    protected FragmentManager mFragmentManager;
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected Toolbar mToolbar;

    private int mTransactionIndex = 1;
    private int getTransactionId() {
        return mTransactionIndex++;
    }
    private void resetTransactionId(@IntRange(from = 0) int v) {
        mTransactionIndex = v;
    }

    public interface IPermissionCallback {
        void onPermissionResult(@NonNull String perm, int result);
    }

    private static final String KEY_PERMISSION = "permission";
    private final SparseArray<HashMap<String, IPermissionCallback>> mPermissionMapHandler = new SparseArray<>();
    private IPermissionCallback pushPermissionsHolderData(int key, String[] perms, @NonNull IPermissionCallback permissionCallback) {
        HashMap<String, IPermissionCallback> permKey = mPermissionMapHandler.get(key);
        if (permKey == null) {
            permKey = new HashMap<>();
        }
        permKey.clear();
        for (String perm: perms) {
            permKey.put(perm, permissionCallback);
        }
        mPermissionMapHandler.put(key, permKey);
        return permissionCallback;
    }

    private void addPermissionsHolderGuardKey(int key, @NonNull IPermissionCallback permissionCallback) {
        HashMap<String, IPermissionCallback> permKey = mPermissionMapHandler.get(key);
        if (permKey == null) {
            permKey = new HashMap<>();
        }
        permKey.put(KEY_PERMISSION, permissionCallback);
        mPermissionMapHandler.put(key, permKey);
    }

    private IPermissionCallback removePermissionHolder(int key, @NonNull String perm) {
        HashMap<String, IPermissionCallback> permCallback = mPermissionMapHandler.get(key);
        if (permCallback != null) {
            return permCallback.get(perm);
        }
        return null;
    }

    private IPermissionCallback getPermissionCallback(int key) {
        HashMap<String, IPermissionCallback> permissionMap = mPermissionMapHandler.get(key);
        for (IPermissionCallback callback: permissionMap.values()) {
            if (callback != null) {
                return callback;
            }
        }
        return null;
    }

    private void iteratePermissionHolder(int key) {
        HashMap<String, IPermissionCallback> permsMap = mPermissionMapHandler.get(key);
        if (permsMap == null) {
            return;
        }
        Iterator<Map.Entry<String, IPermissionCallback>> it = permsMap.entrySet().iterator();
        Map.Entry<String, IPermissionCallback> entry;
        while (it.hasNext()) {
            entry = it.next();
            if (!entry.getKey().equals(KEY_PERMISSION)) {
                entry.getValue().onPermissionResult(entry.getKey(), PackageManager.PERMISSION_GRANTED);
            }
        }
        mPermissionMapHandler.remove(key);
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = IllustratesApplication.getIllustratesApplication();
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
            if (ContextCompat.checkSelfPermission(mContext, perm) != PackageManager.PERMISSION_GRANTED) {
                mPermsNeedful.add(perm);
            }
        }
    }

    protected boolean checkPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(mContext, perm) == PackageManager.PERMISSION_GRANTED;
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

    protected ArrayList<String> updatePermissions(String[] perms, ArrayList<String> granted) {
        if (perms == null) {
            return null;
        }

        ArrayList<String> resultPerms = new ArrayList<>();
        for (String perm : perms) {
            if (ContextCompat.checkSelfPermission(mContext, perm) == PackageManager.PERMISSION_GRANTED) {
                granted.add(perm);
            } else {
                resultPerms.add(perm);
            }
        }
        return resultPerms;
    }

    private void requestPermissionFlow(@NonNull String[] perms, @NonNull IPermissionCallback permissionCallback) {
        int key = getTransactionId();
        if (key != -1 && (key & 0xffff0000) != 0) {
            key = 1;
            resetTransactionId(1);
        }

        if (perms.length == 1) {
            requestActivityCompatPermission(key, perms[0], permissionCallback);
        } else {
            requestActivityCompatPermissions(key, perms, permissionCallback);
        }
    }

    private void requestActivityCompatPermissions(@IntRange(from = 0) int key, @NonNull String[] perms, @NonNull IPermissionCallback permissionCallback) {
        ArrayList<String> granted = new ArrayList<>();
        ArrayList<String> needRequest = updatePermissions(perms, granted);
        if (granted.size() > 0) {
            if (needRequest.size() > 0) {
                String[] grantedArray = new String[granted.size()];
                granted.toArray(grantedArray);
                pushPermissionsHolderData(key, grantedArray, permissionCallback);
            } else {
                for (String perm: granted) {
                    permissionCallback.onPermissionResult(perm, PackageManager.PERMISSION_GRANTED);
                }
                return;
            }
        }
        //TODO check ActivityCompat.shouldShowRequestPermissionRationale for every permission
        if (needRequest.size() > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permsArray = new String[needRequest.size()];
            needRequest.toArray(permsArray);
            addPermissionsHolderGuardKey(key, permissionCallback);
            requestPermissions(permsArray, key);
        }
    }

    private void requestActivityCompatPermission(@IntRange(from = 0) int key, @NonNull String perm, @NonNull IPermissionCallback permissionCallback) {
        if (ContextCompat.checkSelfPermission(mContext, perm) == PackageManager.PERMISSION_GRANTED) {
            permissionCallback.onPermissionResult(perm, PackageManager.PERMISSION_GRANTED);
        } else {
            boolean should = true;//ContextCompat.shouldShowRequestPermissionRationale(this, perm);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                should = shouldShowRequestPermissionRationale(perm);
            }
            if (!should) {
                jumpToAppDetailsDialog();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    addPermissionsHolderGuardKey(key, permissionCallback);
                    requestPermissions(new String[]{perm}, key);
                }
            }
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
            requestPermissionFlow(perms, permissionCallback);
        } else {
            for (String perm: perms) {
                permissionCallback.onPermissionResult(perm, PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    protected void requestPermissions(IPermissionCallback permissionCallback) {
        if (permissionCallback == null) {
            return;
        }

        String[] perms = new String[mPermsNeedful.size()];
        mPermsNeedful.toArray(perms);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionFlow(perms, permissionCallback);
        } else {
            for (String perm: perms) {
                permissionCallback.onPermissionResult(perm, PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    protected void requestPermissions() {
        updatePermInfo();
        String[] perms = new String[mPermsNeedful.size()];
        mPermsNeedful.toArray(perms);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        updatePermInfo();
        permissionResultHandler(requestCode, permissions, grantResults);
    }

    private void permissionResultHandler(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermissionMapHandler.size() > 0) {
            IPermissionCallback callback = getPermissionCallback(requestCode);
            iteratePermissionHolder(requestCode);
            for (int i = 0; i < permissions.length; i++) {
                if (callback != null) {
                    callback.onPermissionResult(permissions[i], grantResults[i]);
                }
            }
        }
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        updatePermInfo();
    }

    @Override
    public void onDestroy() {
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

    protected void jumpToAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        startActivity(intent);
    }

    private void jumpToAppDetailsDialog() {
        if (getActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jumpToAppSettings();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create();
        builder.show();
    }
}
