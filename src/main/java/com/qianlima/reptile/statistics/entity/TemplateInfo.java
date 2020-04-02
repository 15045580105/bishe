package com.qianlima.reptile.statistics.entity;

import lombok.Data;

/**
 * @author liuchanglin
 * @version 1.0
 * @ClassName: TemplateInfo
 * @Description: 模版信息
 * @date 2020/4/1 2:03 下午
 */
@Data
public class TemplateInfo {
    /**
     * id
     */
    private Integer id;
    /**
     * pot
     */
    private String potName;
    /**
     * 备注
     */
    private String content;
    /**
     * 是否为项目
     */
    private Integer isxm;
    /**
     * 类别
     */
    private String cat;
    /**
     * 状态 -2=未配置；-1=删除；0=待审; 1=正常;
     */
    private Integer state;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 采集类别
     */
    private String collectStrategy;
}
