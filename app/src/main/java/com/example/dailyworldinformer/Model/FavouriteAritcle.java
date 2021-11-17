package com.example.dailyworldinformer.Model;



public class FavouriteAritcle {

    String id;
    String uid;
    String url;
    String image;
    String title;
    String description;
    String source;
    String publishAt;
    String author;
    int count;

    public FavouriteAritcle() {
    }

    public FavouriteAritcle(String id, String uid, String url, String image, String title, String description, String source, String publishAt, String author, int count) {
        this.id = id;
        this.uid = uid;
        this.url = url;
        this.image = image;
        this.title = title;
        this.description = description;
        this.source = source;
        this.publishAt = publishAt;
        this.author = author;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
