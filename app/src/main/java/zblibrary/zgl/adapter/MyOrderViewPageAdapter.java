package zblibrary.zgl.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.fragment.MyOrderFragment;

/**
 * 消息内容子页面适配器
 */
public class MyOrderViewPageAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    public MyOrderViewPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.titles = new ArrayList<>();
        this.titles.add(context.getString(R.string.myorder_all));
        this.titles.add(context.getString(R.string.myorder_dzf));
        this.titles.add(context.getString(R.string.myorder_dfh));
        this.titles.add(context.getString(R.string.myorder_dsh));
        this.titles.add(context.getString(R.string.myorder_finish));
        this.titles.add(context.getString(R.string.myorder_close));
    }


    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return MyOrderFragment.createInstance("ALL");
        }else  if(position==1){
            return MyOrderFragment.createInstance("WAIT_PAY");
        }else  if(position==2){
            return MyOrderFragment.createInstance("WAIT_SEND");
        }else  if(position==3){
            return MyOrderFragment.createInstance("WAIT_RECEIVE");
        }else  if(position==4){
            return MyOrderFragment.createInstance("FINISHED");
        }else  if(position==5){
            return MyOrderFragment.createInstance("CLOSED");
        }
        return MyOrderFragment.createInstance("");
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


