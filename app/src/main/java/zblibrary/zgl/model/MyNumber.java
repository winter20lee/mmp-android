
package zblibrary.zgl.model;

import java.util.List;

public class MyNumber {

	public String planNum;
	public List<LuckNumberListModel> luckNumberList;

	public static class LuckNumberListModel {
		public int luckNumber;
		public int isWinNumber;
	}
}
