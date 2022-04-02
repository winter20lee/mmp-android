package zblibrary.zgl.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.LoginActivity;
import zblibrary.zgl.activity.MainTabActivity;
import zblibrary.zgl.activity.MyLikeActivity;
import zblibrary.zgl.activity.SetActivity;
import zblibrary.zgl.activity.UserInfoActivity;
import zblibrary.zgl.activity.WatchHistoryActivity;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.ui.AlertDialog.OnDialogButtonClickListener;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**我的
 */
public class MineFragment extends BaseFragment implements OnClickListener, OnDialogButtonClickListener , OnHttpResponseListener {
	private ImageView mine_head;
	private TextView mine_name,mine_phone,mine_down_state;
	private final int REQUEST = 12001;
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
		initData();
	}

	@Override
	public void initView() {//必须调用
		mine_head = findView(R.id.mine_head);
		mine_name = findView(R.id.mine_name);
		mine_phone = findView(R.id.mine_phone);
		mine_down_state = findView(R.id.mine_down_state);
	}

	@Override
	public void initData() {//必须调用
		mine_name.setText(MApplication.getInstance().getCurrentUserNickName());
		mine_head.setImageResource(R.mipmap.defult_head);
		mine_down_state.setText("正在下载（"+ MyDownFilesFragment.TasksManager.getImpl().getDowningCounts()+"）/ " +
				"已下载（"+ MyDownFilesFragment.TasksManager.getImpl().getDownedCounts()+"）");

	}


	private void logout() {
		MApplication.getInstance().logout();
		initData();
		((MainTabActivity)getActivity()).selectFragment(0);
	}

	@Override
	public void initEvent() {//必须调用
		findView(R.id.mine_head,this);
		findView(R.id.mine_set,this);
		findView(R.id.mine_like,this);
		findView(R.id.mine_down,this);
		findView(R.id.mine_history,this);
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
			case R.id.mine_head:
				if(!MApplication.getInstance().isLoggedIn()){
					toActivity(LoginActivity.createIntent(context));
				}else{
					toActivity(UserInfoActivity.createIntent(context));
				}
				break;
			case R.id.mine_like:
				if(!MApplication.getInstance().isLoggedIn()){
					toActivity(LoginActivity.createIntent(context));
				}else{
					toActivity(MyLikeActivity.createIntent(context));
				}
				break;
			case R.id.mine_down:
				((MainTabActivity)getActivity()).showDownFile();
				break;
			case R.id.mine_history:
				if(!MApplication.getInstance().isLoggedIn()){
					toActivity(LoginActivity.createIntent(context));
				}else{
					toActivity(WatchHistoryActivity.createIntent(context));
				}
				break;
			case R.id.mine_set:
				toActivity(SetActivity.createIntent(context));
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

				}
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {

	}

}