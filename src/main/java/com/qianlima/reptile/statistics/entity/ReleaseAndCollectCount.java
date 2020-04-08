package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: ReleaseAndCollect
 * @Description: 发布量采集量 count
 * @date 2020/4/1 5:30 下午
 */
@Data
public class ReleaseAndCollectCount {
    private String date;
    private Integer releaseCount = 0;
    private Integer collectCount = 0;
}
