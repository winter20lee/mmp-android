package zuo.biao.library.util;

/**
 * Glide 图片加载工具类
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.ExecutionException;

import zuo.biao.library.R;
import zuo.biao.library.transform.GlideCircleTransform;
import zuo.biao.library.transform.GlideRoundTransform;

public class GlideUtil {
    private static String TAG = "GlideUtil";
    //加载失败 、占位   图片
    private static final int errorImg = R.mipmap.error;
    private static final int errorCircleImg = R.mipmap.defult_head;

    private static AnimationDrawable getLoadingDrawable(Context context){
        AnimationDrawable animationDrawable=(AnimationDrawable)context.getResources().getDrawable(R.drawable.loading);
        animationDrawable.start();
        return animationDrawable;
    }


    /**
     * 正常
     */
    public static void load(Context context, String url, ImageView imageView) {
        if(StringUtil.isEmpty(url)){
            imageView.setImageResource(errorImg);
            return;
        }
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) context).isDestroyed())
                    return;
            }
        }
        RequestOptions options = new RequestOptions()
                .error(errorImg)
                .placeholder(getLoadingDrawable(context));

        Glide.with(context)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 正常
     */
    public static void loadNoLoading(Context context, String url, ImageView imageView) {
        if(StringUtil.isEmpty(url)){
            imageView.setImageResource(errorImg);
            return;
        }
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) context).isDestroyed())
                    return;
            }
        }
        RequestOptions options = new RequestOptions()
                .error(errorImg);

        Glide.with(context)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 圆形
     */
    public static void loadCircle(Context context, String url, ImageView imageView) {
        if(StringUtil.isEmpty(url)){
            imageView.setImageResource(errorCircleImg);
            return;
        }
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) context).isDestroyed())
                    return;
            }
        }
        RequestOptions options = new RequestOptions()
                .error(errorCircleImg)
                .placeholder(getLoadingDrawable(context))
                .transform(new GlideCircleTransform());

        Log.i(TAG, url);
        Glide.with(context)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .into(imageView);
    }

    /**
     * 加载成圆角
     */
    public static void loadRound(Context context, String url, ImageView imageView) {
        if(StringUtil.isEmpty(url)){
            imageView.setImageResource(errorImg);
            return;
        }
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) context).isDestroyed())
                    return;
            }
        }
        int r = (int) context.getResources().getDimension(R.dimen.radius);
        RequestOptions options = new RequestOptions()
                .error(errorImg)
                .placeholder(getLoadingDrawable(context))
                .transform(new GlideRoundTransform(context, r));

        Log.i(TAG, url);
        Glide.with(context)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .into(imageView);
    }

    public static void loadRound(Context context, String url, ImageView imageView, @DimenRes int rRes) {
        if(StringUtil.isEmpty(url)){
            imageView.setImageResource(errorImg);
            return;
        }
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (((Activity) context).isDestroyed())
                    return;
            }
        }
        int r;
        try {
            r = (int) context.getResources().getDimension(rRes);
        } catch (Exception e) {
            r = rRes;
        }
        RequestOptions options = new RequestOptions()
                .error(errorImg)
                .placeholder(getLoadingDrawable(context))
                .transform(new GlideRoundTransform(context, r));

        Log.i(TAG, url);
        Glide.with(context)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return false;
                    }
                })
                .into(imageView);
    }
}
