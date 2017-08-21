package zms.song.illustrates.widget.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zms.song.illustrates.R;

/**
 * Created by song on 2017/8/21.
 */

public class MixedAdapter extends RecyclerView.Adapter<MixedAdapter.ViewHolder> {

    public interface IOnItemClickListener {
        public void onItemClicked(View view);
    }

    public interface IOnItemLongClickListener {
        public void onItemLongClicked(View view);
    }

    private Context mContext;
    private List<String> mList;

    private IOnItemClickListener mItemClickListener;
    private IOnItemLongClickListener mItemLongListener;

    public MixedAdapter(@NonNull Context context, @NonNull List<String> list) {
        mContext = context.getApplicationContext();
        mList = list;
    }

    public void setOnClickedListener(IOnItemClickListener itemListener) {
        mItemClickListener = itemListener;
    }

    public IOnItemClickListener getClickListener() {
        return mItemClickListener;
    }

    public void setOnLongClickedListener(IOnItemLongClickListener itemListener) {
        mItemLongListener = itemListener;
    }

    public IOnItemLongClickListener getLongClickListener() {
        return mItemLongListener;
    }

    public void addItem(String string) {
        int pos = mList.size();
        mList.add(pos, string + pos);
        notifyItemInserted(pos);
    }

    public void insertItem(int pos, String string) {
        mList.add(pos, string);
        notifyItemInserted(pos);
    }

    public void removeItem(int pos) {
        mList.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_mixed, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClicked(v);
                }
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemClickListener != null) {
                    mItemLongListener.onItemLongClicked(v);
                    return true;
                }
                return false;
            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position >= 0 && position < mList.size()) {
            holder.mIndexTextView.setText(String.valueOf(position));
            holder.mTitleTextView.setText(mList.get(position));
            holder.mPosition = position;
            holder.mItemView.setTag(position);
            //holder.mIcon.setImageResource(R.drawable.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0: mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        ImageView mIcon;
        TextView mIndexTextView;
        TextView mTitleTextView;
        int mPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.icon_image_view);
            mIndexTextView = (TextView) itemView.findViewById(R.id.index_text_view);
            mTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            mItemView = itemView;
        }
    }

}