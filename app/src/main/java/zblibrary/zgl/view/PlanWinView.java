
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import zblibrary.zgl.R;
import zblibrary.zgl.model.PlaneWin;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.GlideUtil;

public class PlanWinView extends BaseView<PlaneWin.ResultModel> {
	private static final String TAG = "PlanWinView";

	public PlanWinView(Activity context, ViewGroup parent) {
		super(context, R.layout.plan_win_view, parent);
	}

	public ImageView iv_plan_win_head;
	public TextView tv_plan_win_planno,tv_plan_win_luckno;
	public TextView tv_plan_win_planuser,tv_plan_win_time,tv_plan_win_id;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		iv_plan_win_head = findView(R.id.iv_plan_win_head);
		tv_plan_win_planno = findView(R.id.tv_plan_win_planno);
		tv_plan_win_luckno = findView(R.id.tv_plan_win_luckno );
		tv_plan_win_planuser = findView(R.id.tv_plan_win_planuser);
		tv_plan_win_time = findView(R.id.tv_plan_win_time);
		tv_plan_win_id = findView(R.id.tv_plan_win_id);
		return super.createView();
	}

	@Override
	public void bindView(PlaneWin.ResultModel data_){
		super.bindView(data_ != null ? data_ : new PlaneWin.ResultModel());
		GlideUtil.loadCircle(context,data.avatar,iv_plan_win_head);
		tv_plan_win_planno.setText( data.planNum);
		tv_plan_win_luckno.setText( data.winLuckNumber+"");
		tv_plan_win_planuser.setText(data.winUsername);
		tv_plan_win_id.setText("(ID:"+data.mobile + ")");
		tv_plan_win_time.setText( data.gmtFinish);
	}
}