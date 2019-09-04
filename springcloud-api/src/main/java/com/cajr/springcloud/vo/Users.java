package com.cajr.springcloud.vo;

import java.util.Date;

/**
 * @Author CAJR
 * @create 2019/9/4 13:01
 */

public class Users {
    private Long id;

    private String prefList;

    private Date latestLogTime;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefList() {
        return prefList;
    }

    public void setPrefList(String prefList) {
        this.prefList = prefList;
    }

    public Date getLatestLogTime() {
        return latestLogTime;
    }

    public void setLatestLogTime(Date latestLogTime) {
        this.latestLogTime = latestLogTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}