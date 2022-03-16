
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.model.LatestAnnou;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;

/**
 * 最新揭晓
 */
public class LatestAnnoView extends BaseView<LatestAnnou.ResultModel> {
	public LatestAnnoView(Activity context, ViewGroup parent) {
		super(context, R.layout.latest_anno_view, parent);
	}

	public ImageView latest_anno_iv;
	public TextView latest_anno_title;
	public TextView latest_anno_qihao;
	public TextView latest_anno_xyhm,latest_anno_jxsj;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		latest_anno_iv = findView(R.id.latest_anno_iv);
		latest_anno_title = findView(R.id.latest_anno_title);
		latest_anno_qihao = findView(R.id.latest_anno_qihao);
		latest_anno_xyhm = findView(R.id.latest_anno_xyhm);
		latest_anno_jxsj = findView(R.id.latest_anno_jxsj);
		return super.createView();
	}

	@Override
	public void bindView(LatestAnnou.ResultModel data_){
		super.bindView(data_ != null ? data_ : new LatestAnnou.ResultModel());
		GlideUtil.loadRound(context,data.goodsImg.get(0),latest_anno_iv);
		latest_anno_title.setText(data.goodsName);
		latest_anno_qihao.setText(data.planNum+"");
		latest_anno_xyhm.setText(data.winLuckNumber+"");
		latest_anno_jxsj.setText(data.gmtFinish);
	}
}