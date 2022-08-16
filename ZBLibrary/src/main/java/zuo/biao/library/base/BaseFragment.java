package zuo.biao.library.base;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import zuo.biao.library.R;
import zuo.biao.library.interfaces.FragmentPresenter;
import zuo.biao.library.util.ButtonUtils;
import zuo.biao.library.util.Log;

/**基础android.support.v4.app.Fragment，通过继承可获取或使用 里面创建的 组件 和 方法
 * @see #context
 * @see #view
 * @see #onCreateView
 * @see #setContentView
 * @see #runUiThread
 * @see #runThread
 * @see #onDestroy
 * @use extends BaseFragment
 */
public abstract class BaseFragment extends Fragment implements FragmentPresenter {
	private static final String TAG = "BaseFragment";

	/**
	 * 添加该Fragment的Activity
	 * @warn 不能在子类中创建
	 */
	protected BaseActivity context = null;
	/**
	 * 该Fragment全局视图
	 * @must 非abstract子类的onCreateView中return view;
	 * @warn 不能在子类中创建
	 */
	protected View view = null;
	/**
	 * 布局解释器
	 * @warn 不能在子类中创建
	 */
	protected LayoutInflater inflater = null;
	/**
	 * 添加这个Fragment视图的布局
	 * @warn 不能在子类中创建
	 */
	@Nullable
	protected ViewGroup container = null;

	private boolean isAlive = false;
	private boolean isRunning = false;
	private static PopupWindow loadingPopup;
	public static View loadingView;
	/**
	 * @must 在非abstract子类的onCreateView中super.onCreateView且return view;
	 */
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
		context = (BaseActivity) getActivity();
		isAlive = true;

		this.inflater = inflater;
		this.container = container;
		View loadingView = context.getLayoutInflater().inflate(R.layout.pop_loading, null);

