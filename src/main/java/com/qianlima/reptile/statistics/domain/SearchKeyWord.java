package com.qianlima.reptile.statistics.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "SearchKeyWord")
@Data
public class SearchKeyWord {

    @Id
    private ObjectId id;

    /*
    统计类型   qianlima-bt  qianlima-qw
     */
    private String keyword;

    /**
     * 统计日期  2020-01-01、2020-01-02         2020-01-01 == querytime eq
     */
    private Date updateTime;

    /**
     * 磐石账户
     */
    private String operator;
}
