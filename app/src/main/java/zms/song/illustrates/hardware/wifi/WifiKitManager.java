package zms.song.illustrates.hardware.wifi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Collection;

import zms.song.illustrates.base.IllustratesApplication;

/**
 * Created by song on 10/21/2017.
 */

public class WifiKitManager {
    private static final String TAG = "WifiKitManager";

    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mWifiP2pChannel;
    private WifiManager mWifiManager;
    private WifiP2pDevice mWifiP2pDevice;
    //private WifiP2pManager.ChannelListener mChannelListener;
    private IntentFilter mIntentFilter;
    private WifiStateChangeReceiver mWifiReceiver;
    private static String mWiDirectAddress;

    private static final String mDevPhone = "10-0050F204-5";
    private static final String mDevOther = "3-0050F204-5";

    private Context mContext;

    private static WifiKitManager mWifiKitManager;

    public WifiKitManager() {
        mContext = IllustratesApplication.getIllustratesApplication();
        mIntentFilter = new IntentFilter();
        mWifiP2pManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        mWifiP2pChannel = mWifiP2pManager.initialize(mContext, Looper.getMainLooper(), null);
        mWifiReceiver = new WifiStateChangeReceiver(mWifiP2pManager, mWifiP2pChannel, mThisDeviceChange);
        init();
    }

    public static synchronized WifiKitManager getInstance() {
        if (mWifiKitManager == null) {
            mWifiKitManager = new WifiKitManager();
        }
        return mWifiKitManager;
    }

    private void init() {
        initIntentFilter();
    }

    private void initIntentFilter() {
        mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
    }

