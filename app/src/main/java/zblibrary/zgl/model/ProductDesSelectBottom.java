
package zblibrary.zgl.model;

import java.util.ArrayList;
import java.util.List;

public class ProductDesSelectBottom {

	public List<AttributionListModel> attributionList;
	public List<SkuInfoModel> skuInfo;
	public static class AttributionListModel {
		public String attributionKeyName;
		public List<String> attributionValName;
	}

	public static class SkuInfoModel {
		public int price;
		public int toSell;
		public int stock;
		public ArrayList<String> attributionValues;
		public int skuId;
	}

	public int getSkuId(String seleResult){
		if(skuInfo!=null && skuInfo.size()>0){
			for (SkuInfoModel sku:skuInfo) {
				String result="";
				for(int i=0;i<sku.attributionValues.size();i++){
					result+=sku.attributionValues.get(i);
				}
				if(result.equals(seleResult)){
					return sku.skuId;
				}
			}
		}
		return 0;
	}

	public int getPrice(String seleResult){
		if(skuInfo!=null && skuInfo.size()>0){
			for (SkuInfoModel sku:skuInfo) {
				String result="";
				for(int i=0;i<sku.attributionValues.size();i++){
					result+=sku.attributionValues.get(i);
				}
				if(result.equals(seleResult)){
					return sku.price;
				}
			}
		}
		return 0;
	}

	public int getStock(String seleResult){
		if(skuInfo!=null && skuInfo.size()>0){
			for (SkuInfoModel sku:skuInfo) {
				String result="";
				for(int i=0;i<sku.attributionValues.size();i++){
					result+=sku.attributionValues.get(i);
				}
				if(result.equals(seleResult)){
					return sku.stock;
				}
			}
		}
		return 0;
	}
}
