package zblibrary.zgl.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import zblibrary.zgl.R;
import zuo.biao.library.util.StringUtil;

public class SortView extends RelativeLayout implements View.OnClickListener {
    private RelativeLayout sortView,sort_root,sort_rl;

    private TextView sort_title;
    private ImageView sort_iv;

    private LinearLayout sort_filter;

    private LinearLayout sort_rqsp_up;
    private TextView sort_rqsp_tv_up;
    private ImageView sort_rqsp_iv_up;

    private LinearLayout sort_rqsp_down;
    private TextView sort_rqsp_tv_down;
    private ImageView sort_rqsp_iv_down;


    private LinearLayout sort_zjsj_up;
    private TextView sort_zjsj_tv_up;
    private ImageView sort_zjsj_iv_up;

    private LinearLayout sort_zjsj_down;
    private TextView sort_zjsj_tv_down;
    private ImageView sort_zjsj_iv_down;


    private LinearLayout sort_jjmy_up;
    private TextView sort_jjmy_tv_up;
    private ImageView sort_jjmy_iv_up;

    private LinearLayout sort_jjmy_down;
    private TextView sort_jjmy_tv_down;
    private ImageView sort_jjmy_iv_down;


    private LinearLayout sort_zxrs_up;
    private TextView sort_zxrs_tv_up;
    private ImageView sort_zxrs_iv_up;

    private LinearLayout sort_zxrs_down;
    private TextView sort_zxrs_tv_down;
    private ImageView sort_zxrs_iv_down;


    private int now_select = 0;// 0  人气 1最新上架 2即将满员 3总需人数
    private int now_up_down = 0;//0-上 低→高   1-下  高→低
    private OnItemClickListener onItemClickListener;

    private boolean isMall;
    private Context context;
    public SortView(Context context) {
        super(context);
        initView(context);
    }

    public SortView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SortView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        sortView =(RelativeLayout) LayoutInflater.from(context).inflate(R.layout.sort_view, this,false);
        addView(sortView);

        sort_root = sortView.findViewById(R.id.sort_root);
        sort_rl = sortView.findViewById(R.id.sort_rl);

        sort_title = sortView.findViewById(R.id.sort_title);
        sort_iv = sortView.findViewById(R.id.sort_iv);

        sort_filter = sortView.findViewById(R.id.sort_filter);

        sort_rqsp_up = sortView.findViewById(R.id.sort_rqsp_up);
        sort_rqsp_tv_up = sortView.findViewById(R.id.sort_rqsp_tv_up);
        sort_rqsp_iv_up = sortView.findViewById(R.id.sort_rqsp_iv_up);

        sort_rqsp_down = sortView.findViewById(R.id.sort_rqsp_down);
        sort_rqsp_tv_down = sortView.findViewById(R.id.sort_rqsp_tv_down);
        sort_rqsp_iv_down = sortView.findViewById(R.id.sort_rqsp_iv_down);


        sort_zjsj_up = sortView.findViewById(R.id.sort_zjsj_up);
        sort_zjsj_tv_up = sortView.findViewById(R.id.sort_zjsj_tv_up);
        sort_zjsj_iv_up = sortView.findViewById(R.id.sort_zjsj_iv_up);

        sort_zjsj_down = sortView.findViewById(R.id.sort_zjsj_down);
        sort_zjsj_tv_down = sortView.findViewById(R.id.sort_zjsj_tv_down);
        sort_zjsj_iv_down = sortView.findViewById(R.id.sort_zjsj_iv_down);

        sort_jjmy_up = sortView.findViewById(R.id.sort_jjmy_up);
        sort_jjmy_tv_up = sortView.findViewById(R.id.sort_jjmy_tv_up);
        sort_jjmy_iv_up = sortView.findViewById(R.id.sort_jjmy_iv_up);

        sort_jjmy_down = sortView.findViewById(R.id.sort_jjmy_down);
        sort_jjmy_tv_down = sortView.findViewById(R.id.sort_jjmy_tv_down);
        sort_jjmy_iv_down = sortView.findViewById(R.id.sort_jjmy_iv_down);


