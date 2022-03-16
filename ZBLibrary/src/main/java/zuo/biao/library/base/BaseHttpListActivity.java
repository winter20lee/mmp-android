
package zuo.biao.library.base;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import zuo.biao.library.R;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.interfaces.OnLoadListener;
import zuo.biao.library.interfaces.OnStopLoadListener;
import zuo.biao.library.util.Log;

public abstract class BaseHttpListActivity<T, LV extends AbsListView, A extends ListAdapter>
		extends BaseListActivity<T, LV, A>
		implements OnHttpResponseListener, OnStopLoadListener, OnRefreshListener, OnLoadMoreListener {

	private static final String TAG = "BaseHttpListActivity";
	protected SmartRefreshLayout srlBaseHttpList;

	@Override
	public void initView() {
		super.initView();
		srlBaseHttpList = findView(R.id.srlBaseHttpList);
	}

	@Override
	public void setAdapter(A adapter) {
		if (adapter instanceof BaseAdapter) {
			((BaseAdapter) adapter).setOnLoadListener(new OnLoadListener() {
				@Override
				public void onRefresh() {
					srlBaseHttpList.autoRefresh();
				}

				@Override
				public void onLoadMore() {
					srlBaseHttpList.autoLoadMore();
				}
			});
		}
		super.setAdapter(adapter);
	}
	@Override
	public void initData() {
		super.initData();
	}

	/**
	 * @param page 用-page作为requestCode
	 */
	@Override
	public abstract void getListAsync(int page);

	/**
	 * 将JSON串转为List（已在非UI线程中）
	 * *直接JSON.parseArray(json, getCacheClass());可以省去这个方法，但由于可能json不完全符合parseArray条件，所以还是要保留。
	 * *比如json只有其中一部分能作为parseArray的字符串时，必须先提取出这段字符串再parseArray
	 */
	public abstract List<T> parseArray(String json);

	@Override
	public void initEvent() {//必须调用
		super.initEvent();
		setOnStopLoadListener(this);

		srlBaseHttpList.setOnRefreshListener(this);
		srlBaseHttpList.setOnLoadMoreListener(this);
	}


	/**重写后可自定义对这个事件的处理
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}

	@Override
	public void onRefresh(RefreshLayout refreshlayout) {
		onRefresh();
	}

	@Override
	public void onLoadMore(RefreshLayout refreshlayout) {
		onLoadMore();
	}


	@Override
	public void onStopRefresh() {
		runUiThread(new Runnable() {

			@Override
			public void run() {
				srlBaseHttpList.finishRefresh();
				srlBaseHttpList.finishLoadMore();
			}
		});
	}
	@Override
	public void onStopLoadMore(final boolean isHaveMore) {
		runUiThread(new Runnable() {

			@Override
			public void run() {
				if (isHaveMore) {
					srlBaseHttpList.finishLoadMore();
				} else {
					srlBaseHttpList.finishLoadMoreWithNoMoreData();
				}
			}
		});
	}

	/**处理Http请求结果
	 * @param requestCode  = -page {@link #getListAsync(int)}
	 * @param resultJson
	 * @param e
	 */
	@Override
	public void onHttpResponse(final int requestCode, final String resultJson, final Exception e) {
		runThread(TAG + "onHttpResponse", new Runnable() {

			@Override
			public void run() {
				int page = 0;
				if (requestCode > 0) {
					Log.w(TAG, "requestCode > 0, 应该用BaseListFragment#getListAsync(int page)中的page的负数作为requestCode!");
				} else {
					page = - requestCode;
				}

				onResponse(page, parseArray(resultJson), e);
			}
		});
	}

	/**处理结果
	 * @param page
	 * @param list
	 * @param e
	 */
	public void onResponse(int page, List<T> list, Exception e) {
		if ((list == null || list.isEmpty()) && e != null) {
			onLoadFailed(page, e);
		} else {
			onLoadSucceed(page, list);
		}
	}
}