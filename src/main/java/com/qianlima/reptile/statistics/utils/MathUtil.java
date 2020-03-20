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
            rate = Double.parseDouble(df.format((float) a / b));
        }catch (Exception e ){
            logger.error(" math divi has error, a={},b={}",e,a,b);
        }
        return rate;
    }

}
