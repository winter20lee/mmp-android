package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.ReceivingAddress;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.ui.AlertDialog;
import zuo.biao.library.util.StringUtil;

public class AddAndEditAddressActivity extends BaseActivity implements View.OnClickListener, OnHttpResponseListener {
	private static final String TAG = "AddReceivingAddressActivity";
	private static final int REQUEST_CODE_ADD = 60001;
	private static final int REQUEST_CODE_DEL = 60002;
	private static final int REQUEST_CODE_EDIT = 60003;
	private EditText add_address_name;
	private EditText add_address_phone;
	private EditText add_address_add;
	private EditText add_address_des;
	private ImageView add_address_defult;
	private Button add_address_save;
	private TextView add_address_del;
	private boolean isDefault = true;
	private ReceivingAddress.ResultModel resultModel;
	public static Intent createIntent(Context context, ReceivingAddress.ResultModel resultModel) {
		return new Intent(context, AddAndEditAddressActivity.class)
				.putExtra(INTENT_ID,resultModel);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_receiving_address_activity);
		intent = getIntent();
		resultModel = (ReceivingAddress.ResultModel) intent.getSerializableExtra(INTENT_ID);
		initView();
		initData();
		initEvent();
	}


	@Override
	public void initView() {//必须调用
		add_address_name = findView(R.id.add_address_name);
		add_address_phone = findView(R.id.add_address_phone);
		add_address_add = findView(R.id.add_address_add);
		add_address_des = findView(R.id.add_address_des);
		add_address_defult = findView(R.id.add_address_defult);
		add_address_save = findView(R.id.add_address_save);
		add_address_del  = findView(R.id.add_address_del);
		if(resultModel!=null){
			add_address_del.setVisibility(View.VISIBLE);
			tvBaseTitle.setText(getString(R.string.add_address_edit));
		}
	}


	@Override
	public void initData() {//必须调用
		if(resultModel ==null){
			return;
		}
		add_address_name.setText(resultModel.realName);
		add_address_phone.setText(resultModel.contact);
		add_address_add.setText(resultModel.address);
		add_address_des.setText(resultModel.area);
		isDefault = resultModel.isDefault;
		setDefultAdd();
	}
	@Override
	public void initEvent() {//必须调用
		add_address_defult.setOnClickListener(this);
		add_address_save.setOnClickListener(this);
		add_address_del.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.add_address_defult:
				isDefault=!isDefault;
				setDefultAdd();
				break;
			case R.id.add_address_save:
				if(StringUtil.isEmpty(add_address_add.getText().toString())){
					showShortToast(getString(R.string.add_address_address_empty_toast));
					return;
				}else if(StringUtil.isEmpty(add_address_des.getText().toString())){
					showShortToast(getString(R.string.add_address_des_empty_toast));
					return;
				}else if(StringUtil.isEmpty(add_address_phone.getText().toString())){
					showShortToast(getString(R.string.add_address_phone_empty_toast));
					return;
				}else if(StringUtil.isEmpty(add_address_name.getText().toString())){
					showShortToast(getString(R.string.add_address_name_empty_toast));
					return;
				}else{
					if(resultModel==null){
						HttpRequest.addAddress(add_address_add.getText().toString(),add_address_des.getText().toString(),
								add_address_phone.getText().toString(),
								isDefault,add_address_name.getText().toString(),REQUEST_CODE_ADD,new OnHttpResponseListenerImpl(this));
					}else{
						HttpRequest.updateAddress(resultModel.id,
								add_address_add.getText().toString(),add_address_des.getText().toString(),
								add_address_phone.getText().toString(),
								isDefault,add_address_name.getText().toString(),REQUEST_CODE_ADD,new OnHttpResponseListenerImpl(this));
					}
				}
				break;
			case R.id.add_address_del:
				new AlertDialog(context, "Confirm Delete", "Do you want to delete this address?Cannot recover after delete",
						"Confirm delete","Stay on page", 0, new AlertDialog.OnDialogButtonClickListener() {
					@Override
					public void onDialogButtonClick(int requestCode, boolean isPositive) {
						if(isPositive){
							HttpRequest.delAddress(resultModel.id,REQUEST_CODE_DEL,new OnHttpResponseListenerImpl(AddAndEditAddressActivity.this));
						}
					}
				}).show();
				break;
		}
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_CODE_ADD:
			case REQUEST_CODE_EDIT:
				showShortToast("Save success");
				finish();
				break;
			case REQUEST_CODE_DEL:
				showShortToast("Delete the success");
				finish();
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		showShortToast(message);
	}

	private void setDefultAdd(){
		if(isDefault){
			add_address_defult.setImageResource(R.mipmap.address_select);
		}else{
			add_address_defult.setImageResource(R.mipmap.address_unselect);
		}
	}
}
