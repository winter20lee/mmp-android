package zblibrary.zgl.view;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zblibrary.zgl.R;
import zblibrary.zgl.model.SearchComm;
import zuo.biao.library.base.BaseView;

public class SearchCommViewItem extends BaseView<SearchComm>{

	public SearchCommViewItem(Activity context, ViewGroup parent) {
		super(context, R.layout.search_comm_item_view, parent);
	}

	public TextView search_com_word;
	@SuppressLint("InflateParams")
	@Override
	public View createView() {
		search_com_word = findView(R.id.search_com_word);
		return super.createView();
	}

	@Override
	public void bindView(SearchComm data_){
		super.bindView(data_ != null ? data_ : new SearchComm());

		search_com_word.setText("士大夫发生");
	}
}