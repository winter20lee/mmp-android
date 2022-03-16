
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.PlanePart;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;
import zuo.biao.library.util.StringUtil;

public class PlanPartView extends BaseView<PlanePart.ResultModel> {
	private static final String TAG = "PlanPartView";

	public PlanPartView(Activity context, ViewGroup parent) {
		super(context, R.layout.plan_part_view, parent);
	}

	public ImageView iv_plan_win_head;
	public TextView tv_plan_win_planno;
	public TextView tv_plan_win_planuser,tv_plan_win_time,tv_plan_win_price;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		iv_plan_win_head = findView(R.id.iv_plan_win_head);
		tv_plan_win_planno = findView(R.id.tv_plan_win_planno);
		tv_plan_win_planuser = findView(R.id.tv_plan_win_planuser);
		tv_plan_win_time = findView(R.id.tv_plan_win_time);
		tv_plan_win_price = findView(R.id.tv_plan_win_price);
		return super.createView();
	}

	@Override
	public void bindView(PlanePart.ResultModel data_){
		super.bindView(data_ != null ? data_ : new PlanePart.ResultModel());
		GlideUtil.loadCircle(context,data.avatar,iv_plan_win_head);
		tv_plan_win_planuser.setText(data.nickname);
		tv_plan_win_planno.setText( data.partNumber+"");
		String payMoney = StringUtil.changeF2Y(data.payMoney);
		tv_plan_win_price.setText(payMoney);
		tv_plan_win_time.setText( data.participateCreate);
	}
}