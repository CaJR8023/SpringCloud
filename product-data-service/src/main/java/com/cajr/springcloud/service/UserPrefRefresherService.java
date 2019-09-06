package com.cajr.springcloud.service;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 13:36
 */
public interface UserPrefRefresherService {


    public void refresh();

    /**
     * 所有用户的喜好关键词列表TFIDF值随时间进行自动衰减更新
     */
    public void autoDecRefresh();


}
