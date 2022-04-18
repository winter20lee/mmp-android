package zblibrary.zgl.activity;

import static zuo.biao.library.interfaces.Presenter.INTENT_ID;

import zblibrary.zgl.adapter.ActorRecommendAdapter;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.fragment.MyDownFilesFragment;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.DataManager;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.ListByPos;
import zblibrary.zgl.model.SecondCategory;
import zblibrary.zgl.model.PlayVideoDes;
import zblibrary.zgl.model.RefreshDownEvent;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.FirstCategoryView;
import zuo.biao.library.base.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.SystemBarTintManager;
import zuo.biao.library.ui.EmptyRecyclerView;
import zuo.biao.library.ui.FlowLayout;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**视频详情
 */
public class PlayVideoDetailsActivity extends GSYBaseActivityDetail<StandardGSYVideoPlayer> implements OnClickListener, OnBottomDragListener
        , OnHttpResponseListener {
    public static final String TAG = "ProductDetailsActivity";
    public static final int REQUEST_CODE_DES = 10001;
    private static final int REQUEST_MALL_REFRESH = 10002;
    private static final int REQUEST_MALL_LIKE = 10003;
    private static final int REQUEST_PLAY_RECORD = 10004;
    private static final int REQUEST_ADD_FAV = 10005;
    private static final int REQUEST_CANCLE_FAV = 10006;
    private static final int REQUEST_DOWNLOAD_COUNT = 10007;
    private StandardGSYVideoPlayer videoPlayer;
    private long videoId = 0;
    private TextView product_details_name,product_details_price,product_details_jianjie,play_video_name
            ,play_video_birthday,play_video_shengao,play_video_sanwei;
    private PlayVideoDes productDes;
    private FlowLayout msearch_history;
    private LinearLayout play_video_tflx,play_video_cnxh;
    private ImageView play_video_head,play_video_like;
    private EmptyRecyclerView play_video_recomm;
    private ActorRecommendAdapter actorRecommendAdapter;
    public static Intent createIntent(Context context, long productId) {
        return new Intent(context, PlayVideoDetailsActivity.class).putExtra(INTENT_ID, productId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video_details_activity);
        SystemBarTintManager.setStatusBarFull(this);
        Intent intent = getIntent();
        videoId = intent.getLongExtra(INTENT_ID, videoId);
        if(!MApplication.getInstance().isVip() && MApplication.getInstance().playCount>0){
            Toast.makeText(this,"播放超过次数",Toast.LENGTH_SHORT).show();
        }
        if (videoId == 0) {
            CommonUtil.showShortToast(this,"视频不存在！");
            return;
        }
        initView();
        initData();
        initEvent();
        HttpRequest.getVideoDes(videoId,REQUEST_CODE_DES, new OnHttpResponseListenerImpl(this));
        //猜你喜欢
        HttpRequest.getRecommend(videoId,REQUEST_MALL_LIKE, new OnHttpResponseListenerImpl(this));
        if(!MApplication.getInstance().isVip()){
            HttpRequest.getDownloadCnt(REQUEST_DOWNLOAD_COUNT,  new OnHttpResponseListenerImpl(this));
        }

    }

    public void initView() {//必须调用
        videoPlayer = findViewById(R.id.video_player);
        videoPlayer.getBackButton().setVisibility(View.GONE);
        product_details_name = findViewById(R.id.product_details_name);
        product_details_price = findViewById(R.id.product_details_price);
        product_details_jianjie = findViewById(R.id.product_details_jianjie);
        msearch_history =  findViewById(R.id.msearch_history);
        play_video_tflx = findViewById(R.id.play_video_tflx);
        play_video_cnxh = findViewById(R.id.play_video_cnxh);
        play_video_name = findViewById(R.id.play_video_name);
        play_video_birthday = findViewById(R.id.play_video_birthday);
        play_video_shengao = findViewById(R.id.play_video_shengao);
        play_video_sanwei = findViewById(R.id.play_video_sanwei);
        play_video_head = findViewById(R.id.play_video_head);
        play_video_recomm = findViewById(R.id.play_video_recomm);
        play_video_like = findViewById(R.id.play_video_like);
    }

    public void initData() {//必须调用
        if(productDes==null)
            return;
        initVideoBuilderMode();
        boolean isAutoPlay = DataManager.getInstance().getAutoPlayState();
        if(isAutoPlay){
            videoPlayer.startPlayLogic();
        }
        if(productDes.isfav){
            play_video_like.setImageResource(R.mipmap.collection_s);
            play_video_like.setTag(true);
        }else{
            play_video_like.setImageResource(R.mipmap.collection);
            play_video_like.setTag(false);
        }
        product_details_name.setText(productDes.name);
        product_details_jianjie.setText(productDes.tag);
        product_details_price.setText(productDes.playCnt+"次播放");

        if(productDes.videoActor!=null){
            findViewById(R.id.product_details_actor).setVisibility(View.VISIBLE);
            LayoutInflater mInflater = LayoutInflater.from(this);
            TextView tv = (TextView) mInflater.inflate(R.layout.suosou_item,msearch_history, false);
            tv.setText(productDes.videoActor.name);
            msearch_history.addView(tv);
            play_video_name.setText(productDes.videoActor.name);
            play_video_birthday.setText("生日："+productDes.videoActor.birthday);
            play_video_shengao.setText("身高："+productDes.videoActor.height);
            play_video_sanwei.setText("三围："+productDes.videoActor.bwh);
            GlideUtil.load(this,productDes.videoActor.img,play_video_head);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            play_video_recomm.setLayoutManager(layoutManager);
            actorRecommendAdapter = new ActorRecommendAdapter(this);
            play_video_recomm.setAdapter(actorRecommendAdapter);
            actorRecommendAdapter.refresh(productDes.actorVideoList);
        }
        //同分类下
        HttpRequest.getSearch(1,4,productDes.catalogSecondLevelId,"",REQUEST_MALL_REFRESH, new OnHttpResponseListenerImpl(this));
        //播放记录
        HttpRequest.getPlay(videoId,REQUEST_PLAY_RECORD, new OnHttpResponseListenerImpl(this));
    }

    public void initEvent() {//必须调用
        findViewById(R.id.play_video_back).setOnClickListener(this);
        findViewById(R.id.play_video_share).setOnClickListener(this);
        findViewById(R.id.play_video_down).setOnClickListener(this);
        play_video_like.setOnClickListener(this);
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
                if(productDes==null)
                    return;
                break;
            case R.id.play_video_down:
                if(productDes==null)
                    return;
                if(productDes.videoUrl.endsWith(".mp4") || productDes.videoUrl.endsWith(".MP4")){
                    if(!MApplication.getInstance().isVip()){
                        if(MApplication.getInstance().downloadCount>0){
                            CommonUtil.showShortToast(this,"已超过下载次数");
                            return;
                        }
                        HttpRequest.getDownload(productDes.id,0,new OnHttpResponseListenerImpl(this));
                    }
                    MyDownFilesFragment.TasksManager.getImpl().addTask(productDes.id,productDes.name,productDes.coverUrl,productDes.videoUrl);
                    EventBus.getDefault().post(new RefreshDownEvent(true));
                    CommonUtil.showShortToast(this,"已加入下载队列");
                }else{
                    CommonUtil.showShortToast(this,"此视频类型无法下载");
                }

                break;
            case R.id.play_video_like:
                if(productDes==null)
                    return;
                if(play_video_like.getTag()!=null ){
                    boolean tag = (boolean) play_video_like.getTag();
                    if(tag){
                        HttpRequest.getFavCancel(videoId,REQUEST_CANCLE_FAV, new OnHttpResponseListenerImpl(this));
                        return;
                    }
                }
                HttpRequest.getFav(videoId,REQUEST_ADD_FAV, new OnHttpResponseListenerImpl(this));
                break;
            default:
                break;
        }
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
        switch (requestCode){
            case REQUEST_CODE_DES:
                productDes =GsonUtil.GsonToBean(resultData, PlayVideoDes.class);
                initData();
                break;
            case REQUEST_MALL_REFRESH:
                SecondCategory.VideoListBean videoListBean = GsonUtil.GsonToBean(resultData, SecondCategory.VideoListBean.class);
                SecondCategory secondCategory = new SecondCategory();
                secondCategory.videoPageData = videoListBean;
                SecondCategory.VideoCatalogBean catalogBean = new SecondCategory.VideoCatalogBean();
                catalogBean.name = "同分类下";
                secondCategory.videoCatalog = catalogBean;
                FirstCategoryView receivingAddressView = new FirstCategoryView(this,play_video_tflx,false);
                play_video_tflx.addView(receivingAddressView.createView());
                receivingAddressView.bindView(secondCategory);
                break;
            case REQUEST_MALL_LIKE:
                if(StringUtil.isEmpty(resultData)){
                    return;
                }
                ArrayList<SecondCategory.VideoListBean.ResultBean> resultBeanArrayList = (ArrayList<SecondCategory.VideoListBean.ResultBean>)
                        GsonUtil.jsonToList(resultData, SecondCategory.VideoListBean.ResultBean.class);
                secondCategory = new SecondCategory();
                videoListBean = new SecondCategory.VideoListBean();
                secondCategory.videoPageData = videoListBean;
                videoListBean.result = resultBeanArrayList;
                catalogBean = new SecondCategory.VideoCatalogBean();
                catalogBean.name = "猜你喜欢";
                secondCategory.videoCatalog = catalogBean;
                FirstCategoryView receivingAddressView1 = new FirstCategoryView(this,play_video_cnxh);
                play_video_cnxh.addView(receivingAddressView1.createView());
                receivingAddressView1.bindView(secondCategory);
                break;
            case REQUEST_ADD_FAV:
                play_video_like.setImageResource(R.mipmap.collection_s);
                play_video_like.setTag(true);
                break;
            case REQUEST_CANCLE_FAV:
                play_video_like.setImageResource(R.mipmap.collection);
                play_video_like.setTag(false);
                break;
            case REQUEST_DOWNLOAD_COUNT:
                MApplication.getInstance().downloadCount = Integer.parseInt(resultData);
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
    public StandardGSYVideoPlayer getGSYVideoPlayer() {
        return videoPlayer;
    }

    @Override
    public GSYVideoOptionBuilder getGSYVideoOptionBuilder() {
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideUtil.load(this,productDes.coverUrl,imageView);
        String source;
        if(StringUtil.isNotEmpty(productDes.videoUrl,true)){
            String localSource =  FileDownloadUtils.getDefaultSaveFilePath(productDes.videoUrl);
            if(StringUtil.isFilePathExist(localSource)){
                source = localSource;
            }else{
                source = productDes.videoUrl;
            }
        }else{
            source = productDes.videoUrl;
        }
        return new GSYVideoOptionBuilder()
                .setThumbImageView(imageView)
                .setUrl(source)
                .setCacheWithPlay(true)
                .setIsTouchWiget(true)
                //.setAutoFullWithSize(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setFullHideStatusBar(true)
                .setShowFullAnimation(false)//打开动画
                .setNeedLockFull(true)
                .setSeekRatio(1);
    }

    @Override
    public void clickForFullScreen() {

    }

    @Override
    public boolean getDetailOrientationRotateAuto() {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}
