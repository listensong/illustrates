package zms.song.illustrates.util;

import android.support.annotation.DrawableRes;

import java.io.File;

import zms.song.illustrates.R;

/**
 * Created by song on 10/21/2017.
 */

public class FileUtil {
    private static final String mDevPhone = "10-0050F204-5";
    private static final String mDevOther = "3-0050F204-5";

    @DrawableRes
    public static int getDevTypeDrawableRes(String primaryDeviceType) {
        int resId;
        switch (primaryDeviceType) {
            case mDevPhone:
                resId = R.mipmap.ic_launcher;
                break;
            case mDevOther:
                resId = R.mipmap.ic_launcher;
                break;
            default:
                resId = R.mipmap.ic_launcher;
                break;
        }
        return resId;
    }

    public static boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