        sort_zxrs_up = sortView.findViewById(R.id.sort_zxrs_up);
        sort_zxrs_tv_up = sortView.findViewById(R.id.sort_zxrs_tv_up);
        sort_zxrs_iv_up = sortView.findViewById(R.id.sort_zxrs_iv_up);

        sort_zxrs_down = sortView.findViewById(R.id.sort_zxrs_down);
        sort_zxrs_tv_down = sortView.findViewById(R.id.sort_zxrs_tv_down);
        sort_zxrs_iv_down = sortView.findViewById(R.id.sort_zxrs_iv_down);

        sortView.setOnClickListener(this);

        sort_filter.setOnClickListener(this);
        sort_rqsp_up.setOnClickListener(this);
        sort_rqsp_down.setOnClickListener(this);
        sort_zjsj_up.setOnClickListener(this);
        sort_zjsj_down.setOnClickListener(this);
        sort_jjmy_up.setOnClickListener(this);
        sort_jjmy_down.setOnClickListener(this);
        sort_zxrs_up.setOnClickListener(this);
        sort_zxrs_down.setOnClickListener(this);

        onClick(sort_jjmy_up);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.sort_root:
                goneView();
                return;
            case R.id.sort_filter:
                visibleView();
                return;
            case R.id.sort_rqsp_up:
                resetView();
                sort_rqsp_up.setSelected(true);
                sort_rqsp_tv_up.setSelected(true);
                sort_rqsp_iv_up.setSelected(true);
                now_select=0;
                now_up_down = 0;
                sort_title.setText(getResources().getString(R.string.class_sear_rqsp));
                sort_iv.setImageResource(R.mipmap.sort_up_title);
                break;
            case R.id.sort_rqsp_down:
                resetView();
                sort_rqsp_down.setSelected(true);
                sort_rqsp_tv_down.setSelected(true);
                sort_rqsp_iv_down.setSelected(true);
                now_select=0;
                now_up_down = 1;
                sort_title.setText(getResources().getString(R.string.class_sear_rqsp));
                sort_iv.setImageResource(R.mipmap.sort_down_title);
                break;
            case R.id.sort_zjsj_up:
                resetView();
                sort_zjsj_up.setSelected(true);
                sort_zjsj_tv_up.setSelected(true);
                sort_zjsj_iv_up.setSelected(true);
                now_select=1;
                now_up_down = 0;
                sort_title.setText(getResources().getString(R.string.class_sear_zxsj));
                sort_iv.setImageResource(R.mipmap.sort_up_title);
                break;
            case R.id.sort_zjsj_down:
                resetView();
                sort_zjsj_down.setSelected(true);
                sort_zjsj_tv_down.setSelected(true);
                sort_zjsj_iv_down.setSelected(true);
                now_select=1;
                now_up_down = 1;
                sort_title.setText(getResources().getString(R.string.class_sear_zxsj));
                sort_iv.setImageResource(R.mipmap.sort_down_title);
                break;
            case R.id.sort_jjmy_up:
                resetView();
                sort_jjmy_up.setSelected(true);
                sort_jjmy_tv_up.setSelected(true);
                sort_jjmy_iv_up.setSelected(true);
                now_select=2;
                now_up_down = 0;
                sort_title.setText(getResources().getString(R.string.class_sear_jjmy));
                sort_iv.setImageResource(R.mipmap.sort_up_title);
                break;
            case R.id.sort_jjmy_down:
                resetView();
                sort_jjmy_down.setSelected(true);
                sort_jjmy_tv_down.setSelected(true);
                sort_jjmy_iv_down.setSelected(true);
                now_select=2;
                now_up_down = 1;
                sort_title.setText(getResources().getString(R.string.class_sear_jjmy));
                sort_iv.setImageResource(R.mipmap.sort_down_title);
                break;
            case R.id.sort_zxrs_up:
                resetView();
                sort_zxrs_up.setSelected(true);
                sort_zxrs_tv_up.setSelected(true);
                sort_zxrs_iv_up.setSelected(true);
                now_select=3;
                now_up_down = 0;
                sort_title.setText(getResources().getString(R.string.class_sear_zxrs));
                sort_iv.setImageResource(R.mipmap.sort_up_title);
                break;
            case R.id.sort_zxrs_down:
                resetView();
                sort_zxrs_down.setSelected(true);
                sort_zxrs_tv_down.setSelected(true);
                sort_zxrs_iv_down.setSelected(true);
                now_select=3;
                now_up_down = 1;
                sort_title.setText(getResources().getString(R.string.class_sear_zxrs));
                sort_iv.setImageResource(R.mipmap.sort_down_title);
                break;
            }
        if(onItemClickListener !=null ){
            onItemClickListener.onClick(now_select,now_up_down);
        }
    }

    private void resetView(){
        sort_rqsp_up.setSelected(false);
        sort_rqsp_tv_up.setSelected(false);
        sort_rqsp_iv_up.setSelected(false);

        sort_rqsp_down.setSelected(false);
        sort_rqsp_tv_down.setSelected(false);
        sort_rqsp_iv_down.setSelected(false);

        sort_zjsj_up.setSelected(false);
        sort_zjsj_tv_up.setSelected(false);
        sort_zjsj_iv_up.setSelected(false);

        sort_zjsj_down.setSelected(false);
        sort_zjsj_tv_down.setSelected(false);
        sort_zjsj_iv_down.setSelected(false);

        sort_jjmy_up.setSelected(false);
        sort_jjmy_tv_up.setSelected(false);
        sort_jjmy_iv_up.setSelected(false);

        sort_jjmy_down.setSelected(false);
        sort_jjmy_tv_down.setSelected(false);
        sort_jjmy_iv_down.setSelected(false);


        sort_zxrs_up.setSelected(false);
        sort_zxrs_tv_up.setSelected(false);
        sort_zxrs_iv_up.setSelected(false);

        sort_zxrs_down.setSelected(false);
        sort_zxrs_tv_down.setSelected(false);
        sort_zxrs_iv_down.setSelected(false);
        goneView();
    }

    public void setMallState(){
        isMall = true;
        onClick(sort_rqsp_up);
    }

    private void visibleView(){
        sort_filter.setVisibility(View.GONE);
        if(isMall){
            sort_rqsp_up.setVisibility(View.VISIBLE);
            sort_rqsp_down.setVisibility(View.VISIBLE);
            sort_zjsj_up.setVisibility(View.VISIBLE);
            sort_zjsj_down.setVisibility(View.VISIBLE);
        }else{
            sort_jjmy_up.setVisibility(View.VISIBLE);
            sort_jjmy_down.setVisibility(View.VISIBLE);
            sort_zjsj_up.setVisibility(View.VISIBLE);
            sort_zjsj_down.setVisibility(View.VISIBLE);
            sort_zxrs_up.setVisibility(View.VISIBLE);
            sort_zxrs_down.setVisibility(View.VISIBLE);
        }
        sortView.setBackgroundColor(Color.parseColor("#aa000000"));
        RelativeLayout.LayoutParams layoutParamsRoot =new RelativeLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT);
        sortView.setLayoutParams(layoutParamsRoot);
        sort_rl.setBackgroundResource(R.drawable.radius_4_shap);
        sort_rl.setPadding(StringUtil.dp2px(context,12),StringUtil.dp2px(context,12),StringUtil.dp2px(context,12),StringUtil.dp2px(context,30));
    }

    private void goneView(){
        sort_filter.setVisibility(View.VISIBLE);
        sort_rqsp_up.setVisibility(View.GONE);
        sort_rqsp_down.setVisibility(View.GONE);
        sort_zjsj_up.setVisibility(View.GONE);
        sort_zjsj_down.setVisibility(View.GONE);
        sort_jjmy_up.setVisibility(View.GONE);
        sort_jjmy_down.setVisibility(View.GONE);
        sort_zxrs_up.setVisibility(View.GONE);
        sort_zxrs_down.setVisibility(View.GONE);
        sortView.setBackgroundColor(Color.TRANSPARENT);
        RelativeLayout.LayoutParams layoutParamsRoot =new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        sortView.setLayoutParams(layoutParamsRoot);
        sort_rl.setBackgroundColor(Color.WHITE);
        sort_rl.setPadding(StringUtil.dp2px(context,12),StringUtil.dp2px(context,12),StringUtil.dp2px(context,12),StringUtil.dp2px(context,12));
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(int now_select,int now_up_down);
    }
}
