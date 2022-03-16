
package zblibrary.zgl.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.model.Message;
import zblibrary.zgl.util.HttpRequest;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.util.Log;

/**消息详情
 */
public class MessageDesActivity extends BaseActivity implements  OnBottomDragListener{
	public static final String TAG = "MessageDesActivity";

	/**获取启动MessageDesActivity的intent
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context, Message.MessageData messageData) {
		return new Intent(context, MessageDesActivity.class)
				.putExtra(INTENT_ID, messageData);
	}
	private Message.MessageData messageData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_des_activity, this);
		intent = getIntent();
		messageData = (Message.MessageData) intent.getSerializableExtra(INTENT_ID);
		initView();
		initData();
		initEvent();
		setReadState();
	}

	private TextView mess_des_send;
	private TextView mess_des_content;
	private TextView mess_des_time,mess_des_title;

	@Override
	public void initView() {//必须调用
		mess_des_send = findView(R.id.mess_des_send);
		mess_des_content = findView(R.id.mess_des_content);
		mess_des_time = findView(R.id.mess_des_time);
		mess_des_title = findView(R.id.mess_des_title);
	}


	@Override
	public void initData() {//必须调用
		mess_des_send.setText(messageData.messageOpUserName);
		mess_des_content.setText(messageData.content);
		mess_des_time.setText(messageData.messageGmtCreate);
		mess_des_title.setText(messageData.title);
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

	private void setReadState(){
		HttpRequest.getReadMessage(messageData.id, new OnHttpResponseListener() {
			@Override
			public void onHttpResponse(int requestCode, String resultJson, Exception e) {
				Log.d(TAG,"resultJson:"+resultJson);
			}
		});
	}
}