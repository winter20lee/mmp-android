package zblibrary.zgl.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int top_bottom;
    private int left_right;//声明间距 //使用构造函数定义间距
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = top_bottom;
        outRect.bottom = top_bottom;
        //获得当前item的位置
        int position = parent.getChildAdapterPosition(view);
        //根据position确定item需要留出的位置
        switch (position % 2) {
            case 0:
                //位于左侧的item
                outRect.right = this.left_right;
                break;
            case 1:
                //位于右侧的item
                outRect.left = this.left_right;
                break;
            default:
                break;
        }
    }

    public SpaceItemDecoration(int top_bottom,int left_right) {
        this.top_bottom = top_bottom;
        this.left_right = left_right;
    }
}