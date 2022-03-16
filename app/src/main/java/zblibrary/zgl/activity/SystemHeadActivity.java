package zblibrary.zgl.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.BottomGridAdapter;
import zblibrary.zgl.model.Customize;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.util.GlideUtil;

public class SystemHeadActivity extends BaseActivity implements OnItemClickListener {
	private static final String TAG = "SystemHeadActivity";
	private String seleUrl;
	/**启动BottomMenuWindow的Intent
	 * @param context
	 * @param nameList
	 * @return
	 */
	public static Intent createIntent(Context context, String seleUrl,ArrayList<Customize> nameList) {
		return new Intent(context, SystemHeadActivity.class).
				putExtra(INTENT_TITLE,seleUrl).
				putExtra(INTENT_ID, (Serializable)nameList);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_head_activity);
		initView();
		initData();
		initEvent();
	}

	private GridView system_head_grid;
	private Button system_head_pre_save,system_head_pre_cancel;
	private ImageView system_head_pre;
	@Override
	public void initView() {//必须调用
		system_head_pre = findView(R.id.system_head_pre);
		system_head_grid = findView(R.id.system_head_grid);
		system_head_pre_save = findView(R.id.system_head_pre_save);
		system_head_pre_cancel = findView(R.id.system_head_pre_cancel);
	}

	private ArrayList<Customize> nameList = null;

	private BottomGridAdapter adapter;
	@Override
	public void initData() {//必须调用
		intent = getIntent();
		seleUrl = intent.getStringExtra(INTENT_TITLE);
		nameList = (ArrayList<Customize>) intent.getSerializableExtra(INTENT_ID);
		if (nameList == null || nameList.size() <= 0) {
			Log.e(TAG, "init   nameList == null || nameList.size() <= 0 >> finish();return;");
			finish();
			return;
		}
		GlideUtil.loadCircle(context,seleUrl,system_head_pre);
		adapter = new BottomGridAdapter(this);
		system_head_grid.setAdapter(adapter);
		adapter.refresh(nameList);
	}

	@Override
	public void initEvent() {//必须调用
		adapter.setOnItemClickListener(this);
		system_head_pre_save.setOnClickListener(v -> {
			intent = new Intent();
			intent.putExtra(RESULT_DATA, seleUrl);
			setResult(RESULT_OK, intent);
			finish();
		});
		system_head_pre_cancel.setOnClickListener(view -> finish());
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		seleUrl = nameList.get(position).getValue();
		GlideUtil.loadCircle(context,seleUrl,system_head_pre);
	}
}