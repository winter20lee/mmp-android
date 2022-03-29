package zblibrary.zgl.activity;

import zblibrary.zgl.fragment.MyDownFilesFragment;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.FirstBanner;
import zblibrary.zgl.model.SecondCategory;
import zblibrary.zgl.model.PlayVideoDes;
import zblibrary.zgl.model.RefreshDownEvent;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.FirstCategoryView;
import zuo.biao.library.base.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.FlowLayout;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**商品详情
 */
public class PlayVideoDetailsActivity extends BaseActivity implements OnClickListener, OnBottomDragListener
        , OnHttpResponseListener {
    public static final String TAG = "ProductDetailsActivity";
    public static final int REQUEST_CODE_DES = 10001;
    private static final int REQUEST_MALL_REFRESH = 10002;
    private static final int REQUEST_MALL_LIKE = 10003;
    private StandardGSYVideoPlayer videoPlayer;
    private long goodsId = 0;
    private TextView product_details_name,product_details_price,product_details_jianjie;
    private PlayVideoDes productDes;
    private List<FirstBanner> firstBannerList = new ArrayList<>();
    private OrientationUtils orientationUtils;
    private FlowLayout msearch_history;
    private LinearLayout play_video_tflx,play_video_cnxh;
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
        //同分类下
        HttpRequest.getFirstMall(1,REQUEST_MALL_REFRESH, new OnHttpResponseListenerImpl(this));
        //猜你喜欢
        HttpRequest.getFirstMall(1,REQUEST_MALL_LIKE, new OnHttpResponseListenerImpl(this));
    }

    @Override
    public void initView() {//必须调用
        videoPlayer = findView(R.id.video_player);
        product_details_name = findView(R.id.product_details_name);
        product_details_price = findView(R.id.product_details_price);
        product_details_jianjie = findView(R.id.product_details_jianjie);
        msearch_history =  findView(R.id.msearch_history);
        play_video_tflx = findView(R.id.play_video_tflx);
        play_video_cnxh = findView(R.id.play_video_cnxh);
    }

    @Override
    public void initData() {//必须调用
        if(productDes==null)
            return;
        initPlayer();
        product_details_name.setText(productDes.name);
        product_details_jianjie.setText(productDes.desc);
        product_details_price.setText(StringUtil.changeF2Y(productDes.price));

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
        findView(R.id.play_video_back,this);
        findView(R.id.play_video_share,this);
        findView(R.id.play_video_down,this);
        findView(R.id.play_video_like,this);
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
            case R.id.play_video_back:
                finish();
                break;
            case R.id.play_video_share:
                break;
            case R.id.play_video_down:
                MyDownFilesFragment.TasksManager.getImpl().addTask("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
                EventBus.getDefault().post(new RefreshDownEvent(true));
                break;
            case R.id.play_video_like:
                break;
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
                productDes =GsonUtil.GsonToBean(resultData, PlayVideoDes.class);
                initData();
                break;
            case REQUEST_MALL_REFRESH:
                SecondCategory secondCategory = GsonUtil.GsonToBean(resultData, SecondCategory.class);
                FirstCategoryView receivingAddressView = new FirstCategoryView(context,play_video_tflx);
                play_video_tflx.addView(receivingAddressView.createView());
                receivingAddressView.setTitle("同分类下");
                receivingAddressView.bindView(secondCategory);
                break;
            case REQUEST_MALL_LIKE:
                SecondCategory secondCategory1 = GsonUtil.GsonToBean(resultData, SecondCategory.class);
                FirstCategoryView receivingAddressView1 = new FirstCategoryView(context,play_video_cnxh);
                play_video_cnxh.addView(receivingAddressView1.createView());
                receivingAddressView1.setTitle("猜你喜欢");
                receivingAddressView1.bindView(secondCategory1);
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
