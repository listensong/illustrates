package zms.song.illustrates.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import zms.song.illustrates.R;
import zms.song.illustrates.base.BaseActivity;
import zms.song.illustrates.widget.EditText.LimitEditText;
import zms.song.illustrates.widget.toast.HelloToast;

public class WidgetActivity extends BaseActivity {

    private TextView mToastTextView;
    private LimitEditText mLimitEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);

        mToastTextView = (TextView) findViewById(R.id.toast_text_view);
        mToastTextView.setOnClickListener(mClickListener);

        mLimitEditText = (LimitEditText) findViewById(R.id.limit_edit_text);
        mLimitEditText.setExceedListener(new LimitEditText.OnExceedCallback() {
            @Override
            public void onExceed(int maxLen) {
                HelloToast.makeText("onExceed " + maxLen).show();
            }
        });

    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HelloToast.makeText(getApplicationContext(), "This is a toast!", Toast.LENGTH_SHORT).show();
        }
    };
}
