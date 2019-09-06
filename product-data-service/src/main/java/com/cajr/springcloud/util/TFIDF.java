package com.cajr.springcloud.util;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

/**
 * @Author CAJR
 * @create 2019/9/5 12:03
 */
public class TFIDF {

    public static Result spilt(String text){
        return ToAnalysis.parse(text);
    }

    /**
     *
     * @param title 文本标题
     * @param content 文本内容
     * @param keyNum 返回的关键词数目
     * @return
     */
    public static List<Keyword> getTfidf(String title,String content,int keyNum){
        KeyWordComputer keyWordComputer = new KeyWordComputer(keyNum);
        return keyWordComputer.computeArticleTfidf(title, content);
    }

    /**
     *
     * @param content 文本内容
     * @param keyNums 返回的关键词数目
     * @return
     */
    public static List<Keyword> getTfidf(String content,int keyNums){
        KeyWordComputer keyWordComputer = new KeyWordComputer(keyNums);
        return keyWordComputer.computeArticleTfidf(content);
    }
}