		return view;
	}

	public  void initLoadingPopup() {
		loadingPopup = new PopupWindow(loadingView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		loadingPopup.setFocusable(true);
		loadingPopup.setClippingEnabled(false);
		loadingPopup.setBackgroundDrawable(new ColorDrawable());
		if (!context.isFinishing()){
			loadingPopup.showAtLocation(context.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		}
	}

	/**
	 * 隐藏加载框
	 */
	public static void hideLoadingPopup() {
		if (loadingPopup != null) {
			loadingPopup.dismiss();
		}
	}

	/**设置界面布局
	 * @warn 最多调用一次
	 * @param layoutResID
	 * @use 在onCreateView后调用
	 */
	public void setContentView(int layoutResID) {
		setContentView(inflater.inflate(layoutResID, container, false));
	}
	/**设置界面布局
	 * @warn 最多调用一次
	 * @param v
	 * @use 在onCreateView后调用
	 */
	public void setContentView(View v) {
		setContentView(v, null);
	}
	/**设置界面布局
	 * @warn 最多调用一次
	 * @param v
	 * @param params
	 * @use 在onCreateView后调用
	 */
	public void setContentView(View v, ViewGroup.LayoutParams params) {
		view = v;
	}


	/**
	 * 可用于 打开activity与fragment，fragment与fragment之间的通讯（传值）等
	 */
	protected Bundle argument = null;
	/**
	 * 可用于 打开activity以及activity之间的通讯（传值）等；一些通讯相关基本操作（打电话、发短信等）
	 */
	protected Intent intent = null;

	/**通过id查找并获取控件，使用时不需要强转
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(int id) {
		return (V) view.findViewById(id);
	}
	/**通过id查找并获取控件，并setOnClickListener
	 * @param id
	 * @param l
	 * @return
	 */
	public <V extends View> V findView(int id, OnClickListener l) {
		V v = findView(id);
		v.setOnClickListener(l);
		return v;
	}
	/**通过id查找并获取控件，使用时不需要强转
	 * @warn 调用前必须调用setContentView
	 * @param id
	 * @return
	 */
	public <V extends View> V findViewById(int id) {
		return findView(id);
	}
	/**通过id查找并获取控件，并setOnClickListener
	 * @param id
	 * @param l
	 * @return
	 */
	public <V extends View> V findViewById(int id, OnClickListener l) {
		return findView(id, l);
	}


	public Intent getIntent() {
		return context.getIntent();
	}

	/**在UI线程中运行，建议用这个方法代替runOnUiThread
	 * @param action
	 */
	public final void runUiThread(Runnable action) {
		if (isAlive() == false) {
			Log.w(TAG, "runUiThread  isAlive() == false >> return;");
			return;
		}
		context.runUiThread(action);
	}
	/**运行线程
	 * @param name
	 * @param runnable
	 * @return
	 */
	public final Handler runThread(String name, Runnable runnable) {
		if (isAlive() == false) {
			Log.w(TAG, "runThread  isAlive() == false >> return null;");
			return null;
		}
		return context.runThread(name + hashCode(), runnable);//name, runnable);同一Activity出现多个同名Fragment可能会出错
	}

	/**展示加载进度条,无标题
	 * @param stringResId
	 */
	public void showProgressDialog(int stringResId){
		if (isAlive() == false) {
			Log.w(TAG, "showProgressDialog  isAlive() == false >> return;");
			return;
		}
		context.showProgressDialog(context.getResources().getString(stringResId));
	}
	/**展示加载进度条,无标题
	 * @param dialogMessage
	 */
	public void showProgressDialog(String dialogMessage){
		if (isAlive() == false) {
			Log.w(TAG, "showProgressDialog  isAlive() == false >> return;");
			return;
		}
		context.showProgressDialog(dialogMessage);
	}
	/**展示加载进度条
	 * @param dialogTitle 标题
	 * @param dialogMessage 信息
	 */
	public void showProgressDialog(String dialogTitle, String dialogMessage){
		if (isAlive() == false) {
			Log.w(TAG, "showProgressDialog  isAlive() == false >> return;");
			return;
		}
		context.showProgressDialog(dialogTitle, dialogMessage);
	}

	/** 隐藏加载进度
	 */
	public void dismissProgressDialog(){
		if (isAlive() == false) {
			Log.w(TAG, "dismissProgressDialog  isAlive() == false >> return;");
			return;
		}
		context.dismissProgressDialog();
	}

	/**打开新的Activity，向左滑入效果
	 * @param intent
	 */
	public void toActivity(Intent intent) {
		toActivity(intent, true);
	}
	/**打开新的Activity
	 * @param intent
	 * @param showAnimation
	 */
	public void toActivity(Intent intent, boolean showAnimation) {
		toActivity(intent, -1, showAnimation);
	}
	/**打开新的Activity，向左滑入效果
	 * @param intent
	 * @param requestCode
	 */
	public void toActivity(Intent intent, int requestCode) {
		toActivity(intent, requestCode, true);
	}
	/**打开新的Activity
	 * @param intent
	 * @param requestCode
	 * @param showAnimation
	 */
	public void toActivity(final Intent intent, final int requestCode, final boolean showAnimation) {
		runUiThread(() -> {
			if (intent == null) {
				Log.w(TAG, "toActivity  intent == null >> return;");
				return;
			}
			if(ButtonUtils.isFastDoubleClick()){
				Log.w(TAG, "toActivity  多次点击无效 >> return;");
				return;
			}
			//fragment中使用context.startActivity会导致在fragment中不能正常接收onActivityResult
			if (requestCode < 0) {
				startActivity(intent);
			} else {
				startActivityForResult(intent, requestCode);
			}
			if (showAnimation) {
				context.overridePendingTransition(R.anim.right_push_in, R.anim.hold);
			} else {
				context.overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
			}
		});
	}
	/**快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
	 * @param stringResId
	 */
	public void showShortToast(int stringResId) {
		if (isAlive() == false) {
			Log.w(TAG, "showProgressDialog  isAlive() == false >> return;");
			return;
		}
		context.showShortToast(stringResId);
	}
	/**快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
	 * @param string
	 */
	public void showShortToast(String string) {
		if (isAlive() == false) {
			Log.w(TAG, "showProgressDialog  isAlive() == false >> return;");
			return;
		}
		context.showShortToast(string);
	}
	/**快捷显示short toast方法，需要long toast就用 Toast.makeText(string, Toast.LENGTH_LONG).show(); ---不常用所以这个类里不写
	 * @param string
	 * @param isForceDismissProgressDialog
	 */
	public void showShortToast(String string, boolean isForceDismissProgressDialog) {
		if (isAlive() == false) {
			Log.w(TAG, "showProgressDialog  isAlive() == false >> return;");
			return;
		}
		context.showShortToast(string, isForceDismissProgressDialog);
	}

	@Override
	public final boolean isAlive() {
		return isAlive && context != null;// & ! isRemoving();导致finish，onDestroy内runUiThread不可用
	}
	@Override
	public final boolean isRunning() {
		return isRunning & isAlive();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "\n onResume <<<<<<<<<<<<<<<<<<<<<<<");
		super.onResume();
		isRunning = true;
		Log.d(TAG, "onResume >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}

	@Override
	public void onPause() {
		Log.d(TAG, "\n onPause <<<<<<<<<<<<<<<<<<<<<<<");
		super.onPause();
		isRunning = false;
		Log.d(TAG, "onPause >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}

	/**销毁并回收内存
	 * @warn 子类如果要使用这个方法内用到的变量，应重写onDestroy方法并在super.onDestroy();前操作
	 */
	@Override
	public void onDestroy() {
		Log.d(TAG, "\n onDestroy <<<<<<<<<<<<<<<<<<<<<<<");
		dismissProgressDialog();
		if (view != null) {
			try {
				view.destroyDrawingCache();
			} catch (Exception e) {
				Log.w(TAG, "onDestroy  try { view.destroyDrawingCache();" +
						" >> } catch (Exception e) {\n" + e.getMessage());
			}
		}

		isAlive = false;
		isRunning = false;
		super.onDestroy();

		view = null;
		inflater = null;
		container = null;

		intent = null;
		argument = null;

		context = null;

		Log.d(TAG, "onDestroy >>>>>>>>>>>>>>>>>>>>>>>>\n");
	}
}