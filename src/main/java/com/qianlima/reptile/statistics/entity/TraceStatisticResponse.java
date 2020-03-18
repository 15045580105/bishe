package com.qianlima.reptile.statistics.entity;

import com.qianlima.reptile.statistics.domain.TraceStatistic;
import lombok.Data;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: TraceStatisticResponse
 * @date 2020/3/18 11:48 上午
 */
@Data
public class TraceStatisticResponse {

    /**
     * 发布量
     */
    private TraceStatistic releaseCount;

    /**
     * 采集量
     */
    private TraceStatistic collectCount;
}
