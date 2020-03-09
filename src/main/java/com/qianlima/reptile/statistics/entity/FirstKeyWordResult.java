package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @description: 一级关键词统计结果
 * @author: sx
 * @create: 2020-03-09 15:27:11
 **/
@Data
public class FirstKeyWordResult {
    /**
     * 序号
     */
    private Integer order;
    /**
     * 一级关键词
     */
    private String firstKeyWord;
    /**
     * 国信网统计数
     */
    private Integer guoXinCount;
    /**
     * 中国招标网统计数
     */
    private Integer chainBiddingCount;
    /**
     * 中国采招网统计数
     */
    private Integer chainRecruitmentCount;
    /**
     * 导航网统计数
     */
    private Integer navigateCount;
    /**
     * 千里马统计数
     */
    private Integer qlmCount;

    public FirstKeyWordResult(Integer order, String firstKeyWord, Integer guoXinCount, Integer chainBiddingCount, Integer chainRecruitmentCount, Integer navigateCount, Integer qlmCount) {
        this.order = order;
        this.firstKeyWord = firstKeyWord;
        this.guoXinCount = guoXinCount;
        this.chainBiddingCount = chainBiddingCount;
        this.chainRecruitmentCount = chainRecruitmentCount;
        this.navigateCount = navigateCount;
        this.qlmCount = qlmCount;
    }
}