    public void startDiscoverDevices() {
        mWifiP2pManager.discoverPeers(mWifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "discoverPeers onSuccess");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "discoverPeers onFailure " + i);
            }
        });
    }

    public void startDiscoverDevices(@NonNull WifiP2pManager.ActionListener listener) {
        mWifiP2pManager.discoverPeers(mWifiP2pChannel, listener);
    }

    public void stopDiscoverDevices() {
        mWifiP2pManager.stopPeerDiscovery(mWifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "stopPeerDiscovery onSuccess");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "stopPeerDiscovery onFailure " + i);
            }
        });
    }

    public void stopDiscoverDevices(@NonNull WifiP2pManager.ActionListener listener) {
        mWifiP2pManager.stopPeerDiscovery(mWifiP2pChannel, listener);
    }

    public interface IWifiP2pConnect {
        void onConnected();
        void onFail();
        void onCancel();
    }

    public void connectTargetDevice(String address) {
        if (address == null || address.isEmpty()) {
            return;
        }
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = address;
        mWifiP2pManager.connect(mWifiP2pChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "connect onSuccess ");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "connect onFailure ");
            }
        });
    }

    public void connectTargetDevice(String address, IWifiP2pConnect connect) {
        if (address == null || address.isEmpty()) {
            return;
        }
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = address;
        mWifiReceiver.setWifiP2pConnectResponse(connect);
        mWifiP2pManager.connect(mWifiP2pChannel, config, new WifiP2pManager.ActionListener() {
            //如果对方接收到请求，就会回调这个，用以说明连接请求成功发出，等待对方回应
            @Override
            public void onSuccess() {
                Log.d(TAG, "connect onSuccess ");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "connect onFailure ");
            }
        });
    }

    public void connectTargetDevice(String address, IWifiP2pConnect connect, WifiP2pManager.ActionListener listener) {
        if (address == null || address.isEmpty()) {
            return;
        }
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = address;
        mWifiReceiver.setWifiP2pConnectResponse(connect);
        mWifiP2pManager.connect(mWifiP2pChannel, config, listener);
    }

    public void cancelConnect() {
        mWifiP2pManager.cancelConnect(mWifiP2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "cancelConnect onSuccess ");
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "cancelConnect onFailure ");
            }
        });

    }

    public void cancelConnect(WifiP2pManager.ActionListener listener) {
        mWifiP2pManager.cancelConnect(mWifiP2pChannel, listener);
    }

    public void requestGroupInfo(WifiP2pManager.GroupInfoListener listener) {
        mWifiP2pManager.requestGroupInfo(mWifiP2pChannel, listener);
    }

    public void onResume() {
        Log.d(TAG, "onResume ");
        mContext.registerReceiver(mWifiReceiver, mIntentFilter);
    }

    public void onPause() {
        Log.d(TAG, "onPause ");
        mContext.unregisterReceiver(mWifiReceiver);
    }

    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    public void setWifiEnable(boolean enable) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
            mWifiManager.setWifiEnabled(enable);
        }
    }

    @Nullable
    public String getWiDirectAddress() {
        return mWiDirectAddress;
    }


    public interface IWifiStateCallback {
        public void onEnabled();
        public void onDisabling();
        public void onDisabled();
    }
    public void setWifiStateCallback(IWifiStateCallback callback) {
        mWifiReceiver.setWifiStateCallback(callback);
    }

    public interface IWifiP2pStateChange {
        public void onWifiP2pEnable();
        public void onWifiP2pDisable();
    }
    public void setWifiP2pStateCallback(IWifiP2pStateChange callback) {
        mWifiReceiver.setWifiP2pStateCallback(callback);
    }

    public interface IWifiP2pPeersChange {
        public void onDevicesChanged(Collection<WifiP2pDevice> devices);
    }
    public void setWifiP2pPeersCallback(IWifiP2pPeersChange callback) {
        mWifiReceiver.setWifiP2pPeersCallback(callback);
    }

    public interface IWifiP2pConnectionChange {
        public void onConnected(WifiP2pInfo wifiP2pInfo);
        public void onCanceled(WifiP2pInfo wifiP2pInfo);
    }
    public void setWifiP2pConnectionCallback(IWifiP2pConnectionChange wifiP2pConnectionChange) {
        mWifiReceiver.setWifiP2pConnectionChangeCallback(wifiP2pConnectionChange);
    }

    public interface IWifiP2pConnectionInfo {
        public void onConnectionInfo(WifiP2pInfo info);
    }
    public void setWifiP2pConnectionInfoCallback(IWifiP2pConnectionInfo callback) {
        mWifiReceiver.setWifiP2pConnectionInfoCallback(callback);
    }

    public interface IWifiP2pThisDeviceChange {
        public void onThisDevice(WifiP2pDevice device);
    }
    private IWifiP2pThisDeviceChange mThisDeviceChange = new IWifiP2pThisDeviceChange() {
        @Override
        public void onThisDevice(WifiP2pDevice device) {
            mWiDirectAddress = device.deviceAddress;
            Log.d(TAG, "IWifiP2pThisDeviceChange " + mWiDirectAddress);
        }
    };
    public void setWifiP2pThisDeviceCallback(IWifiP2pThisDeviceChange callback) {
        mWifiReceiver.setWifiP2pThisDeviceCallback(callback);
    }

    public interface IWifiP2pDiscoverChange {
        public void onP2pDiscoverChange(int discoverState);
    }
    public void setWifiP2pDiscoverCallback(IWifiP2pDiscoverChange callback) {
        mWifiReceiver.setWifiP2pDiscoverCallback(callback);
    }

    public static class WifiStateChangeReceiver extends BroadcastReceiver {
        private static final String LOG_TAG = "WifiKitManager";

        WeakReference<IWifiStateCallback> mWifiStateCallback;
        WeakReference<IWifiP2pStateChange> mWifiP2pStateChange;
        WeakReference<IWifiP2pPeersChange> mWifiP2pPeersChange;
        WeakReference<IWifiP2pConnectionChange> mWifiP2pConnectionChange;
        WeakReference<IWifiP2pConnectionInfo> mWifiP2pConnectionInfo;
        WeakReference<IWifiP2pThisDeviceChange> mWifiP2pThisDeviceChange;
        WeakReference<IWifiP2pThisDeviceChange> mThisDeviceChange;
        WeakReference<IWifiP2pDiscoverChange> mWifiP2pDiscoverChange;
        WeakReference<IWifiP2pConnect> mWifiP2pConnect;

        WeakReference<WifiP2pManager> mWifiP2pManager;
        WeakReference<WifiP2pManager.Channel> mChannel;

        public WifiStateChangeReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel) {
            mWifiP2pManager = new WeakReference<>(wifiP2pManager);
            mChannel = new WeakReference<>(channel);
        }

        public WifiStateChangeReceiver(WifiP2pManager wifiP2pManager, WifiP2pManager.Channel channel, IWifiP2pThisDeviceChange thisDeviceChange) {
            mWifiP2pManager = new WeakReference<>(wifiP2pManager);
            mChannel = new WeakReference<>(channel);
            mThisDeviceChange = new WeakReference<>(thisDeviceChange);
        }

        public void setWifiStateCallback(IWifiStateCallback callback) {
            mWifiStateCallback = new WeakReference<>(callback);
        }

        public void setWifiP2pStateCallback(IWifiP2pStateChange callback) {
            mWifiP2pStateChange = new WeakReference<>(callback);
        }

        public void setWifiP2pPeersCallback(IWifiP2pPeersChange callback) {
            mWifiP2pPeersChange = new WeakReference<>(callback);
        }

        public void setWifiP2pConnectionChangeCallback(IWifiP2pConnectionChange callback) {
            mWifiP2pConnectionChange = new WeakReference<>(callback);
        }

        public void setWifiP2pConnectionInfoCallback(IWifiP2pConnectionInfo callback) {
            mWifiP2pConnectionInfo = new WeakReference<>(callback);
        }

        public void setWifiP2pThisDeviceCallback(IWifiP2pThisDeviceChange callback) {
            mWifiP2pThisDeviceChange = new WeakReference<>(callback);
        }

        public void setWifiP2pDiscoverCallback(IWifiP2pDiscoverChange callback) {
            mWifiP2pDiscoverChange = new WeakReference<>(callback);
        }

        public void setWifiP2pConnectResponse(IWifiP2pConnect callback) {
            mWifiP2pConnect = new WeakReference<>(callback);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(LOG_TAG, "onReceive " + action);
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    if (mWifiP2pStateChange != null && mWifiP2pStateChange.get() != null) {
                        mWifiP2pStateChange.get().onWifiP2pEnable();
                    }
                } else {
                    if (mWifiP2pStateChange != null && mWifiP2pStateChange.get() != null) {
                        mWifiP2pStateChange.get().onWifiP2pDisable();
                    }
                }
            } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                if (mWifiP2pManager.get() != null && mChannel.get() != null) {
                    mWifiP2pManager.get().requestPeers(mChannel.get(), new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                            if (mWifiP2pPeersChange != null && mWifiP2pPeersChange.get() != null) {
                                mWifiP2pPeersChange.get().onDevicesChanged(wifiP2pDeviceList.getDeviceList());
                            }
                        }
                    });
                }
            } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                WifiP2pInfo wifiP2pInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
                if (mWifiP2pConnect != null && mWifiP2pConnect.get() != null) {
                    if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                        mWifiP2pConnect.get().onCancel();
                    } else if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        mWifiP2pConnect.get().onConnected();
                    }
                    mWifiP2pConnect.clear();
                } else {
                    if (networkInfo.isConnected()) {
                        if (mWifiP2pManager.get() != null && mChannel.get() != null) {
                            mWifiP2pManager.get().requestConnectionInfo(mChannel.get(), new WifiP2pManager.ConnectionInfoListener() {
                                @Override
                                public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                                    if (mWifiP2pConnectionInfo != null && mWifiP2pConnectionInfo.get() != null) {
                                        mWifiP2pConnectionInfo.get().onConnectionInfo(wifiP2pInfo);
                                    }
                                }
                            });
                        }

                        if (mWifiP2pConnectionChange != null && mWifiP2pConnectionChange.get() != null) {
                            mWifiP2pConnectionChange.get().onConnected(wifiP2pInfo);
                        }
                    } else {
                        if (mWifiP2pConnectionChange != null && mWifiP2pConnectionChange.get() != null) {
                            mWifiP2pConnectionChange.get().onCanceled(wifiP2pInfo);
                        }
                    }
                }
            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                if (mWifiP2pThisDeviceChange != null && mWifiP2pThisDeviceChange.get() != null) {
                    mWifiP2pThisDeviceChange.get().onThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
                }
                if (mThisDeviceChange != null && mThisDeviceChange.get() != null) {
                    mThisDeviceChange.get().onThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
                }
            } else if (WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION.equals(action)) {
                if (mWifiP2pDiscoverChange != null && mWifiP2pDiscoverChange.get() != null) {
                    mWifiP2pDiscoverChange.get().onP2pDiscoverChange(intent.getIntExtra(WifiP2pManager.EXTRA_DISCOVERY_STATE, 1));
                }
            } else if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int wifiNewState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                Log.d(LOG_TAG, action + " " + wifiNewState);
                switch (wifiNewState) {
                    case WifiManager.WIFI_STATE_DISABLING:
                        if (mWifiStateCallback != null && mWifiStateCallback.get() != null) {
                            mWifiStateCallback.get().onDisabling();
                        }
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        if (mWifiStateCallback != null && mWifiStateCallback.get() != null) {
                            mWifiStateCallback.get().onDisabled();
                        }
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        if (mWifiStateCallback != null && mWifiStateCallback.get() != null) {
                            mWifiStateCallback.get().onEnabled();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
