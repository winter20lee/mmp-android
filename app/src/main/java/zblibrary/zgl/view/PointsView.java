
package zblibrary.zgl.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.Points;
import zuo.biao.library.base.BaseView;

public class PointsView extends BaseView<Points.ResultModel>{

	public PointsView(Activity context, ViewGroup parent) {
		super(context, R.layout.points_view, parent);
	}

	public TextView point_title;
	public TextView point_time,point_content;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		point_title = findView(R.id.point_title );
		point_time = findView(R.id.point_time);
		point_content = findView(R.id.point_content);
		return super.createView();
	}

	@Override
	public void bindView(Points.ResultModel data_){
		super.bindView(data_ != null ? data_ : new Points.ResultModel());
		if(data.changeType ==1){
			point_title.setText("Treasure Order");
			point_content.setText("+"+data.changeAmount);
			point_content.setTextColor(Color.parseColor("#E4393C"));
		}else if(data.changeType ==2){
			point_title.setText("Treasure consumption");
			point_content.setText("-"+data.changeAmount);
			point_content.setTextColor(Color.parseColor("#49B79C"));
		}if(data.changeType ==3){
			point_title.setText("Manual deduction");
			point_content.setText("-"+data.changeAmount);
			point_content.setTextColor(Color.parseColor("#49B79C"));
		}
		point_time.setText(data.gmtCreate);
	}
}