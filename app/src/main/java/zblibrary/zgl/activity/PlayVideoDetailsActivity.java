package zblibrary.zgl.activity;

import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.FirstBanner;
import zblibrary.zgl.model.ProductDes;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.FlowLayout;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.WebViewSettingsUtils;

/**商品详情
 */
public class PlayVideoDetailsActivity extends BaseActivity implements OnClickListener, OnBottomDragListener
        , OnHttpResponseListener {
    public static final String TAG = "ProductDetailsActivity";
    public static final int REQUEST_CODE_DES = 10001;
    private StandardGSYVideoPlayer videoPlayer;
    private long goodsId = 0;
    private TextView product_details_name,product_details_price,product_details_jianjie;
    private WebView product_details_content;
    private ProductDes productDes;
    private List<FirstBanner> firstBannerList = new ArrayList<>();
    private TextView product_details_duobao,product_details_buy,product_details_gouwuche;
    private OrientationUtils orientationUtils;
    private FlowLayout msearch_history;
    public static Intent createIntent(Context context, long productId) {
        return new Intent(context, PlayVideoDetailsActivity.class).putExtra(INTENT_ID, productId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video_details_activity, this);
        intent = getIntent();
        goodsId = intent.getLongExtra(INTENT_ID, goodsId);
        if (goodsId == 0) {
            finishWithError("商品不存在！");
            return;
        }
        initView();
        initData();
        initEvent();
        HttpRequest.getProductDes(goodsId,REQUEST_CODE_DES, new OnHttpResponseListenerImpl(this));
    }

    @Override
    public void initView() {//必须调用
        videoPlayer = findView(R.id.video_player);
        product_details_name = findView(R.id.product_details_name);
        product_details_price = findView(R.id.product_details_price);
        product_details_content = findView(R.id.product_details_content);
        product_details_duobao = findView(R.id.product_details_duobao);
        product_details_buy = findView(R.id.product_details_buy);
        product_details_gouwuche = findView(R.id.product_details_gouwuche);
        product_details_jianjie = findView(R.id.product_details_jianjie);
        msearch_history =  findView(R.id.msearch_history);
        WebViewSettingsUtils.setWebView(product_details_content);
    }

    @Override
    public void initData() {//必须调用
        if(productDes==null)
            return;
        initPlayer();
        product_details_name.setText(productDes.name);
        product_details_jianjie.setText(productDes.desc);
        String good_spe = "";
        if(StringUtil.isNotEmpty(productDes.goodsSpec,true)){
            productDes.goodsSpec1 = GsonUtil.jsonToList(productDes.goodsSpec,ProductDes.GoodsSpecModel.class);
        }
        if(productDes.goodsSpec1!=null && productDes.goodsSpec1.size()>0){
            for (ProductDes.GoodsSpecModel goodsSpecListModel:productDes.goodsSpec1) {
                good_spe += goodsSpecListModel.attributionValName+" ";
            }
        }
        product_details_price.setText(StringUtil.changeF2Y(productDes.price));
        product_details_content.loadDataWithBaseURL(null, productDes.detail, "text/html", "utf-8", null);
        if(productDes.toLoot==1){
            product_details_duobao.setTextColor(Color.WHITE);
            product_details_duobao.setBackgroundResource(R.drawable.radius_20_red_shap);
            product_details_duobao.setOnClickListener(this);
        }

        for (int i = 0; i < 5; i++)
        {
            LayoutInflater mInflater = LayoutInflater.from(context);
            TextView tv = (TextView) mInflater.inflate(R.layout.suosou_item,msearch_history, false);
            tv.setText("测试"+i);
            msearch_history.addView(tv);
        }
    }

    @Override
    public void initEvent() {//必须调用
        findView(R.id.product_details_sele).setOnClickListener(this);
        product_details_buy.setOnClickListener(this);
        product_details_gouwuche.setOnClickListener(this);
    }

    @Override
    public void onDragBottom(boolean rightToLeft) {
        if (rightToLeft) {
            return;
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_details_sele:
                toActivity(ProductDesSelectWindow.createIntent(this,ProductDesSelectWindow.INTENT_FROM_TYPE_SPE, productDes.id,productDes.transData()));
                break;
            case R.id.product_details_buy:
                if(!MApplication.getInstance().isLoggedIn()){
                    toActivity(LoginActivity.createIntent(this));
                }else{
                    toActivity(ProductDesSelectWindow.createIntent(this,ProductDesSelectWindow.INTENT_FROM_TYPE_BUY, productDes.id,productDes.transData()));
                }
                break;
            case R.id.product_details_gouwuche:
                if(!MApplication.getInstance().isLoggedIn()){
                    toActivity(LoginActivity.createIntent(this));
                }else{
                    toActivity(ProductDesSelectWindow.createIntent(this,ProductDesSelectWindow.INTENT_FROM_TYPE_CAR, productDes.id));
                }
                break;
            case R.id.product_details_duobao:
                toActivity(FirstDuobaoActivity.createIntent(context,productDes.id));
            default:
                break;
        }
    }

    private void initPlayer(){
        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        videoPlayer.setUp(source1, true, "测试视频");
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.defult_head);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.startWindowFullscreen(context, false, true);
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ///不需要屏幕旋转
        videoPlayer.setNeedOrientationUtils(false);

        videoPlayer.startPlayLogic();
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
        switch (requestCode){
            case REQUEST_CODE_DES:
                productDes =GsonUtil.GsonToBean(resultData,ProductDes.class);
                initData();
                break;
        }
    }

    @Override
    public void onHttpError(int requestCode, Exception e, String message) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
///       不需要回归竖屏
//        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            videoPlayer.getFullscreenButton().performClick();
//            return;
//        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}
