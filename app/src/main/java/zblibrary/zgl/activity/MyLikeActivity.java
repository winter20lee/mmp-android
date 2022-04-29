
package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.adapter.WatchHistoryAdapter;
import zblibrary.zgl.application.MApplication;
import zblibrary.zgl.fragment.MyDownFilesFragment;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.Customize;
import zblibrary.zgl.model.MyLike;
import zblibrary.zgl.model.SecondCategory;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.WatchHistoryView;
import zuo.biao.library.base.BaseHttpListActivity;
import zuo.biao.library.base.BaseView;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.util.GsonUtil;


/**
 * 我的收藏
 */
public class MyLikeActivity extends BaseHttpListActivity<MyLike.ResultBean, ListView, WatchHistoryAdapter> implements 
		OnBottomDragListener, View.OnClickListener, OnHttpResponseListener ,WatchHistoryView.ItemClickListener {

	private ArrayList<Integer> selIds = new ArrayList<>();
	private TextView mylike_edit,mylike_sel_all,mylike_sel_del;
	public static Intent createIntent(Context context) {
		return new Intent(context, MyLikeActivity.class);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_like_activity, this);

		initView();
		initData();
		initEvent();
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		lvBaseList.setDividerHeight(0);
		mylike_edit = findView(R.id.mylike_edit);
		mylike_sel_all = findView(R.id.mylike_sel_all);
		mylike_sel_del = findView(R.id.mylike_sel_del);
	}

	@Override
	public void setList(final List<MyLike.ResultBean> list) {
		setList(new AdapterCallBack<WatchHistoryAdapter>() {

			@Override
			public WatchHistoryAdapter createAdapter() {
				return new WatchHistoryAdapter(context,MyLikeActivity.this);
			}

			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}
		});
	}

	@Override
	public void initData() {//必须调用
		super.initData();
		vBaseEmptyView.setEmptyText("暂时没有收藏记录");
		vBaseEmptyView.setEmptySecondText("您可以去首页推荐看看");
	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getMyfav(page,-page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<MyLike.ResultBean> parseArray(String json) {
		MyLike myLike ;
		try {
			myLike = GsonUtil.GsonToBean(GsonUtil.GsonData(json), MyLike.class);
		} catch (Exception e) {
			return new ArrayList<>();
		}
		if(myLike ==null || myLike.result == null ){
			return new ArrayList<>();
		}

		if(myLike.totalPage > myLike.pageNo){
			onStopLoadMore(true);
		}else{
			onStopLoadMore(false);
		}
		return myLike.result;
	}

	@Override
	public void initEvent() {//必须调用
		super.initEvent();
		mylike_edit.setOnClickListener(this);
		mylike_sel_all.setOnClickListener(this);
		mylike_sel_del.setOnClickListener(this);
	}


	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {

			return;
		}

		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		toActivity(PlayVideoDetailsActivity.createIntent(context,list.get(position).videoId));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.mylike_edit:
				if (mylike_edit.getText().toString().equals("编辑")) {
					mylike_edit.setText("完成");
					MApplication.getInstance().isEditFav = true;
					findView(R.id.mylike_bottom_divid).setVisibility(View.VISIBLE);
					findView(R.id.mylike_bottom_edit).setVisibility(View.VISIBLE);
				} else {
					mylike_edit.setText("编辑");
					MApplication.getInstance().isEditFav = false;
					findView(R.id.mylike_bottom_divid).setVisibility(View.GONE);
					findView(R.id.mylike_bottom_edit).setVisibility(View.GONE);
				}
				if (adapter != null) {
					adapter.notifyDataSetInvalidated();
				}
				break;
			case R.id.mylike_sel_all:
				if(mylike_sel_all.getText().toString().equals("全选")){
					for (int i=0;i<list.size();i++) {
						MyLike.ResultBean resultBean = list.get(i);
						resultBean.isSele = true;
					}
					mylike_sel_all.setText("取消全选");
				}else{
					for (int i=0;i<list.size();i++) {
						MyLike.ResultBean resultBean = list.get(i);
						resultBean.isSele = false;
					}
					mylike_sel_all.setText("全选");
				}

				if (adapter != null) {
					adapter.notifyDataSetInvalidated();
				}
				break;
			case R.id.mylike_sel_del:
				for (int i=0;i<list.size();i++) {
					MyLike.ResultBean resultBean = list.get(i);
					if(resultBean.isSele){
						selIds.add(resultBean.videoId);
					}
				}
				showProgressDialog("取消中...");
				HttpRequest.getFavCancel(selIds,1111, new OnHttpResponseListenerImpl(this));
				break;
		}
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		for (Integer id: selIds) {
			for(int i=0;i<list.size();i++){
				if(id == list.get(i).videoId){
					list.remove(i);
				}
			}
		}
		adapter.refresh(list);
		selIds.clear();
		showShortToast("取消收藏成功");
		dismissProgressDialog();
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		dismissProgressDialog();
		showShortToast("取消收藏失败");
	}

	@Override
	protected void onDestroy() {
		MApplication.getInstance().isEditFav = false;
		super.onDestroy();
	}


	@Override
	public void onItemClickListener() {
		checkSelAll();
	}

	private void checkSelAll(){
		int total = 0;
		for (int i=0;i<list.size();i++) {
			MyLike.ResultBean resultBean = list.get(i);
			if(resultBean.isSele){
				++total;
			}
		}
		if(total==list.size()){
			mylike_sel_all.setText("取消全选");
		}else{
			mylike_sel_all.setText("全选");
		}
	}
}