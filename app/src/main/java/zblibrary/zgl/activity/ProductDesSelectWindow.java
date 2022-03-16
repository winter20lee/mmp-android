package zblibrary.zgl.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.interfaces.OnHttpResponseListener;
import zblibrary.zgl.manager.OnHttpResponseListenerImpl;
import zblibrary.zgl.model.ProductDesSelectBottom;
import zblibrary.zgl.model.ShoppingCart;
import zblibrary.zgl.util.HttpRequest;
import zblibrary.zgl.view.BottomProductDecSelectView;
import zuo.biao.library.R;
import zuo.biao.library.base.BaseBottomWindow;
import zuo.biao.library.ui.MaxHeightWrapLayout;
import zuo.biao.library.util.GsonUtil;

public class ProductDesSelectWindow extends BaseBottomWindow implements View.OnClickListener, OnHttpResponseListener, BottomProductDecSelectView.ItemListener {
	private static final String TAG = "ProductDesSelectWindow";
	private static final int REQUEST_CODE_SPEC = 90001;
	private static final int REQUEST_CODE_ADD_GOOD = 90003;
	private static final int REQUEST_CODE_ADD_EDIT = 90004;
	private ImageView ivBaseClose;
	private LinearLayout lvBottomContent;
	private MaxHeightWrapLayout vMaxHeightWrapLayout;
	private Button product_des_add_shopcar,product_des_buy,product_des_sure,product_des_nostock;
	private  long goodsId;
	public static final int INTENT_FROM_TYPE_SPE = 1;//规格
	public static final int INTENT_FROM_TYPE_CAR = 2;//购物车
	public static final int INTENT_FROM_TYPE_BUY = 3;//购买
	public static final int INTENT_FROM_TYPE_EDIT = 4;//修改
	private int formType = 1;
	private  ArrayList<BottomProductDecSelectView> bottomProductDecSelectViewArrayList = new ArrayList<>();
	private ProductDesSelectBottom productDesSelectBottom;
	private TextView shop_cart_num,product_sel_stock;
	private String id;
	private ShoppingCart.ResultModel resultModelSubmitOrder;

	public static Intent createIntent(Context context,int type, long goodsId) {
		return new Intent(context, ProductDesSelectWindow.class).
				putExtra(INTENT_ITEM_TYPE, type).
				putExtra(INTENT_ITEMS, goodsId);
	}

	public static Intent createIntent(Context context,int type, long goodsId,String id) {
		return createIntent(context,type,goodsId).putExtra(INTENT_ITEM_IDS, id);
	}

	public static Intent createIntent(Context context,int type, long goodsId,ShoppingCart.ResultModel resultModelSubmitOrder) {
		return createIntent(context,type,goodsId).putExtra(INTENT_ITEM_TITLE, resultModelSubmitOrder);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_des_select_window);

		intent = getIntent();
		formType = intent.getIntExtra(INTENT_ITEM_TYPE,1);
		id = intent.getStringExtra(INTENT_ITEM_IDS);
		goodsId = intent.getLongExtra(INTENT_ITEMS,0);
		resultModelSubmitOrder = (ShoppingCart.ResultModel) intent.getSerializableExtra(INTENT_ITEM_TITLE);
		HttpRequest.getProductDesSpec(goodsId,REQUEST_CODE_SPEC, new OnHttpResponseListenerImpl(this));

