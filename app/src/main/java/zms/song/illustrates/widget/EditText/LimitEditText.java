package zms.song.illustrates.widget.EditText;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

/**
 * Created by song on 10/21/2017.
 */

public class LimitEditText extends android.support.v7.widget.AppCompatEditText {
    private static final String mNameSpaceAndroid = "http://schemas.android.com/apk/res/android";
    private static final String mAttrMaxLength = "maxLength";

    private static LengthLimitFilter mLengthLimitFilter;

    private static final InputFilter[] NO_FILTERS = new InputFilter[0];

    public LimitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFilter(attrs);
    }

    public LimitEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFilter(attrs);
    }

    private void initFilter(AttributeSet attrs) {
        int maxLen = attrs.getAttributeIntValue(mNameSpaceAndroid, mAttrMaxLength, -1);
        if (maxLen > -1) {
            mLengthLimitFilter = new LengthLimitFilter(maxLen);
            setFilters(new InputFilter[]{mLengthLimitFilter});
        } else {
            setFilters(NO_FILTERS);
        }
    }

    public interface OnExceedCallback {
        void onExceed(int maxLen);
    }
    public void setExceedListener(OnExceedCallback callback) {
        if (mLengthLimitFilter != null) {
            mLengthLimitFilter.setOnExceedCallback(callback);
        }
    }

    private static class LengthLimitFilter implements InputFilter {
        private final int mMaxLen;
        WeakReference<OnExceedCallback> mExceedCallback;

        public LengthLimitFilter(int maxLen) {
            mMaxLen = maxLen;
        }

        public void setOnExceedCallback(OnExceedCallback callback) {
            mExceedCallback = new WeakReference<>(callback);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int keep = mMaxLen - (dest.length() - (dend - dstart));
            if (keep <= 0) {
                //达到字数上限
                if (mExceedCallback != null && mExceedCallback.get() != null) {
                    mExceedCallback.get().onExceed(mMaxLen);
                }
                return "";
            } else if (keep >= end - start) {
                return null; // keep original
            } else {
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        public int getMax() {
            return mMaxLen;
        }
    }
}
