
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import zblibrary.zgl.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;


/**
 * 帮助详情
 */
public class HelpDetailsActivity extends BaseActivity implements OnBottomDragListener {

	private TextView help_details_tv,help_details_title;
	private String title,content;
	public static Intent createIntent(Context context,String title,String content) {
		return new Intent(context, HelpDetailsActivity.class).
				putExtra(INTENT_ID,content).putExtra(INTENT_TITLE,title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_details_activity, this);
		intent = getIntent();
		content = intent.getStringExtra(INTENT_ID);
		title = intent.getStringExtra(INTENT_TITLE);
		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {//必须调用
		help_details_title = findView(R.id.help_details_title);
		help_details_tv = findView(R.id.help_details_tv);
	}


	@Override
	public void initData() {//必须调用
		help_details_title.setText(title);
		help_details_tv.setText(Html.fromHtml(content));
	}


	@Override
	public void initEvent() {//必须调用

	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {
			return;
		}
		finish();
	}
}