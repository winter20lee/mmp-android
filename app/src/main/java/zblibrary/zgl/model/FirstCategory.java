package zblibrary.zgl.model;
import android.graphics.drawable.Drawable;

import zuo.biao.library.base.BaseModel;

public class FirstCategory  {
        public int id;
        public String name;
        public int weight;
        public String icon;
        public String iconSelected;
        public String iconBig;
        public Drawable drawable;
        public Drawable drawableSelected;
        public static class FirstCategorySerializable extends BaseModel{

                public String name;
                public String iconBig;
                @Override
                protected boolean isCorrect() {
                        return true;
                }
        }
        public FirstCategorySerializable transData(){
                FirstCategorySerializable firstCategorySerializable = new FirstCategorySerializable();
                firstCategorySerializable.iconBig = this.iconBig;
                firstCategorySerializable.name = this.name;
                return firstCategorySerializable;
        }
}
