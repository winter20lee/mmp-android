package zblibrary.zgl.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import zblibrary.zgl.R;

public class SearchLayout extends LinearLayout{

    private  Context context;
    private ImageView button_history;
    public EditText et_searchtext_search;
    private RelativeLayout searchview;
    private TextView button_cancle;
    public SearchLayout(Context context) {
        super(context);
        this.context = context;
        InitView();
    }

    public SearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        InitView();
    }

    public SearchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        InitView();
    }

    private void  InitView(){
        searchview =(RelativeLayout)LayoutInflater.from(context).inflate(R.layout.msearch_top, null);
        addView(searchview);

        button_history = (ImageView) searchview.findViewById(R.id.button_history);
        et_searchtext_search = (EditText) searchview.findViewById(R.id.et_searchtext_search);
        button_cancle = searchview.findViewById(R.id.button_cancle);
        setLinstener();
    }


    private void setLinstener() {

        button_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sCBlistener != null)
                sCBlistener.Back();
            }
        });


        et_searchtext_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = et_searchtext_search.getText().toString().trim();
                    if(sCBlistener!=null){
                        sCBlistener.Search(search);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void isSearchView(boolean isSearchView){
        if(isSearchView){
            button_cancle.setVisibility(View.VISIBLE);
            button_history.setVisibility(View.GONE);
        }
    }

    public interface setSearchCallBackListener{
        /**
         * @param str  搜索关键字
         */
        void Search(String str);
        /**
         * 后退
         */
        void Back();
    }

    private setSearchCallBackListener sCBlistener;
    /**
     * 设置接口回调
     * @param sCb   setCallBackListener接口
     */
    public void SetCallBackListener(setSearchCallBackListener sCb){
        sCBlistener=sCb;
    }
}
