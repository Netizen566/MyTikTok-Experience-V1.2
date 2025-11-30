package com.example.mytiktok;

import android.os.Handler;
import java.util.Random;

public class InteractionManager {
    private static final long MOCK_DELAY = 300;
    private Handler handler = new Handler();
    private Random random = new Random();

    public void performLike(String itemId, boolean isLike, LikeOperationCallback callback) {
        handler.postDelayed(() -> {
            if (random.nextFloat() < 0.9) {
                callback.onSuccess();//有可能成功有可能失败
            } else {
                callback.onFailure();
            }
        }, MOCK_DELAY);
    }

    public void performCollect(String itemId, boolean isCollect, CollectOperationCallback callback) {
        handler.postDelayed(() -> {
            if (random.nextFloat() < 0.9) {
                callback.onSuccess();
            } else {
                callback.onFailure();
            }
        }, MOCK_DELAY);
    }

    public interface LikeOperationCallback {
        void onSuccess();
        void onFailure();
    }

    public interface CollectOperationCallback {
        void onSuccess();
        void onFailure();
    }
}
