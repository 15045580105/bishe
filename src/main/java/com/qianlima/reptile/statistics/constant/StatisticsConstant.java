package com.qianlima.reptile.statistics.constant;

/**
 * @description: 统计常量
 * @author: sx
 * @create: 2020-03-09 14:31:09
 **/
public class StatisticsConstant {
    /**
     * 对应 keyword_data_comparsion 表 web字段类型
     */
    public static final String[] WEBS = new String[]{"chinabidding", "zhaobiao", "bidcenter", "okcis", "qlm"};

    /**
     * 发布状态
     */
    public static final Integer PUBLISH = 1;

    /**
     * 采集状态11
     */
    public static final Integer COLLECTION = 0;

    /**
     * 数据来源
     */
    public static final String[] DataSourceList = new String[]{"webcha", "competeProduct", "bridgePage", "manualEdit", "octopus","mainCrawler"};

    public static final String webcha = "webcha";

    public static final String competeProduct = "competeProduct";

    public static final String bridgePage = "bridgePage";

    public static final String manualEdit = "manualEdit";

    public static final String octopus = "octopus";

    public static final String mainCrawler = "mainCrawler";
}
