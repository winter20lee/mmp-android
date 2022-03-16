
package zuo.biao.library.base;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import zuo.biao.library.R;
import zuo.biao.library.interfaces.OnHttpResponseListener;
import zuo.biao.library.interfaces.OnLoadListener;
import zuo.biao.library.interfaces.OnStopLoadListener;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.Log;


/**基础http网络列表的Fragment
 * @author Lemon
 * @param <T> 数据模型(model/JavaBean)类
 * @param <VH> ViewHolder或其子类
 * @param <A> 管理LV的Adapter
 * @see #getListAsync(int)
 * @see #onHttpResponse(int, String, Exception)
 * @see
 *   <pre>
 *       基础使用：<br />
 *       extends BaseHttpRecyclerFragment 并在子类onCreateView中srlBaseHttpRecycler.autoRefresh(), 具体参考.UserRecyclerFragment
 *       <br /><br />
 *       列表数据加载及显示过程：<br />
 *       1.srlBaseHttpRecycler.autoRefresh触发刷新 <br />
 *       2.getListAsync异步获取列表数据 <br />
 *       3.onHttpResponse处理获取数据的结果 <br />
 *       4.setList把列表数据绑定到adapter <br />
 *   </pre>
 */
public abstract class BaseHttpRecyclerFragment<T, VH extends RecyclerView.ViewHolder, A extends RecyclerView.Adapter<VH>>
		extends BaseRecyclerFragment<T, VH, A>
		implements OnHttpResponseListener, OnStopLoadListener, OnRefreshListener, OnLoadMoreListener {
	private static final String TAG = "BaseHttpRecyclerFragment";





	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.base_http_recycler_fragment);
		return view;
	}

	protected SmartRefreshLayout srlBaseHttpRecycler;

	@Override
	public void initView() {
		super.initView();

		srlBaseHttpRecycler = findView(R.id.srlBaseHttpRecycler);

	}

	@Override
	public void setAdapter(A adapter) {
		if (adapter instanceof BaseAdapter) {
			((zuo.biao.library.base.BaseAdapter) adapter).setOnLoadListener(new OnLoadListener() {
				@Override
				public void onRefresh() {
					srlBaseHttpRecycler.autoRefresh();
				}

				@Override
				public void onLoadMore() {
					srlBaseHttpRecycler.autoLoadMore();
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

		srlBaseHttpRecycler.setOnRefreshListener(this);
		srlBaseHttpRecycler.setOnLoadMoreListener(this);
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
				srlBaseHttpRecycler.finishRefresh();
				srlBaseHttpRecycler.finishLoadMore();
			}
		});
	}
	@Override
	public void onStopLoadMore(final boolean isHaveMore) {
		runUiThread(new Runnable() {

			@Override
			public void run() {
				if (isHaveMore) {
					srlBaseHttpRecycler.finishLoadMore();
				} else {
					srlBaseHttpRecycler.finishLoadMoreWithNoMoreData();
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

				String resultData =null;
				try {
					resultData = GsonUtil.GsonData(resultJson);;
				} catch (Exception e1) {
					Log.e(TAG, "onHttpResponse  try { sonObject = new JSONObject(resultJson);... >>" +
							" } catch (JSONException e1) {\n" + e1.getMessage());
				}

				onResponse(page, parseArray(resultData), e);
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