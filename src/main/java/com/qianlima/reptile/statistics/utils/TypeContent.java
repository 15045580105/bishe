package com.qianlima.reptile.statistics.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: bazhuaya
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-06-20 14:40
 **/
public class TypeContent {
    public static Map<String,Integer> getTypeMap(){
        Map<String,Integer> typeMap = new HashMap<>();
        typeMap.put("预告",0);
        typeMap.put("公告",1);
        typeMap.put("变更",2);
        typeMap.put("结果",3);
        typeMap.put("国土",4);
        typeMap.put("全部",5);
        return  typeMap;
    }

    public static Map<String,Integer> getAreaMap(){
        Map<String,Integer> areaMap = new HashMap<>();
        areaMap.put("全国",2703);
        areaMap.put("安徽",1);
        areaMap.put("北京",2);
        areaMap.put("福建",3);
        areaMap.put("甘肃",4);
        areaMap.put("广东",5);
        areaMap.put("广西",6);
        areaMap.put("贵州",7);
        areaMap.put("海南",8);
        areaMap.put("河北",9);
        areaMap.put("河南",10);
        areaMap.put("黑龙江",11);
        areaMap.put("湖北",12);
        areaMap.put("湖南",13);
        areaMap.put("吉林",14);
        areaMap.put("江苏",15);
        areaMap.put("江西",16);
        areaMap.put("辽宁",17);
        areaMap.put("内蒙古",18);
        areaMap.put("宁夏",19);
        areaMap.put("青海",20);
        areaMap.put("山东",21);
        areaMap.put("山西",22);
        areaMap.put("陕西",23);
        areaMap.put("上海",24);
        areaMap.put("四川",25);
        areaMap.put("天津",26);
        areaMap.put("西藏",27);
        areaMap.put("新疆",28);
        areaMap.put("云南",29);
        areaMap.put("浙江",30);
        areaMap.put("重庆",31);
        return  areaMap;
    }
}
