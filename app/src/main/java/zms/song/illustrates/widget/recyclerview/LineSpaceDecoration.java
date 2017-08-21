package zms.song.illustrates.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by song on 2017/8/21.
 */

public class LineSpaceDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;
    public LineSpaceDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent != null && parent.getChildPosition(view) != 0) {
            outRect.top = mSpace;
        }
    }
}
