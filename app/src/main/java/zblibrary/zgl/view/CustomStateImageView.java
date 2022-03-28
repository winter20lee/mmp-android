package zblibrary.zgl.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


public class CustomStateImageView extends AppCompatImageView {
    public CustomStateImageView(Context context) {
        super(context);
    }

    public CustomStateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomStateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StateListDrawable getResource(Drawable normal,Drawable selected_pressed) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(View.PRESSED_ENABLED_STATE_SET, selected_pressed);
        bg.addState(View.FOCUSED_STATE_SET, selected_pressed);
        bg.addState(View.SELECTED_STATE_SET, selected_pressed);
        bg.addState(View.EMPTY_STATE_SET, normal);
        return bg;
    }
}
