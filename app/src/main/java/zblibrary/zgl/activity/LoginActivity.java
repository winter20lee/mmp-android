package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import zblibrary.zgl.R;
import zblibrary.zgl.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.LoginEvent;
import zblibrary.zgl.model.User;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.CodeCount;
import zblibrary.zgl.view.VerificationCodeInputView;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.EditTextUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener, OnHttpResponseListener, TextWatcher, OnBottomDragListener {
	private static final String TAG = "LoginActivity ";
	private static final int REQUEST_CODE_CODE = 30000;
	private static final int REQUEST_CODE_LOGIN = REQUEST_CODE_CODE+1;
	private TextView login_message,login_message_time;
	private CodeCount count;
	private EditText login_phone,login_password;
	private VerificationCodeInputView login_code;
	private LinearLayout login_phone_num,login_ver_code;
	public static Intent createIntent(Context context) {
		return new Intent(context, LoginActivity.class);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity,this);
		initView();
		initData();
		initEvent();
	}


	@Override
	public void initView() {//必须调用
		login_message = findView(R.id.login_message);
		login_phone = findView(R.id.login_phone);
		login_password = findView(R.id.login_password);
		login_message_time = findView(R.id.login_message_time);
		login_phone_num = findView(R.id.login_phone_num);
		login_ver_code = findView(R.id.login_ver_code);
	}


	@Override
	public void initData() {//必须调用
		count = new CodeCount(login_message_time, 60000, 1000);
	}
	@Override
	public void initEvent() {//必须调用
		login_message.setOnClickListener(this);
		login_phone.addTextChangedListener(this);
		login_password.addTextChangedListener(this);
//		login_code.setOnCompleteListener(new VerificationCodeInputView.Listener() {
//			@Override
//			public void onComplete(String content) {
//					showProgressDialog("");
//					HttpRequest.loginByVerifyCode(login_phone.getText().toString(),login_code.getText().toString(),
//							REQUEST_CODE_LOGIN,new OnHttpResponseListenerImpl(LoginActivity.this));
//			}
//		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (count != null) {
			count.cancel();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.login_message:
//				if(sendVerifyCode()){
//					login_phone_num.setVisibility(View.GONE);
//					login_ver_code.setVisibility(View.VISIBLE);
//					login_ver_code.requestFocus();
//				}
				//登录
				HttpRequest.loginByPassword(login_phone.getText().toString(),login_password.getText().toString(),
						REQUEST_CODE_LOGIN,new OnHttpResponseListenerImpl(this));
				break;
		}
	}

	private boolean sendVerifyCode(){
		if(EditTextUtil.isInputedCorrect(this,login_phone,EditTextUtil.TYPE_PHONE,"")){
			HttpRequest.sendVerifyCode(login_phone.getText().toString(),REQUEST_CODE_CODE,new OnHttpResponseListenerImpl(this));
			count.start();
			return true;
		}
		return false;
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {
			return;
		}
		if(login_phone_num.getVisibility() == View.GONE){
			login_phone_num.setVisibility(View.VISIBLE);
			login_ver_code.setVisibility(View.GONE);
			return;
		}
		finish();
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		dismissProgressDialog();
		Log.d(TAG,resultData);
		if(requestCode == REQUEST_CODE_CODE){
			showShortToast("验证码已发送");
		}else if(requestCode == REQUEST_CODE_LOGIN){
			if(StringUtil.isEmpty(resultData)){
				showShortToast("登陆失败");
				return;
			}
			User user = GsonUtil.GsonToBean(resultData,User.class);
			if(user==null){
				showShortToast("登陆失败");
				return;
			}
			MApplication.getInstance().saveCurrentUser(user);
			showShortToast("登陆成功");
			EventBus.getDefault().post(new LoginEvent());
			finish();
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		dismissProgressDialog();
		Log.d(TAG,e.getMessage());
		showShortToast(message);
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		if(StringUtil.isNotEmpty(login_phone.getText().toString(),true) &&
				StringUtil.isNotEmpty(login_password.getText().toString(),true)){
			login_message.setEnabled(true);
		}else{
			login_message.setEnabled(false);
		}
	}
}
