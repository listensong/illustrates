package zms.song.illustrates.hardware.camera1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import zms.song.illustrates.R;
import zms.song.illustrates.base.PermissionBaseFragment;
import zms.song.illustrates.databinding.FragmentCamera1SurfaceViewBinding;
import zms.song.illustrates.util.Util;

/**

 */
public class Camera1SurfaceViewFragment extends PermissionBaseFragment {

    private FragmentCamera1SurfaceViewBinding mCameraBinding;
    private ViewGroup mPreviewGroup;

    public Camera1SurfaceViewFragment() {
        // Required empty public constructor
    }

    public static Camera1SurfaceViewFragment getNewInstance() {
        return new Camera1SurfaceViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCameraBinding = FragmentCamera1SurfaceViewBinding.inflate(inflater, container, false);
        mCameraBinding.setCameraPresenter(this);
        mMainView = mCameraBinding.getRoot();
        mPreviewGroup = mMainView.findViewById(R.id.preview_layout);
        checkCamera();
        return mCameraBinding.getRoot();
    }

    private void checkCamera() {
        if (Util.supportCamera(mContext)) {
            if (Util.cameraPermissionGranted(mContext)) {
                initSurfaceView();
            } else {
                requestPermissions(new IPermissionCallback() {
                    @Override
                    public void onPermissionResult(@NonNull String perm, int result) {
                        if (result == PackageManager.PERMISSION_GRANTED) {
                            initSurfaceView();
                        } else {
                            Camera1Activity activity = (Camera1Activity) getActivity();
                            if (activity != null) {
                                activity.onBackPressed();
                            }
                        }
                    }
                }, Manifest.permission.CAMERA);
            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.unsupported_camera_device), Toast.LENGTH_SHORT).show();
        }
    }

    private CameraSurfaceView mCameraProView;
    private Camera mCamera;
    private void initSurfaceView() {
        mCamera = CameraSurfaceView.openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCameraProView = new CameraSurfaceView(mContext, mCamera);
        mCameraProView.initCamera(90);
        mPreviewGroup.addView(mCameraProView);
    }


    Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    Camera.PictureCallback mRawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            mCameraProView.startPreview();
            Util.saveDataPicture(data);
        }
    };


    public void onSwitchBtnAction(View view) {
        mCameraProView.switchCamera();
    }

    public void onShutterBtnAction(View view) {
        if (mCameraProView != null) {
            mCameraProView.takePicture(mShutterCallback, null, mJpegCallback);
        }
    }

    public void onMoreBtnAction(View view) {
        Toast.makeText(mContext, "onMoreBtnAction", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        mCameraBinding = null;
        super.onDestroy();
    }
}
