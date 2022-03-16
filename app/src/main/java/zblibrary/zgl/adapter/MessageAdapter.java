package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.Message;
import zblibrary.zgl.view.MessageView;
import zuo.biao.library.base.BaseAdapter;

public class MessageAdapter extends BaseAdapter<Message.MessageData, MessageView> {

	public MessageAdapter(Activity context) {
		super(context);
	}

	@Override
	public MessageView createView(int position, ViewGroup parent) {
		return new MessageView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).id;
	}

}