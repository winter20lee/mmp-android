package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.MyNumberAdapter;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.MyNumber;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.GsonUtil;

public class MyNumberActivity extends BaseActivity implements OnHttpResponseListener {

	/**启动BottomMenuWindow的Intent
	 * @param context
	 * @param goodsId
	 * @return
	 */
	public static Intent createIntent(Context context, long goodsId) {
		return new Intent(context, MyNumberActivity.class).
				putExtra(INTENT_ID, goodsId);
	}

	/**启动BottomMenuWindow的Intent
	 * @param context
	 * @param orderNo
	 * @return
	 */
	public static Intent createIntent(Context context, String orderNo) {
		return new Intent(context, MyNumberActivity.class).
				putExtra(INTENT_ID, orderNo);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_number_activity);
		initView();
		initData();
		initEvent();
	}

	private GridView my_number_gridview;
	private TextView my_number,my_number_purchasing;
	@Override
	public void initView() {//必须调用
		my_number_gridview = findView(R.id.my_number_gridview);
		my_number = findView(R.id.my_number);
		my_number_purchasing = findView(R.id.my_number_purchasing);
	}

	private MyNumberAdapter adapter;
	@Override
	public void initData() {//必须调用
		intent = getIntent();
		long goodsId = intent.getLongExtra(INTENT_ID,0);
		if(goodsId!=0){
			HttpRequest.getBuyLuck(goodsId,0, new OnHttpResponseListenerImpl(this));
		}else{
			String orderNo = intent.getStringExtra(INTENT_ID);
			HttpRequest.getBuyLuckNum(orderNo,0, new OnHttpResponseListenerImpl(this));
		}


		adapter = new MyNumberAdapter(this);
		my_number_gridview.setAdapter(adapter);

	}

	@Override
	public void initEvent() {//必须调用
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		try {
			MyNumber lootBottomGrid = GsonUtil.GsonToBean(resultData, MyNumber.class);
			List<MyNumber.LuckNumberListModel> nameList = lootBottomGrid.luckNumberList;
			adapter.refresh(nameList);
			my_number.setText(lootBottomGrid.planNum);
			my_number_purchasing.setText(nameList.size()+"");
		}catch (Exception e){

		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {

	}
}