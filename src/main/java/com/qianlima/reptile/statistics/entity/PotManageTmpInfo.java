package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: PotManageTmpInfo
 * @date 2020/4/10 11:28 上午
 */
@Data
public class PotManageTmpInfo {
    /**
     * 模版号
     */
    private Integer id;
    /**
     * 所属pot
     */
    private String potName;
    /**
     * 备注
     */
    private String potNote;
    /**
     * 修正次数
     */
    private Integer ModifyTimes = 0;
    /**
     * 是否是项目：1为是，0为不是',
     */
    private Integer isXm;
    /**
     * 类别
     */
    private String cat;
    /**
     * 状态 -2=未配置；-1=删除；0=待审; 1=正常;'
     */
    private Integer state;
    /**
     * 近三十天采集量
     */
    private Integer monthCollectCount;
    /**
     * 近三十天发布量
     */
    private Integer monthReleaseCount;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * 采集类别 0:主模板,1:人工编辑,2:八爪鱼,3:桥接页面,4:竞品采集',
     */
    private Integer collectStrategy;
    /**
     * 采集策略变化次数
     */
    private Integer collectStrategyChangeTimes = 0;
    /**
     * 总数
     */
    private Integer total;

}
