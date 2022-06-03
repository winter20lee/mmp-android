package zblibrary.zgl.activity;

import static zuo.biao.library.interfaces.Presenter.INTENT_ID;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
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
import android.widget.AdapterView;
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
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.SystemBarTintManager;
import zuo.biao.library.ui.AlertDialog;
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
        if (videoId == 0) {
            CommonUtil.showShortToast(this,"视频不存在！");
            return;
        }
        if(!MApplication.getInstance().isVip() && MApplication.getInstance().playCount>0){
            showDialog();
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
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);
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
            if(!MApplication.getInstance().isVip() && MApplication.getInstance().playCount>0){
                videoPlayer.getStartButton().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog();
                    }
                });
            }else{
                videoPlayer.startPlayLogic();
            }

        }
        if(productDes.isfav == 1){
            play_video_like.setImageResource(R.mipmap.collection_s);
            play_video_like.setTag(true);
        }else{
            play_video_like.setImageResource(R.mipmap.collection);
            play_video_like.setTag(false);
        }
        product_details_name.setText(productDes.name);
        product_details_jianjie.setText("# "+productDes.tag);
        product_details_price.setText(productDes.playCnt+"次播放");
        ((TextView)findViewById(R.id.product_details_name_jianjie)).setText(productDes.name);
        if(StringUtil.isEmpty(productDes.length)){
            findViewById(R.id.product_details_long_jianjie).setVisibility(View.GONE);
        }else{
            ((TextView)findViewById(R.id.product_details_long_jianjie)).setText("时长："+productDes.length);
        }
        ((TextView)findViewById(R.id.product_details_play_jianjie)).setText(productDes.playCnt+"次播放");

        ((TextView)findViewById(R.id.product_details_tag_jianjie)).setText("标签："+"# "+productDes.tag);
        if(productDes.videoActor!=null){
            findViewById(R.id.product_details_actor).setVisibility(View.VISIBLE);
            LayoutInflater mInflater = LayoutInflater.from(this);
            TextView tv = (TextView) mInflater.inflate(R.layout.suosou_item,msearch_history, false);
            tv.setText(productDes.videoActor.name);
            msearch_history.addView(tv);
            play_video_name.setText(productDes.videoActor.name);
            ((TextView)findViewById(R.id.product_details_act_jianjie)).setText("演员："+productDes.videoActor.name);
            play_video_birthday.setText("生日："+productDes.videoActor.birthday);
            play_video_shengao.setText("身高："+productDes.videoActor.height);
            play_video_sanwei.setText("三围："+productDes.videoActor.bwh);
            GlideUtil.load(this,productDes.videoActor.img,play_video_head,R.mipmap.defult_head_round);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            play_video_recomm.setLayoutManager(layoutManager);
            actorRecommendAdapter = new ActorRecommendAdapter(this);
            actorRecommendAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent it  = PlayVideoDetailsActivity.createIntent(PlayVideoDetailsActivity.this,productDes.actorVideoList.get(i).id);
                    startActivity(it);
                }
            });
            play_video_recomm.setAdapter(actorRecommendAdapter);
            actorRecommendAdapter.refresh(productDes.actorVideoList);

            if(StringUtil.isEmpty(productDes.videoActor.director)){
                findViewById(R.id.product_details_actor_jianjie).setVisibility(View.GONE);
            }else{
                ((TextView)findViewById(R.id.product_details_actor_jianjie)).setText("导演："+productDes.videoActor.director);
            }
            if(StringUtil.isEmpty(productDes.videoActor.bango)){
                findViewById(R.id.product_details_fanhao_jianjie).setVisibility(View.GONE);
            }else{
                ((TextView)findViewById(R.id.product_details_fanhao_jianjie)).setText("番号："+productDes.videoActor.bango);
            }
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
        findViewById(R.id.product_details_close_jianjie).setOnClickListener(this);
        findViewById(R.id.product_details_jj).setOnClickListener(this);
    }

    private void showDialog(){
        new AlertDialog(this,"","今日的播放次数已使用完，充值VIP获取无限播放次数","去充值",
                "取消",0,new AlertDialog.OnDialogButtonClickListener(){

            @Override
            public void onDialogButtonClick(int requestCode, boolean isPositive) {
                if(isPositive){
                    Intent it = MainTabActivity.createIntent(PlayVideoDetailsActivity.this);
                    CommonUtil.toActivity(PlayVideoDetailsActivity.this,it);
                }else{

                }
            }
        }).show();
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
                            new AlertDialog(this,"","今日的下载次数已使用完，充值VIP获取无限下载次数","去充值",
                                    "取消",0,new AlertDialog.OnDialogButtonClickListener(){

                                @Override
                                public void onDialogButtonClick(int requestCode, boolean isPositive) {
                                    if(isPositive){
                                        Intent it = MainTabActivity.createIntent(PlayVideoDetailsActivity.this);
                                        CommonUtil.toActivity(PlayVideoDetailsActivity.this,it);
                                    }else{

                                    }
                                }
                            }).show();
                            return;
                        }
                        HttpRequest.getDownload(productDes.id,0,new OnHttpResponseListenerImpl(this));
                    }
                    MyDownFilesFragment.TasksManager.getImpl().addTask(productDes.id,productDes.name,productDes.coverUrl,productDes.videoUrl);
                    EventBus.getDefault().post(new RefreshDownEvent(true));
                    CommonUtil.showShortToast(this,"已加入下载队列");
                    MApplication.getInstance().downloadCount++;
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
            case R.id.product_details_close_jianjie:
                findViewById(R.id.product_details_ll_jianjie).setVisibility(View.GONE);
                break;
            case R.id.product_details_jj:
                findViewById(R.id.product_details_ll_jianjie).setVisibility(View.VISIBLE);
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
                catalogBean.id = productDes.catalogSecondLevelId;
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
                FirstCategoryView receivingAddressView1 = new FirstCategoryView(this,play_video_cnxh,productDes.tag);
                play_video_cnxh.addView(receivingAddressView1.createView());
                receivingAddressView1.bindView(secondCategory);
                break;
            case REQUEST_ADD_FAV:
                play_video_like.setImageResource(R.mipmap.collection_s);
                play_video_like.setTag(true);
                CommonUtil.showShortToast(this,"已收藏");
                break;
            case REQUEST_CANCLE_FAV:
                play_video_like.setImageResource(R.mipmap.collection);
                play_video_like.setTag(false);
                CommonUtil.showShortToast(this,"已取消");
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
                .setAutoFullWithSize(true)
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
