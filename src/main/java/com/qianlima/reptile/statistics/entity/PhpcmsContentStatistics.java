package com.qianlima.reptile.statistics.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

/**
 * @program: qianlima-reptile-statistics-service
 * @description:
 * @author: Zhao Xiaoyang
 * @create: 2019-11-08 18:01
 **/
@Data
public class PhpcmsContentStatistics {
    @Id
    private ObjectId id;
    private String updateTimeStr;
    private Long updateTime;
    private Integer publishCount;
    private Integer catchCount;
}
