package com.example.mytiktok;

public class ExperienceItem {
    private String id;
    private String title;
    private String content;
    private String author;
    private int likeCount;
    private int collectCount;
    private int avatarResId;
    private int coverResId;
    private boolean isLiked;
    private boolean isCollected;
    private String coverImageUrl;
    private String avatarImageUrl;

    public ExperienceItem(String id,String title, String content, String author,
                          int likeCount, int collectCount,
                          boolean isLiked,boolean isCollected,
                          String coverImageUrl,String avatarImageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.likeCount = likeCount;
        this.collectCount = collectCount;
        this.isLiked = isLiked;
        this.isCollected = isCollected;
        this.coverImageUrl = coverImageUrl;
        this.avatarImageUrl = avatarImageUrl;
    }

    public void toggleLike() {
        if (isLiked) {
            likeCount = Math.max(0, likeCount - 1); // 防止负数
        } else {
            likeCount++;
        }
        isLiked = !isLiked;
    }

    public void toggleCollect() {
        if (isCollected) {
            collectCount = Math.max(0, collectCount - 1);
        } else {
            collectCount++;
        }
        isCollected = !isCollected;
    }
    //region getMethods
    public String getId() { return id;}
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public int getLikeCount() { return likeCount; }
    public int getCollectCount() { return collectCount; }
    public int getAvatarResId() {return avatarResId; }
    public int getCoverResId() {return coverResId;}
    public boolean getIsLiked() {return isLiked;}
    public boolean getCollected(){return isCollected;}
    public String getCoverImageUrl() { return coverImageUrl; }
    public String getAvatarImageUrl() { return avatarImageUrl; }
    //endregion

    //region setMethods
    public void setId(String id) {this.id = id;}
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) {this.content = content;}
    public void setAuthor(String author) {this.author = author; }
    public void setLikeCount(int likeCount) {this.likeCount = likeCount; }
    public void setCollectCount(int collectCount) {this.collectCount = collectCount; }
    public void setAvatarResId(int avatarResId) {this.avatarResId = avatarResId; }
    public void setCoverResId(int coverResId) {this.coverResId = coverResId; }
    public void setLiked(boolean isLiked) {this.isLiked = isLiked; }
    public void setCollected(boolean isCollected) {this.isCollected = isCollected; }
    //endregion
}