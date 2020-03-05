package com.qianlima.reptile.statistics.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "DataStatistics")
@Data
public class DataStatistics {

    @Id
    private ObjectId id;

    /*
    统计类型
     */
    private String type;

    /**
     * 统计日期
     */
    private String queryDate;
    /**
     * 统计参数
     */
    private List<String> queryParam;

    /**
     * 统计数量
     */
    private Double count;

}
