package com.cajr.springcloud.common.content;

import java.util.Comparator;
import java.util.Map;

/**
 * @Author CAJR
 * @create 2019/9/5 13:16
 */
public class CustomizedComparator implements Comparator<String> {
    private Map<String,Double> base;

    public CustomizedComparator(Map<String, Double> base) {
        this.base = base;
    }

    @Override
    public int compare(String o1, String o2) {
        if (base.get(o1) >= base.get(o2)){
            return 1;
        }
        return -1;
    }
}
