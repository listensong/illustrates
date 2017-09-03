package zms.song.illustrates.hardware;

import android.os.Bundle;
import android.view.View;

import zms.song.illustrates.R;
import zms.song.illustrates.base.ContainerActivity;
import zms.song.illustrates.hardware.camera1.Camera1Activity;

public class HardWareActivity extends ContainerActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_ware);
        initBtnData();
    }

    private void initBtnData() {
        pushActivity(R.id.camera1_text_view, Camera1Activity.class);
        //pushActivity(R.id.camera2_text_view, Camera2Activity.class);

        initBasicView(this);
    }

    @Override
    public void onClick(View view) {
        onClicked(view);
    }

}
