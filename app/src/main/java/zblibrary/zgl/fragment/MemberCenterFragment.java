package zblibrary.zgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.OrderActivity;
import zblibrary.zgl.adapter.MemberCardAdapter;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.FirstBanner;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;

/**会员中心
 */
public class MemberCenterFragment extends BaseFragment implements
		OnHttpResponseListener{
	private static final int REQUEST_BANNER = 10000;
	private List<FirstBanner> firstBannerList = new ArrayList<>();
	private Gallery member_center_gallery;
	private ImageView member_center_equity;
	private TextView member_canter_order;
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
		HttpRequest.getFirstBanner(REQUEST_BANNER, new OnHttpResponseListenerImpl(this));
		return view;
	}


	@Override
	public void initView() {//必须调用
		member_center_gallery = findView(R.id.member_center_gallery);
		member_center_equity = findView(R.id.member_center_equity);
		member_canter_order = findView(R.id.member_canter_order);
	}

	@Override
	public void initData() {//必须调用

	}

	@Override
	public void initEvent() {//必须调用
		member_center_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				GlideUtil.load(context,firstBannerList.get(i).imgUrl,member_center_equity);
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
		member_canter_order.setOnClickListener(view -> toActivity(OrderActivity.createIntent(context)));
	}



	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_BANNER:
				firstBannerList.clear();
				firstBannerList.addAll(GsonUtil.jsonToList(resultData,FirstBanner.class));
				//设置适配器
				member_center_gallery.setAdapter(new MemberCardAdapter(context, firstBannerList));
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		showShortToast(message);
	}
}