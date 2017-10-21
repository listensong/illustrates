package zms.song.illustrates.hardware.wifi;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import zms.song.illustrates.R;
import zms.song.illustrates.util.FileUtil;

/**
 * Created by song on 10/21/2017.
 */

public class WifiDeviceAdapter extends RecyclerView.Adapter<WifiDeviceAdapter.ViewHolder>{

    private Context mContext;
    private WifiP2pDeviceList mDevList;
    private ArrayList<WifiP2pDevice> mList = new ArrayList<>();

    public WifiDeviceAdapter(Context context, Collection<WifiP2pDevice> devices) {
        mContext = context.getApplicationContext();
        initList(devices);
    }

    public WifiDeviceAdapter(Context context) {
        mContext = context.getApplicationContext();
    }

    private ArrayList<WifiP2pDevice> initList(Collection<WifiP2pDevice> devices) {
        if (devices != null) {
            mList.addAll(devices);
        }
        return mList;
    }

    public void updateList(Collection<WifiP2pDevice> devices) {
        if (devices != null) {
            mList.clear();
            mList.addAll(devices);
            notifyDataSetChanged();
        }
    }

    public interface IOnItemClickListener {
        public void onItemClickedListener(View view, int position);
    }
    private IOnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(IOnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_wifi_dev, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mDevNameTextView.setText(mList.get(position).deviceName);
        //SysUtil.getWifiP2pDeviceType(mList.get(position).deviceName);
        //holder.mDevTypeTextView.setText(mList.get(position).primaryDeviceType);
        holder.mDevTypeImageView.setImageResource(FileUtil.getDevTypeDrawableRes(mList.get(position).primaryDeviceType));
        holder.mDevMacTextView.setText(mList.get(position).deviceAddress);
        holder.mItemView.setTag(mList.get(position));
        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClickedListener(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        TextView mDevNameTextView;
        TextView mDevTypeTextView;
        TextView mDevMacTextView;

        ImageView mDevTypeImageView;

        public ViewHolder(View view) {
            super(view);
            mItemView = view;
            mDevNameTextView = view.findViewById(R.id.dev_name_text_view);
            //mDevTypeTextView = view.findViewById(R.id.dev_type_text_view);
            mDevMacTextView = view.findViewById(R.id.dev_mac_text_view);
            mDevTypeImageView = view.findViewById(R.id.dev_type_image_view);
        }
    }
}
