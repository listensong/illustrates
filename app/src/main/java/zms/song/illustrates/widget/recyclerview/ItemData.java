package zms.song.illustrates.widget.recyclerview;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Created by song on 2017/8/23.
 */

public class ItemData {
    private String mName;
    private String mTitle;
    private String mContent;
    @DrawableRes
    private int mResId;
    private boolean mChecked;
    private boolean mShow;
    private boolean mUsed;

    public ItemData(@NonNull String name, @NonNull String title, String content, @DrawableRes int res, boolean checked) {
        this.mName = name;
        this.mTitle = title;
        this.mContent = content;
        this.mResId = res;
        this.mChecked = checked;
        this.mShow = false;
        this.mUsed = false;
    }

    public String getName() {
        return mName;
    }

    public String getContent() {
        return mContent;
    }

    public String getTitle() {
        return mTitle;
    }

    @DrawableRes
    public int getDrawableRes() {
        return mResId;
    }

    public boolean getChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    public boolean getShow() {
        return mShow;
    }

    public void setShow(boolean show) {
        mShow = show;
    }

    public boolean getUsed() {
        return mUsed;
    }

    public void setUsed(boolean used) {
        mUsed = used;
    }

    public boolean invertCheckedState() {
        mChecked = !mChecked;
        return mChecked;
    }
}
