package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: OctopusLogInfo
 * @date 2020/3/9 1:34 上午
 */
@Data
public class OctopusLogInfo {
    private String status;
    private Integer count;

    @Override
    public String toString() {
        return "OctopusLogInfo{" +
                "status='" + status + '\'' +
                ", count=" + count +
                '}';
    }
}
