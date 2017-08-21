package zms.song.illustrates.permission;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import zms.song.illustrates.R;
import zms.song.illustrates.base.PermissionBaseActivity;


/**
* Demo for How to use permissionBaseActivity
* */
public class PermissionActivity extends PermissionBaseActivity implements View.OnClickListener {

    private TextView mMainTextView;
    private TextView mMainTextView1;
    private TextView mMainTextView2;
    private TextView mMainTextView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        pushPermsData(
                Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,  Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        initBindView();
        initViewListener();
    }

    private void initBindView() {
        mMainTextView  = (TextView) findViewById(R.id.permission_text_view);
        mMainTextView1 = (TextView) findViewById(R.id.permission1_text_view);
        mMainTextView2 = (TextView) findViewById(R.id.permission2_text_view);
        mMainTextView3 = (TextView) findViewById(R.id.permission3_text_view);
    }

    private void initViewListener() {
        mMainTextView.setOnClickListener(this);
        mMainTextView1.setOnClickListener(this);
        mMainTextView2.setOnClickListener(this);
        mMainTextView3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.permission_text_view:
                requestPermissions();
                break;
            case R.id.permission1_text_view:
                requestPermissions(new IPermissionCallback() {
                    @Override
                    public void onPermissionResult(@NonNull String perm, int result) {
                        Log.e(TAG, "onPermissionResult1 " + perm + " " + result);
                    }
                });
                break;
            case R.id.permission2_text_view:
                requestPermissions(new IPermissionCallback() {
                    @Override
                    public void onPermissionResult(@NonNull String perm, int result) {
                        Log.e(TAG, "onPermissionResult2 " + perm + " " + result);
                    }
                }, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA );
                break;
            case R.id.permission3_text_view:
                requestPermissions(new IPermissionsCallback() {
                    @Override
                    public void onPermissionsResult(@NonNull String[] perms, @NonNull int[] result) {
                        Log.e(TAG, "onPermissionResult3 " + Arrays.toString(perms));
                    }
                }, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA );
                break;
        }
    }

}
