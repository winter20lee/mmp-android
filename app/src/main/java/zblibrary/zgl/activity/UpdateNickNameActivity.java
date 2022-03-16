
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.StringUtil;

/**更换昵称
 */
public class UpdateNickNameActivity extends BaseActivity implements OnClickListener, OnBottomDragListener, OnHttpResponseListener {
	public static final String TAG = "UpdateNickNameActivity";
	/**获取启动UserActivity的intent
	 * @param context
	 * @return
	 */
	private String nickname;
	public static Intent createIntent(Context context) {
		return new Intent(context, UpdateNickNameActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_nickname_activity, this);

		initView();
		initData();
		initEvent();
	}

	private EditText update_nick_name;
	private TextView update_nick_save;
	@Override
	public void initView() {
		update_nick_name = findView(R.id.update_nick_name);
		update_nick_save = findView(R.id.update_nick_save);
		findView(R.id.update_nick_cancle,this);
	}


	@Override
	public void initData() {//必须调用
		update_nick_name.setText(MApplication.getInstance().getCurrentUserNickName());
	}

	@Override
	public void initEvent() {//必须调用
		update_nick_save.setOnClickListener(this);
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
		case R.id.update_nick_save:
			showProgressDialog("");
			nickname = update_nick_name.getText().toString();
			if(StringUtil.isNotEmpty(nickname,true)){
				HttpRequest.updateUserNickname(nickname, 0, new OnHttpResponseListenerImpl(this));
			}
			break;
		case R.id.update_nick_cancle:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		dismissProgressDialog();
		if(resultCode==1000){
			showShortToast("Modified successfully");
			MApplication.getInstance().saveCurrentUserNickName(nickname);
			finish();
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		dismissProgressDialog();
		showShortToast(message);
	}
}