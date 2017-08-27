package zms.song.illustrates.widget.recyclerview;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zms.song.illustrates.R;
import zms.song.illustrates.base.BaseActivity;

public class RecyclerViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        initView();
        initItemList();
        initRecyclerAdapter();
        initSwipeRefresh();
    }

    private RecyclerView mSampleRecyclerView;
    private void initView() {
        mSampleRecyclerView = (RecyclerView) findViewById(R.id.sample_recycler_view);
    }

    private List<ItemData> mItemList = new ArrayList<>();
    private void initItemList() {
        if (mItemList == null) {
            mItemList = new ArrayList<>();
        }
        for (int i = 0; i < 20; i++) {
            mItemList.add(i, new ItemData("name", "title", "c:" + i, R.mipmap.ic_launcher, false));
        }
    }

    private void addItem() {
    }

    private MixedAdapter mMixedAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mLastVisiblePos;
    private void initRecyclerAdapter() {
        mMixedAdapter = new MixedAdapter(this, mItemList);
        mSampleRecyclerView.setAdapter(mMixedAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mSampleRecyclerView.setLayoutManager(mLayoutManager);
        mSampleRecyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = getResources().getDimensionPixelSize(R.dimen.item_space);
        mSampleRecyclerView.addItemDecoration(new LineSpaceDecoration(space));
        mMixedAdapter.setOnClickedListener(new MixedAdapter.IOnItemClickListener() {
            @Override
            public void onItemClicked(View view) {
                Toast.makeText(mContext, "onItemClicked " + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        mMixedAdapter.setOnLongClickedListener(new MixedAdapter.IOnItemLongClickListener() {
            @Override
            public void onItemLongClicked(View view) {
                Toast.makeText(mContext, "onItemLongClicked " + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        mLastVisiblePos = mLayoutManager.findLastVisibleItemPosition();
        //滚动监听
        mSampleRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //0：当前屏幕停止滚动；1：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2：随用户的操作，屏幕上产生的惯性滑动；
                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
                if (RecyclerView.SCROLL_STATE_IDLE == newState && mLastVisiblePos + 2 >= mLayoutManager.getItemCount() ) {
                    Toast.makeText(mContext, "onScroll TryToLoadMore ", Toast.LENGTH_SHORT).show();
                    addItem();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisiblePos = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(mContext, "onRefresh", Toast.LENGTH_SHORT).show();
            swipeRefreshSimulated();
        }
    };

    private void swipeRefreshSimulated() {
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
    private void initSwipeRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int)TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()
//        ));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_color1, R.color.swipe_color2);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    @Override
    public void onBackPressed() {

        if (mMixedAdapter != null) {
            if (mMixedAdapter.checkBoxColumnVisible()) {
                mMixedAdapter.setCheckBoxColumn(false);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
