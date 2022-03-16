
package zuo.biao.library.ui;

import java.util.List;

import zuo.biao.library.R;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.util.StringUtil;

import android.app.Activity;

import androidx.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TopTabView extends BaseView<String[]> {
	private static final String TAG = "TopTabView";

	/**
	 */
	public interface OnTabSelectedListener {
		//		void beforeTabSelected(TextView tvTab, int position, int id);
		void onTabSelected(TextView tvTab, int position, int id);
	}

	private OnTabSelectedListener onTabSelectedListener;
	public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
		this.onTabSelectedListener = onTabSelectedListener;
	}

	private LayoutInflater inflater;
	public TopTabView(Activity context) {
		super(context, R.layout.top_tab_view);
		this.inflater = context.getLayoutInflater();
	}
	private int minWidth;
	public TopTabView(Activity context, int minWidth) {
		this(context);
		this.minWidth = minWidth;
	}

	public TopTabView(Activity context, int minWidth, @LayoutRes int resource){
		super(context, resource);
		this.minWidth = minWidth;
		this.inflater = context.getLayoutInflater();
	}


	private int currentPosition = 0;
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}


	public TextView tvTopTabViewTabFirst;
	public TextView tvTopTabViewTabLast;

	@Override
	public View createView() {
		tvTopTabViewTabFirst = findView(R.id.tvTopTabViewTabFirst);
		tvTopTabViewTabLast = findView(R.id.tvTopTabViewTabLast);

		return super.createView();
	}

	public String[] names;//传进来的数据

	public int getCount() {
		return names.length;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}
	public TextView getCurrentTab() {
		return tvTabs[getCurrentPosition()];
	}

	private int lastPosition = 1;
	/**
	 * @param nameList
	 */
	public void bindView(List<String> nameList){
		if (nameList != null) {
			for (int i = 0; i < nameList.size(); i++) {
				names[i] = nameList.get(i);
			}
		}
		bindView(names);
	}
	private int width;
	@Override
	public void bindView(String[] names){
		if (names == null || names.length < 2) {
			Log.e(TAG, "setInerView names == null || names.length < 2 >> return; ");
			return;
		}
		super.bindView(names);
		this.names = names;
		this.lastPosition = getCount() - 1;

		tvTabs = new TextView[getCount()];

		tvTabs[0] = tvTopTabViewTabFirst;
		tvTabs[lastPosition] = tvTopTabViewTabLast;

		for (int i = 0; i < tvTabs.length; i++) {
			final int position = i;
//
			tvTabs[position].setText(StringUtil.getTrimedString(names[position]));
			tvTabs[position].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					select(position);
				}
			});

			width = tvTabs[position].getWidth();
			if (minWidth < width) {
				minWidth = width;
			}
		}

		select(currentPosition);
	}

	private TextView[] tvTabs;
	/**选择tab
	 * @param position
	 */
	public void select(int position) {
		Log.i(TAG, "select  position = " + position);
		if (position < 0 || position >= getCount()) {
			Log.e(TAG, "select  position < 0 || position >= getCount() >> return;");
			return;
		}

		for (int i = 0; i < tvTabs.length; i++) {
			tvTabs[i].setSelected(i == position);
		}

		if (onTabSelectedListener != null) {
			onTabSelectedListener.onTabSelected(tvTabs[position]
					, position, tvTabs[position].getId());
		}

		this.currentPosition = position;
	}

}