		initView();
		initData();
		initEvent();
	}

	@Override
	public void initView() {//必须调用
		super.initView();
		ivBaseClose = findView(R.id.ivBaseClose);
		lvBottomContent = findView(R.id.lvBottomContent);
		product_des_add_shopcar = findView(R.id.product_des_add_shopcar);
		product_des_buy = findView(R.id.product_des_buy);
		vMaxHeightWrapLayout = findView(R.id.vMaxHeightWrapLayout);
		shop_cart_num = findView(R.id.shop_cart_num);
		 findView(R.id.shop_cart_minus,this);
		 findView(R.id.shop_cart_plus,this);
		product_des_sure = findView(R.id.product_des_sure);
		product_sel_stock  = findView(R.id.product_sel_stock);
		product_des_nostock = findView(R.id.product_des_nostock);
	}



	@Override
	public void initData() {//必须调用
		super.initData();
		setBottomUI();
	}

	private void setBottomUI(){
		if(formType==INTENT_FROM_TYPE_SPE){
			product_des_buy.setVisibility(View.VISIBLE);
			product_des_add_shopcar.setVisibility(View.VISIBLE);
			product_des_sure.setVisibility(View.GONE);
			product_des_nostock.setVisibility(View.GONE);
		} else if(formType==INTENT_FROM_TYPE_CAR){
			product_des_buy.setVisibility(View.GONE);
			product_des_add_shopcar.setVisibility(View.VISIBLE);
			product_des_sure.setVisibility(View.GONE);
			product_des_nostock.setVisibility(View.GONE);
		}else if(formType==INTENT_FROM_TYPE_BUY){
			product_des_add_shopcar.setVisibility(View.GONE);
			product_des_buy.setVisibility(View.VISIBLE);
			product_des_sure.setVisibility(View.GONE);
			product_des_nostock.setVisibility(View.GONE);
		}else if(formType == INTENT_FROM_TYPE_EDIT){
			product_des_buy.setVisibility(View.GONE);
			product_des_add_shopcar.setVisibility(View.GONE);
			product_des_sure.setVisibility(View.VISIBLE);
			product_des_nostock.setVisibility(View.GONE);
		}
	}

	@Override
	public void initEvent() {//必须调用
		super.initEvent();
		ivBaseClose.setOnClickListener(this);
		vBaseBottomWindowRoot.setOnClickListener(this);
		product_des_buy.setOnClickListener(this);
		product_des_add_shopcar.setOnClickListener(this);
		vMaxHeightWrapLayout.setOnClickListener(this);
		product_des_sure.setOnClickListener(this);
	}

	@Override
	protected void setResult() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.vBaseBottomWindowRoot:
			case R.id.ivBaseClose:
				finish();
				break;
			case R.id.product_des_add_shopcar:
				int skuId = getSkuId();
				if(skuId ==0){
					showShortToast(getString(R.string.shopping_car_dite));
					return;
				}
				HttpRequest.addProduct(goodsId,skuId,Integer.parseInt(shop_cart_num.getText().toString()),REQUEST_CODE_ADD_GOOD, new OnHttpResponseListenerImpl(this));
				break;
			case R.id.product_des_buy:
				skuId = getSkuId();
				if(skuId ==0){
					showShortToast(getString(R.string.shopping_car_dite));
					return;
				}
				resultModelSubmitOrder.goodsCount = Integer.parseInt(shop_cart_num.getText().toString());
				resultModelSubmitOrder.skuId = skuId;
				resultModelSubmitOrder.goodsSpec = getSpec();
				resultModelSubmitOrder.goodsPrice = getPrice();
				toActivity(SubmitOrderActivity.createIntent(context,resultModelSubmitOrder));
				break;
			case R.id.shop_cart_minus:
				if(shop_cart_num.getText().toString().equals("0")){
					return;
				}
				int num = Integer.parseInt(shop_cart_num.getText().toString());
				num = num-1;
				shop_cart_num.setText(num+"");
				break;
			case R.id.shop_cart_plus:
				num = Integer.parseInt(shop_cart_num.getText().toString());
				num = num + 1;
				shop_cart_num.setText(num+"");
				break;
			case R.id.product_des_sure:
				skuId = getSkuId();
				if(skuId ==0){
					showShortToast(getString(R.string.shopping_car_dite));
					return;
				}
				HttpRequest.modifyShopCarGoods(id,skuId,Integer.parseInt(shop_cart_num.getText().toString()),REQUEST_CODE_ADD_EDIT, new OnHttpResponseListenerImpl(this));
				break;
		}
	}

	@Override
	public void onHttpSuccess(int requestCode, int resultCode, String resultData, String message) {
		switch (requestCode){
			case REQUEST_CODE_SPEC:
				productDesSelectBottom= GsonUtil.GsonToBean(resultData, ProductDesSelectBottom.class);
				for(int i=0;i<productDesSelectBottom.attributionList.size();i++){
					BottomProductDecSelectView bottomProductDecSelectView = new BottomProductDecSelectView(this,lvBottomContent,ProductDesSelectWindow.this);
					lvBottomContent.addView(bottomProductDecSelectView.createView());
					bottomProductDecSelectViewArrayList.add(bottomProductDecSelectView);
					bottomProductDecSelectView.bindView(productDesSelectBottom.attributionList.get(i));
				}
				onItemListener();
				break;
			case REQUEST_CODE_ADD_GOOD:
				showShortToast("Shopping cart added successfully");
				finish();
				break;
			case REQUEST_CODE_ADD_EDIT:
				showShortToast("onfiguration modification succeeded");
				finish();
				break;
		}
	}

	@Override
	public void onHttpError(int requestCode, Exception e, String message) {
		switch (requestCode){
			case REQUEST_CODE_ADD_GOOD:
				showShortToast("Failed to add shopping cart");
				break;
		}
	}

	private int getSkuId(){
		String result1="";
		for(int i=0;i<bottomProductDecSelectViewArrayList.size();i++){
			BottomProductDecSelectView bottomProductDecSelectView = bottomProductDecSelectViewArrayList.get(i);
			result1+=bottomProductDecSelectView.seleResult;
		}
		return  productDesSelectBottom.getSkuId(result1);
	}

	private int getPrice(){
		String result1="";
		for(int i=0;i<bottomProductDecSelectViewArrayList.size();i++){
			BottomProductDecSelectView bottomProductDecSelectView = bottomProductDecSelectViewArrayList.get(i);
			result1+=bottomProductDecSelectView.seleResult;
		}
		return  productDesSelectBottom.getPrice(result1);
	}

	private int getStock(){
		String result1="";
		for(int i=0;i<bottomProductDecSelectViewArrayList.size();i++){
			BottomProductDecSelectView bottomProductDecSelectView = bottomProductDecSelectViewArrayList.get(i);
			result1+=bottomProductDecSelectView.seleResult;
		}
		return  productDesSelectBottom.getStock(result1);
	}

	private List<ShoppingCart.ResultModel.GoodsSpecModel> getSpec(){
		List<ShoppingCart.ResultModel.GoodsSpecModel> goodsSpec = new ArrayList<>();
		for(int i=0;i<bottomProductDecSelectViewArrayList.size();i++){
			BottomProductDecSelectView bottomProductDecSelectView = bottomProductDecSelectViewArrayList.get(i);
			ShoppingCart.ResultModel.GoodsSpecModel goodsSpecModel = new ShoppingCart.ResultModel.GoodsSpecModel();
			goodsSpecModel.attributionValName = bottomProductDecSelectView.seleResult;
			goodsSpec.add(goodsSpecModel);
		}
		return goodsSpec ;
	}

	@Override
	public void onItemListener() {
		if(getStock()>0){
			product_sel_stock.setText(getString(R.string.bottom_pruduct_sel_stock));
			product_sel_stock.setTextColor(Color.parseColor("#999999"));
			setBottomUI();
		}else{
			product_sel_stock.setText(getString(R.string.bottom_pruduct_sel_stock_out));
			product_sel_stock.setTextColor(Color.parseColor("#E2291F"));
			product_des_buy.setVisibility(View.GONE);
			product_des_add_shopcar.setVisibility(View.GONE);
			product_des_sure.setVisibility(View.GONE);
			product_des_nostock.setVisibility(View.VISIBLE);
		}
	}
}