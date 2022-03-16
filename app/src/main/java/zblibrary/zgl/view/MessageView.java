
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.Message;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.StringUtil;

public class MessageView extends BaseView<Message.MessageData>{
	private static final String TAG = "MessageView";
	public MessageView(Activity context, ViewGroup parent) {
		super(context, R.layout.message_view, parent);
	}

	public View message_red;
	public TextView message_title;
	public TextView message_time,message_content;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		message_red = findView(R.id.message_red);
		message_title = findView(R.id.message_title );
		message_time = findView(R.id.message_time);
		message_content = findView(R.id.message_content);
		return super.createView();
	}

	@Override
	public void bindView(Message.MessageData data_){
		super.bindView(data_ != null ? data_ : new Message.MessageData());
		message_red.setVisibility(data.readStatus? View.INVISIBLE : View.VISIBLE);
		message_title.setText(StringUtil.getTrimedString(data.title));
		message_time.setText(data.messageGmtCreate);
		message_content.setText(data.content);
		findView(R.id.divider).setVisibility(data.isEnd ? View.INVISIBLE : View.VISIBLE);
	}
}