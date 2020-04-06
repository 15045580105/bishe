package com.qianlima.reptile.statistics.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

public class MathUtil {
    private static final Logger logger = LoggerFactory.getLogger(MathUtil.class);

    private static DecimalFormat df = new DecimalFormat("0.000");//设置保留位数

    public static Double divi(int a, int b) {
        double rate = 0.000;
        try {
            // * 100 的目的是小数转整数，例如： 0.75  *100 即转成 75，前台在 75后拼接%，即得 75%
            rate = Double.parseDouble(df.format((float) a * 100 / b)) ;
        }catch (Exception e ){
            logger.error(" math divi has error={}, a={},b={}",e,a,b);
        }
        return rate;
    }

}
