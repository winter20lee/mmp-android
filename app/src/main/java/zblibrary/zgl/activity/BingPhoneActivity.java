package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import zblibrary.zgl.R;
import zblibrary.zgl.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.LoginEvent;
import zblibrary.zgl.model.User;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.CodeCount;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.EditTextUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class BingPhoneActivity extends BaseActivity implements View.OnClickListener, OnHttpResponseListener, TextWatcher {
	private static final String TAG = "LoginActivity ";
	private static final int REQUEST_CODE_CODE = 30000;
	private static final int REQUEST_CODE_LOGIN = REQUEST_CODE_CODE+1;
	private TextView login_message;
	private CodeCount count;
	private EditText login_phone,login_code;
	private TextView login_login;
	public static Intent createIntent(Context context) {
		return new Intent(context, BingPhoneActivity.class);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.binding_phone_activity);
		initView();
		initData();
		initEvent();
		EventBus.getDefault().register(this);
	}


	@Override
	public void initView() {//必须调用
		//login_message = findView(R.id.login_message);
		login_phone = findView(R.id.login_phone);
		login_code = findView(R.id.login_code);
		login_login = findView(R.id.login_login);
	}


	@Override
	public void initData() {//必须调用
		count = new CodeCount(login_message, 60000, 1000);
	}
	@Override
	public void initEvent() {//必须调用
		//login_message.setOnClickListener(this);
		login_phone.addTextChangedListener(this);
		login_code.addTextChangedListener(this);
		login_login.setOnClickListener(this);
		findView(R.id.login_tologin,this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onLoginEvent(LoginEvent baseEvent){
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (count != null) {
			count.cancel();
		}
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
//			case R.id.login_message:
//				sendVerifyCode();
//				break;
			case R.id.login_login:
//				if(EditTextUtil.isInputedCorrect(this,login_phone,EditTextUtil.TYPE_PHONE,"") && checkCode()){
//					showProgressDialog("");
//					HttpRequest.bindMobile(login_phone.getText().toString(),login_code.getText().toString(),
//							REQUEST_CODE_LOGIN,new OnHttpResponseListenerImpl(this));
//				}
				//注册
				HttpRequest.bindMobile(login_phone.getText().toString(),login_code.getText().toString(),
							REQUEST_CODE_LOGIN,new OnHttpResponseListenerImpl(this));
				break;
			case R.id.login_tologin:
				toActivity(LoginActivity.createIntent(this));
				break;
		}
	}

	private void sendVerifyCode(){
		if(EditTextUtil.isInputedCorrect(this,login_phone,EditTextUtil.TYPE_PHONE,"")){
			HttpRequest.sendVerifyCode(login_phone.getText().toString(),REQUEST_CODE_CODE,new OnHttpResponseListenerImpl(this));
			count.start();
		}
	}

	private boolean  checkCode(){
		String phone = login_code.getText().toString();
		if(StringUtil.isEmpty(phone,true)) {
			showShortToast("Code cannot be empty");
			return false;
		}else if(phone.length() !=6){
			showShortToast("The code number must be 6 digits");
			return false;
		}
		return true;
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		dismissProgressDialog();
		Log.d(TAG,resultData);
		if(requestCode == REQUEST_CODE_CODE){
			showShortToast("验证码已发送");
		}else if(requestCode == REQUEST_CODE_LOGIN){
			if(StringUtil.isEmpty(resultData)){
				showShortToast("注册失败");
				return;
			}
			User user = GsonUtil.GsonToBean(resultData,User.class);
			if(user==null){
				showShortToast("注册失败");
				return;
			}
			MApplication.getInstance().saveCurrentUser(user);
			showShortToast("注册成功");
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
				StringUtil.isNotEmpty(login_code.getText().toString(),true)){
			login_login.setEnabled(true);
		}else{
			login_login.setEnabled(false);
		}
	}
}
