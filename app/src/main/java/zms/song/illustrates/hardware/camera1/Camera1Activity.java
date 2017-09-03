package zms.song.illustrates.hardware.camera1;

import android.os.Bundle;

import zms.song.illustrates.R;
import zms.song.illustrates.base.PermissionBaseActivity;
import zms.song.illustrates.util.Util;

public class Camera1Activity extends PermissionBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera1);
        Util.pushFragment(mFragmentManager, Camera1SurfaceViewFragment.getNewInstance());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
