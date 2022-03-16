package zuo.biao.library.util;
import android.app.Application;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;

public class DownUtils {
    public static void init(Application context){
        FileDownloader.setupOnApplicationOnCreate(context)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }
}
