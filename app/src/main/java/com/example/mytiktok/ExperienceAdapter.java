package com.example.mytiktok;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder> {
    private List<ExperienceItem> experienceList;
    private InteractionManager interactionManager;
    private ImagePreloadManager imagePreloadManager;
    private int lastPreloadPosition = -1;
    public ExperienceAdapter(List<ExperienceItem> experienceList) {
        this.experienceList = experienceList;
        this.interactionManager = new InteractionManager();
        this.imagePreloadManager = new ImagePreloadManager();
    }

    public void setGlideRequestManager(RequestManager requestManager) {
        this.imagePreloadManager.setGlideRequestManager(requestManager);
    }
    public void updateList(List<ExperienceItem> newList) {
        this.experienceList = newList;
        this.lastPreloadPosition = -1;//重置上次加载位置
        notifyDataSetChanged();
    }
    public void addData(List<ExperienceItem> newItems){
        int startPosition = experienceList.size();
        experienceList.addAll(newItems);
        notifyItemRangeInserted(startPosition, newItems.size());
    }

    private void performLikeOperation(String itemId, boolean isLiked, InteractionManager.LikeOperationCallback callback) {
        interactionManager.performLike(itemId, isLiked, callback);
    }

    private void performCollectOperation(String itemId, boolean isCollected, InteractionManager.CollectOperationCallback callback) {
        interactionManager.performCollect(itemId, isCollected, callback);
    }

    //预加载当前位置后面的几个item的图片
    private void preloadAheadItems(int currentPosition) {
        if (experienceList == null || experienceList.isEmpty()) return;

        // 预加载后面6个item的图片
        int preloadCount = 6;
        Set<String> urlsToPreload = new HashSet<>();

        for (int i = 1; i <= preloadCount; i++) {
            int targetPosition = currentPosition + i;
            if (targetPosition < experienceList.size()) {
                ExperienceItem item = experienceList.get(targetPosition);
                // 添加封面图和头像图的URL
                urlsToPreload.add(item.getCoverImageUrl());
                urlsToPreload.add(item.getAvatarImageUrl());
            }
        }

        // 执行预加载
        if (!urlsToPreload.isEmpty()) {
            imagePreloadManager.preloadImages(urlsToPreload);
        }
    }

    @Override
    public int getItemCount() {
        return experienceList != null ?experienceList.size() : 0;
    }

    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_experience_card, parent, false);
        return new ExperienceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ExperienceItem item = experienceList.get(position);
        holder.bind(item);

        // 触发预加载：当滚动到新位置时预加载后面的图片
        if (position > lastPreloadPosition) {
            preloadAheadItems(position);
            lastPreloadPosition = position;
        }
    }

    class ExperienceViewHolder extends RecyclerView.ViewHolder {
        private View cardView;
        private TextView tvTitle, tvSummary, tvAuthor, tvLikes, tvCollects;
        private ImageView ivAvatar,ivCover;
        private boolean isButtonClick = false;
        public ExperienceViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSummary = itemView.findViewById(R.id.tv_summary);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvLikes = itemView.findViewById(R.id.tv_likes);
            tvCollects = itemView.findViewById(R.id.tv_collects);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivCover = itemView.findViewById(R.id.iv_cover);
        }

        public void bind(ExperienceItem item) {
            tvTitle.setText(item.getTitle());
            tvSummary.setText(item.getContent());
            tvAuthor.setText(item.getAuthor());
            tvLikes.setText(formatCount(item.getLikeCount()));
            tvCollects.setText(formatCount(item.getCollectCount()));

            Glide.with(itemView.getContext())
                    .load(item.getCoverImageUrl())
                    .into(ivCover);

            Glide.with(itemView.getContext())
                    .load(item.getAvatarImageUrl())
                    .circleCrop() //头像裁剪
                    .into(ivAvatar);

            updateCollectUI(item);
            updateLikeUI(item);
            tvLikes.setOnClickListener(v -> {
                isButtonClick = true;
                v.setClickable(true);
                //立即消费
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onLikeClicked(position);
                }
                v.setPressed(true);
            });
            tvCollects.setOnClickListener(v -> {
                isButtonClick = true;
                v.setClickable(true);
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onCollectClicked(position);
                }
                v.setPressed(true);
            });
            itemView.setOnClickListener(v -> {
                if (!isButtonClick) {
                    //Log.d("LikeBug","Enter");
                }else{
                    isButtonClick = false;
                }
            });
        }
        private void updateLikeUI(ExperienceItem item){
            tvLikes.setText(formatCount(item.getLikeCount()));
            if(item.getIsLiked()){
                tvLikes.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_like_filled,0,0,0);
                tvLikes.setTextColor(tvLikes.getContext().getColor(R.color.red));
            }else{
                tvLikes.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_like,0,0,0);
                tvLikes.setTextColor(tvLikes.getContext().getColor(R.color.gray));
            }
        }
        private void updateCollectUI(ExperienceItem item){
            tvCollects.setText(formatCount(item.getCollectCount()));
            if (item.getCollected()) {
                tvCollects.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_collect_filled, 0, 0, 0);
                tvCollects.setTextColor(tvCollects.getContext().getColor(R.color.red));
            } else {
                tvCollects.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_collect, 0, 0, 0);
                tvCollects.setTextColor(tvCollects.getContext().getColor(R.color.gray));
            }
        }
        private String formatCount(int count) {
            if (count < 1000) {
                return String.valueOf(count);
            } else if (count < 10000) {
                return String.format("%.1fk", count / 1000.0);
            } else {
                return String.format("%.1fw", count / 10000.0);
            }
        }
        //乐观更新
        private void onLikeClicked(int position){
            ExperienceItem item = experienceList.get(position);
            item.toggleLike();
            notifyItemChanged(position);
            performLikeOperation(item.getId(), item.getIsLiked(),
                    new InteractionManager.LikeOperationCallback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onFailure() {
                            item.toggleLike();
                            notifyItemChanged(position);
                        }
                    });
        }
        private void onCollectClicked(int position){
            ExperienceItem item = experienceList.get(position);
            item.toggleCollect();
            notifyItemChanged(position);
            performCollectOperation(item.getId(), item.getCollected(),
                    new InteractionManager.CollectOperationCallback() {
                        @Override
                        public void onSuccess() {
                        }
                        @Override
                        public void onFailure() {
                            item.toggleCollect();
                            notifyItemChanged(position);
                        }
                    });
        }
    }

    public void onDestroy() {
        if (imagePreloadManager != null) {
            imagePreloadManager.clearPreloadCache();
        }
    }
}
