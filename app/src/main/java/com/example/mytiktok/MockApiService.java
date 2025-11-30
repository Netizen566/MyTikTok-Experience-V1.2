package com.example.mytiktok;

import android.os.Handler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockApiService {
    private static final long NETWORK_DELAY = 500;
    private Handler handler = new Handler();
    private static MockApiService instance;
    //单例模式，统一管理模拟数据
    public static MockApiService getInstance() {
        if (instance == null) {
            instance = new MockApiService();
        }
        return instance;
    }

    // 获取数据
    public void getRecommendData(int page, int pageSize, DataCallback<List<ExperienceItem>> callback) {
        handler.postDelayed(() -> {
                List<ExperienceItem> data = generateMockItems(page, pageSize);
                callback.onSuccess(data);
        }, NETWORK_DELAY);
    }

    private List<ExperienceItem> generateMockItems(int page, int pageSize) {
        List<ExperienceItem> items = new ArrayList<>();
        String[] authors = {"不是营销号", "乐邦-詹士", "威风的龙", "AI助手", "安卓开发人", "摄影艺术", "开心时刻", "华尔街之狼"};

        int startIndex = page * pageSize;
        long timestamp = System.currentTimeMillis(); // 添加时间戳

        for (int i = 0; i < pageSize; i++) {
            int index = startIndex + i;
            Random rand = new Random(index);

            items.add(new ExperienceItem(
                    "item_" + index,
                    "这是标题编号 " + (index + 1),
                    "这是第 " + (index + 1) + " 条摘要...",
                    authors[i % 8],
                    rand.nextInt(5000) + 100,
                    rand.nextInt(2000) + 30,
                    rand.nextBoolean(),
                    rand.nextFloat() > 0.7,
                    // 使用时间戳确保每次刷新都是新图片
                    "https://picsum.photos/400/600?random=" + index + "&t=" + timestamp,
                    "https://picsum.photos/100/100?random=" + index + "&t=" + timestamp
            ));
        }
        return items;
    }
    private List<ExperienceItem> filterByCategory(String category, int page) {
        // 直接返回推荐数据
        return generateMockItems(page, 10);
    }

    public interface DataCallback<T> {
        void onSuccess(T data);
    }
}
