package com.cajr.springcloud.common.content;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @Author CAJR
 * @create 2019/9/4 16:41
 */
public class CustomizedHashMap<K,V> extends HashMap<K,V>{
    private static final long serialVersionUID = 1L;

    @Override
    public String toString(){
        String toString="{";
        Iterator<K> keyIte=this.keySet().iterator();
        while(keyIte.hasNext()){
            K key=keyIte.next();
            toString+="\""+key+"\":"+this.get(key)+",";
        }
        if("{".equals(toString)){
            toString="{}";
        }
        else{
            toString=toString.substring(0, toString.length()-1)+"}";
        }
        return toString;

    }

    public CustomizedHashMap<K,V> copyFromLinkedHashMap(LinkedHashMap<K,V> linkedHashMap){
//		Iterator<K> ite = linkedHashMap.keySet().iterator();
//		while(ite.hasNext()){
//			K key=ite.next();
//			this.put(key,linkedHashMap.get(key));
//		}
        this.putAll(linkedHashMap);
        return this;
    }
}
