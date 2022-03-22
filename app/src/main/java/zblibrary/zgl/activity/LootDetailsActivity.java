package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.BannerViewPagerHolder;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.FirstBanner;
import zblibrary.zgl.model.LootDes;
import zblibrary.zgl.model.PlanePart;
import zblibrary.zgl.model.PlaneWin;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.MyScrollView;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;
import zuo.biao.library.util.WebViewSettingsUtils;

/**夺宝详情
 */
public class LootDetailsActivity extends BaseActivity implements OnClickListener, OnBottomDragListener
        , OnHttpResponseListener,MyScrollView.OnMyScrollListener {
    public static final String TAG = "LootDetailsActivity";
    public static final int REQUEST_CODE_DES = 20001;
    public static final int REQUEST_PLANE_WIN = 20002;
    public static final int REQUEST_PLANE_PART = 20003;
    private MZBannerView mzBannerView;
    private long goodsId = 0;
    private String lootPlanId = "";
    private WebView product_details_content;
    private TextView product_details_name,tv_loot_details_now_all,
            tv_loot_details_last_all,tv_good_spe,tv_price,tv_real_price,
            tv_loot_progress,tv_loot_details_num,tv_loot_look,loot_details_banner_bqcy,loot_details_banner_wqjx,
            loot_details_banner_spxq,loot_details_banner_top,product_details_state,tv_loot_yjx_user,tv_loot_yjx_num,tv_loot_yjx_lucknum,tv_loot_yjx_time;
    private LootDes lootDes;
    private List<FirstBanner> firstBannerList = new ArrayList<>();
    private LinearLayout product_details_duobao,product_details_buy,loot_details_top;
    private View loot_details_banner_bqcy_,loot_details_banner_wqjx_,loot_details_banner_spxq_,loot_details_banner_top_;
    private MyScrollView loot_details_sv;
    private int pos_top,pos_bqcy,pos_wqjx,pos_spxq;
    private ImageView iv_loot_yjx_iv;
    private PlanePart planePart;
    public static Intent createIntent(Context context, long productId) {
        return new Intent(context, LootDetailsActivity.class).putExtra(INTENT_ID, productId);
    }

    public static Intent createIntent(Context context, String lootPlanId) {
        return new Intent(context, LootDetailsActivity.class).putExtra(INTENT_ID, lootPlanId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loot_details_activity, this);
        intent = getIntent();
        goodsId = intent.getLongExtra(INTENT_ID, goodsId);
        lootPlanId = intent.getStringExtra(INTENT_ID);
        if (goodsId == 0 && StringUtil.isEmpty(lootPlanId)) {
            finishWithError("Goods does not exist");
            return;
        }
        initView();
        initData();
        initEvent();
        if(goodsId != 0 ){
            HttpRequest.getLootDes(goodsId,REQUEST_CODE_DES, new OnHttpResponseListenerImpl(this));
        }else{
            HttpRequest.getLootWinDes(Integer.parseInt(lootPlanId),REQUEST_CODE_DES, new OnHttpResponseListenerImpl(this));
        }
    }

    @Override
    public void initView() {//必须调用
        mzBannerView = findView(R.id.product_details_banner);
        product_details_name = findView(R.id.product_details_name);
        product_details_content = findView(R.id.product_details_content);
        product_details_duobao = findView(R.id.product_details_duobao);
        product_details_buy = findView(R.id.product_details_buy);
        tv_loot_details_now_all = findView(R.id.tv_loot_details_now_all);
        tv_loot_details_last_all = findView(R.id.tv_loot_details_last_all);
        tv_good_spe = findView(R.id.tv_good_spe);
        tv_price  = findView(R.id.tv_price);
        tv_real_price = findView(R.id.tv_real_price);
        tv_loot_progress  = findView(R.id.tv_loot_progress);
        tv_loot_details_num = findView(R.id.tv_loot_details_num);
        tv_loot_look = findView(R.id.tv_loot_look);
        product_details_state = findView(R.id.product_details_state);

        loot_details_top = findView(R.id.loot_details_top);

        loot_details_banner_bqcy = findView(R.id.loot_details_banner_bqcy);
        loot_details_banner_wqjx = findView(R.id.loot_details_banner_wqjx);
        loot_details_banner_spxq = findView(R.id.loot_details_banner_spxq);
        loot_details_banner_top = findView(R.id.loot_details_banner_top);

        loot_details_banner_bqcy_ = findView(R.id.loot_details_banner_bqcy_);
        loot_details_banner_wqjx_ = findView(R.id.loot_details_banner_wqjx_);
        loot_details_banner_spxq_ = findView(R.id.loot_details_banner_spxq_);
        loot_details_banner_top_ = findView(R.id.loot_details_banner_top_);

        loot_details_sv = findView(R.id.loot_details_sv);
        iv_loot_yjx_iv = findView(R.id.iv_loot_yjx_iv);
        tv_loot_yjx_user = findView(R.id.tv_loot_yjx_user);
        tv_loot_yjx_num = findView(R.id.tv_loot_yjx_num);
        tv_loot_yjx_lucknum = findView(R.id.tv_loot_yjx_lucknum);
        tv_loot_yjx_time = findView(R.id.tv_loot_yjx_time);
        WebViewSettingsUtils.setWebView(product_details_content);
    }

    @Override
    public void initData() {//必须调用
        if(lootDes==null)
            return;
        mzBannerView.setPages(getBanner(), (MZHolderCreator<BannerViewPagerHolder>) () -> new BannerViewPagerHolder(true));
        mzBannerView.start();
        product_details_name.setText("\u3000\u3000\u3000\u3000\u3000\u3000"+lootDes.goodsName);

        String good_spe = "";
        if(lootDes.goodsSpecList!=null && lootDes.goodsSpecList.size()>0){
            for (LootDes.GoodsSpecListModel goodsSpecListModel:lootDes.goodsSpecList) {
                good_spe += goodsSpecListModel.name+" "+goodsSpecListModel.value+" ";
            }
        }
        tv_good_spe.setText(good_spe);
        tv_price.setText(StringUtil.changeF2Y(lootDes.unitPrice));
        tv_real_price.setText(getString(R.string.loot_sub_spjz)+" "+StringUtil.changeF2Y(lootDes.price));
        tv_loot_progress.setText(getString(R.string.product_details_sold)+" "+lootDes.lootScheduleShow);
            if(lootDes.participationStatus){
                ((TextView)findView(R.id.loot_details_canyu_text)).setText(getString(R.string.loot_details_yicanyu));
                tv_loot_look.setVisibility(View.VISIBLE);
            }else{
                ((TextView)findView(R.id.loot_details_canyu_text)).setText(getString(R.string.loot_details_no_canyu));
                tv_loot_look.setVisibility(View.GONE);
            }

        product_details_content.loadDataWithBaseURL(null, lootDes.mobileDetail, "text/html", "utf-8", null);
        tv_loot_details_num.setText(String.format(getString(R.string.loot_details_now_canyu), lootDes.planNum));


        //往期揭晓
        HttpRequest.getPlaneWin(lootDes.lootId,1,REQUEST_PLANE_WIN, new OnHttpResponseListenerImpl(this));

        setTopTabSelect(1,false);
        if(StringUtil.isNotEmpty(lootPlanId,true)){
            findView(R.id.ll_loot_yijiexiao).setVisibility(View.VISIBLE);
            GlideUtil.loadCircle(context,lootDes.avatar,iv_loot_yjx_iv);
            tv_loot_yjx_user.setText(lootDes.mobile);
            String planNum = String.format(getString(R.string.loot_details_yijiexiao_num), lootDes.planNum);
            tv_loot_yjx_num.setText(planNum);
            tv_loot_yjx_lucknum.setText(lootDes.winLuckNumber);
            tv_loot_yjx_time.setText(lootDes.gmtFinish);
            tv_good_spe.setText(lootDes.goodsSpec);
            lootDes.status = 3;
        }else{
            //本期参与
            HttpRequest.getPlanePart(lootDes.planeId,1,REQUEST_PLANE_PART, new OnHttpResponseListenerImpl(this));
        }
        product_details_state.setText(setState(lootDes.status));

    }

    @Override
    public void initEvent() {//必须调用
        tv_loot_details_now_all.setOnClickListener(this);
        tv_loot_details_last_all.setOnClickListener(this);
        tv_loot_look.setOnClickListener(this);

        loot_details_banner_top.setOnClickListener(this);
        loot_details_banner_bqcy.setOnClickListener(this);
        loot_details_banner_wqjx.setOnClickListener(this);
        loot_details_banner_spxq.setOnClickListener(this);

        loot_details_sv.addOnScrollListner(this);

        product_details_duobao.setOnClickListener(this);
        product_details_buy.setOnClickListener(this);

        findView(R.id.kefu).setOnClickListener(v -> {
            if(StringUtil.isNotEmpty(MApplication.getInstance().getServiceUrl(),true)){
                toActivity(WebViewActivity.createIntent(context,"Service",MApplication.getInstance().getServiceUrl()));
            }
        });
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
            case R.id.tv_loot_details_now_all:
                toActivity(PlanPartActivity.createIntent(this,lootDes.planeId,lootDes.planNum,planePart.totalCount));
                break;
            case R.id.tv_loot_details_last_all:
                toActivity(PlanWinActivity.createIntent(this,lootDes.lootId));
                break;
            case R.id.tv_loot_look:
                toActivity(MyNumberActivity.createIntent(this,lootDes.planeId));
                break;
            case R.id.loot_details_banner_top:
                setTopTabSelect(1,false);
                break;
            case R.id.loot_details_banner_bqcy:
                setTopTabSelect(2,false);
                break;
            case R.id.loot_details_banner_wqjx:
                setTopTabSelect(3,false);
                break;
            case R.id.loot_details_banner_spxq:
                setTopTabSelect(4,false);
                break;
            case R.id.product_details_duobao:
                if(!MApplication.getInstance().isLoggedIn()){
                    toActivity(LoginActivity.createIntent(this));
                }else{
                    if(goodsId != 0 ){
                        toActivity(LootSubmitOrderActivity.createIntent(this,lootDes));
                    }else {
                        toActivity(LootDetailsActivity.createIntent(context,lootDes.lootId));
                    }
                }
                break;
            case R.id.product_details_buy:
                if(!MApplication.getInstance().isLoggedIn()){
                    toActivity(LoginActivity.createIntent(this));
                }else{
                    toActivity(PlayVideoDetailsActivity.createIntent(this,lootDes.goodsId));
                }
                break;
            default:
                break;
        }
    }

    private List<FirstBanner> getBanner(){
        if(lootDes!=null && lootDes.detailsImage!=null && lootDes.detailsImage.size()>0){
            for(int i=0;i<lootDes.detailsImage.size();i++){
                FirstBanner dataEntry = new FirstBanner();
                dataEntry.imgUrl =  lootDes.detailsImage.get(i);
                firstBannerList.add(dataEntry);
            }
        }
        return firstBannerList;
    }

    @Override
    public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
        switch (requestCode){
            case REQUEST_PLANE_WIN:
                PlaneWin planeWins = GsonUtil.GsonToBean(resultData,PlaneWin.class);
                setPlanWin(planeWins.result);
                break;
            case REQUEST_PLANE_PART:
                findView(R.id.rl_loot_details_bqcy).setVisibility(View.VISIBLE);
                planePart = GsonUtil.GsonToBean(resultData,PlanePart.class);
                setPlanPart(planePart.result);
                break;
            case REQUEST_CODE_DES:
                lootDes =GsonUtil.GsonToBean(resultData, LootDes.class);
                initData();
                break;
        }
    }

    @Override
    public void onHttpError(int requestCode, Exception e, String message) {

    }

    private void setPlanPart(List<PlanePart.ResultModel> planeWins){
        try {
            ImageView iv_loot_details_canyu_1 = findView(R.id.iv_loot_details_canyu_1);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(0).avatar,iv_loot_details_canyu_1);
            iv_loot_details_canyu_1.setVisibility(View.VISIBLE);

            ImageView iv_loot_details_canyu_2 = findView(R.id.iv_loot_details_canyu_2);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(1).avatar,iv_loot_details_canyu_2);
            iv_loot_details_canyu_2.setVisibility(View.VISIBLE);

            ImageView iv_loot_details_canyu_3 = findView(R.id.iv_loot_details_canyu_3);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(2).avatar,iv_loot_details_canyu_3);
            iv_loot_details_canyu_3.setVisibility(View.VISIBLE);

            ImageView iv_loot_details_canyu_4 = findView(R.id.iv_loot_details_canyu_4);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(3).avatar,iv_loot_details_canyu_4);
            iv_loot_details_canyu_4.setVisibility(View.VISIBLE);

            ImageView iv_loot_details_canyu_5 = findView(R.id.iv_loot_details_canyu_5);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(4).avatar,iv_loot_details_canyu_5);
            iv_loot_details_canyu_5.setVisibility(View.VISIBLE);
        }catch (Exception e){
        }
    }

    private void setPlanWin(List<PlaneWin.ResultModel> planeWins){
        try {
            ImageView iv_loot_details_last_canyu_1 = findView(R.id.iv_loot_details_last_canyu_1);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(0).avatar,iv_loot_details_last_canyu_1);
            iv_loot_details_last_canyu_1.setVisibility(View.VISIBLE);
            findView(R.id.iv_loot_details_last_canyu_bot_1).setVisibility(View.VISIBLE);

            ImageView iv_loot_details_last_canyu_2 = findView(R.id.iv_loot_details_last_canyu_2);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(1).avatar,iv_loot_details_last_canyu_2);
            iv_loot_details_last_canyu_2.setVisibility(View.VISIBLE);
            findView(R.id.iv_loot_details_last_canyu_bot_2).setVisibility(View.VISIBLE);

            ImageView iv_loot_details_last_canyu_3 = findView(R.id.iv_loot_details_last_canyu_3);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(2).avatar,iv_loot_details_last_canyu_3);
            iv_loot_details_last_canyu_3.setVisibility(View.VISIBLE);
            findView(R.id.iv_loot_details_last_canyu_bot_3).setVisibility(View.VISIBLE);

            ImageView iv_loot_details_last_canyu_4 = findView(R.id.iv_loot_details_last_canyu_4);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(3).avatar,iv_loot_details_last_canyu_4);
            iv_loot_details_last_canyu_4.setVisibility(View.VISIBLE);
            findView(R.id.iv_loot_details_last_canyu_bot_4).setVisibility(View.VISIBLE);

            ImageView iv_loot_details_last_canyu_5 = findView(R.id.iv_loot_details_last_canyu_5);
            GlideUtil.loadCircle(LootDetailsActivity.this,planeWins.get(4).avatar,iv_loot_details_last_canyu_5);
            iv_loot_details_last_canyu_5.setVisibility(View.VISIBLE);
            findView(R.id.iv_loot_details_last_canyu_bot_5).setVisibility(View.VISIBLE);
        }catch (Exception e){
        }
    }

    private String setState(int state){
        if(state==1){
            return getString(R.string.loot_details_dsx);
        }else if(state==2){
            product_details_state.setBackgroundResource(R.drawable.radius_4_black_shap);
            return getString(R.string.loot_details_dbz);
        }else if(state==3){
            product_details_state.setBackgroundResource(R.drawable.radius_4_yellow_shap);
            return getString(R.string.loot_details_yjs);
        }
        return "";
    }

    private void setTopTabSelect(int pos,boolean isScroll){
        loot_details_banner_top.setSelected(false);
        loot_details_banner_top_.setSelected(false);
        loot_details_banner_bqcy.setSelected(false);
        loot_details_banner_bqcy_.setSelected(false);
        loot_details_banner_wqjx.setSelected(false);
        loot_details_banner_wqjx_.setSelected(false);
        loot_details_banner_spxq.setSelected(false);
        loot_details_banner_spxq_.setSelected(false);
        pos_top=0;
        pos_bqcy = findView(R.id.rl_loot_details_bqcy).getTop();
        pos_wqjx = findView(R.id.rl_loot_details_wqjx).getTop();
        pos_spxq = findView(R.id.loot_details_spxq_ll).getTop();
        switch (pos){
            case 1:
                loot_details_banner_top.setSelected(true);
                loot_details_banner_top_.setSelected(true);
                if(!isScroll){
                    loot_details_sv.smoothScrollTo(0,pos_top);
                    loot_details_top.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                loot_details_banner_bqcy.setSelected(true);
                loot_details_banner_bqcy_.setSelected(true);
                if(!isScroll)
                loot_details_sv.smoothScrollTo(0,pos_bqcy - loot_details_top.getHeight());
                break;
            case 3:
                loot_details_banner_wqjx.setSelected(true);
                loot_details_banner_wqjx_.setSelected(true);
                if(!isScroll)
                loot_details_sv.smoothScrollTo(0,pos_wqjx - loot_details_top.getHeight());
                break;
            case 4:
                loot_details_banner_spxq.setSelected(true);
                loot_details_banner_spxq_.setSelected(true);
                if(!isScroll)
                loot_details_sv.smoothScrollTo(0,pos_spxq-loot_details_top.getHeight());
                break;
        }
    }


    @Override
    public void onScrollStateChanged(MyScrollView view, int state) {
        Log.d(TAG,state+"");
    }

    @Override
    public void onScroll(MyScrollView view, int scrollY) {
        if(scrollY<5){
            loot_details_top.setVisibility(View.VISIBLE);
        }else{
            loot_details_top.setVisibility(View.VISIBLE);
        }
        if(scrollY<pos_bqcy){
            setTopTabSelect(1,true);
        }else if(scrollY<pos_wqjx && scrollY>pos_bqcy){
            setTopTabSelect(2,true);
        }else if(scrollY<pos_spxq && scrollY>pos_wqjx){
            setTopTabSelect(3,true);
        }else{
            setTopTabSelect(4,true);
        }
    }

    @Override
    public void onScrollToTop() {
        loot_details_top.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScrollToBottom() {
        setTopTabSelect(4,true);
    }
}
