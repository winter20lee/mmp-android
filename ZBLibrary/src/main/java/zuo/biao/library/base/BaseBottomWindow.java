
package zuo.biao.library.base;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AnimationUtils;

import zuo.biao.library.R;
import zuo.biao.library.util.Log;

/**基础底部弹出界面Activity
 * @warn 不要在子类重复这个类中onCreate中的代码
 * @use extends BaseBottomWindow
 */
public abstract class BaseBottomWindow extends BaseActivity {
		private static final String TAG = "BaseBottomWindow";

	public static final String INTENT_ITEMS = "INTENT_ITEMS";
	public static final String INTENT_ITEM_IDS = "INTENT_ITEM_IDS";
	public static final String INTENT_ITEM_TITLE = "INTENT_ITEM_TITLE";
	public static final String INTENT_ITEM_TYPE = "INTENT_ITEM_TYPE";

	public static final String RESULT_TITLE = "RESULT_TITLE";
	public static final String RESULT_ITEM = "RESULT_ITEM";
	public static final String RESULT_ITEM_ID = "RESULT_ITEM_ID";

	protected View vBaseBottomWindowRoot;//子Activity全局背景View
	@Override
	public void initView() {//必须调用
		enterAnim = exitAnim = R.anim.null_anim;

		vBaseBottomWindowRoot = findView(R.id.vBaseBottomWindowRoot);

		vBaseBottomWindowRoot.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_window_enter));
	}

	@Override
	public void initData() {//必须调用

	}

	/**
	 * 设置需要返回的结果
	 */
	protected abstract void setResult();
	@Override
	public void initEvent() {//必须调用

		//			vBaseBottomWindowRoot.setOnClickListener(new OnClickListener() {
		//
		//				@Override
		//				public void onClick(View v) {
		//					finish();
		//				}
		//			});

	}


	@Override
	public void onForwardClick(View v) {
		setResult();
		finish();
	}


	@SuppressLint("HandlerLeak")
	public Handler exitHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			BaseBottomWindow.super.finish();
		}
	};


	public boolean isExit = false;
	/**带动画退出,并使退出事件只响应一次
	 */
	@Override
	public void finish() {
		Log.d(TAG, "finish >>> isExit = " + isExit);
		if (isExit) {
			return;
		}
		isExit = true;

		vBaseBottomWindowRoot.startAnimation(AnimationUtils.loadAnimation(context, R.anim.bottom_window_exit));
		vBaseBottomWindowRoot.setVisibility(View.GONE);

		exitHandler.sendEmptyMessageDelayed(0, 200);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		vBaseBottomWindowRoot = null;
	}
}