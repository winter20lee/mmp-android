package zuo.biao.library.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import zuo.biao.library.base.BaseApplication;
import zuo.biao.library.interfaces.OnUpLoadResponseListener;
import zuo.biao.library.util.GsonUtil;
import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;

public class UpLoadOssManager {
    // 访问的endpoint地址
    public static  String OSS_ENDPOINT = "http://oss-ap-southeast-6.aliyuncs.com";
//    //callback 测试地址
//    public static final String OSS_CALLBACK_URL = "http://oss-demo.aliyuncs.com:23450";
//    // STS 鉴权服务器地址。
//    // 或者根据工程sts_local_server目录中本地鉴权服务脚本代码启动本地STS鉴权服务器。
    public static final String STS_SERVER_URL = "http://****/sts/getsts";//STS 地址

    public static  String BUCKET_NAME = "12shop";
    public static final String OSS_ACCESS_KEY_ID = "LTAI5tN2RQcVPxFEZRsBE4SX";;
    public static final String OSS_ACCESS_KEY_SECRET = "w9E1Rz37m473o6vD7Fybr6gJWgk8Od";

    private static OSSCredentialProvider credentialProvider;
    private static  ClientConfiguration conf;
    private static OSS oss;
    private static Context context;
    private UpLoadOssManager(Context context) {
        this.context = context;
    }

    private static UpLoadOssManager instance;
    public static UpLoadOssManager getInstance() {
        if (instance == null) {
            synchronized (UpLoadOssManager.class) {
                if (instance == null) {
                    instance = new UpLoadOssManager(BaseApplication.getInstance());
                }
            }
        }
        return instance;
    }

    public static boolean isNeedUpdateCredentialProvider(){
            return credentialProvider==null ? true : false;
    }

    public static void updateCredentialProvider(String accessKeyId, String secretKeyId, String securityToken,String endpoint,String bucket){
        if(credentialProvider ==null){
            if(StringUtil.isNotEmpty(endpoint,true)){
                OSS_ENDPOINT = endpoint;
            }
            if(StringUtil.isNotEmpty(bucket,true)){
                BUCKET_NAME = bucket;
            }
            // 在移动端建议使用STS的方式初始化OSSClient。
            credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, secretKeyId, securityToken);
            // 配置类如果不设置，会有默认配置。
            conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒。
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒。
            conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个。
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次。
            oss = new OSSClient(BaseApplication.getInstance(), OSS_ENDPOINT, credentialProvider);
        }
    }

    public static void UploadImage(String path,final OnUpLoadResponseListener listener){

        String objectKey = "avatar/"+getUserId()+"/"+System.currentTimeMillis()+ StringUtil.getContentType(path);
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, objectKey, path);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String url = oss.presignPublicObjectURL(BUCKET_NAME, objectKey);
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                Log.d("url",url);
                if(listener!=null){
                    listener.onUploadSuccess(url);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 客户端异常，例如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务端异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
                if(listener!=null){
                    listener.onUploadFile();
                }
            }
        });
        // task.cancel(); // 可以取消任务。
        // task.waitUntilFinished(); // 等待任务完成。
    }

    private static String getUserId() {
        SharedPreferences sdf = context.getSharedPreferences("PATH_USER", Context.MODE_PRIVATE);
        if (sdf == null) {
            Log.e("", "get sdf == null >>  return;");
            return "";
        }
        String userJosn = (sdf.getString("KEY_USER", null));
        try {
            return GsonUtil.GsonId(userJosn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
