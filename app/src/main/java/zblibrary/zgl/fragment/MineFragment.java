package zblibrary.zgl.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.activity.WatchHistoryActivity;
import zblibrary.zgl.activity.LoginActivity;
import zblibrary.zgl.activity.MainTabActivity;
import zblibrary.zgl.activity.MessageActivity;
import zblibrary.zgl.activity.MyOrderActivity;
import zblibrary.zgl.activity.AllOrdersActivity;
import zblibrary.zgl.activity.PointDestailActivity;
import zblibrary.zgl.activity.ReceivingAddressActivity;
import zblibrary.zgl.activity.UserInfoActivity;
import zblibrary.zgl.activity.WinningGoodsActivity;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.BusinessCount;
import zblibrary.zgl.model.PointsRules;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**我的
 */
public class MineFragment extends BaseFragment implements OnClickListener, OnDialogButtonClickListener , OnHttpResponseListener {
	private ImageView mine_head;
	private TextView mine_name,mine_id,mine_phone,mine_points;
	private final int REQUEST = 12001;
	private final int MESSAGE = 12002;
	public static MineFragment createInstance() {
		return new MineFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.mine_fragment);
		initView();
		initData();
		initEvent();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		//积分
		HttpRequest.getUserPointsRules(REQUEST, new OnHttpResponseListenerImpl(this));
		//消息数量
		HttpRequest.getBusinessCount(MESSAGE, new OnHttpResponseListenerImpl(this));
		initData();
	}

	@Override
	public void initView() {//必须调用
		mine_head = findView(R.id.mine_head);
		mine_name = findView(R.id.mine_name);
		mine_id = findView(R.id.mine_id);
		mine_phone = findView(R.id.mine_phone);
		mine_points = findView(R.id.mine_points);
		((TextView)findView(R.id.mine_points_des)).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
	}

	@Override
	public void initData() {//必须调用
		mine_name.setText(MApplication.getInstance().getCurrentUserNickName());
		mine_phone.setText("Phone:"+MApplication.getInstance().getCurrentUserPhone());
		mine_id.setText("ID:"+MApplication.getInstance().getCurrentUserId());
		if(StringUtil.isNotEmpty(MApplication.getInstance().getCurrentUserAvatar(),true)){
			GlideUtil.loadCircle(context,MApplication.getInstance().getCurrentUserAvatar(),mine_head);
		}else{
			mine_head.setImageResource(R.mipmap.defult_head);
		}
	}


	private void logout() {
		MApplication.getInstance().logout();
		initData();
		((MainTabActivity)getActivity()).selectFragment(0);
	}

	@Override
	public void initEvent() {//必须调用

		findView(R.id.mine_set).setOnClickListener(this);
		findView(R.id.mine_shdz).setOnClickListener(this);
		findView(R.id.mine_exit_iv).setOnClickListener(this);
		findView(R.id.mine_daizhifu).setOnClickListener(this);
		findView(R.id.mine_daifahuo).setOnClickListener(this);
		findView(R.id.mine_daishouhuo).setOnClickListener(this);
		findView(R.id.mine_yiwancheng).setOnClickListener(this);
		findView(R.id.mine_yiguanbi).setOnClickListener(this);
		findView(R.id.mine_gmjl).setOnClickListener(this);
		findView(R.id.mine_zjjl).setOnClickListener(this);
		findView(R.id.mine_znxx).setOnClickListener(this);
		findView(R.id.mine_bzzn).setOnClickListener(this);
		findView(R.id.mine_all).setOnClickListener(this);
		findView(R.id.mine_kefu).setOnClickListener(this);
		findView(R.id.mine_points_des).setOnClickListener(this);
	}

	@Override
	public void onDialogButtonClick(int requestCode, boolean isPositive) {
		if (! isPositive) {
			return;
		}
		switch (requestCode) {
		case 0:
			logout();
			break;
		default:
			break;
		}
	}



	@Override
	public void onClick(View v) {//直接调用不会显示v被点击效果
		switch (v.getId()) {
			case R.id.mine_set:
				if(!MApplication.getInstance().isLoggedIn()){
					toActivity(LoginActivity.createIntent(context));
				}else{
					toActivity(UserInfoActivity.createIntent(context));
				}
				break;
			case R.id.mine_daizhifu:
				toActivity(MyOrderActivity.createIntent(context,1));
				break;
			case R.id.mine_daifahuo:
				toActivity(MyOrderActivity.createIntent(context,2));
				break;
			case R.id.mine_daishouhuo:
				toActivity(MyOrderActivity.createIntent(context,3));
				break;
			case R.id.mine_yiwancheng:
				toActivity(MyOrderActivity.createIntent(context,4));
				break;
			case R.id.mine_yiguanbi:
				toActivity(MyOrderActivity.createIntent(context,5));
				break;
			case R.id.mine_all:
				toActivity(MyOrderActivity.createIntent(context,0));
				break;
			case R.id.mine_kefu:
				if(StringUtil.isNotEmpty(MApplication.getInstance().getServiceUrl(),true)){
					toActivity(WebViewActivity.createIntent(context,"Service",MApplication.getInstance().getServiceUrl()));
				}
				break;
			case R.id.mine_exit_iv:
				new AlertDialog(context, "Log out", "Are you sure to log out？", true, 0, this).show();
				break;
			case R.id.mine_gmjl:
				toActivity(AllOrdersActivity.createIntent(context));
				break;
			case R.id.mine_zjjl:
				toActivity(WinningGoodsActivity.createIntent(context));
				break;
			case R.id.mine_shdz:
				toActivity(ReceivingAddressActivity.createIntent(context));
				break;
			case R.id.mine_znxx:
				if(MApplication.getInstance().isLoggedIn()){
					toActivity(MessageActivity.createIntent(context));
				}else{
					toActivity(LoginActivity.createIntent(context));
				}
				break;
			case R.id.mine_bzzn:
				toActivity(WatchHistoryActivity.createIntent(context));
				break;
			case R.id.mine_points_des:
				toActivity(PointDestailActivity.createIntent(context));
				break;
			default:
				break;
		}
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST:
				if(StringUtil.isNotEmpty(resultData,true)){
					PointsRules pointsRules = GsonUtil.GsonToBean(resultData,PointsRules.class);
					if(pointsRules !=null){
						mine_points.setText(pointsRules.points+"");
						MApplication.getInstance().setPointsRules(pointsRules);
					}
				}
				break;
			case MESSAGE:
				if(StringUtil.isNotEmpty(resultData,true)){
					BusinessCount businessCount = GsonUtil.GsonToBean(resultData,BusinessCount.class);
					if(businessCount !=null ){
						if(businessCount.waitPayCount>0){
							findView(R.id.mine_daizhifu_points).setVisibility(View.VISIBLE);
						}else{
							findView(R.id.mine_daizhifu_points).setVisibility(View.GONE);
						}

						if(businessCount.waitDeliverCount>0){
							findView(R.id.mine_daifahuo_points).setVisibility(View.VISIBLE);
						}else{
							findView(R.id.mine_daifahuo_points).setVisibility(View.GONE);
						}

						if(businessCount.waitSignCount>0){
							findView(R.id.mine_daishouhuo_points).setVisibility(View.VISIBLE);
						}else{
							findView(R.id.mine_daishouhuo_points).setVisibility(View.GONE);
						}
						if(businessCount.allOrderCount>0){
							findView(R.id.mine_gmjl_points).setVisibility(View.VISIBLE);
						}else{
							findView(R.id.mine_gmjl_points).setVisibility(View.GONE);
						}
						if(businessCount.winOrderCount>0){
							findView(R.id.mine_zjjl_points).setVisibility(View.VISIBLE);
						}else{
							findView(R.id.mine_zjjl_points).setVisibility(View.GONE);
						}
						if(businessCount.waitReadMessageCount>0){
							findView(R.id.mine_znxx_points).setVisibility(View.VISIBLE);
						}else{
							findView(R.id.mine_znxx_points).setVisibility(View.GONE);
						}
					}
				}
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {

	}
}