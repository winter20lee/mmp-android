
package zuo.biao.library.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import zuo.biao.library.R;
import zuo.biao.library.util.CommonUtil;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class ImageAlertDialog extends Dialog implements View.OnClickListener {

	private Activity context;
	private String image;
	private String link;

	/**
	 * 带监听器参数的构造函数
	 */
	public ImageAlertDialog(Activity context, String image, String link) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.image = image;
		this.link = link;
	}

	private RoundImageView tvAlertDialogImage;
	private ImageView btnAlertDialogNegative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.image_alert_dialog);
		setCanceledOnTouchOutside(true);

		tvAlertDialogImage = findViewById(R.id.tvAlertDialogImage);
		btnAlertDialogNegative = findViewById(R.id.btnAlertDialogNegative);
		btnAlertDialogNegative.setOnClickListener(this);
		tvAlertDialogImage.setOnClickListener(this);
		tvAlertDialogImage.setRadius(StringUtil.dp2px(context,12));
		GlideUtil.load(context,image,tvAlertDialogImage);
	}

	@Override
	public void onClick(final View v) {
		int id = v.getId();
		if (id == R.id.tvAlertDialogImage) {
			if (StringUtil.isNotEmpty(link, true)) {
				CommonUtil.toActivity(context, WebViewActivity.createIntent(context, "", link));
				dismiss();
			}
		} else if (id == R.id.btnAlertDialogNegative) {
			dismiss();
		}
	}

	@Override
	public void show() {
		super.show();
		/**
		 * 设置宽度全屏，要设置在show的后面
		 */
		WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
		layoutParams.gravity= Gravity.BOTTOM;
		layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
		getWindow().getDecorView().setPadding(0, 0, 0, 0);
		getWindow().setAttributes(layoutParams);
	}

}

