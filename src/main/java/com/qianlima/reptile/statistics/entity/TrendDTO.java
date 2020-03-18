package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @description: 趋势实体
 * @author: sx
 * @create: 2020-03-18 15:49:54
 **/
@Data
public class TrendDTO {
    /**
     * 当前日期
     */
    private String currentDayStr;
    /**
     * 采集量
     */
    private String catchCount;
    /**
     * 发布量
     */
    private String publishCount;

    public TrendDTO(String currentDayStr, String catchCount, String publishCount) {
        this.currentDayStr = currentDayStr;
        this.catchCount = catchCount;
        this.publishCount = publishCount;
    }
}
