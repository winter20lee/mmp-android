
package zblibrary.zgl.model;

import zuo.biao.library.base.BaseModel;

public class Customize extends BaseModel {
	public String catalog;
	public String key;
	public String weight;
	private String value;
	private boolean isSelect;
	public boolean isEnd;
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}

	@Override
	protected boolean isCorrect() {//根据自己的需求决定，也可以直接 return true
		return true;
	}

	public boolean isKefu(){
		if(key.equals("CUSTOMER_SERVICE_LINK")){
			return true;
		}
		return false;
	}

}
