package zms.song.illustrates.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import zms.song.illustrates.R;
import zms.song.illustrates.base.BaseActivity;
import zms.song.illustrates.widget.toast.HelloToast;

public class WidgetActivity extends BaseActivity {

    private TextView mToastTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        mToastTextView = (TextView) findViewById(R.id.toastTextView);
        mToastTextView.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HelloToast.makeText(getApplicationContext(), "This is a toast!", Toast.LENGTH_SHORT).show();
        }
    };
}
