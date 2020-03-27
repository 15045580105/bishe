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
    public static final String[] DataSourceList = new String[]{"webcha", "competeProduct", "bridgePage", "manualEdit", "octopus","mainCrawler","allSource"};

    /**
     * 微信
     */
    public static final String webcha = "webcha";

    /**
     * 竞品
     */
    public static final String competeProduct = "competeProduct";

    /**
     * 桥接页面
     */
    public static final String bridgePage = "bridgePage";

    /**
     * 人工编辑
     */
    public static final String manualEdit = "manualEdit";

    /**
     * 八爪鱼
     */
    public static final String octopus = "octopus";

    /**
     * 主爬
     */
    public static final String mainCrawler = "mainCrawler";

    /**
     * 所有来源之和
     */
    public static final String allSource = "allSource";

    /**
     * 参数不能为空状态码
     */
    public static final Integer EORROR_PARAMETER_CANNOT_BE_EMPTY = 100;

    /**
     * 开始日期不能晚于结束日期状态码
     */
    public static final Integer EORROR_START_NO_LATER_THAN_END = 101;

    /**
     * 没有数据状态码
     */
    public static final Integer EORROR_NO_DATA = 102;

    /**
     * 发布采集mongo表名
     */
    public static final String MONGO_COLLECTION_NAME = "phpcmsContentStatistics";
}
