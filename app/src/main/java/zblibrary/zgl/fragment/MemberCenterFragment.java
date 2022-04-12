package zblibrary.zgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.OrderActivity;
import zblibrary.zgl.adapter.BannerViewPagerHolder;
import zblibrary.zgl.adapter.MemberCardAdapter;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.ListByPos;
import zblibrary.zgl.model.MemberCenter;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**会员中心
 */
public class MemberCenterFragment extends BaseFragment implements
		OnHttpResponseListener{
	private static final int REQUEST_BANNER = 10000;
	private static final int REQUEST_MEMBER = 10001;
	private List<ListByPos> listByPos = new ArrayList<>();
	private List<MemberCenter> memberCenters = new ArrayList<>();
	private Gallery member_center_gallery;
	private ImageView member_center_equity,member_canter_head;
	private TextView member_canter_order,center_pay;
	private TextView member_canter_price,member_canter_name;
	private MZBannerView member_center_ad ;
	private MZHolderCreator mzHolderCreator;
	public static MemberCenterFragment createInstance() {
		return new MemberCenterFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.member_center_fragment);
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
		if(mzHolderCreator ==null){
			mzHolderCreator = (MZHolderCreator<BannerViewPagerHolder>) () -> new BannerViewPagerHolder();
		}
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
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				GlideUtil.loadNoLoading(context,memberCenters.get(i).descImg,member_center_equity);
				member_canter_price.setText("¥ "+memberCenters.get(i).discountPrice);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		member_canter_order.setOnClickListener(view -> toActivity(OrderActivity.createIntent(context)));
		center_pay.setOnClickListener(view -> showShortToast("暂未开通"));
	}



	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_BANNER:
				listByPos.clear();
				listByPos.addAll(GsonUtil.jsonToList(resultData,ListByPos.class));
				member_center_ad.setPages(listByPos,mzHolderCreator );
				member_center_ad.start();
				break;
			case REQUEST_MEMBER:
				memberCenters.clear();
				memberCenters.addAll(GsonUtil.jsonToList(resultData,MemberCenter.class));
				//设置适配器
				member_center_gallery.setAdapter(new MemberCardAdapter(context, memberCenters));
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		showShortToast(message);
	}
}