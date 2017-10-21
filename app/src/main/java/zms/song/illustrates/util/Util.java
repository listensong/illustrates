package zms.song.illustrates.util;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import zms.song.illustrates.R;
import zms.song.illustrates.base.IllustratesApplication;

/**
 * Created by song on 9/3/2017.
 */

public class Util {
    public static void pushFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = fragment.getClass().getSimpleName() + "_" + fragment.hashCode();
        fragmentTransaction.add(R.id.container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.container);
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public static boolean cameraPermissionGranted(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean supportCamera(@NonNull Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        return false;
    }


    public static boolean saveDataPicture(byte[] data) {
        String targetFile = Environment.getExternalStorageDirectory() +
                File.separator + "Illustrate" +
                File.separator + "Camera_" +
                System.currentTimeMillis() + ".jpg";
        return saveDataPicture(null, data, 100, targetFile);
    }

    public static boolean saveDataPicture(byte[] data, int quality) {
        String targetFile = Environment.getExternalStorageDirectory() +
                File.separator + "Illustrate" +
                File.separator + "Camera_" +
                System.currentTimeMillis() + ".jpg";
        return saveDataPicture(null, data, quality, targetFile);
    }

    public static boolean saveDataPicture(Context context, byte[] data, int quality, @NonNull String targetFile) {

        if (data == null) {
            return false;
        }

        if (targetFile.isEmpty()) {
            return false;
        }

        if (context == null) {
            context = IllustratesApplication.getIllustratesApplication();
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("HelloWorld", "saveDataPicture permission error WRITE_EXTERNAL_STORAGE");
            return false;
        }

        if (quality < 0 || quality > 100) {
            quality = 100;
        }

        File file = new File(targetFile);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            stream.flush();
            stream.close();
            bitmap.recycle();
            bitmap = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
