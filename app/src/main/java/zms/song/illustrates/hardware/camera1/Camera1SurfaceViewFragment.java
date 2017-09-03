package zms.song.illustrates.hardware.camera1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zms.song.illustrates.R;
import zms.song.illustrates.base.PermissionBaseFragment;

/**

 */
public class Camera1SurfaceViewFragment extends PermissionBaseFragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera1_surface_view, container, false);
    }
}
