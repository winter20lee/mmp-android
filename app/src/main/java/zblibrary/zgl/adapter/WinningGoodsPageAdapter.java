package zblibrary.zgl.adapter;
import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;
import zblibrary.zgl.R;
import zblibrary.zgl.fragment.LastFragment;

/**
 * 消息内容子页面适配器
 */
public class WinningGoodsPageAdapter extends FragmentPagerAdapter {
	private List<String> titles;
	public WinningGoodsPageAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.titles = new ArrayList<>();
		this.titles.add(context.getString(R.string.myorder_all));
		this.titles.add(context.getString(R.string.winning_goods_dlq));
		this.titles.add(context.getString(R.string.myorder_dfh));
		this.titles.add(context.getString(R.string.myorder_dzz));
		this.titles.add(context.getString(R.string.myorder_dsh));
		this.titles.add(context.getString(R.string.myorder_finish));
	}


	@Override
	public Fragment getItem(int position) {
		if(position==0){
			return LastFragment.createInstance("ALL");
		}else  if(position==1){
			return LastFragment.createInstance("WAIT_PICK");
		}else  if(position==2){
			return LastFragment.createInstance("WAIT_SEND");
		}else  if(position==3){
			return LastFragment.createInstance("WAIT_TRANSFER");
		} else  if(position==4){
			return LastFragment.createInstance("WAIT_RECEIVE");
		}else  if(position==5){
			return LastFragment.createInstance("FINISHED");
		}
		return LastFragment.createInstance("");
	}

	@Override
	public int getCount() {
		return titles.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles.get(position);
	}
}


