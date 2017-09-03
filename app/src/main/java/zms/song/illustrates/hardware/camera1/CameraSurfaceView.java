package zms.song.illustrates.hardware.camera1;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by song on 2017/9/3.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraSurfaceView.class.getSimpleName();
    private Camera mCamera;
    private Camera.Parameters mParameters;
    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private static int mCurrentCameraFacing = -1;

    public CameraSurfaceView(Context context, Camera camera) {
        super(context);

        this.mContext = context;
        this.mCamera = camera;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void initCamera(int degrees) {
        init();
        mCamera.setDisplayOrientation(degrees);
        setParameters(mContext);
    }

    public void init() {
        if (mCurrentCameraFacing != -1) {
            init(mCurrentCameraFacing);
        } else {
            init(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Camera.CameraInfo.CAMERA_FACING_BACK, Camera.CameraInfo.CAMERA_FACING_FRONT})
    @interface CameraIdType {}

    public void init(@CameraIdType int cameraId) {
        if (mCamera == null) {
            try {
                if (Camera.getNumberOfCameras() >= 2) {
                    mCamera = Camera.open(cameraId);
                } else {
                    mCamera = Camera.open();
                }
            } catch (Exception e) {
                Log.e(TAG, "Camera Open Fail " + e.getMessage());
            }
        }
        if (mCamera == null) {
            Log.e(TAG, "Camera Open Fail");
            return;
        }
        mCurrentCameraFacing = cameraId;
        mParameters = mCamera.getParameters();
        mParameters.setPictureFormat(PixelFormat.JPEG);
        mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        if (!Build.MODEL.equals("KORIDY H30")) {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else {
            mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        mCamera.setParameters(mParameters);
    }

    public void setParameters(@NonNull Context context) {
        Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters != null) {
            List<Camera.Size> picSize = parameters.getSupportedPictureSizes();
            List<Camera.Size> preSize = parameters.getSupportedPreviewSizes();
            Camera.Size optimalPicSize = getOptimalSize(picSize, display.getHeight(), display.getWidth());
            Camera.Size optimalPreSize = getOptimalSize(preSize, display.getHeight(), display.getWidth());

            if (optimalPicSize != null) {
                parameters.setPictureSize(optimalPicSize.width, optimalPicSize.height);
            }
            if (optimalPreSize != null) {
                parameters.setPictureSize(optimalPreSize.width, optimalPreSize.height);
            }
            parameters.set("orientation", "portrait");
            mCamera.setParameters(parameters);
        }
    }

    private Camera.Size getOptimalSize(List<Camera.Size> sizes, int width, int height) {
        if (sizes == null) {
            return null;
        }
        final double Aspect = 0.05;
        double targetRatio = (double) width / height;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        for (Camera.Size size: sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > Aspect) {
                continue;
            }
            if (Math.abs(size.height - height) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - height);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size: sizes) {
                if (Math.abs(size.height - height) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - height);
                }
            }
        }
        return optimalSize;
    }

    public static Camera openCamera(@CameraIdType int cameraId) {
        Camera camera = null;
        try {
            if (Camera.getNumberOfCameras() >= 2) {
                camera = Camera.open(cameraId);
            } else {
                camera = Camera.open();
            }
        } catch (Exception e) {
            Log.e(TAG, "Camera Open Fail" + e.getMessage());
        }
        mCurrentCameraFacing = cameraId;
        return camera;
    }

    public Camera switchCamera() {
        int cameraCount = Camera.getNumberOfCameras();
        if (cameraCount == 0) {
            return null;
        }
        if (mCurrentCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        } else if (mCurrentCameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        }

        return mCamera;
    }

    public Camera switchCamera(@CameraIdType int cameraId) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        try {
            mCamera = Camera.open(cameraId);
        } catch (Exception e) {
            Log.e(TAG, "Camera " + cameraId + " Open Fail" + e.getMessage());
            return null;
        }
        mCurrentCameraFacing = cameraId;
        mCamera.setDisplayOrientation(90);
        setParameters(mContext);
        showPreview(mSurfaceHolder);

        return mCamera;
    }


    public void takePicture(Camera.ShutterCallback shutterCallback) {
        takePicture(shutterCallback, null, null, null);
    }

    public void takePicture(Camera.ShutterCallback shutterCallback,
                            Camera.PictureCallback rawCallback) {
        takePicture(shutterCallback, rawCallback, null, null);
    }

    public void takePicture(Camera.ShutterCallback shutterCallback,
                            Camera.PictureCallback rawCallback,
                            Camera.PictureCallback jpegCallback) {
        takePicture(shutterCallback, rawCallback, null, jpegCallback);
    }

    public void takePicture(Camera.ShutterCallback shutterCallback,
                            Camera.PictureCallback rawCallback,
                            Camera.PictureCallback postViewCallback,
                            Camera.PictureCallback jpegCallback) {
        Log.e(TAG, "takePicture ");
        mCamera.takePicture(shutterCallback, rawCallback, postViewCallback, jpegCallback);
    }

    public void showPreview(SurfaceHolder surfaceHolder) {
        Log.e(TAG, "showPreview ");
        if (surfaceHolder == null) {
            return;
        }

        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            return;
        }


        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
            mCamera.cancelAutoFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mCamera.getParameters().getFocusMode().contains("fixed")) {
            return;
        }

        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    mCamera.cancelAutoFocus();
                    doAutoFocus();
                }
            }
        });
    }

    public void stopPreview() {
        Log.e(TAG, "stopPreview ");
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                Log.e(TAG, "Error stop camera preview " + e.getMessage());
            }
        }
    }

    public void startPreview() {
        showPreview(mSurfaceHolder);
    }

    public void doAutoFocus() {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(mParameters);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                mCamera.cancelAutoFocus();
                Camera.Parameters parameter = mCamera.getParameters();
                if (!Build.MODEL.equals("KORIDY H30")) {
                    parameter.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
                } else {
                    parameter.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }
                camera.setParameters(mParameters);
            }
        });
    }

    public void destroy() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void release() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        showPreview(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.e(TAG, "surfaceChanged ");
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }
        stopPreview();
        showPreview(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        destroy();
    }
}
