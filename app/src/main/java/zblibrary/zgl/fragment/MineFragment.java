package zblibrary.zgl.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.BingPhoneActivity;
import zblibrary.zgl.activity.LoginActivity;
import zblibrary.zgl.activity.MainTabActivity;
import zblibrary.zgl.activity.MyLikeActivity;
import zblibrary.zgl.activity.SetActivity;
import zblibrary.zgl.activity.UserInfoActivity;
import zblibrary.zgl.activity.WatchHistoryActivity;
import zblibrary.zgl.application.MApplication;
import zuo.biao.library.base.BaseFragment;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

/**我的
 */
public class MineFragment extends BaseFragment implements OnClickListener {
	private ImageView mine_head;
	private TextView mine_name,mine_phone,mine_down_state,mine_mzgycs_,mine_xzcs_,mine_zskf,mine_kftj,mine_left_count,mine_total_count;
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
		initData();
	}

	@Override
	public void initView() {//必须调用
		mine_head = findView(R.id.mine_head);
		mine_name = findView(R.id.mine_name);
		mine_phone = findView(R.id.mine_phone);
		mine_down_state = findView(R.id.mine_down_state);
		mine_mzgycs_ = findView(R.id.mine_mzgycs_);
		mine_xzcs_ = findView(R.id.mine_xzcs_);
		mine_zskf = findView(R.id.mine_zskf);
		mine_kftj = findView(R.id.mine_kftj);
		mine_left_count = findView(R.id.mine_left_count);
		mine_total_count = findView(R.id.mine_total_count);
	}

	@Override
	public void initData() {//必须调用
		mine_name.setText(MApplication.getInstance().getCurrentUserNickName());
		if(StringUtil.isEmpty(MApplication.getInstance().getCurrentUserAvatar())){
			mine_head.setImageResource(R.mipmap.defult_head);
		}else {
			GlideUtil.loadCircle(context,MApplication.getInstance().getCurrentUserAvatar(),mine_head);
		}
		mine_down_state.setText("正在下载（"+ MyDownFilesFragment.TasksManager.getImpl().getDowningCounts()+"）/ " +
				"已下载（"+ MyDownFilesFragment.TasksManager.getImpl().getDownedCounts()+"）");
		if(MApplication.getInstance().isBindUserPhone()){
			mine_phone.setVisibility(View.INVISIBLE);
		}else{
			mine_phone.setVisibility(View.VISIBLE);
		}
		if(MApplication.getInstance().isVip()){
			mine_mzgycs_.setText("无限制");
			mine_xzcs_.setText("无限制");
			mine_zskf.setTextColor(Color.parseColor("#282828"));
			mine_kftj.setTextColor(Color.parseColor("#282828"));
			findView(R.id.mine_zskf_).setVisibility(View.VISIBLE);
			findView(R.id.mine_kftj_).setVisibility(View.VISIBLE);
			mine_total_count.setText("");
			mine_left_count.setText("无限制");
		}else{
			mine_mzgycs_.setText("1");
			mine_xzcs_.setText("1");
			mine_zskf.setTextColor(Color.parseColor("#CCCCCC"));
			mine_kftj.setTextColor(Color.parseColor("#CCCCCC"));
			findView(R.id.mine_zskf_).setVisibility(View.INVISIBLE);
			findView(R.id.mine_kftj_).setVisibility(View.INVISIBLE);
			mine_total_count.setText(" / 1");
			if(MApplication.getInstance().playCount==0){
				mine_left_count.setText((1-MApplication.getInstance().playCount)+"");
			}else{
				mine_left_count.setText("0");
			}
		}
	}


	@Override
	public void initEvent() {//必须调用
		findView(R.id.mine_head,this);
		findView(R.id.mine_set,this);
		findView(R.id.mine_like,this);
		findView(R.id.mine_down,this);
		findView(R.id.mine_history,this);
		findView(R.id.mine_phone,this);
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
			case R.id.mine_phone:
				toActivity(BingPhoneActivity.createIntent(context));
				break;
			default:
				break;
		}
	}
}