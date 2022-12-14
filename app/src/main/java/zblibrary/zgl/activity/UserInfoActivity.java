package zblibrary.zgl.activity;

import static zuo.biao.library.manager.UploadUtil.UPLOAD_SUCCESS_CODE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import com.yanzhenjie.permission.runtime.Permission;
import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.uitl.TConstant;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zblibrary.zgl.R;
import zblibrary.zgl.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.UploadAvatar;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.manager.SystemBarTintManager;
import zuo.biao.library.manager.UploadUtil;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.ui.ItemDialog;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.PermissionUtils;
import zuo.biao.library.util.SettingUtil;
import zuo.biao.library.util.StringUtil;

public class UserInfoActivity extends TakePhotoActivity implements View.OnClickListener ,
		OnHttpResponseListener, DatePickerDialog.OnDateSetListener , UploadUtil.OnUploadProcessListener {
	private static final String TAG = "UserInfoActivity";
	private static final int REQUEST_TO_BOTTOM_MENU = 10;
	private static final int REQUEST_INFO = 50003;
	private String picturePath;
	private ImageView mUserInfoHeadpic;
	private TakePhoto takePhoto;
	private File cameraFile;
	private TextView user_info_change_nickname,user_info_userid,user_info_phonenum,user_info_save,user_info_jianjie;
	public static Intent createIntent(Context context) {
		return new Intent(context, UserInfoActivity.class);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_activity);
		SystemBarTintManager.setStatusBarMode(this);
		initView();
		initData();
		initEvent();
	}

	public void initView() {//????????????
		mUserInfoHeadpic = findViewById(R.id.user_info_headpic);
		user_info_change_nickname = findViewById(R.id.user_info_change_nickname);
		user_info_userid = findViewById(R.id.user_info_userid);
		user_info_phonenum = findViewById(R.id.user_info_phonenum);
		user_info_save = findViewById(R.id.user_info_save);
		user_info_jianjie = findViewById(R.id.user_info_jianjie);
	}


	public void initData() {//????????????
		if(takePhoto==null){
			takePhoto = getTakePhoto();
		}
		user_info_userid.setText(MApplication.getInstance().getCurrentUserNickName());
		user_info_change_nickname.setText(MApplication.getInstance().getCurrentUserSex());
		user_info_phonenum.setText(MApplication.getInstance().getCurrentUserBirthday());
		user_info_jianjie.setText(MApplication.getInstance().getCurrentUserPersonal());
		picturePath = MApplication.getInstance().getCurrentUserAvatar();
		if(StringUtil.isNotEmpty(picturePath,true)){
			GlideUtil.loadCircle(this,picturePath,mUserInfoHeadpic);
		}else{
			mUserInfoHeadpic.setImageResource(R.mipmap.defult_head);
		}
	}
	public void initEvent() {//????????????
		findViewById(R.id.user_info_headpic).setOnClickListener(this);
		user_info_change_nickname.setOnClickListener(this);
		user_info_phonenum.setOnClickListener(this);
		user_info_save.setOnClickListener(this);
		user_info_jianjie.setOnClickListener(this);
		findViewById(R.id.user_info_birth_right).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.user_info_headpic:
				Intent intent = new Intent(this, BottomMenuWindow.class);
				intent.putExtra(BottomMenuWindow.INTENT_TITLE, "");
				intent.putExtra(BottomMenuWindow.INTENT_ITEMS, new String[]{"??????", "??????"});
				startActivityForResult(intent, REQUEST_TO_BOTTOM_MENU);
				overridePendingTransition(0, 0);
				break;
			case R.id.user_info_phonenum:
			case R.id.user_info_birth_right:
				initCalendar();
				break;
			case R.id.user_info_change_nickname:
				ItemDialog itemDialog = new ItemDialog(this,new String[]{"???", "???"},"????????????",0,new ItemDialog.OnDialogItemClickListener(){

					@Override
					public void onDialogItemClick(int requestCode, int position, String item) {
						user_info_change_nickname.setText(item);
					}
				});
				itemDialog.show();
				break;
			case R.id.user_info_save:
				CommonUtil.showProgressDialog(UserInfoActivity.this,"????????????????????????...");
				if(StringUtil.isEmpty(picturePath)){
					int sex ;
					if(user_info_change_nickname.getText().toString().equals("???")){
						sex = 1;
					}else{
						sex = 2;
					}
					HttpRequest.updateUserInfo("",user_info_userid.getText().toString(),user_info_phonenum.getText().toString(),sex,
							user_info_jianjie.getText().toString(),REQUEST_INFO,new OnHttpResponseListenerImpl(UserInfoActivity.this));
				} else if(!StringUtil.isEmpty(picturePath) && picturePath.startsWith("http")){
					int sex ;
					if(user_info_change_nickname.getText().toString().equals("???")){
						sex = 1;
					}else{
						sex = 2;
					}
					HttpRequest.updateUserInfo(picturePath,user_info_userid.getText().toString(),user_info_phonenum.getText().toString(),sex,
							user_info_jianjie.getText().toString(),REQUEST_INFO,new OnHttpResponseListenerImpl(UserInfoActivity.this));
				}else{
					upLoadOssAvatar();
				}
				break;
		}
	}

	@Override
	public void takeSuccess(TResult result) {
		super.takeSuccess(result);
		setPicture(result.getImages().get(0).getOriginalPath());
	}

	/**????????????
	 * @param path
	 */
	private void setPicture(String path) {
		if (StringUtil.isFilePath(path) == false) {
			Log.e(TAG, "setPicture  StringUtil.isFilePath(path) == false >> showShortToast(???????????????);return;");
			CommonUtil.showShortToast(this,"Picture not found");
			return;
		}
		picturePath = path;
		GlideUtil.loadCircle(UserInfoActivity.this,picturePath,mUserInfoHeadpic);
	}

	/**
	 * ??????????????????
	 */
	public void selectPicFromCamera() {
		// ????????????????????????????????????????????????
		cameraFile = new File(DataKeeper.imagePath, "photo" + System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		Uri uri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			uri = FileProvider.getUriForFile(this, BaseApplication.getInstance().getApplicationInfo().packageName + ".fileProvider", cameraFile);
		} else {
			uri = Uri.fromFile(cameraFile);
		}
		takePhoto.onPickFromCapture(uri);
	}

	/**
	 * ?????????????????????
	 */
	public void selectPicFromLocal() {
		PermissionUtils.requestPermission(this, new PermissionUtils.PermissionQuestListener() {
			@Override
			public void onGranted() {
				takePhoto.onPickFromGallery();
			}

			@Override
			public void onDenied(List<String> data) {

			}

			@Override
			public boolean isUserDefultDialog() {
				return true;
			}

			@Override
			public PermissionUtils.PermissionDialogMessage onAlwaysDeniedData() {
				PermissionUtils.PermissionDialogMessage permissionDialogMessage = new PermissionUtils.PermissionDialogMessage();
				permissionDialogMessage.title = "Turn on storage permissions";
				permissionDialogMessage.message = "We need to get storage permissions";
				return permissionDialogMessage;
			}

			@Override
			public void onDefultDialogPrositiveClick() {
			}

			@Override
			public void onDefultDialogNegativeClick() {
			}

			@Override
			public void onAlwaysDeniedPermission() {

			}
		}, Permission.READ_EXTERNAL_STORAGE,Permission.WRITE_EXTERNAL_STORAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
			case REQUEST_TO_BOTTOM_MENU:
				if (data != null) {
					switch (data.getIntExtra(BottomMenuWindow.RESULT_ITEM_ID, -1)) {
						case 0:
							selectPicFromCamera();//??????
							break;
						case 1:
							selectPicFromLocal();//???????????????
							return;
						default:
							break;
					}
				}
				break;
			case TConstant.RC_PICK_PICTURE_FROM_CAPTURE:
				setPicture(cameraFile.getAbsolutePath());
				break;
			default:
				break;
		}
	}

	public void onReturnClick(View v) {
		onBackPressed();
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_INFO:
				CommonUtil.dismissProgressDialog(UserInfoActivity.this);
				CommonUtil.showShortToast(this,"????????????");
				MApplication.getInstance().setCurrentUserPersonal(user_info_jianjie.getText().toString());
				MApplication.getInstance().setCurrentUserBirthday(user_info_phonenum.getText().toString());
				MApplication.getInstance().setCurrentUserSex(user_info_change_nickname.getText().toString());
				MApplication.getInstance().setCurrentUserNickName(user_info_userid.getText().toString());
				MApplication.getInstance().setCurrentUserAvatar(picturePath);
				finish();
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		picturePath = MApplication.getInstance().getCurrentUserAvatar();
		GlideUtil.loadCircle(UserInfoActivity.this,picturePath,mUserInfoHeadpic);
		CommonUtil.dismissProgressDialog(UserInfoActivity.this);
	}

	private void upLoadOssAvatar(){

		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(UserInfoActivity.this); //?????????????????????????????????

		Map<String, String> params = new HashMap<String, String>();//??????map??????
		uploadUtil.uploadFile(picturePath, "file1", SettingUtil.IMAGE_BASE_URL+"/upload/UploadServlet", params);
	}

	private void initCalendar(){
		//???????????????????????????????????????????????????????????????
		Calendar calendar = Calendar.getInstance();
		//????????????????????????????????????????????????????????????????????????
		//DatePickerDialog????????????????????????????????????????????????
		DatePickerDialog dialog = new DatePickerDialog(this,this
				,calendar.get(Calendar.YEAR)//??????
				,calendar.get(Calendar.MONTH)//??????
				,calendar.get(Calendar.DAY_OF_MONTH));//??????
		//????????????????????????????????????
		dialog.show();
	}

	@Override
	public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
		String month1;
		if(month+1<10){
			month1 = "0"+(month+1);
		}else{
			month1 = ""+(month+1);
		}
		String dayOfMonth1;
		if(dayOfMonth<10){
			dayOfMonth1 = "0"+dayOfMonth;
		}else{
			dayOfMonth1 = ""+dayOfMonth;
		}

		user_info_phonenum.setText(year+"-"+month1+"-"+dayOfMonth1);
	}

	@Override
	public void onUploadDone(int responseCode, String message) {
		CommonUtil.dismissProgressDialog(UserInfoActivity.this);if(responseCode == UPLOAD_SUCCESS_CODE){
			UploadAvatar uploadAvatar = GsonUtil.GsonToBean(message,UploadAvatar.class);
			picturePath = uploadAvatar.data.images.get(0).oUrl;
			int sex ;
			if(user_info_change_nickname.getText().toString().equals("???")){
				sex = 1;
			}else{
				sex = 2;
			}
			CommonUtil.showProgressDialog(UserInfoActivity.this,"????????????????????????...");
			HttpRequest.updateUserInfo(picturePath,user_info_userid.getText().toString(),user_info_phonenum.getText().toString(),sex,
					user_info_jianjie.getText().toString(),REQUEST_INFO,new OnHttpResponseListenerImpl(UserInfoActivity.this));
		}else{
			CommonUtil.showShortToast(this,message);
		}
		Log.d("---",responseCode+message);
	}

	@Override
	public void onUploadProcess(int uploadSize) {
		Log.d("---",uploadSize+"");
	}

	@Override
	public void initUpload(int fileSize) {
		Log.d("---",fileSize+"");
	}
}
