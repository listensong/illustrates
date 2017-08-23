package zms.song.illustrates;

import android.os.Bundle;
import android.view.View;

import zms.song.illustrates.base.ContainerActivity;
import zms.song.illustrates.hello.HelloActivity;
import zms.song.illustrates.permission.PermissionActivity;
import zms.song.illustrates.widget.recyclerview.RecyclerViewActivity;

public class MainActivity extends ContainerActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBtnData();
    }

    private void initBtnData() {
        pushActivity(R.id.hello_text_view, HelloActivity.class);
        pushActivity(R.id.permission_text_view, PermissionActivity.class);
        pushActivity(R.id.recycler_text_view, RecyclerViewActivity.class);

        initBasicVIew(this);
    }

    @Override
    public void onClick(View view) {
        onClicked(view);
    }

}
