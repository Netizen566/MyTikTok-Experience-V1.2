package com.example.mytiktok;

import java.util.List;

public class DataRepository {
    private MockApiService mockApiService;

    public DataRepository() {
        this.mockApiService = MockApiService.getInstance();
    }

    public void loadRecommendData(int page, MockApiService.DataCallback<List<ExperienceItem>> callback) {
        mockApiService.getRecommendData(page, 10, callback);
    }

}