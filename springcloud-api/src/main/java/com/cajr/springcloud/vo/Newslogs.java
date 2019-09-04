package com.cajr.springcloud.vo;

import java.util.Date;

/**
 * @Author CAJR
 * @create 2019/9/4 13:01
 */

public class Newslogs {
    private Long id;

    private Long userId;

    private Long newsId;

    private Date viewTime;

    private Integer preferDegree;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public Date getViewTime() {
        return viewTime;
    }

    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
    }

    public Integer getPreferDegree() {
        return preferDegree;
    }

    public void setPreferDegree(Integer preferDegree) {
        this.preferDegree = preferDegree;
    }
}