
package zblibrary.zgl.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import zblibrary.zgl.model.LatestAnnou;
import zblibrary.zgl.view.LatestAnnoView;
import zuo.biao.library.base.BaseAdapter;

/**最近揭晓
 */
public class LatestAnnoAdapter extends BaseAdapter<LatestAnnou.ResultModel, LatestAnnoView> {

	public LatestAnnoAdapter(Activity context) {
		super(context);
	}

	@Override
	public LatestAnnoView createView(int position, ViewGroup parent) {
		return new LatestAnnoView(context, parent);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).lootPlanId;
	}

}