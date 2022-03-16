package zblibrary.zgl.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.R;
import zblibrary.zgl.fragment.AllOrderFragment;

/**
 * 消息内容子页面适配器
 */
public class AllOrderViewPageAdapter extends FragmentPagerAdapter {
    private List<String> titles;
    public AllOrderViewPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.titles = new ArrayList<>();
        this.titles.add(context.getString(R.string.myorder_all));
        this.titles.add(context.getString(R.string.myorder_dzf));
        this.titles.add(context.getString(R.string.allorder_djx));
        this.titles.add(context.getString(R.string.allorder_yjx));
        this.titles.add(context.getString(R.string.myorder_close));
        this.titles.add(context.getString(R.string.allorder_ytk));
    }


    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return AllOrderFragment.createInstance("ALL");
        }else  if(position==1){
            return AllOrderFragment.createInstance("WAIT_PAY");
        }else  if(position==2){
            return AllOrderFragment.createInstance("WAIT_OPEN");
        }else  if(position==3){
            return AllOrderFragment.createInstance("OPENED");
        }else  if(position==4){
            return AllOrderFragment.createInstance("CLOSED");
        }else  if(position==5){
            return AllOrderFragment.createInstance("REFUNDED");
        }
        return AllOrderFragment.createInstance("");
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


