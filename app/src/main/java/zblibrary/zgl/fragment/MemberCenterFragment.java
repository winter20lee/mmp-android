package zblibrary.zgl.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.stx.xhb.androidx.XBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.MainTabActivity;
import zblibrary.zgl.activity.OrderActivity;
import zblibrary.zgl.activity.PlayVideoDetailsActivity;
import zblibrary.zgl.adapter.BannerHolderCreator;
import zblibrary.zgl.adapter.MemberCardAdapter;
import zblibrary.zgl.adapter.PayMethodAdapter;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.ListByPos;
import zblibrary.zgl.model.MemberCenter;
import zblibrary.zgl.model.Pay;
import zblibrary.zgl.model.RefreshDownEvent;
import zblibrary.zgl.model.User;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.ZoomFadePageTransformer;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**会员中心
 */
public class MemberCenterFragment extends BaseFragment implements
		OnHttpResponseListener{
	private static final int REQUEST_BANNER = 10000;
	private static final int REQUEST_MEMBER = 10001;
	private static final int REQUEST_PAY = 10002;
	private static final int REQUEST_PAY_STATE = 10003;
	private static final int REQUEST_CODE_RESULT= 12005;
	private static final int REQUEST_CODE_REFRESH= 12006;
	private List<ListByPos> listByPos = new ArrayList<>();
	private List<MemberCenter> memberCenters = new ArrayList<>();
	private Gallery member_center_gallery;
	private ImageView member_center_equity,member_canter_head;
	private TextView member_canter_order,center_pay;
	private TextView member_canter_price,member_canter_name;
	private XBanner member_center_ad ;
	private int pos;
	PayMethodAdapter payMethodAdapter;
	Pay pay;
	public static MemberCenterFragment createInstance() {
		return new MemberCenterFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.member_center_fragment);
		EventBus.getDefault().register(this);
		initView();
		initData();
		initEvent();
		//banner
		HttpRequest.getListByPos(2,0,REQUEST_BANNER, new OnHttpResponseListenerImpl(this));
		HttpRequest.getMemberShip(REQUEST_MEMBER, new OnHttpResponseListenerImpl(this));
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void initView() {//必须调用
		member_center_gallery = findView(R.id.member_center_gallery);
		member_center_equity = findView(R.id.member_center_equity);
		member_canter_order = findView(R.id.member_canter_order);
		member_canter_price = findView(R.id.member_canter_price);
		member_center_ad = findView(R.id.member_center_ad);
		center_pay = findView(R.id.center_pay);
		member_canter_name = findView(R.id.member_canter_name);
		member_canter_head = findView(R.id.member_canter_head);
	}

	@Override
	public void initData() {//必须调用
		member_canter_name.setText(MApplication.getInstance().getCurrentUserNickName());
		if(MApplication.getInstance().isBindUserPhone()){
			findView(R.id.member_canter_phone).setVisibility(View.GONE);
		}else{
			findView(R.id.member_canter_phone).setVisibility(View.VISIBLE);
		}
		if(StringUtil.isEmpty(MApplication.getInstance().getCurrentUserAvatar())){
			member_canter_head.setImageResource(R.mipmap.defult_head);
		}else {
			GlideUtil.loadCircle(context,MApplication.getInstance().getCurrentUserAvatar(),member_canter_head);
		}
	}

	@Override
	public void initEvent() {//必须调用
		member_center_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				if (position >= memberCenters.size()) {
					position = position % memberCenters.size();
				}
				pos = position;
				GlideUtil.loadNoLoading(context,memberCenters.get(position).descImg,member_center_equity);
				member_canter_price.setText("¥ "+memberCenters.get(position).discountPrice);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		member_canter_order.setOnClickListener(view -> toActivity(OrderActivity.createIntent(context)));
		center_pay.setOnClickListener(view -> {
			showPopuWindow();
		});
		member_center_ad.setOnItemClickListener((banner, model, view, position) -> toActivity(WebViewActivity.createIntent(context,"",((ListByPos)model).link)));
		findView(R.id.center_kefu).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(MApplication.getInstance().getAppInitInfo()!=null && StringUtil.isNotEmpty(MApplication.getInstance().getAppInitInfo().csLink,true)){
					toActivity(WebViewActivity.createIntent(context,"客服",MApplication.getInstance().getAppInitInfo().csLink));
				}
			}
		});
	}



	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_BANNER:
				listByPos.clear();
				listByPos.addAll(GsonUtil.jsonToList(resultData,ListByPos.class));
				member_center_ad.setAutoPlayAble(listByPos.size()>1);
				member_center_ad.setBannerData(listByPos, new BannerHolderCreator());
				member_center_ad.setCustomPageTransformer(new ZoomFadePageTransformer());
				break;
			case REQUEST_MEMBER:
				memberCenters.clear();
				memberCenters.addAll(GsonUtil.jsonToList(resultData,MemberCenter.class));
				//设置适配器
				member_center_gallery.setAdapter(new MemberCardAdapter(context, memberCenters));
				member_center_gallery.setSelection(3);
				break;
			case REQUEST_PAY:
				dismissProgressDialog();
				pay = GsonUtil.GsonToBean(resultData,Pay.class);
				new AlertDialog(getActivity(),"支付提示","是否已完成支付？","我已支付","支付失败",0,new AlertDialog.OnDialogButtonClickListener(){

					@Override
					public void onDialogButtonClick(int requestCode, boolean isPositive) {
						if(isPositive){
							HttpRequest.getCurrentUserInfo(REQUEST_CODE_REFRESH,new OnHttpResponseListenerImpl(MemberCenterFragment.this));
						}else{

						}
					}
				}).show();
				toActivity(WebViewActivity.createIntent(context,"支付",pay.redirectUrl),REQUEST_CODE_RESULT);
				break;
			case REQUEST_PAY_STATE:
				dismissProgressDialog();
				String paystate = GsonUtil.GsonToBean(resultData,String.class);
				if(paystate.equals("paid")){
					showShortToast("支付成功");
					HttpRequest.getCurrentUserInfo(REQUEST_CODE_REFRESH,new OnHttpResponseListenerImpl(MemberCenterFragment.this));
				}else{
					showShortToast("支付失败");
				}
				break;
			case REQUEST_CODE_REFRESH:
				User.UserInfoBean userInfoBean = GsonUtil.GsonToBean(resultData, User.UserInfoBean.class);
				User user = MApplication.getInstance().getCurrentUser();
				user.userInfo = userInfoBean;
				MApplication.getInstance().saveCurrentUser(user);
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		showShortToast(message);
		dismissProgressDialog();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onRefreshEvent(MemberCenter.PaymentMethodMembershipRespListBean paymentMethodMembershipRespListBean){
		List<MemberCenter.PaymentMethodMembershipRespListBean> paymentMethodMembershipRespList = memberCenters.get(pos).paymentMethodMembershipRespList;
		for(MemberCenter.PaymentMethodMembershipRespListBean pay:paymentMethodMembershipRespList){
			if(paymentMethodMembershipRespListBean.paymentMethodId == pay.paymentMethodId){
				pay.isSel = true;
			}else {
				pay.isSel = false;
			}
		}
		payMethodAdapter.notifyDataSetInvalidated();
	}

	private void showPopuWindow(){
		if(memberCenters == null || memberCenters.size()==0){
			return;
		}
		View inflate = LayoutInflater.from(getContext()).inflate(R.layout.pay_pop_list_view, null);
		ImageView pay_pop_close = inflate.findViewById(R.id.pay_pop_close);
		TextView pay_pop_num = inflate.findViewById(R.id.pay_pop_num);
		GridView pay_pop_list = inflate.findViewById(R.id.pay_pop_list);
		Button pay_pop_pay = inflate.findViewById(R.id.pay_pop_pay);
		int discountPrice = memberCenters.get(pos).discountPrice;
		pay_pop_num.setText(discountPrice+"");
		List<MemberCenter.PaymentMethodMembershipRespListBean> paymentMethodMembershipRespList = memberCenters.get(pos).paymentMethodMembershipRespList;
		paymentMethodMembershipRespList.get(0).isSel = true;
		 payMethodAdapter = new PayMethodAdapter(context);
		pay_pop_list.setAdapter(payMethodAdapter);
		payMethodAdapter.refresh(paymentMethodMembershipRespList);
		final PopupWindow popupWindow = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		//测量view 注意这里，如果没有测量  ，下面的popupHeight高度为-2  ,因为LinearLayout.LayoutParams.WRAP_CONTENT这句自适应造成的
		inflate.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		int popupWidth = inflate.getMeasuredWidth();    //  获取测量后的宽度
		int popupHeight = inflate.getMeasuredHeight();  //获取测量后的高度
		int[] location = new int[2];
		// 允许点击外部消失
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//注意这里如果不设置，下面的setOutsideTouchable(true);允许点击外部消失会失效
		popupWindow.setOutsideTouchable(true);   //设置外部点击关闭ppw窗口
		popupWindow.setFocusable(true);
		// 获得位置 这里的v是目标控件，就是你要放在这个v的上面还是下面
		View member_center_pop = findView(R.id.member_center_pop);
		member_center_pop.getLocationOnScreen(location);
		//这里就可自定义在上方和下方了 ，这种方式是为了确定在某个位置，某个控件的左边，右边，上边，下边都可以
		popupWindow.showAtLocation(member_center_pop, Gravity.NO_GRAVITY, (location[0] + member_center_pop.getWidth() / 2) - popupWidth / 2, location[1] - StringUtil.dp2px(context,200));
		pay_pop_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});
		pay_pop_pay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int paymentMethodId = 0;
				for (MemberCenter.PaymentMethodMembershipRespListBean paymentMethodMembershipRespListBean:paymentMethodMembershipRespList) {
					if(paymentMethodMembershipRespListBean.isSel){
						paymentMethodId = paymentMethodMembershipRespListBean.paymentMethodId;
					}
				}
				if(paymentMethodId==0){
					showShortToast("请选择支付方式");
					return;
				}
				popupWindow.dismiss();
				showProgressDialog("支付中...");
				HttpRequest.getPay(paymentMethodId,memberCenters.get(pos).levelCode,REQUEST_PAY,new OnHttpResponseListenerImpl(MemberCenterFragment.this));
			}
		});
	}

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode != RESULT_OK) {
//			return;
//		}
//		switch (requestCode) {
//			case REQUEST_CODE_RESULT:
//				HttpRequest.getPayState(pay.orderNo,REQUEST_PAY_STATE,new OnHttpResponseListenerImpl(MemberCenterFragment.this));
//				break;
//		}
//	}
}