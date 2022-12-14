package zuo.biao.library.ui;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import zuo.biao.library.R;

public class EmptyView extends LinearLayout{

    private ImageView iv_empty_view;
    private TextView tv_empty_view;
    private TextView tv_empty_view_1;
    public EmptyView(Context context) {
        super(context);
        initView(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void  initView(Context context){
        LinearLayout empty_view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.empty_view, this,false);
        addView(empty_view);
        iv_empty_view =  empty_view.findViewById(R.id.iv_empty_view);
        tv_empty_view = empty_view.findViewById(R.id.tv_empty_view);
        tv_empty_view_1 = empty_view.findViewById(R.id.tv_empty_view_1);
    }

    public void setEmptyImage(int resId){
        iv_empty_view.setImageResource(resId);
    }
    public void setEmptyText(String emptyText){
        tv_empty_view.setText(emptyText);
    }
    public void setEmptySecondText(String emptyText){
        tv_empty_view_1.setText(emptyText);
    }
}
