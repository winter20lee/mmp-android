
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.MessageAdapter;
import zblibrary.zgl.model.Message;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;

/**
 * 站内信
 */
public class MessageActivity extends BaseHttpListActivity<Message.MessageData, ListView, MessageAdapter> implements OnBottomDragListener {


	public static Intent createIntent(Context context) {
		return new Intent(context, MessageActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_activity, this);

		initView();
		initData();
		initEvent();
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		lvBaseList.setDividerHeight(0);
	}

	@Override
	public void setList(final List<Message.MessageData> list) {
		setList(new AdapterCallBack<MessageAdapter>() {

			@Override
			public MessageAdapter createAdapter() {
				return new MessageAdapter(context);
			}

			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}
		});
	}

	@Override
	public void initData() {//必须调用
		super.initData();

	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getMessageList(page, -page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<Message.MessageData> parseArray(String json) {
		try {
			String data = GsonUtil.GsonData(json);
			Message message = GsonUtil.GsonToBean(data,Message.class);
			List<Message.MessageData> messageDataList = message.result;
			onStopRefresh();
			if(message.totalPage > message.pageNo){
				onStopLoadMore(true);
			}else{
				messageDataList.get(messageDataList.size()-1).isEnd = true;
				onStopLoadMore(false);
			}
			return messageDataList;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public void initEvent() {//必须调用
		super.initEvent();

	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		toActivity(MessageDesActivity.createIntent(context, list.get(position)));
		list.get(position).readStatus = true;
		adapter.notifyDataSetInvalidated();
	}
}