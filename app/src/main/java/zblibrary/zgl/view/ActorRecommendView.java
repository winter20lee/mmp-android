
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.model.PlayVideoDes;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.ui.RoundImageView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

/**
 * 演员推荐
 */
public class ActorRecommendView extends BaseView<PlayVideoDes.ActorVideoListBean> {
	public ActorRecommendView(Activity context, ViewGroup parent) {
		super(context, R.layout.actor_recommend_view, parent);
	}

	public RoundImageView latest_anno_iv;
	public TextView latest_anno_title;
	public TextView latest_anno_qihao;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		latest_anno_iv = findView(R.id.latest_anno_iv);
		latest_anno_title = findView(R.id.latest_anno_title);
		latest_anno_qihao = findView(R.id.latest_anno_qihao);
		return super.createView();
	}

	@Override
	public void bindView(PlayVideoDes.ActorVideoListBean data_){
		super.bindView(data_ != null ? data_ : new PlayVideoDes.ActorVideoListBean());
		//GlideUtil.loadRound(context,data.goodsImg.get(0),latest_anno_iv);
		latest_anno_iv.setRadius(StringUtil.dp2px(context,2));
		GlideUtil.load(context,data.coverUrl,latest_anno_iv);
		latest_anno_title.setText(data.name);
		if(StringUtil.isEmpty(data.length)){
			latest_anno_qihao.setText("");
		}
		else{
			latest_anno_qihao.setText(data.length);
		}
	}
}