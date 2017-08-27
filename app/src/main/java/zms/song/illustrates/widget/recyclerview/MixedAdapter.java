package zms.song.illustrates.widget.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zms.song.illustrates.Anim.AnimUtil;
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
    private List<ItemData> mList;
    private int mSelectState = View.GONE;

    private IOnItemClickListener mItemClickListener;
    private IOnItemLongClickListener mItemLongListener;

    public MixedAdapter(@NonNull Context context, @NonNull List<ItemData> list) {
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

    }

    public void insertItem(int pos, String string) {

    }

    public boolean setCheckBoxColumn(boolean show) {
        if (show) {
            if (mSelectState == View.GONE) {
                mSelectState = View.VISIBLE;
                notifyDataSetChanged();
                return true;
            }
        } else {
            if (mSelectState == View.VISIBLE) {
                mSelectState = View.GONE;
                notifyDataSetChanged();
                return false;
            }
        }
        return false;
    }

    public boolean checkBoxColumnVisible() {
        return mSelectState == View.VISIBLE;
    }

    public void removeItem(int pos) {
        mList.remove(pos);
        notifyItemRemoved(pos);
    }

    private void itemClick(View view, int position) {
        if (mSelectState == View.VISIBLE) {
            //mList.get(position).invertCheckedState();
            //notifyItemChanged(position);
        } else {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClicked(view);
            }
        }
    }


    private boolean itemLongClick(View view, int position) {
        if (mItemLongListener != null) {
            mItemLongListener.onItemLongClicked(view);
            if (mSelectState == View.GONE) {
                mSelectState = View.VISIBLE;
            } else {
                mSelectState = View.GONE;
            }
            notifyDataSetChanged();
        }
        return true;
    }

    private void checkedStateChanged(int position, boolean checked) {
        mList.get(position).setChecked(checked);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_mixed, parent, false);
        return new ViewHolder(view);
    }

    //private boolean mCanBeShow = true;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position >= 0 && position < mList.size()) {
            holder.mIndexTextView.setText(String.valueOf(position));
            holder.mTitleTextView.setText(mList.get(position).getTitle());
            holder.mIcon.setImageResource(mList.get(position).getDrawableRes());

            if (mSelectState == View.VISIBLE) {
                if (!mList.get(position).getShow()) {//if (mCanBeShow) {
                    mList.get(position).setShow(true);
                    AnimUtil.applyObjectAnimSetSync(holder.mContentLayout, R.animator.item_translate_in, holder.mCheckBox, R.animator.item_fade_in, null);
                }
            } else {
                if (mList.get(position).getShow()) {
                    mList.get(position).setShow(false);
                    AnimUtil.applyObjectAnimSetSync(holder.mContentLayout, R.animator.item_translate_out, holder.mCheckBox, R.animator.item_fade_out, null);
                }
            }

            holder.mCheckBox.setOnCheckedChangeListener(null);
            holder.mCheckBox.setChecked(mList.get(position).getChecked());
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkedStateChanged(position, isChecked);
                }
            });

            holder.mPosition = position;

            holder.mItemView.setTag(position);
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectState == View.VISIBLE) {
                        holder.mCheckBox.setChecked(mList.get(position).invertCheckedState());
                    }
                    itemClick(v, position);
                }
            });
            holder.mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return itemLongClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0: mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View mItemView;
        View mContentLayout;
        ImageView mIcon;
        TextView mIndexTextView;
        TextView mTitleTextView;
        CheckBox mCheckBox;
        int mPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.icon_image_view);
            mIndexTextView = itemView.findViewById(R.id.index_text_view);
            mTitleTextView = itemView.findViewById(R.id.title_text_view);
            mCheckBox = itemView.findViewById(R.id.selected_check_box);
            mContentLayout = itemView.findViewById(R.id.content_layout);
            mItemView = itemView;
        }
    }

}