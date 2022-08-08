
package zblibrary.zgl.view;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import im.crisp.client.ChatActivity;
import zblibrary.zgl.R;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.model.Order;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.WebViewActivity;
import zuo.biao.library.util.StringUtil;

public class OrderView extends BaseView<Order.MessageData>{
	public OrderView(Activity context, ViewGroup parent) {
		super(context, R.layout.order_view, parent);
	}

	public TextView order_num,order_price;
	public TextView order_copy,order_time;
	public TextView order_state,order_name,order_kefu;
	private ClipboardManager mClipboardManager;
	public ImageView order_card;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		order_num = findView(R.id.order_num);
		order_copy = findView(R.id.order_copy );
		order_state = findView(R.id.order_state);
		order_name = findView(R.id.order_name);
		order_card = findView(R.id.order_card);
		order_price  = findView(R.id.order_price);
		order_time = findView(R.id.order_time);
		order_kefu = findView(R.id.order_kefu);
		// 1. 注册mClipboardManager
		mClipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
		return super.createView();
	}

	@Override
	public void bindView(Order.MessageData data_){
		super.bindView(data_ != null ? data_ : new Order.MessageData());
		order_num.setText(StringUtil.getTrimedString(data.orderNo));
		order_copy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String mData = data.orderNo;
				//创建一个新的文本clip对象
				ClipData mClipData = ClipData.newPlainText("Simple text",mData);
				//把clip对象放在剪贴板中
				mClipboardManager.setPrimaryClip(mClipData);
				showShortToast("复制成功！");
			}
		});
		if(data.buyMemberLevelCode.equals("lifelong")){
			order_card.setImageResource(R.mipmap.zhongshenka);
		}else if(data.buyMemberLevelCode.equals("annualy")){
			order_card.setImageResource(R.mipmap.nianka);
		} else if(data.buyMemberLevelCode.equals("quarterly")){
			order_card.setImageResource(R.mipmap.jika);
		} else if(data.buyMemberLevelCode.equals("monthly")){
			order_card.setImageResource(R.mipmap.yueka);
		} else{
			order_card.setImageResource(R.mipmap.tiyanka);
		}
		order_price.setText("￥"+data.orderAmount);
		order_time.setText(data.gmtUpdate);
		order_state.setText(data.payStatusName);
		order_kefu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent crispIntent = new Intent(context, ChatActivity.class);
				context.startActivity(crispIntent);
//				if(MApplication.getInstance().getAppInitInfo()!=null && StringUtil.isNotEmpty(MApplication.getInstance().getAppInitInfo().csLink,true)){
//					toActivity(WebViewActivity.createIntent(context,"客服",MApplication.getInstance().getAppInitInfo().csLink));
//				}
			}
		});
	}
}