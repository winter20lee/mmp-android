package zuo.biao.library.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import zuo.biao.library.R;

public class RatioRelativeLayout extends RelativeLayout {

    /**
     * 宽高比例
     */
    private float mRatio = 0f;

    public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioRelativeLayout);

        mRatio = typedArray.getFloat(R.styleable.RatioRelativeLayout_relativeRatio, 0f);
        typedArray.recycle();
    }

    public RatioRelativeLayout(Context context) {
        super(context);
    }

    /**
     * 设置RelativeLayout的宽高比
     *
     * @param ratio
     */
    public void setRatio(float ratio) {
        mRatio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (mRatio != 0) {
            float height = width / mRatio;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}