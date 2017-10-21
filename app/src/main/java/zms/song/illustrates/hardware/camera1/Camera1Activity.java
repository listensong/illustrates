package zms.song.illustrates.hardware.camera1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import zms.song.illustrates.R;
import zms.song.illustrates.base.PermissionBaseActivity;
import zms.song.illustrates.util.Util;

public class Camera1Activity extends PermissionBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera1);
        checkWritePermission();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mFragmentManager != null && mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        }
        finish();
    }

    private void checkWritePermission() {
        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            checkCamera();
        } else {
            requestPermissions(new IPermissionCallback() {
                @Override
                public void onPermissionResult(@NonNull String perm, int result) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        checkCamera();
                    } else {
                        finish();
                    }
                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void checkCamera() {
        if (Util.supportCamera(mContext)) {
            if (checkPermission(Manifest.permission.CAMERA)) {
                Util.pushFragment(mFragmentManager, Camera1SurfaceViewFragment.getNewInstance());
            } else {
                requestPermissions(new IPermissionCallback() {
                    @Override
                    public void onPermissionResult(@NonNull String perm, int result) {
                        if (result == PackageManager.PERMISSION_GRANTED) {
                            Util.pushFragment(mFragmentManager, Camera1SurfaceViewFragment.getNewInstance());
                        } else {
                            finish();
                        }
                    }
                }, Manifest.permission.CAMERA);
            }
        } else {
            Toast.makeText(mContext, getString(R.string.unsupported_camera_device), Toast.LENGTH_SHORT).show();
        }
    }
}
