package com.cajr.springcloud.algorithms;

import com.cajr.springcloud.mapper.UsersMapper;
import com.cajr.springcloud.util.RecommendUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/4 15:09
 */
public interface RecommendAlgorithm {

    /**
     * 推荐算法的int表示
     */
    //协同过滤
    public static final int CF = 0;
    //基于内容推荐
    public static final int CB = 1;
    //基于新闻热点的推荐
    public static final int HR = 2;

    /**
     * 针对所有用户返回推荐结果
     */
    public default void recommend(){
        recommend(RecommendUtil.getUserList());
    }

    /**
     * 针对特定用户返回推荐结果
     */
    public void recommend(List<Long> users);
}
