package com.example.mytiktok;

import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import java.util.HashSet;
import java.util.Set;

public class ImagePreloadManager {
    private static final String TAG = "PreloadManager";
    private RequestManager glideRequestManager;
    private Set<String> preloadedUrls = new HashSet<>();

    public ImagePreloadManager() {

    }

    //设置 Glide RequestManager
    public void setGlideRequestManager(RequestManager requestManager) {
        this.glideRequestManager = requestManager;
    }

    //预加载图片
    public void preloadImage(String imageUrl) {
        if (imageUrl == null || preloadedUrls.contains(imageUrl)) {
            return;
        }

        // 使用 Glide 预加载图片到内存缓存
        glideRequestManager
                .load(imageUrl)
                .preload();

        preloadedUrls.add(imageUrl);
    }

    //批量预加载图片
    public void preloadImages(Set<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) return;

        for (String url : imageUrls) {
            preloadImage(url);
        }

    }

    //清理预加载缓存
    public void clearPreloadCache() {
        preloadedUrls.clear();
    }
}
