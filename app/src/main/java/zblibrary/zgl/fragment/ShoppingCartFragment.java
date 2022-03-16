
package zblibrary.zgl.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.activity.ProductDesSelectWindow;
import zblibrary.zgl.activity.PlayVideoDetailsActivity;
import zblibrary.zgl.activity.SubmitOrderActivity;
import zblibrary.zgl.adapter.ShoppingCartAdapter;
import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.ShoppingCart;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.ShoppingCartViewItem;
import zuo.biao.library.base.BaseHttpRecyclerFragment;
import zuo.biao.library.interfaces.AdapterCallBack;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.StringUtil;

/**
 * 购物车
 */
public class ShoppingCartFragment extends BaseHttpRecyclerFragment<ShoppingCart.ResultModel, ShoppingCartViewItem, ShoppingCartAdapter>
		implements ShoppingCartViewItem.OnSelectClickListener, OnHttpResponseListener , View.OnClickListener {
	private static final int REQUEST_CODE_DEL = 900001;
	private TextView shopping_cart_total_price,shopping_cart_pay,shop_cart_more_actions;
	private TextView shopping_cart_total_num,shopping_cart_more_del,shopping_cart_more_empty,shopping_cart_more_del_ex;
	private ShoppingCart shoppingCart;
	private ShoppingCart.ResultModel resultModelDel;
	private ImageView iv_shop_cart_sel_all,iv_shop_cart_sel_all_1;
	private ArrayList<ShoppingCart.ResultModel> selResultModelArrayList = new ArrayList<>();
	private LinearLayout shop_cart_sel_all_ll;
	public static ShoppingCartFragment createInstance() {
		ShoppingCartFragment fragment = new ShoppingCartFragment();
		return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		setContentView(R.layout.shopping_cart_fragment);
		argument = getArguments();
		initView();
		initData();
		initEvent();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		onRefresh();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		findView(R.id.shop_cart_sel_all,this);
		findView(R.id.shop_cart_sel_all_1,this);
		iv_shop_cart_sel_all = findView(R.id.iv_shop_cart_sel_all,this);
		iv_shop_cart_sel_all_1 = findView(R.id.iv_shop_cart_sel_all_1,this);
		shop_cart_more_actions = findView(R.id.shop_cart_more_actions,this);
		shopping_cart_total_price = findView(R.id.shopping_cart_total_price);
		shopping_cart_total_num = findView(R.id.shopping_cart_total_num);
		shopping_cart_pay = findView(R.id.shopping_cart_pay,this);
		shopping_cart_more_del = findView(R.id.shopping_cart_more_del,this);
		shopping_cart_more_del_ex = findView(R.id.shopping_cart_more_del_ex,this);
		shopping_cart_more_empty = findView(R.id.shopping_cart_more_empty,this);
		shop_cart_sel_all_ll = findView(R.id.shop_cart_sel_all_ll);
	}

	@Override
	public void setList(final List<ShoppingCart.ResultModel> list) {
		setList(new AdapterCallBack<ShoppingCartAdapter>() {
			@Override
			public ShoppingCartAdapter createAdapter() {
				return new ShoppingCartAdapter(context,ShoppingCartFragment.this);
			}

			@Override
			public void refreshAdapter() {
				adapter.refresh(list);
			}
		});
		onSelect();
	}

	@Override
	public void initData() {//必须调用
		super.initData();
	}

	@Override
	public void getListAsync(final int page) {
		HttpRequest.getProductList(page,-page,this);
		if(page==1){
			onStopLoadMore(true);
		}
	}

	@Override
	public List<ShoppingCart.ResultModel> parseArray(String json) {
		try {
			shoppingCart = GsonUtil.GsonToBean(json,ShoppingCart.class);
			onStopRefresh();
			if(shoppingCart.totalPage > shoppingCart.pageNo){
				onStopLoadMore(true);
			}else{
				onStopLoadMore(false);
			}
			if(shoppingCart!=null && shoppingCart.result!=null && shoppingCart.result.size()>0){
				for (ShoppingCart.ResultModel resultModel:shoppingCart.result) {
					for(ShoppingCart.ResultModel resultModelsel:selResultModelArrayList){
						if(resultModelsel.id.equals(resultModel.id)){
							resultModel.isSelect = true;
						}
					}
				}
			}
			runUiThread(() -> shop_cart_sel_all_ll.setVisibility(View.VISIBLE));

			return  shoppingCart.result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		runUiThread(() -> shop_cart_sel_all_ll.setVisibility(View.GONE));
		return new ArrayList<>();
	}


	@Override
	public void initEvent() {//必须调用
		super.initEvent();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (id > 0) {
			toActivity(PlayVideoDetailsActivity.createIntent(context,id));
		}
	}

	@Override
	public void onSelect() {
		if(shoppingCart==null || shoppingCart.result==null){
			return;
		}
		int goodsPrice = 0 ;
		int countNum = 0;
		selResultModelArrayList.clear();
		for (ShoppingCart.ResultModel resultModel:shoppingCart.result) {
			if(resultModel.isSelect){
				goodsPrice+=resultModel.goodsPrice * resultModel.goodsCount;
				countNum = countNum+resultModel.goodsCount;
				selResultModelArrayList.add(resultModel);
			}
		}
		String price = StringUtil.changeF2Y(goodsPrice);
		String contentPrice = "Total: "+price;
		SpannableString spannableStringPrice = new SpannableString(contentPrice);
		spannableStringPrice.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")),0,"Total:".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableStringPrice.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(getActivity(),10)),0,"Total:".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		shopping_cart_total_price.setText(spannableStringPrice);

		String countNumShow = countNum+"";
		String content = String.format(getString(R.string.shopcart_selenum), countNumShow);
		SpannableString spannableString = new SpannableString(content);
		int start = content.indexOf(countNumShow);
		int end  = start+countNumShow.length();
		spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#E4393C")),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new AbsoluteSizeSpan(StringUtil.sp2px(getActivity(),14)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		shopping_cart_total_num.setText(spannableString);


		if(shoppingCart.result.size()==selResultModelArrayList.size() && selResultModelArrayList.size()!=0){
			iv_shop_cart_sel_all.setImageResource(R.mipmap.shopping_car_select);
			iv_shop_cart_sel_all_1.setImageResource(R.mipmap.shopping_car_select);
		}else{
			iv_shop_cart_sel_all.setImageResource(R.mipmap.shopping_car_unselect);
			iv_shop_cart_sel_all_1.setImageResource(R.mipmap.shopping_car_unselect);
		}
		adapter.notifyItemRangeChanged(0,shoppingCart.result.size());
	}

	@Override
	public void onDelete(ShoppingCart.ResultModel resultModel) {
		ArrayList<String> delIds = new ArrayList<>();
		resultModelDel  = resultModel;
		delIds.add(resultModel.id);
		HttpRequest.removeShopCarGoods(delIds,REQUEST_CODE_DEL,new OnHttpResponseListenerImpl(this));
	}

	@Override
	public void onSelSpec(ShoppingCart.ResultModel resultModel) {
		toActivity(ProductDesSelectWindow.createIntent(getActivity(),ProductDesSelectWindow.INTENT_FROM_TYPE_EDIT,resultModel.goodsId,resultModel.id));
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_CODE_DEL:
				if(resultModelDel!=null){
					shoppingCart.result.remove(resultModelDel);
					resultModelDel=null;
				}else{
					shoppingCart.result.removeAll(selResultModelArrayList);
				}
				adapter.refresh( shoppingCart.result);
				onSelect();
				showShortToast("Deleted successfully");
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		switch (requestCode){
			case REQUEST_CODE_DEL:
				showShortToast("Delete failed");
				break;
		}
	}

	private void changeMoreState(boolean isMore){
		if(isMore){
			shopping_cart_more_del.setVisibility(View.VISIBLE);
			shopping_cart_more_empty.setVisibility(View.VISIBLE);
			shopping_cart_more_del_ex.setVisibility(View.VISIBLE);
			shopping_cart_total_price.setVisibility(View.GONE);
			shopping_cart_total_num.setVisibility(View.GONE);
		}else{
			shopping_cart_more_del.setVisibility(View.GONE);
			shopping_cart_more_empty.setVisibility(View.GONE);
			shopping_cart_more_del_ex.setVisibility(View.GONE);
			shopping_cart_total_price.setVisibility(View.VISIBLE);
			shopping_cart_total_num.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.shopping_cart_pay:
				if(selResultModelArrayList.size()>0){
					toActivity(SubmitOrderActivity.createIntent(context,selResultModelArrayList));
				}
				else{
					showShortToast(getString(R.string.shopping_car_empty_toast));
				}
				break;
			case R.id.shop_cart_more_actions:
				if(shop_cart_more_actions.getText().toString().equals("More actions")){
					shop_cart_more_actions.setText("End");
					changeMoreState(true);
				}else{
					shop_cart_more_actions.setText("More actions");
					changeMoreState(false);
				}
				break;
			case R.id.iv_shop_cart_sel_all:
			case R.id.shop_cart_sel_all:
			case R.id.iv_shop_cart_sel_all_1:
			case R.id.shop_cart_sel_all_1:
				if(shoppingCart.result.size()==selResultModelArrayList.size() && selResultModelArrayList.size()!=0){
					for (ShoppingCart.ResultModel resultModel:shoppingCart.result) {
						resultModel.isSelect = false;
					}
				}else{
					for (ShoppingCart.ResultModel resultModel:shoppingCart.result) {
						resultModel.isSelect = true;
					}
				}
				onSelect();
				break;
			case R.id.shopping_cart_more_del:
				ArrayList<String> delIds = new ArrayList<>();
				for (ShoppingCart.ResultModel resultModel:selResultModelArrayList) {
					delIds.add(resultModel.id);
				}
				HttpRequest.removeShopCarGoods(delIds,REQUEST_CODE_DEL,new OnHttpResponseListenerImpl(this));
				break;
			case R.id.shopping_cart_more_empty:
				ArrayList<String> emptyIds = new ArrayList<>();
				for (ShoppingCart.ResultModel resultModel:shoppingCart.result) {
					emptyIds.add(resultModel.id);
					selResultModelArrayList.add(resultModel);
				}
				HttpRequest.removeShopCarGoods(emptyIds,REQUEST_CODE_DEL,new OnHttpResponseListenerImpl(this));
				break;

		}
	}
}