package com.cajr.springcloud.util;

import java.util.Comparator;
import java.util.Map;

/**
 * @Author CAJR
 * @create 2019/9/5 13:06
 */
public class MapValueComparator implements Comparator<Map.Entry<Long,Double>> {

    @Override
    public int compare(Map.Entry<Long, Double> o1, Map.Entry<Long, Double> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
