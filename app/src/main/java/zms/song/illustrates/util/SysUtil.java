package zms.song.illustrates.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by song on 10/21/2017.
 */

public class SysUtil {
    private static final String TAG = "SysUtil";

    public static String getMacAdress() {
        String mac = null;
        String string = "";
        try {
            Process process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            LineNumberReader lineNumberReader = new LineNumberReader(inputStreamReader);
            for (; string != null; ) {
                string = lineNumberReader.readLine();
                if (string != null) {
                    mac = string.trim();
                    break;
                }
            }
        } catch (IOException e) {
            Log.d(TAG, "fail to cat wlan0 address " + e.getMessage());
        }
        return mac;
    }

    private static final String mWifiDevNameRegEx = "\\[(.+?)\\]";
    public static String getWifiP2pDeviceType(String deviceName) {
        if (deviceName == null || deviceName.isEmpty()) {
            return "None";
        }
        Pattern p = Pattern.compile(mWifiDevNameRegEx);
        Matcher matcher = p.matcher(deviceName);
        if (matcher.find()) {
            Log.e(TAG, "dev: " + matcher.group());
        } else {
            Log.e(TAG, "dev: Not Found");
        }

        return null;
    }
}
