package zuo.biao.library.base;

public class BaseEvent {
    public BaseEvent(String Message){
        this.Message = Message;
    }
    public String Message;
    public BaseEvent(String token,String tag){
        this.token = token;
    }
    public String token;
}
