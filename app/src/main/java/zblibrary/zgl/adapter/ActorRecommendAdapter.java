
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.PlayVideoDes;
import zblibrary.zgl.view.ActorRecommendView;
import zuo.biao.library.base.BaseAdapter;

/**演员推荐
 */
public class ActorRecommendAdapter extends BaseAdapter<PlayVideoDes.ActorVideoListBean, ActorRecommendView> {

	public ActorRecommendAdapter(Activity context) {
		super(context);
	}

	@Override
	public ActorRecommendView createView(int position, ViewGroup parent) {
		return new ActorRecommendView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).id;
	}

}