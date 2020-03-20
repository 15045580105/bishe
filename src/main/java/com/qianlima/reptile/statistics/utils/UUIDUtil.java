package com.qianlima.reptile.statistics.utils;

public class UUIDUtil {

    /**
     * 生成mongo主键
     * @return
     */
    public static String primaryKey(){
        return java.util.UUID.randomUUID().toString().replace("-","");
    }
}
