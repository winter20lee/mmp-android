
package zblibrary.zgl.model;

import zuo.biao.library.base.BaseModel;

/**搜索推荐
 */
public class SearchComm extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String serchKey;


	public String getSerchKey() {
		return serchKey;
	}

	public void setSerchKey(String serchKey) {
		this.serchKey = serchKey;
	}

	@Override
	protected boolean isCorrect() {
		return false;
	}
}
