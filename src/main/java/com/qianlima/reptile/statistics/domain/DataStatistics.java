package com.qianlima.reptile.statistics.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection = "DataStatistics")
@Data
public class DataStatistics {

    @Id
    private ObjectId id;

    /*
    统计类型   qianlima-bt  qianlima-qw
     */
    private String type;

    /**
     * 统计日期  2020-01-01、2020-01-02         2020-01-01 == querytime eq
     */
    private String queryDate;
    /**
     * 统计参数
     */
    private List<Map<String,Long>> countResult;



}
