package com.qianlima.reptile.statistics.domain;
/**
 * @Author : gyx
 * @Description :
 * @Date : Created in 10:13 2020-04-01
 * @Modified By :
 */

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gyx
 * @date 2020-04-01 10:13
 */
@Document(collection = "potInformation")
@Data
public class PotInformation {
    @Id
    private ObjectId id;

    /**
     * 查询时间  格式 2020-02-06
     */
    private String queryDate;
    /**
     * 链接
     */
    private String pot;
    /**
     *模版数
     */
    private long templateNumber;
    /**
     *pot状态
     */
    private String state;
    /**
     *近30天采集量
     */
    private long collectNumber;
    /**
     *近30天发布量
     */
    private long releaseNumber;
    /**
     *创建时间
     */
    private String createTime;
    /**
     *修改时间
     */
    private String updateTime;
    /**
     *完全重复pot数
     */
    private long repeatPot;



}
