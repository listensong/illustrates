package zms.song.illustrates.widget.toast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import zms.song.illustrates.R;

/**
 * Created by song on 10/11/2017.
 */

public class HelloToast {

    private Toast mToast;

    private HelloToast(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.hello_toast, null);
        TextView textView = (TextView) v.findViewById(R.id.center_text_view);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
    }

    public static HelloToast makeText(Context context, CharSequence text, int duration) {
        return new HelloToast(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
