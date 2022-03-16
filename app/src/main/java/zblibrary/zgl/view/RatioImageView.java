package zblibrary.zgl.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import zblibrary.zgl.R;

public class RatioImageView extends AppCompatImageView {

    /**
     * 宽高比例
     */
    private float mRatio = 0f;

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);

        mRatio = typedArray.getFloat(R.styleable.RatioImageView_ratio, 0f);
        typedArray.recycle();
    }

    public RatioImageView(Context context) {
        super(context);
    }

    /**
     * 设置ImageView的宽高比
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