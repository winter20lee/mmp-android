package zblibrary.zgl.model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import zuo.biao.library.base.BaseModel;

public class GoodsCategory {


    public int id;
    public int parentId;
    public ArrayList<SublevelsModel> sublevels;
    public String name;
    public int show;
    public String gmtCreate;
    public String gmtModify;
    public Drawable drawable_nomal;
    public Drawable drawable_press;

    public static class SublevelsModel extends BaseModel {
        public int parentId;
        public String name;
        public String image;
        public int show;
        public String gmtCreate;
        public String gmtModify;

        @Override
        protected boolean isCorrect() {
            return true;
        }
    }
}
