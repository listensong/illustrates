package zms.song.illustrates.hardware.wifi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;

import zms.song.illustrates.R;
import zms.song.illustrates.base.PermissionBaseActivity;
import zms.song.illustrates.widget.toast.HelloToast;


/**
 * Created by song on 10/21/2017.
 */
public class WlanDirectActivity extends PermissionBaseActivity implements View.OnClickListener {
    private static final String TAG = WlanDirectActivity.class.getSimpleName();

    private TextView mWifiTextView;
    private RecyclerView mRecyclerView;
    private ProgressBar mScanProgressBar;
    private WifiDeviceAdapter mDevAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlan_direct);

        initViewId();
        initRecyclerView();
        initWifiKitManager();
        if (!WifiKitManager.getInstance().isWifiEnabled()) {
            WifiKitManager.getInstance().setWifiEnable(true);
        }
    }

    private void initViewId() {
        mWifiTextView = (TextView) findViewById(R.id.enable_wifi_text_view);
        mWifiTextView.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.wifi_dev_recycler_view);
        mScanProgressBar = (ProgressBar) findViewById(R.id.scanning_progress_bar);
        mScanProgressBar.setOnClickListener(this);
    }

    private void initRecyclerView() {
        mDevAdapter = new WifiDeviceAdapter(mContext);
        mDevAdapter.setOnItemClickListener(new WifiDeviceAdapter.IOnItemClickListener() {
            @Override
            public void onItemClickedListener(View view, int position) {
                final WifiP2pDevice device = (WifiP2pDevice) view.getTag();
                if (device != null) {
                    WifiKitManager.getInstance().requestGroupInfo(new WifiP2pManager.GroupInfoListener() {
                        @Override
                        public void onGroupInfoAvailable(WifiP2pGroup group) {
                            if (group != null) {
                                if ((device.equals(group.getOwner()) || group.getClientList() != null && group.getClientList().contains(device))) {
                                    WifiKitManager.getInstance().cancelConnect();
                                    return;
                                }
                            }
                            connectToDevice(device);
                        }
                    });
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mDevAdapter);
    }

    private void connectToDevice(WifiP2pDevice device) {
        if (device == null) {
            return;
        }
        WifiKitManager.getInstance().connectTargetDevice(device.deviceAddress, new WifiKitManager.IWifiP2pConnect() {
            //如果己方主动请求设备连接，然后对方同意了连接请求，那么就会回调这个方法
            @Override
            public void onConnected() {
                Log.d(TAG, "Connect onConnected ");
            }

            @Override
            public void onFail() {
                Log.d(TAG, "Connect onFail ");
            }

            //如果己方主动请求设备连接，然后对方取消了连接请求，那么就会回调这个方法
            @Override
            public void onCancel() {
                Log.d(TAG, "Connect onCancel ");
            }
        });
    }

    private void initWifiKitManager() {
        //WIFI状态
        WifiKitManager.getInstance().setWifiStateCallback(new WifiKitManager.IWifiStateCallback() {
            @Override
            public void onEnabled() {
                HelloToast.makeText(getApplicationContext(), "onEnabled", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onEnabled ");
                startScanDevices();
            }

            @Override
            public void onDisabling() {
                HelloToast.makeText(getApplicationContext(), "onDisabling", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onDisabling ");
            }

            @Override
            public void onDisabled() {
                HelloToast.makeText(getApplicationContext(), "onDisabled", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onDisabled ");
            }
        });

        //扫描到WifiP2p设备，会调用这个回调
        WifiKitManager.getInstance().setWifiP2pPeersCallback(new WifiKitManager.IWifiP2pPeersChange() {
            @Override
            public void onDevicesChanged(Collection<WifiP2pDevice> devices) {
                mDevAdapter.updateList(devices);
            }
        });

        WifiKitManager.getInstance().setWifiP2pDiscoverCallback(new WifiKitManager.IWifiP2pDiscoverChange() {
            @Override
            public void onP2pDiscoverChange(int discoverState) {
                Log.d(TAG, "onP2pDiscoverChange  " + discoverState);
            }
        });

    }

    private void startScanDevices() {
        //启动扫描功能
        WifiKitManager.getInstance().startDiscoverDevices(new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "startScanDevices onSuccess ");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "startScanDevices onFailure ");
            }
        });

        //对方向己方发出连接请求，如果同意，就会回调onConnected
        WifiKitManager.getInstance().setWifiP2pConnectionCallback(new WifiKitManager.IWifiP2pConnectionChange() {
            @Override
            public void onConnected(WifiP2pInfo wifiP2pInfo) {
                Log.d(TAG, "ConnectionCallback onConnected ");
            }

            @Override
            public void onCanceled(WifiP2pInfo wifiP2pInfo) {
                Log.d(TAG, "ConnectionCallback onCanceled ");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.enable_wifi_text_view:
                if (WifiKitManager.getInstance().isWifiEnabled()) {
                    WifiKitManager.getInstance().startDiscoverDevices(new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "startScanDevices onSuccess ");
                        }

                        @Override
                        public void onFailure(int i) {
                            Log.d(TAG, "startScanDevices onFailure ");
                        }
                    });
                } else {
                    requestPermissions(new IPermissionCallback() {
                        @Override
                        public void onPermissionResult(@NonNull String perm, int result) {
                            if (result == PackageManager.PERMISSION_GRANTED) {
                                if (!WifiKitManager.getInstance().isWifiEnabled()) {
                                    WifiKitManager.getInstance().setWifiEnable(true);
                                }
                            }
                        }
                    }, Manifest.permission.CHANGE_WIFI_STATE);
                }
                break;
            case R.id.scanning_progress_bar: {
                startScanDevices();
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        WifiKitManager.getInstance().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        WifiKitManager.getInstance().onPause();
    }
}
