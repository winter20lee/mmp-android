
package zuo.biao.library.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import zuo.biao.library.R;
import zuo.biao.library.util.StringUtil;

public class TextAlertDialog extends Dialog implements View.OnClickListener {

	private Context context;
	private String title;
	private String message;

	/**
	 * 带监听器参数的构造函数
	 */
	public TextAlertDialog(Context context, String title, String message) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.title = title;
		this.message = message;
	}

	private TextView tvTitle;
	private TextView tvMessage;
	private Button btnNegative;
	@Override
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.text_alert_dialog);
		setCanceledOnTouchOutside(true);

		tvTitle = findViewById(R.id.tvAlertDialogTitle);
		tvMessage = findViewById(R.id.tvAlertDialogMessage);
		btnNegative = findViewById(R.id.btnAlertDialogNegative);

		tvTitle.setVisibility(StringUtil.isNotEmpty(title, true) ? View.VISIBLE : View.GONE);
		tvTitle.setText("" + StringUtil.getCurrentString());

		btnNegative.setOnClickListener(this);

		tvMessage.setText(Html.fromHtml(message));
	}

	@Override
	public void onClick(final View v) {
		dismiss();
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

