package zblibrary.zgl.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

import zblibrary.zgl.fragment.FirstRecommendFragment;
import zblibrary.zgl.fragment.SearchFragment;
import zblibrary.zgl.model.GoodsCategory;

/**
 * 消息内容子页面适配器
 */
public class FirstTabLayoutAdapter extends FragmentPagerAdapter {
    private List<GoodsCategory> goodsCategoryList;
    public FirstTabLayoutAdapter(FragmentManager fm) {
        super(fm);
        this.goodsCategoryList = new ArrayList<>();
    }

    /**
     * 数据列表
     *
     * @param datas
     */
    public void setList(List<GoodsCategory> datas) {
        this.goodsCategoryList.clear();
        this.goodsCategoryList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        ArrayList<GoodsCategory.SublevelsModel> sublevelsModels = null;
        if(goodsCategoryList.get(position).sublevels!=null && goodsCategoryList.get(position).sublevels.size()>0){
            sublevelsModels = goodsCategoryList.get(position).sublevels;
        }
        if(position==0){
                return FirstRecommendFragment.createInstance();
        }else{
                return SearchFragment.createInstance("");
        }
    }

    @Override
    public int getCount() {
        return goodsCategoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String plateName = goodsCategoryList.get(position).name;
        if (plateName == null) {
            plateName = "";
        } else if (plateName.length() > 15) {
            plateName = plateName.substring(0, 15) + "...";
        }
        return plateName;
    }
}


