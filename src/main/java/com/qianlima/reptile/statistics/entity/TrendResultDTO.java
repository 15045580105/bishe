package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @description: 趋势结果
 * @author: sx
 * @create: 2020-03-18 17:41:15
 **/
@Data
public class TrendResultDTO {
    /**
     *  某一个月度的发布量、采集量
     */
    private TrendDTO trendDTO;
    /**
     *  采集量同比
     */
    private String catchProportion;
    /**
     *  发布量同比
     */
    private String publishProportion;

    public TrendResultDTO(TrendDTO trendDTO, String catchProportion, String publishProportion) {
        this.trendDTO = trendDTO;
        this.catchProportion = catchProportion;
        this.publishProportion = publishProportion;
    }
}
