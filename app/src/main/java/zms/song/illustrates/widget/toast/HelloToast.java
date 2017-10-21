package zms.song.illustrates.widget.toast;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import zms.song.illustrates.R;
import zms.song.illustrates.base.IllustratesApplication;

/**
 * Created by song on 10/11/2017.
 */

public class HelloToast {

    private Toast mToast;

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ToastDuration {}

    private HelloToast(Context context, CharSequence text, @ToastDuration int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.hello_toast, null);
        TextView textView = (TextView) v.findViewById(R.id.center_text_view);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
    }

    public static HelloToast makeText(Context context, CharSequence text, @ToastDuration int duration) {
        return new HelloToast(context, text, duration);
    }

    public static HelloToast makeText(CharSequence text, @ToastDuration int duration) {
        return new HelloToast(IllustratesApplication.getIllustratesApplication(), text, duration);
    }

    public static HelloToast makeText(@NonNull Context context, CharSequence text) {
        return new HelloToast(context, text, Toast.LENGTH_SHORT);
    }

    public static HelloToast makeText(CharSequence text) {
        return new HelloToast(IllustratesApplication.getIllustratesApplication(), text, Toast.LENGTH_SHORT);
    }

    public static HelloToast makeText(@NonNull Context context, @StringRes int message, @ToastDuration int duration) {
        return new HelloToast(context, context.getString(message), duration);
    }

    public static HelloToast makeText(@StringRes int message, @ToastDuration int duration) {
        return new HelloToast(IllustratesApplication.getIllustratesApplication(),
                IllustratesApplication.getIllustratesApplication().getString(message), duration);
    }

    public static HelloToast makeText(@NonNull Context context, @StringRes int message) {
        return new HelloToast(context, context.getString(message), Toast.LENGTH_SHORT);
    }

    public static HelloToast makeText(@StringRes int message) {
        return new HelloToast(IllustratesApplication.getIllustratesApplication(),
                IllustratesApplication.getIllustratesApplication().getString(message), Toast.LENGTH_SHORT);
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
