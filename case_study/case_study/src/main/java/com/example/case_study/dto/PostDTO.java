package com.example.case_study.dto;

import com.example.case_study.model.Post;

import java.time.LocalDate;

public class PostDTO {
    private Integer id;
    private String title;
    private String content;
    private LocalDate publishDate;
    private String status;
    public PostDTO() {

    }
    // Constructor nhận tham số là đối tượng Post
    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.publishDate = post.getPublishDate();
        this.status = post.getStatus();
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDate getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


}
