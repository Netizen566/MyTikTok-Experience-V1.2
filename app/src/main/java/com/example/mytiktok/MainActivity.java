package com.example.mytiktok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ExperienceAdapter adapter;
    private List<ExperienceItem> currentList;
    private DataRepository dataRepository;
    private int lineNum = 1;
    private int currentPage = 0;
    private boolean isLoading = false;
    private ImageButton btnSwitchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //适配刘海屏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(params);
        }

        setContentView(R.layout.activity_main);

        dataRepository = new DataRepository();
        initViews();
        adjustForSystemBars();
        setupRefresh();
        setupRecyclerView();
        setupLayoutToggle();
        setupTabs();
        loadData(true);
    }

    private void adjustForSystemBars(){
        View decorView = getWindow().getDecorView();
        decorView.setOnApplyWindowInsetsListener((v,insets)->{
            int statusBarHeight = insets.getSystemWindowInsetTop();

            ConstraintLayout.LayoutParams tabLayoutParams = (ConstraintLayout.LayoutParams)tabLayout.getLayoutParams();
            tabLayoutParams.topMargin = statusBarHeight;
            tabLayout.setLayoutParams(tabLayoutParams);

            return insets;
        });
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
        recyclerView = findViewById(R.id.recyclerview_id);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        btnSwitchView = findViewById(R.id.btnToggleLayout);// 初始化
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
    }
    //列表显示模式切换监听
    private void setupLayoutToggle() {
        btnSwitchView.setOnClickListener(v -> {
            lineNum++;
            lineNum = lineNum % 2;

            StaggeredGridLayoutManager layoutManager;
            layoutManager = new StaggeredGridLayoutManager(lineNum + 1, StaggeredGridLayoutManager.VERTICAL);
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(layoutManager);
            //切换列表模式图片资源
            if (lineNum == 1) {
                btnSwitchView.setImageResource(R.drawable.ic_view_d);
            } else {
                btnSwitchView.setImageResource(R.drawable.ic_view);
            }
        });
    }

    //下拉刷新监听
    private void setupRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
        });
    }

    private void setupRecyclerView() {
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(lineNum + 1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        currentList = new ArrayList<>();
        adapter = new ExperienceAdapter(currentList);
        // 在 Activity 创建后设置 Glide RequestManager
        adapter.setGlideRequestManager(Glide.with(this));
        recyclerView.setAdapter(adapter);

        // 滚动监听实现加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 检查是否滚动到底部
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    loadData(false);
                }

                // 当快速滚动时也触发预加载
                if (Math.abs(dy) > 20) {
                    triggerAdvancedPreload();
                }
            }
        });
    }

    private void triggerAdvancedPreload() {
        StaggeredGridLayoutManager layoutManager =
                (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager == null) return;

        int[] lastVisiblePositions = layoutManager.findLastVisibleItemPositions(null);
        if (lastVisiblePositions.length > 0) {
            int lastVisible = lastVisiblePositions[0];
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.onDestroy();
        }
    }

    private void setupTabs() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentPage = 0;//重置
                loadData(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 点击当前选中的Tab，滚动到顶部
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void refreshData() {
        currentPage = 0;
        loadData(true);
    }

    private void loadData(final boolean isRefresh) {
        if (isLoading) return;
        isLoading = true;

        MockApiService.DataCallback<List<ExperienceItem>> callback =
                new MockApiService.DataCallback<List<ExperienceItem>>() {
                    @Override
                    public void onSuccess(List<ExperienceItem> data) {
                        isLoading = false;
                        swipeRefreshLayout.setRefreshing(false);

                        if (isRefresh) {
                            currentList.clear();
                        }

                        if (!data.isEmpty()) {
                            if (isRefresh) {
                                adapter.updateList(data);
                                recyclerView.scrollToPosition(0);
                            } else {
                                adapter.addData(data);
                            }
                            currentPage++;
                        }
                    }

                };

        // 加载数据
        dataRepository.loadRecommendData(currentPage, callback);
    }
}