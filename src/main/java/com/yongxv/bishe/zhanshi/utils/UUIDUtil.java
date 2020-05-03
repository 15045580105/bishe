package com.yongxv.bishe.zhanshi.utils;

public class UUIDUtil {

    /**
     * 生成mongo主键
     * @return
     */
    public static String primaryKey(){
        return java.util.UUID.randomUUID().toString().replace("-","");
    }
}
