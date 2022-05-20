package zblibrary.zgl.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MemberCenter {
        public int id;
        public String levelCode;
        public String levelName;
        public int price;
        public int discountPrice;
        public String cardImg;
        public String descImg;
        public List<PaymentMethodMembershipRespListBean> paymentMethodMembershipRespList;
        public static class PaymentMethodMembershipRespListBean {
                public int paymentMethodId;
                public String paymentMethodName;
                public String tag;
                public String memo;
                public int sortValue;
                public String payType;
                public boolean isSel;
        }
}
