package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import com.yanzhenjie.permission.runtime.Permission;
import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.uitl.TConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.Customize;
import zblibrary.zgl.model.StsTokenAccessKey;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.interfaces.OnUpLoadResponseListener;
import zuo.biao.library.manager.SystemBarTintManager;
import zuo.biao.library.manager.UpLoadOssManager;
import zuo.biao.library.ui.BottomMenuWindow;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.DataKeeper;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.PermissionUtils;
import zuo.biao.library.util.StringUtil;

public class UserInfoActivity extends TakePhotoActivity implements View.OnClickListener , OnHttpResponseListener {
	private static final String TAG = "UserInfoActivity";
	private static final int REQUEST_TO_BOTTOM_MENU = 10;
	private static final int REQUEST_TO_BOTTOM_GRID = 20;
	private static final int REQUEST_TOKEN = 50000;
	private static final int REQUEST_UPLOAD = 50001;
	private static final int REQUEST_DEFULT_HEAD = 50002;
	private String picturePath;
	private ImageView mUserInfoHeadpic;
	private TakePhoto takePhoto;
	private File cameraFile;
	private TextView user_info_change_nickname,user_info_userid,user_info_phonenum;
	private ArrayList<Customize> bottomGrids = new ArrayList<>();
	public static Intent createIntent(Context context) {
		return new Intent(context, UserInfoActivity.class);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_activity);
		SystemBarTintManager.setStatusBarMode(this,true);
		initView();
		initData();
		initEvent();
		HttpRequest.getUserDefultHeadList(REQUEST_DEFULT_HEAD,new OnHttpResponseListenerImpl(this));
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	public void initView() {//必须调用
		mUserInfoHeadpic = findViewById(R.id.user_info_headpic);
		user_info_change_nickname = findViewById(R.id.user_info_change_nickname);
		user_info_userid = findViewById(R.id.user_info_userid);
		user_info_phonenum = findViewById(R.id.user_info_phonenum);
	}


	public void initData() {//必须调用
		if(takePhoto==null){
			takePhoto = getTakePhoto();
		}
		user_info_userid.setText(MApplication.getInstance().getCurrentUserId()+"");
		user_info_change_nickname.setText(MApplication.getInstance().getCurrentUserNickName());
		user_info_phonenum.setText(MApplication.getInstance().getCurrentUserPhone());
		if(StringUtil.isNotEmpty(MApplication.getInstance().getCurrentUserAvatar(),true)){
			GlideUtil.loadCircle(UserInfoActivity.this,MApplication.getInstance().getCurrentUserAvatar(),mUserInfoHeadpic);
		}else{
			mUserInfoHeadpic.setImageResource(R.mipmap.defult_head);
		}
	}
	public void initEvent() {//必须调用
		findViewById(R.id.user_info_headpic).setOnClickListener(this);
		user_info_change_nickname.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.user_info_headpic:
				Intent intent = new Intent(this, BottomMenuWindow.class);
				intent.putExtra(BottomMenuWindow.INTENT_TITLE, "");
				intent.putExtra(BottomMenuWindow.INTENT_ITEMS, new String[]{"System head portrait","Mobile album", "Photographing"});
				startActivityForResult(intent, REQUEST_TO_BOTTOM_MENU);
				break;
			case R.id.user_info_change_nickname:
				intent = new Intent(this, UpdateNickNameActivity.class);
				startActivity(intent);
				break;
		}
	}

	@Override
	public void takeSuccess(TResult result) {
		super.takeSuccess(result);
		setPicture(result.getImages().get(0).getOriginalPath());
	}

	/**显示图片
	 * @param path
	 */
	private void setPicture(String path) {
		if (StringUtil.isFilePath(path) == false) {
			Log.e(TAG, "setPicture  StringUtil.isFilePath(path) == false >> showShortToast(找不到图片);return;");
			CommonUtil.showShortToast(this,"Picture not found");
			return;
		}
		picturePath = path;
		GlideUtil.loadCircle(UserInfoActivity.this,picturePath,mUserInfoHeadpic);
		CommonUtil.showProgressDialog(UserInfoActivity.this,"Uploading...");
		if(path.startsWith("http")){
			HttpRequest.updateUserAvatar(picturePath,REQUEST_UPLOAD,new OnHttpResponseListenerImpl(UserInfoActivity.this));
			return;
		}
		if(UpLoadOssManager.isNeedUpdateCredentialProvider()){
			HttpRequest.getUploadToken(REQUEST_TOKEN,new OnHttpResponseListenerImpl(UserInfoActivity.this));
		}else{
			upLoadOssAvatar();
		}
	}

	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		// 指定调用相机拍照后照片的储存路径
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
	 * 从图库获取图片
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
							if(StringUtil.isNotEmpty(MApplication.getInstance().getCurrentUserAvatar(),true)){
								startActivityForResult(SystemHeadActivity.createIntent(this,MApplication.getInstance().getCurrentUserAvatar(),bottomGrids), REQUEST_TO_BOTTOM_GRID);
							}else{
								startActivityForResult(SystemHeadActivity.createIntent(this,"",bottomGrids), REQUEST_TO_BOTTOM_GRID);
							}
							break;
						case 1:
							selectPicFromCamera();//照相
							return;
						case 2:
							selectPicFromLocal();//从图库筛选
							return;
						default:
							break;
					}
				}
				break;
			case TConstant.RC_PICK_PICTURE_FROM_CAPTURE:
				setPicture(cameraFile.getAbsolutePath());
				break;
			case REQUEST_TO_BOTTOM_GRID:
				if (data != null) {
					String url = data.getStringExtra(SystemHeadActivity.RESULT_DATA);
					setPicture(url);
				}
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
			case REQUEST_UPLOAD:
				GlideUtil.loadCircle(UserInfoActivity.this,picturePath,mUserInfoHeadpic);
				MApplication.getInstance().setCurrentUserAvatar(picturePath);
				CommonUtil.dismissProgressDialog(UserInfoActivity.this);
				break;
			case REQUEST_TOKEN:
				StsTokenAccessKey stsTokenAccessKey = GsonUtil.GsonToBean(resultData,StsTokenAccessKey.class);
				UpLoadOssManager.updateCredentialProvider(stsTokenAccessKey.accessKeyId,stsTokenAccessKey.accessKeySecret,stsTokenAccessKey.securityToken,
						stsTokenAccessKey.endpoint,stsTokenAccessKey.bucket);
				upLoadOssAvatar();
				break;
			case REQUEST_DEFULT_HEAD:
				bottomGrids = (ArrayList<Customize>) GsonUtil.jsonToList(resultData, Customize.class);
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
		UpLoadOssManager.getInstance().UploadImage(picturePath, new OnUpLoadResponseListener() {
			@Override
			public void onUploadSuccess(String url) {
				picturePath = url;
				HttpRequest.updateUserAvatar(picturePath,REQUEST_UPLOAD,new OnHttpResponseListenerImpl(UserInfoActivity.this));
			}

			@Override
			public void onUploadFile() {
				CommonUtil.dismissProgressDialog(UserInfoActivity.this);
				picturePath = MApplication.getInstance().getCurrentUserAvatar();
				GlideUtil.loadCircle(UserInfoActivity.this,picturePath,mUserInfoHeadpic);
			}
		});
	}
}
